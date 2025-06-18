package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "employees", schema = "evaluation")
@Data
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private Boolean isAdmin;

    private String permission;
    private Integer failedCount;
    private Boolean isLocked;
    private LocalDateTime lockedAt;
    private String role;
}
