package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "phases", schema = "evaluation")
@Data
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime selfEvalDue;
    private LocalDateTime peerEvalDue;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer phaseNumber;
}
