package com.example.api.entity;

import jakarta.persistence.*;
// import lombok.Getter; // Lombokが不安定なため、手動でGetter/Setterを定義
// import lombok.Setter;
// import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
// @Getter @Setter @NoArgsConstructor // Lombokの代わりに手動で定義
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    private String permission;

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "locked_at")
    private java.time.LocalDateTime lockedAt;

    private String role;

    // --- Constructors ---
    public Employee() {}

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Boolean getIsAdmin() { return isAdmin; }
    public void setIsAdmin(Boolean isAdmin) { this.isAdmin = isAdmin; }
    public String getPermission() { return permission; }
    public void setPermission(String permission) { this.permission = permission; }
    public Integer getFailedCount() { return failedCount; }
    public void setFailedCount(Integer failedCount) { this.failedCount = failedCount; }
    public Boolean getIsLocked() { return isLocked; }
    public void setIsLocked(Boolean isLocked) { this.isLocked = isLocked; }
    public java.time.LocalDateTime getLockedAt() { return lockedAt; }
    public void setLockedAt(java.time.LocalDateTime lockedAt) { this.lockedAt = lockedAt; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
