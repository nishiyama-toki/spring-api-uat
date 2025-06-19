// employeesテーブルに対応するEntity
package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter @Setter @NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // PK

    @Column(nullable = false)
    private String name;            // 氏名

    @Column(nullable = false, unique = true)
    private String email;           // メールアドレス

    @Column(nullable = false)
    private String password;        // ハッシュ化済み PW

    @Column(name = "is_admin")
    private Boolean isAdmin;        // 管理者フラグ

    @Column(nullable = false)
    private String permission;      // 権限文字列

    @Column(name = "failed_count")
    private Integer failedCount;    // ログイン失敗回数

    @Column(name = "is_locked")
    private Boolean isLocked;       // ロック中か

    @Column(name = "locked_at")
    private java.time.LocalDateTime lockedAt; // ロック時刻

    private String role;            // 役職
}

