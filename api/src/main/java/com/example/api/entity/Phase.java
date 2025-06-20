package com.example.api.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "phases")
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phase_number", nullable = false)
    private Integer phaseNumber;

    @Column(name = "name", nullable = false)
    private String name;

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

    // getters/setters (省略)
    public Long getId() {return id;}
    public void setId(Long id){this.id=id;}
    public Integer getPhaseNumber(){return phaseNumber;}
    public void setPhaseNumber(Integer p){this.phaseNumber=p;}
    public String getName(){return name;}
    public void setName(String n){this.name=n;}
    public LocalDate getStartDate(){return startDate;}
    public void setStartDate(LocalDate d){this.startDate=d;}
    public LocalDate getEndDate(){return endDate;}
    public void setEndDate(LocalDate d){this.endDate=d;}
    public LocalDateTime getSelfEvalDue(){return selfEvalDue;}
    public void setSelfEvalDue(LocalDateTime t){this.selfEvalDue=t;}
    public LocalDateTime getPeerEvalDue(){return peerEvalDue;}
    public void setPeerEvalDue(LocalDateTime t){this.peerEvalDue=t;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime t){this.createdAt=t;}
    public LocalDateTime getUpdatedAt(){return updatedAt;}
    public void setUpdatedAt(LocalDateTime t){this.updatedAt=t;}
}