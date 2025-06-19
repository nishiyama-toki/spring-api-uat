package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "employees")
@Getter @Setter @NoArgsConstructor
public class Employee implements UserDetails {

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
    private LocalDateTime lockedAt; // ロック時刻

    private String role;            // 役職

    // ==== UserDetails の実装部分 ====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 必要に応じてロールを返す
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 有効期限切れの概念を使わないなら true 固定
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(this.isLocked); // ロックされていないとき true
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // パスワード有効期限も使わないなら true 固定
    }

    @Override
    public boolean isEnabled() {
        return true; // アカウント無効フラグなどを使うならここで制御
    }
}
