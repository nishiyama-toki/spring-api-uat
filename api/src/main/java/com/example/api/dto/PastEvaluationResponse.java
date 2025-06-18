// src/main/java/com/example/api/dto/PastEvaluationResponse.java
package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class PastEvaluationResponse {
    @JsonProperty("phase_number")
    private int phaseNumber;

    @JsonProperty("name")
    private String name;

    // （従来の平均用フィールドは残しておいても OK）
    @JsonProperty("skillScore")
    private float skillScore;

    @JsonProperty("businessScore")
    private float businessScore;

    @JsonProperty("teamScore")
    private float teamScore;

    @JsonProperty("comments")
    private List<CommentDTO> comments;

    // ← ここに追加 ↓
    @JsonProperty("rawEvaluations")
    private List<EvaluationRawDTO> rawEvaluations;

    // --- getters / setters ---
    public int getPhaseNumber() { return phaseNumber; }
    public void setPhaseNumber(int n) { this.phaseNumber = n; }

    public String getName() { return name; }
    public void setName(String s) { this.name = s; }

    public float getSkillScore() { return skillScore; }
    public void setSkillScore(float f) { this.skillScore = f; }

    public float getBusinessScore() { return businessScore; }
    public void setBusinessScore(float f) { this.businessScore = f; }

    public float getTeamScore() { return teamScore; }
    public void setTeamScore(float f) { this.teamScore = f; }

    public List<CommentDTO> getComments() { return comments; }
    public void setComments(List<CommentDTO> list) { this.comments = list; }

    public List<EvaluationRawDTO> getRawEvaluations() { return rawEvaluations; }
    public void setRawEvaluations(List<EvaluationRawDTO> list) { this.rawEvaluations = list; }
}
