package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public class PostEvaluationDto {

    @JsonProperty("target_id")
    @NotNull(message = "targetId は必須です")
    private Integer targetId;

    @JsonProperty("skill_score")
    @DecimalMin(value = "1.0", message = "スキルは1.0以上である必要があります")
    @DecimalMax(value = "5.0", message = "スキルは5.0以下である必要があります")
    private Float skillScore;

    @JsonProperty("business_score")
    @DecimalMin(value = "1.0", message = "ビジネスは1.0以上である必要があります")
    @DecimalMax(value = "5.0", message = "ビジネスは5.0以下である必要があります")
    private Float businessScore;

    @JsonProperty("team_score")
    @DecimalMin(value = "1.0", message = "チームマネジメントは1.0以上である必要があります")
    @DecimalMax(value = "5.0", message = "チームマネジメントは5.0以下である必要があります")
    private Float teamScore;

    @JsonProperty("comment")
    @Size(max = 255, message = "コメントは255文字以内で入力してください")
    private String comment;

    @JsonProperty("phase_id")
    private Integer phaseId;

    // --- Getter & Setter ---

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    public Float getSkillScore() {
        return skillScore;
    }

    public void setSkillScore(Float skillScore) {
        this.skillScore = skillScore;
    }

    public Float getBusinessScore() {
        return businessScore;
    }

    public void setBusinessScore(Float businessScore) {
        this.businessScore = businessScore;
    }

    public Float getTeamScore() {
        return teamScore;
    }

    public void setTeamScore(Float teamScore) {
        this.teamScore = teamScore;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Integer phaseId) {
        this.phaseId = phaseId;
    }

    // --- toString（ログ出力用） ---
    @Override
    public String toString() {
        return "PostEvaluationDto{" +
                "targetId=" + targetId +
                ", skillScore=" + skillScore +
                ", businessScore=" + businessScore +
                ", teamScore=" + teamScore +
                ", comment='" + comment + '\'' +
                ", phaseId=" + phaseId +
                '}';
    }
}
