package com.example.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS設定を有効にする
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // CSRF保護はAPIサーバーでは不要なことが多いので無効化
            .csrf(csrf -> csrf.disable())
            // 全てのリクエストを一旦許可（認証は今後JWTフィルターで実装）
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // ★ フロントエンドのURLを許可
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        // 許可するHTTPメソッド
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 許可するHTTPヘッダー
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 全てのパスに対してこの設定を適用
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}