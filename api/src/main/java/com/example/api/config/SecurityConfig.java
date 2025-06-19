package com.example.api.config;

import com.example.api.security.JwtAuthenticationFilter;
import com.example.api.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 🔽 追加されたJwt用のフィルターをDI（JWT認証フィルター）
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 🔽 カスタムユーザー詳細サービスのDI（ログイン認証用）
    private final CustomUserDetailsService customUserDetailsService;

    // 🔧 コンストラクタでDI
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    // 🔐 セキュリティ設定（フィルタチェイン定義）
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable() // 🟡 警告出るが削除予定API。将来は「.csrf(csrf -> csrf.disable())」形式へ
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/login",               // ログインAPI
                    "/api/reset-mail",          // パスワードリセットメール送信
                    "/api/reset-password/**"    // パスワード変更
                ).permitAll()
                .anyRequest().authenticated()  // それ以外はすべて認証必要
            )
            // 🔽 JWTフィルターをログイン処理の前に挿入
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔑 認証マネージャのBean定義
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 🔐 パスワードエンコーダー（ハッシュ化）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 🧩 カスタムユーザー詳細サービス + パスワードエンコーダーを組み合わせる
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // 🌐 CORS（クロスオリジン）設定
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Reactフロント
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // CookieやAuthorizationヘッダを許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
