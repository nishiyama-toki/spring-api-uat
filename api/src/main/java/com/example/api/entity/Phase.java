package com.example.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "phase_number", nullable = false)
    private Integer phaseNumber;  // 期番号 (例：18)

    @Column(name = "name", nullable = false)
    private String name;          // 四半期文字列 (例："1Q")

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "self_eval_due")
    private LocalDateTime selfEvalDue;

    @Column(name = "peer_eval_due")
    private LocalDateTime peerEvalDue;

    @Column(name = "created_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP",
            updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at",
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalDateTime getSelfEvalDue() { return selfEvalDue; }
    public void setSelfEvalDue(LocalDateTime selfEvalDue) { this.selfEvalDue = selfEvalDue; }

    public LocalDateTime getPeerEvalDue() { return peerEvalDue; }
    public void setPeerEvalDue(LocalDateTime peerEvalDue) { this.peerEvalDue = peerEvalDue; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
