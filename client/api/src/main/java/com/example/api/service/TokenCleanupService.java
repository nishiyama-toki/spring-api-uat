// 期限切れトークンの自動削除処理
package com.example.api.service;

import com.example.api.repository.JwtTokenRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TokenCleanupService {

    private final JwtTokenRepository jwtTokenRepository;

    public TokenCleanupService(JwtTokenRepository jwtTokenRepository) {
        this.jwtTokenRepository = jwtTokenRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")  // 毎日3時に実行
    @Transactional
    public void cleanupExpiredTokens() {
        jwtTokenRepository.deleteByExpiredAtBefore(LocalDateTime.now());
    }
}

