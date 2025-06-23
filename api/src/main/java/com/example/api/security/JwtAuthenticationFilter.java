// JwtAuthenticationFilter.java
package com.example.api.security;

import com.example.api.entity.JwtToken;
import com.example.api.repository.JwtTokenRepository;
import com.example.api.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

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
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/login")
                || path.equals("/api/reset-mail")
                || path.equals("/api/reset-password");
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            String token = (authHeader != null && authHeader.startsWith("Bearer "))
                    ? authHeader.substring(7)
                    : null;

            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                if (jwtTokenProvider.isValid(token)) {
                    boolean revoked = jwtTokenRepository.findByToken(token)
                            .map(JwtToken::getIsRevoked)
                            .orElse(true);
                    if (!revoked) {
                        String userId = jwtTokenProvider.extractUserId(token);
                        UserDetails userDetails = userDetailsService.loadUserById(Long.parseLong(userId));

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        log.warn("JWT 認証エラー: トークンが失効しています");
                    }
                } else {
                    log.warn("JWT 認証エラー: トークンが無効です");
                }
            }
        } catch (Exception ex) {
            log.error("JWT 認証中に例外が発生しました: {}", ex.getMessage(), ex);
        }

        filterChain.doFilter(request, response);
    }
}
