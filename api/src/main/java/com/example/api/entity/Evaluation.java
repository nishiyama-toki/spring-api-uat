package com.example.api.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations", schema = "evaluation")
@Data
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer evaluatorId;
    private Integer targetId;
    private Integer phaseId;

    private BigDecimal skillScore;
    private BigDecimal businessScore;
    private BigDecimal teamScore;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
