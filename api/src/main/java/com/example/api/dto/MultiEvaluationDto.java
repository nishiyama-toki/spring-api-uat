package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public class MultiEvaluationDto {

    @JsonProperty("phase_id")
    @NotNull(message = "phaseId は必須です")
    private Integer phaseId;

    @NotEmpty(message = "evaluations は1件以上必要です")
    private List<@Valid PostEvaluationDto> evaluations; // ← 差し替えた！

    public Integer getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Integer phaseId) {
        this.phaseId = phaseId;
    }

    public List<PostEvaluationDto> getEvaluations() {
        return evaluations;
    }

    public void setEvaluations(List<PostEvaluationDto> evaluations) {
        this.evaluations = evaluations;
    }

    @Override
    public String toString() {
        return "MultiEvaluationDto{" +
                "phaseId=" + phaseId +
                ", evaluations=" + evaluations +
                '}';
    }
}
