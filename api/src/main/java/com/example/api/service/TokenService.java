// 発行済みJWTのDB管理（ログアウト検出用）
package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.entity.JwtToken;
import com.example.api.repository.JwtTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenService.class);
    private final JwtTokenRepository jwtTokenRepository;

    /**
     * JWT を発行したあと DB に保存するだけのサービス.
     */
    @Transactional
    public void saveToken(Employee employee, String rawToken) {

        LocalDateTime now = LocalDateTime.now();

        JwtToken jwtToken = new JwtToken();
        jwtToken.setEmployee(employee);
        jwtToken.setToken(rawToken);
        jwtToken.setIsRevoked(false);         // 未失効
        jwtToken.setIssuedAt(now);
        jwtToken.setExpiredAt(now.plusHours(1));

        /* createdAt フィールドを削除したので setCreatedAt(...) は呼ばない */
        jwtTokenRepository.saveAndFlush(jwtToken);
    }
}

