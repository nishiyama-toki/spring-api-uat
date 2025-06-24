package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "employees", schema = "evaluation")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_admin")
    @JsonProperty("isAdmin")//JavaオブジェクトとJSONのプロパティ名の対応を指定するため
    private boolean isAdmin;

    private String permission;
    private Integer failedCount;

    @Column(nullable = false)
    private String role;

    @Column(name = "is_locked")
    private boolean isLocked;

    private Timestamp lockedAt;

    // ----- Getter -----

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public String getRole() {
    return role;
    }

    public String getPermission() {
        return permission;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public Timestamp getLockedAt() {
        return lockedAt;
    }


    // ----- Setter -----

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public void setRole(String role) {
    this.role = role;
    }


    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setLockedAt(Timestamp lockedAt) {
        this.lockedAt = lockedAt;
    }
}
