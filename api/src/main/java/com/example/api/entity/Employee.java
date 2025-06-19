package com.example.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// employeesテーブルに対応するEntity
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // PK

    @Column(nullable = false)
    private String name;           // 氏名

    @Column(nullable = false, unique = true)
    private String email;          // メールアドレス

    @Column(nullable = false)
    private String password;       // ハッシュ化済み PW

    @Column(name = "is_admin")
    private Boolean isAdmin;       // 管理者フラグ

    @Column(nullable = false)
    private String permission;     // 権限文字列

    @Column(name = "failed_count")
    private Integer failedCount;   // ログイン失敗回数

    @Column(name = "is_locked")
    private Boolean isLocked;      // ロック中か

    @Column(name = "locked_at")
    private LocalDateTime lockedAt; // ロック時刻

    private String role;           // 役職

    // --- コンストラクタ ---
    public Employee() {}

    // --- Getter ---
    public Long getId() { return id; }                       // PK
    public String getName() { return name; }                 // 氏名
    public String getEmail() { return email; }               // メールアドレス
    public String getPassword() { return password; }         // ハッシュ化済み PW
    public Boolean getIsAdmin() { return isAdmin; }          // 管理者フラグ
    public String getPermission() { return permission; }     // 権限文字列
    public Integer getFailedCount() { return failedCount; }  // ログイン失敗回数
    public Boolean getIsLocked() { return isLocked; }        // ロック中か
    public LocalDateTime getLockedAt() { return lockedAt; }  // ロック時刻
    public String getRole() { return role; }                 // 役職

    // --- Setter ---
    public void setId(Long id) { this.id = id; }                             // PK
    public void setName(String name) { this.name = name; }                  // 氏名
    public void setEmail(String email) { this.email = email; }              // メールアドレス
    public void setPassword(String password) { this.password = password; }  // ハッシュ化済み PW
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }     // 管理者フラグ
    public void setPermission(String permission) { this.permission = permission; } // 権限文字列
    public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; } // ログイン失敗回数
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; } // ロック中か
    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; } // ロック時刻
    public void setRole(String role) { this.role = role; }                  // 役職

    // --- 補足メソッド（booleanとしてのisAdmin判定）
    public boolean isAdmin() {
        return Boolean.TRUE.equals(isAdmin);
    }
}
