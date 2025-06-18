
// このクラスは JPA（Java Persistence API）による Entity（データベースの1テーブルに対応するJavaクラス）である
package com.example.api.entity;

// --- 必要なアノテーションやクラスをインポート ---
import jakarta.persistence.*;
import java.sql.Timestamp;

// このクラスをJPAのエンティティとして指定（DBテーブルと対応）
@Entity

// 対応するテーブル名とスキーマ名を指定（evaluation.employees）
@Table(name = "employees", schema = "evaluation")
public class Employee {

    // --- フィールド定義 ---

    // 主キー。自動採番（serial）に対応
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // praivate : その変数やメソッドを「同じクラスの中からしか使えない」ように制限するキーワード

    // 氏名（NOT NULL）
    private String name;

    // メールアドレス（NOT NULL、ユニーク制約あり）
    private String email;

    // パスワード（ハッシュ化された値）
    private String password;

    // 管理者フラグ（true: 管理者, false: 一般ユーザー）
    @Column(name = "is_admin")
    private Boolean isAdmin;

    // 権限（例："FULL", "LIMITED"など）
    private String permission;

    // ログイン失敗回数（初期は0）
    @Column(name = "failed_count")
    private Integer failedCount;

    // アカウントロックフラグ
    @Column(name = "is_locked")
    private Boolean isLocked;

    // ロックされた日時（nullの場合は未ロック）
    @Column(name = "locked_at")
    private Timestamp lockedAt;

    // 役職（例："一般社員", "マネージャー"）
    private String role;

    // --- getter・setter（Lombokを使わない場合は手動で書く） ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Timestamp getLockedAt() {
        return lockedAt;
    }

    public void setLockedAt(Timestamp lockedAt) {
        this.lockedAt = lockedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}