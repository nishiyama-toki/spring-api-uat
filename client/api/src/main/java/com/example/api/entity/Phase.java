package com.example.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "phases")
public class Phase {

    /* ---------- カラム ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phase_number", nullable = false)
    private Integer phaseNumber;       // 期（1,2,3…）

    @Column(name = "name", nullable = false)
    private String periodName;         // クォーター名（Q1 / Q2 …）

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "self_eval_due")
    private LocalDateTime selfEvalDue;

    @Column(name = "peer_eval_due")
    private LocalDateTime peerEvalDue;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* ---------- Getter / Setter ---------- */
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

    public String getPeriodName() { return periodName; }
    public void setPeriodName(String periodName) { this.periodName = periodName; }

    // ── もし “名前” を汎用的に呼び出したい場合は ↓ をシンタックスシュガーとして残す
    @Transient
    public String getName() { return periodName; }
    public void setName(String name) { this.periodName = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDateTime getSelfEvalDue() { return selfEvalDue; }
    public void setSelfEvalDue(LocalDateTime selfEvalDue) { this.selfEvalDue = selfEvalDue; }

    public LocalDateTime getPeerEvalDue() { return peerEvalDue; }
    public void setPeerEvalDue(LocalDateTime peerEvalDue) { this.peerEvalDue = peerEvalDue; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
