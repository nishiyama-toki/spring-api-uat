package com.example.api.config;

import com.example.api.security.JwtAuthenticationFilter;
import com.example.api.security.TokenRefreshFilter; // 追加
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

    //追加されたJwt用のフィルターをDI（JWT認証フィルター）
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //カスタムユーザー詳細サービスのDI（ログイン認証用）
    private final TokenRefreshFilter tokenRefreshFilter; // 追加
    private final CustomUserDetailsService customUserDetailsService;

    // 🔧 コンストラクタでDI
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          TokenRefreshFilter tokenRefreshFilter,
                          CustomUserDetailsService customUserDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.tokenRefreshFilter = tokenRefreshFilter;
        this.customUserDetailsService = customUserDetailsService;
    }

    //セキュリティ設定（フィルタチェイン定義）
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/login",
                    "/api/phases",
                    "/api/reset-mail",
                    "/api/reset-password/**"
                ).permitAll()

                 //管理者のみアクセス可能なエンドポイント
            .requestMatchers(
                "/api/admin-only",
                "/api/user_management_register",
                "/api/user_management_edit",
                "/api/user_management_delete",
                "/api/user_management_DB"
            ).hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()  // それ以外はすべて認証必要
            )
            //JWTフィルターをログイン処理の前に挿入し、トークンリフレッシュフィルターをその後に
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterAfter(tokenRefreshFilter, JwtAuthenticationFilter.class) // 追加（認証後にトークン再発行）
            // DaoAuthenticationProvider を明示的に登録
            .authenticationProvider(authenticationProvider());
        return http.build();
    }

    //認証マネージャのBean定義
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //パスワードエンコーダー（ハッシュ化）
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //カスタムユーザー詳細サービス + パスワードエンコーダーを組み合わせる
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    //CORS（クロスオリジン）設定
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // ここを setAllowedOrigins から setAllowedOriginPatterns に変更
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true); // CookieやAuthorizationヘッダを許可

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
