// package com.example.api.config;

// import com.example.api.repository.EmployeeRepository; // ←追加！

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// @EnableMethodSecurity
// public class SecurityConfig {

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http.csrf(csrf -> csrf.disable())
//             .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
//             .httpBasic();

//         return http.build();
//     }

//     @Bean
//     public UserDetailsService userDetailsService(EmployeeRepository employeeRepository) {
//         return username -> employeeRepository.findByEmail(username)
//             .map(emp -> org.springframework.security.core.userdetails.User.builder()
//                 .username(emp.getEmail())
//                 .password(emp.getPassword())
//                 .authorities(emp.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER")
//                 .build()
//             ).orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }
// }
