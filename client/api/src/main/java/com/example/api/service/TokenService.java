// 発行済みJWTのDB管理（ログアウト検出用）
package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.entity.JwtToken;
import com.example.api.repository.JwtTokenRepository;
import com.example.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Instant;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * JWT を発行したあと DB に保存するだけのサービス.
     */
    @Transactional
    public void saveToken(Employee employee, String rawToken) {
        Instant exp = jwtTokenProvider.extractExpiration(rawToken);

        LocalDateTime now = LocalDateTime.now();

        JwtToken jwtToken = new JwtToken();
        jwtToken.setEmployee(employee);
        jwtToken.setToken(rawToken);
        jwtToken.setIsRevoked(false);         // 未失効
        jwtToken.setIssuedAt(now);
        jwtToken.setExpiredAt(exp.atZone(ZoneId.systemDefault()).toLocalDateTime());

        /* createdAt フィールドを削除したので setCreatedAt(...) は呼ばない */
        jwtTokenRepository.saveAndFlush(jwtToken);
    }
}

