package com.example.api.config;

import com.example.api.security.JwtAuthenticationFilter;
import com.example.api.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
 * 統合版 SecurityConfig
 *
 * ・1st バージョンの CORS 設定（exposedHeaders="Authorization" を含む）
 * ・2nd バージョンの HttpMethod 指定 & "/api/phases/**" ワイルドカード許可
 * ・GET /api/multi-evaluations/targets/** は認証必須
 * ・ADMIN 専用エンドポイントは hasRole("ADMIN")
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // --- 認証不要エンドポイント ---
                .requestMatchers(
                    "/api/login",
                    "/api/reset-mail",
                    "/api/reset-password/**",
                    "/api/phases/**"   // ワイルドカード許可
                ).permitAll()

                // --- ログイン済みユーザーに許可（例: 多面評価対象取得） ---
                .requestMatchers(HttpMethod.GET, "/api/multi-evaluations/targets/**").authenticated()

                // --- 管理者専用 ---
                .requestMatchers(
                    "/api/admin-only",
                    "/api/admin-only/**",
                    "/api/user_management_register",
                    "/api/user_management_edit",
                    "/api/user_management_delete",
                    "/api/user_management_DB",
                    "/api/submission_period",
                    "/api/submission_period_edit",
                    "/api/unsubmitted",
                    "/api/reminder/batch"
                ).hasRole("ADMIN")

                // --- その他はすべて認証必須 ---
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * CORS 設定
     * 本番環境では allowedOriginPatterns を限定すること
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*")); // TODO: 本番では限定する
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("Authorization")); // JWT をフロントに返す場合など
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
