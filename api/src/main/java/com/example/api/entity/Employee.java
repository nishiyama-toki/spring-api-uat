package com.example.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// employeesテーブルに対応するEntity
@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
public class Employee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Longに変更

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(nullable = false)
    private String permission;          // 例: "ADMIN" / "USER"

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    private String role;

    // 補助メソッド
    public boolean isAdmin() {
        return Boolean.TRUE.equals(isAdmin);
    }

    // ==== UserDetails の実装 ====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // "ADMIN" → "ROLE_ADMIN" に変換して返す
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.permission.toUpperCase()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Boolean.TRUE.equals(this.isLocked);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
