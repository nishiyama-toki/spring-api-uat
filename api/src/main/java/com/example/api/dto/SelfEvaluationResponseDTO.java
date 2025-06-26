package com.example.api.dto;

import com.example.api.entity.Evaluation;
import java.math.BigDecimal;

// DBから取得した自己評価データをフロントエンドに返すためのクラス
public class SelfEvaluationResponseDTO {

    private BigDecimal skillScore;
    private BigDecimal businessScore;
    private BigDecimal teamScore;
    private String comment;

    // 空のコンストラクタ
    public SelfEvaluationResponseDTO() {}

    // EvaluationエンティティからこのDTOを生成するためのコンストラクタ
    public SelfEvaluationResponseDTO(Evaluation evaluation) {
        this.skillScore = evaluation.getSkillScore();
        this.businessScore = evaluation.getBusinessScore();
        this.teamScore = evaluation.getTeamScore();
        this.comment = evaluation.getComment();
    }

    // --- Getters and Setters ---
    public BigDecimal getSkillScore() { return skillScore; }
    public void setSkillScore(BigDecimal skillScore) { this.skillScore = skillScore; }

    public BigDecimal getBusinessScore() { return businessScore; }
    public void setBusinessScore(BigDecimal businessScore) { this.businessScore = businessScore; }

    public BigDecimal getTeamScore() { return teamScore; }
    public void setTeamScore(BigDecimal teamScore) { this.teamScore = teamScore; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
