package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
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
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ★ IntegerからLongに変更

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_id", nullable = false)
    private Employee evaluator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Employee target;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    private Phase phase;

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
    // 評価者
    @Column(nullable = false)
    private Integer evaluatorId;

    // 被評価者
    @Column(nullable = false)
    private Integer targetId;
    
    @Column
    private Float skill_score;

    @Column
    private Float business_score;

    @Column
    private Float team_score;

    @Column(columnDefinition = "text")
    private String comment;

    @Column(nullable = false)
    private Integer phaseId;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    // ----------------------------
    // ライフサイクルフック
    // ----------------------------
    @PrePersist//ライフサイクルフックを使用することによって更新や登録後に勝手に時間関係の処理をいれてくれる
    public void onCreate() {
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updated_at = LocalDateTime.now();
    }

    // ----------------------------
    // Getter & Setter
    // ----------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(Integer evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Float getSkill_score() {
        return skill_score;
    }

    public void setSkill_score(Float skill_score) {
        this.skill_score = skill_score;
    }

    public Float getBusiness_score() {
        return business_score;
    }

    public void setBusiness_score(Float business_score) {
        this.business_score = business_score;
    }

    public Float getTeam_score() {
        return team_score;
    }

    public void setTeam_score(Float team_score) {
        this.team_score = team_score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Integer phaseId) {
        this.phaseId = phaseId;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Employee getEvaluator() { return evaluator; }
    public void setEvaluator(Employee evaluator) { this.evaluator = evaluator; }
    public Employee getTarget() { return target; }
    public void setTarget(Employee target) { this.target = target; }
    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }
    public BigDecimal getSkillScore() { return skillScore; }
    public void setSkillScore(BigDecimal skillScore) { this.skillScore = skillScore; }
    public BigDecimal getBusinessScore() { return businessScore; }
    public void setBusinessScore(BigDecimal businessScore) { this.businessScore = businessScore; }
    public BigDecimal getTeamScore() { return teamScore; }
    public void setTeamScore(BigDecimal teamScore) { this.teamScore = teamScore; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
