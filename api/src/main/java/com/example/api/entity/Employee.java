package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees", schema = "evaluation") // ← schemaは必須
@Getter @Setter @NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;  // ← Integerで統一

    @Column(nullable = false)
    private String name; // 氏名

    @Column(nullable = false, unique = true)
    private String email; // メールアドレス（ユニーク制約）

    @Column(nullable = false)
    private String password; // ハッシュ化パスワード

    @Column(name = "is_admin")
    private Boolean isAdmin; // 管理者フラグ

    @Column(nullable = false)
    private String permission; // 権限文字列

    @Column(name = "failed_count")
    private Integer failedCount; // ログイン失敗回数

    @Column(name = "is_locked")
    private Boolean isLocked; // ロック中か

    @Column(name = "locked_at")
    private LocalDateTime lockedAt; // ロック時刻

    private String role; // 役職名など
}
