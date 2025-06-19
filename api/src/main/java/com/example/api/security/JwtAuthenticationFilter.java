// リクエストからトークン抽出・検証処理
package com.example.api.security;

import com.example.api.entity.JwtToken;
import com.example.api.repository.JwtTokenRepository;
import com.example.api.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenRepository jwtTokenRepository; // 失効判定用

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   CustomUserDetailsService userDetailsService,
                                   JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        /* --- 1. Authorization ヘッダーからトークン抽出 --- */
        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        /* --- 2. トークン検証 & SecurityContext 未設定の場合のみ処理 --- */
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 2-1 署名 & 期限チェック
            if (jwtTokenProvider.isValid(token)) {

                // 2-2 DB上で失効していないかチェック
                boolean revoked = jwtTokenRepository.findByToken(token)
                                 .map(JwtToken::getIsRevoked)
                                 .orElse(true);          // 見つからない→無効扱い
                if (!revoked) {

                    // 2-3 ユーザーIDを取得し UserDetails をロード
                    String userId = jwtTokenProvider.extractUserId(token);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                    // 2-4 SecurityContext に認証情報をセット
                    UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        /* --- 3. 後続フィルターへ --- */
        filterChain.doFilter(request, response);
    }

    /* ログインAPIのみ除外 */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/api/login".equals(request.getRequestURI());
    }
}
