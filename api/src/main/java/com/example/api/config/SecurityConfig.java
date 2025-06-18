// テスト用にログイン認証を無効化
package com.example.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

   @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .cors().and()
        .csrf().disable()
        .authorizeHttpRequests(auth -> auth
            // .requestMatchers("/api/**").permitAll() // ← 一旦コメントアウト
            // .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll() // ← 一旦コメントアウト
            .anyRequest().permitAll() // ← すべてのリクエストを許可
        )
        .formLogin().disable(); // ログインページ無効化

    return http.build();
}


    //パスワードエンコーダー（BCrypt）をBean登録
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
