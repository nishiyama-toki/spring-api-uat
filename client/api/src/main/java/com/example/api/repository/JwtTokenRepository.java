// DBへのトークン保存リポジトリ
package com.example.api.repository;

import com.example.api.entity.JwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.time.LocalDateTime;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);
    void deleteByExpiredAtBefore(LocalDateTime now);
}
