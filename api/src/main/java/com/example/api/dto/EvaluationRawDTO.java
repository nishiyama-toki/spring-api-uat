// src/main/java/com/example/api/dto/EvaluationRawDTO.java
package com.example.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EvaluationRawDTO {
    @JsonProperty("skillScore")
    private float skillScore;

    @JsonProperty("businessScore")
    private float businessScore;

    @JsonProperty("teamScore")
    private float teamScore;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("evaluatorName")
    private String evaluatorName;

    public EvaluationRawDTO(
        float skillScore,
        float businessScore,
        float teamScore,
        String comment,
        String evaluatorName
    ) {
        this.skillScore    = skillScore;
        this.businessScore = businessScore;
        this.teamScore     = teamScore;
        this.comment       = comment;
        this.evaluatorName = evaluatorName;
    }

    // getters・setter（省略可）
    public float getSkillScore()    { return skillScore; }
    public float getBusinessScore() { return businessScore; }
    public float getTeamScore()     { return teamScore; }
    public String getComment()      { return comment; }
    public String getEvaluatorName(){ return evaluatorName; }
}
