package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
@Data
public class Evaluation {

    @ManyToOne // 多対一の関係を定義
    @JoinColumn(name = "target_id", insertable = false, updatable = false) // "target_id" カラムで結合
    private Employee target; // 結合先のEmployeeオブジェクトを保持するフィールド

    @ManyToOne
    @JoinColumn(name = "evaluator_id", insertable = false, updatable = false)
    private Employee evaluator;

    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false, insertable = false, updatable = false)
    private Phase phase;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluator_id")
    private Long evaluatorId;

    @Column(name = "target_id")
    private Long targetId;

    // @Column(name = "phase_id")
    // private Long phaseId;

    @Column(name = "skill_score")
    private Double skillScore;

    @Column(name = "business_score")
    private Double businessScore;

    @Column(name = "team_score")
    private Double teamScore;

    @Column(name = "comment")
    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}