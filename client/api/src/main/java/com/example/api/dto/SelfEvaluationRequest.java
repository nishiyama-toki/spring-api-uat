package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class SelfEvaluationRequest {

    @JsonProperty("phase_id")
    private Long phaseId; // ★ IntegerからLongに変更

    @JsonProperty("evaluator_id")
    private Long evaluatorId;

    @JsonProperty("target_id")
    private Long targetId;

    @JsonProperty("skill_score")
    private BigDecimal skillScore;

    @JsonProperty("business_score")
    private BigDecimal businessScore;

    @JsonProperty("team_score")
    private BigDecimal teamScore;

    private String comment;

    // --- Getters and Setters (IDをLongに変更) ---
    public Long getPhaseId() { return phaseId; }
    public void setPhaseId(Long phaseId) { this.phaseId = phaseId; }

    public Long getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(Long evaluatorId) { this.evaluatorId = evaluatorId; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }
    
    // 他のgetter/setterは変更なし
    public BigDecimal getSkillScore() { return skillScore; }
    public void setSkillScore(BigDecimal skillScore) { this.skillScore = skillScore; }
    public BigDecimal getBusinessScore() { return businessScore; }
    public void setBusinessScore(BigDecimal businessScore) { this.businessScore = businessScore; }
    public BigDecimal getTeamScore() { return teamScore; }
    public void setTeamScore(BigDecimal teamScore) { this.teamScore = teamScore; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
