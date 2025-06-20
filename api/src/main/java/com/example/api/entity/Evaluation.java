package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Data
public class Evaluation {

    @Column(name = "evaluator_id", nullable = false)
    private Long evaluatorId;
    @Column(name = "target_id", nullable = false)
    private Long targetId;
    @Column(name = "phase_id", nullable = false)
    private Long phaseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id", insertable = false, updatable = false)
    private Employee evaluator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", insertable = false, updatable = false)
    private Employee target;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", insertable = false, updatable = false)
    private Phase phase;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "skill_score", precision = 3, scale = 1)
    private BigDecimal skillScore;
    @Column(name = "business_score", precision = 3, scale = 1)
    private BigDecimal businessScore;
    @Column(name = "team_score", precision = 3, scale = 1)
    private BigDecimal teamScore;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}