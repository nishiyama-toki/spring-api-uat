package com.example.api.config;

import com.example.api.security.JwtAuthenticationFilter;
import com.example.api.security.TokenRefreshFilter;
import com.example.api.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * アプリ全体の Spring Security 設定クラス。
 * - CORS をここに集約
 * - JWT 認証フィルターとリフレッシュトークンフィルターを挿入
 * - DaoAuthenticationProvider で CustomUserDetailsService を使用
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TokenRefreshFilter tokenRefreshFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          TokenRefreshFilter tokenRefreshFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.tokenRefreshFilter     = tokenRefreshFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    /** 認可・フィルターチェイン定義 */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // ★ 認証不要エンドポイント
                .requestMatchers(
                    "/api/login",
                    "/api/phases",
                    "/api/reset-mail",
                    "/api/reset-password/**"
                ).permitAll()

                // ★ 管理者のみ
                .requestMatchers(
                    "/api/admin-only",
                    "/api/user_management_register",
                    "/api/user_management_edit",
                    "/api/user_management_delete",
                    "/api/user_management_DB"
                ).hasAuthority("ROLE_ADMIN")

                // ★ それ以外は要認証
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(tokenRefreshFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    /** 認証マネージャ */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /** パスワードハッシュ用エンコーダー */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** DaoAuthenticationProvider で CustomUserDetailsService を利用 */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /** CORS 設定を Security 直下に集約 */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // Spring 6 以降はパターン指定に setAllowedOriginPatterns を推奨
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
