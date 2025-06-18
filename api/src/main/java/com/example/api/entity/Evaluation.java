// package com.example.api.entity;

// import jakarta.persistence.*;
// import java.math.BigDecimal;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "evaluations", schema = "evaluation")
// public class Evaluation {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Integer id;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "evaluator_id", nullable = false)
//     private Employee evaluator;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "target_id", nullable = false)
//     private Employee target;

//     @ManyToOne(fetch = FetchType.LAZY)
//     @JoinColumn(name = "phase_id", nullable = false)
//     private Phase phase;

//     @Column(name = "skill_score", precision = 3, scale = 1)
//     private BigDecimal skillScore;

//     @Column(name = "business_score", precision = 3, scale = 1)
//     private BigDecimal businessScore;

//     @Column(name = "team_score", precision = 3, scale = 1)
//     private BigDecimal teamScore;

//     @Column(columnDefinition = "TEXT")
//     private String comment;

//     @Column(name = "created_at",
//             columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP",
//             updatable = false)
//     private LocalDateTime createdAt;

//     @Column(name = "updated_at",
//             columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
//     private LocalDateTime updatedAt;

//     public Integer getId() { return id; }
//     public void setId(Integer id) { this.id = id; }

//     public Employee getEvaluator() { return evaluator; }
//     public void setEvaluator(Employee evaluator) { this.evaluator = evaluator; }

//     public Employee getTarget() { return target; }
//     public void setTarget(Employee target) { this.target = target; }

//     public Phase getPhase() { return phase; }
//     public void setPhase(Phase phase) { this.phase = phase; }

//     public BigDecimal getSkillScore() { return skillScore; }
//     public void setSkillScore(BigDecimal skillScore) { this.skillScore = skillScore; }

//     public BigDecimal getBusinessScore() { return businessScore; }
//     public void setBusinessScore(BigDecimal businessScore) { this.businessScore = businessScore; }

//     public BigDecimal getTeamScore() { return teamScore; }
//     public void setTeamScore(BigDecimal teamScore) { this.teamScore = teamScore; }

//     public String getComment() { return comment; }
//     public void setComment(String comment) { this.comment = comment; }

//     public LocalDateTime getCreatedAt() { return createdAt; }
//     public LocalDateTime getUpdatedAt() { return updatedAt; }
// }


package com.example.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(name = "created_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP",
            updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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
    // ★★★ 不足していたメソッドを追加 ★★★
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    // ★★★ 不足していたメソッドを追加 ★★★
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
