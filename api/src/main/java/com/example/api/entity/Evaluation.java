package com.example.api.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation.evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
}
