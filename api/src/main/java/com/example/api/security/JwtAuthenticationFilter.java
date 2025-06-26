package com.example.api.security;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final EmployeeRepository employeeRepository;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   EmployeeRepository employeeRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.employeeRepository = employeeRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
                                    throws ServletException, IOException {

        String path = request.getRequestURI();

        //レンダーのデバックログ
        System.out.println(">>> リクエストパス: " + path);

        // ★★★ ログインとパスワードリセット関連はスルー！★★★
        if (path.equals("/api/login") || path.startsWith("/api/reset-")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveJwt(request);

        if (token != null && jwtTokenProvider.isValid(token)) {

            /* === 1. トークンから情報を取得 === */
            Long userId = Long.valueOf(jwtTokenProvider.extractUserId(token));
            String role = jwtTokenProvider.extractRole(token);   // "ROLE_ADMIN" か "ADMIN"

            /* === 2. 社員情報を取得 === */
            Employee employee = employeeRepository.findById(userId).orElse(null);

            if (employee != null) {

                //レンダーでのデバック用に追記、消しても良い
                System.out.println("▼▼▼ フィルター検証 ▼▼▼");
                System.out.println("トークンのrole: " + role); // JWTから取り出した
                String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase();
                System.out.println("GrantedAuthorityに入れる値: " + authority);
                System.out.println("employee.email: " + employee.getEmail());
                System.out.println("SecurityContextに登録してる？: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("▲▲▲ END ▲▲▲");

                /* === 3. ROLE_ 接頭辞の二重付与を防いで GrantedAuthority を作成 === */
                List<GrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(authority));

                /* === 4. SecurityContext にセット === */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(employee, null, authorities);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    /** Authorization ヘッダーから Bearer トークンを取り出す */
    private String resolveJwt(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (StringUtils.hasText(bearer) && bearer.startsWith("Bearer "))
               ? bearer.substring(7)
               : null;
    }
}