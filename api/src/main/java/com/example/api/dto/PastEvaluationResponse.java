package com.example.api.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 過去の評価履歴の結果をフロントエンドに返すためのクラス(DTO)
 */
public class PastEvaluationResponse {
    private String name;
    private Integer phaseNumber; // ← 追加！
    private BigDecimal averageSkillScore;
    private BigDecimal averageBusinessScore;
    private BigDecimal averageTeamScore;
    private List<CommentDTO> comments;
    private List<EvaluationRawDTO> rawEvaluations;

    public PastEvaluationResponse() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getPhaseNumber() { return phaseNumber; } // ← 追加！
    public void setPhaseNumber(Integer phaseNumber) { this.phaseNumber = phaseNumber; } // ← 追加！

    public BigDecimal getAverageSkillScore() { return averageSkillScore; }
    public void setAverageSkillScore(BigDecimal averageSkillScore) { this.averageSkillScore = averageSkillScore; }

    public BigDecimal getAverageBusinessScore() { return averageBusinessScore; }
    public void setAverageBusinessScore(BigDecimal averageBusinessScore) { this.averageBusinessScore = averageBusinessScore; }

    public BigDecimal getAverageTeamScore() { return averageTeamScore; }
    public void setAverageTeamScore(BigDecimal averageTeamScore) { this.averageTeamScore = averageTeamScore; }

    public List<CommentDTO> getComments() { return comments; }
    public void setComments(List<CommentDTO> comments) { this.comments = comments; }

    public List<EvaluationRawDTO> getRawEvaluations() { return rawEvaluations; }
    public void setRawEvaluations(List<EvaluationRawDTO> rawEvaluations) { this.rawEvaluations = rawEvaluations; }
}
