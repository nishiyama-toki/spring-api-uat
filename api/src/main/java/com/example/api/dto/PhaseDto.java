package com.example.api.phase.dto;

import com.example.api.entity.Phase;
import java.time.LocalDate;

public class PhaseDto {

    private String periodName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer phaseNumber;

    // --- エンティティ変換 ---
    public Phase toEntity() {
    Phase p = new Phase();
    p.setName(this.periodName); // 修正された名前
    p.setStartDate(this.startDate);
    p.setEndDate(this.endDate);
    p.setPhaseNumber(this.phaseNumber);
    return p;
}

    // --- Getter & Setter ---
    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(Integer phaseNumber) {
        this.phaseNumber = phaseNumber;
    }
}
