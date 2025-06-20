package com.example.api.security;

import com.example.api.service.JwtService;
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
    private final JwtTokenRepository jwtTokenRepository;

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

        try {
            String authHeader = request.getHeader("Authorization");
            String token = null;
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenProvider.isValid(token)) {
                    boolean revoked = jwtTokenRepository.findByToken(token)
                            .map(JwtToken::getIsRevoked)
                            .orElse(true); // 見つからない→無効扱い
                    if (!revoked) {
                        String userId = jwtTokenProvider.extractUserId(token);
                        // ==== ここを修正！ ====
                        UserDetails userDetails = userDetailsService.loadUserById(Integer.parseInt(userId));
                        // =====================

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        System.out.println("JWT認証エラー: トークンが失効しています");
                    }
                } else {
                    System.out.println("JWT認証エラー: トークンが無効です");
                }
            } else {
                System.out.println("❌ トークン署名 or 有効期限エラー");
            }
        } catch (Exception ex) {
            System.out.println("JWT認証エラー: " + ex.getMessage());
            ex.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // ここで認証不要エンドポイントを列挙
        String path = request.getRequestURI();
        return path.equals("/api/login")
            || path.equals("/api/reset-mail")
            || path.startsWith("/api/reset-password");
    }
}
