package com.example.api.security;

import com.example.api.entity.Employee;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenRefreshFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenRefreshFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response); // 認証後に実行されるようにする

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Employee employee) {
            String newToken = jwtTokenProvider.generateToken(employee);
            response.setHeader("Authorization", "Bearer " + newToken); // ヘッダーに新しいトークン
        }
    }

    // ログインなど一部のAPIは除外
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/api/login");
    }
}
