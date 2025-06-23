// jwt_tokens テーブルの管理
package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "jwt_tokens")
@Getter @Setter @NoArgsConstructor
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                         // PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;               // 発行対象

    @Column(nullable = false, length = 255)
    private String token;                    // JWT 本体

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;          // 発行日時

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;         // 失効日時

    @Column(name = "is_revoked", nullable = false)
    private Boolean isRevoked;               // 無効化フラグ
}
