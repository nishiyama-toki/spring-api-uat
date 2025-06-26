package com.example.api.dto;

import java.time.LocalDate;

// Lombokのアノテーションは削除し、手動でGetter/Setter/ofメソッドを定義する形にしました
public class EvaluationResponseDTO {

    private Long      phaseId;
    private Long      targetId;
    private String    targetName;
    private Integer   phaseNumber;
    private String    quarterName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String    evaluationType;

    // 空のコンストラクタ
    public EvaluationResponseDTO() {
    }

    /**
     * DTOを生成するためのstaticファクトリメソッド
     */
    public static EvaluationResponseDTO of(
            Long phaseId,
            Long targetId,
            String targetName,
            Integer phaseNumber,
            String quarterName,
            LocalDate startDate,
            LocalDate endDate,
            String evaluationType) {
        
        EvaluationResponseDTO dto = new EvaluationResponseDTO();
        dto.setPhaseId(phaseId);
        dto.setTargetId(targetId);
        dto.setTargetName(targetName);
        dto.setPhaseNumber(phaseNumber);
        dto.setQuarterName(quarterName);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setEvaluationType(evaluationType);
        return dto;
    }

    // --- Getters and Setters ---
    public Long getPhaseId() { return phaseId; }
    public void setPhaseId(Long phaseId) { this.phaseId = phaseId; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }

    public Integer getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; }

    public String getQuarterName() { return quarterName; }
    public void setQuarterName(String quarterName) { this.quarterName = quarterName; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getEvaluationType() { return evaluationType; }
    public void setEvaluationType(String evaluationType) { this.evaluationType = evaluationType; }
}