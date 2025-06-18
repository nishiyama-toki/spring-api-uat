package com.example.api.entity;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Data;

@Entity 
@Table(name = "employees")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
    
    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "permission")
    private String permission;

    @Column(name = "failed_count")
    private Integer failedCount;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "role")
    private String role;

    @ManyToOne
    @JoinColumn(name = "target_id", insertable = false, updatable = false)
    private Employee target;
}