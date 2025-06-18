package com.example.api.dto;

import java.math.BigDecimal;
// ★★★ 修正点①：スラッシュ(/)をドット(.)に修正 ★★★
import com.fasterxml.jackson.annotation.JsonProperty;

public class SelfEvaluationRequest {

    // ★★★ 修正点②：変数名をJavaの標準的なキャメルケースに統一 ★★★
    @JsonProperty("phase_id")
    private Integer phaseId;

    @JsonProperty("evaluator_id")
    private Integer evaluatorId;

    @JsonProperty("target_id")
    private Integer targetId;

    @JsonProperty("skill_score")
    private BigDecimal skillScore;

    @JsonProperty("business_score")
    private BigDecimal businessScore;

    @JsonProperty("team_score")
    private BigDecimal teamScore;

    private String comment;

    // --- 以下、Getters と Setters ---
    // (上記の変数名変更に合わせて、正しく動作するようになっています)
    public Integer getPhaseId() { return phaseId; }
    public void setPhaseId(Integer phaseId) { this.phaseId = phaseId; }

    public Integer getEvaluatorId() { return evaluatorId; }
    public void setEvaluatorId(Integer evaluatorId) { this.evaluatorId = evaluatorId; }

    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }

    public BigDecimal getSkillScore() { return skillScore; }
    public void setSkillScore(BigDecimal skillScore) { this.skillScore = skillScore; }

    public BigDecimal getBusinessScore() { return businessScore; }
    public void setBusinessScore(BigDecimal businessScore) { this.businessScore = businessScore; }

    public BigDecimal getTeamScore() { return teamScore; }
    public void setTeamScore(BigDecimal teamScore) { this.teamScore = teamScore; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
