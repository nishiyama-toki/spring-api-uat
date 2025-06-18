package com.example.api.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PhaseResponse {
    private Long phaseId;
    private Integer phaseNumber;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isClosed;
}
