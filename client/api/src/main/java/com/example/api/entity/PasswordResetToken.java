// password_reset_tokensテーブルに対応するENtity
package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
@Getter @Setter @NoArgsConstructor
public class PasswordResetToken {

    @Id                                              // PK は token 文字列
    @Column(length = 255)
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;                       // 対象ユーザー

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;                 // 有効期限

    private Boolean used;                            // 使用済みフラグ

    @Column(name = "created_at")
    private LocalDateTime createdAt;                 // 発行日時

}
