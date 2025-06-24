package com.example.api.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 過去の評価履歴の結果をフロントエンドに返すためのクラス(DTO)
 */
public class PastEvaluationResponse {
    // このDTOに含まれるべきフィールドを想定して定義
    private String name;
    private BigDecimal averageSkillScore;
    private BigDecimal averageBusinessScore;
    private BigDecimal averageTeamScore;
    private List<CommentDTO> comments;
    private List<EvaluationRawDTO> rawEvaluations;

    // テストコードから new PastEvaluationResponse() で呼び出せるように、空のコンストラクタを用意
    public PastEvaluationResponse() {}

    // --- 以下、エラーログで不足していたGetters/Setters ---
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

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
