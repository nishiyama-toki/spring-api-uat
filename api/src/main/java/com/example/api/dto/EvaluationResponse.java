package com.example.api.dto;

import lombok.Data;

@Data
public class EvaluationResponse {
    // 誰の評価か
    private Long targetId;
    private String targetName;

    // 各項目の平均値
    private Double averageSkillScore;
    private Double averageBusinessScore;
    private Double averageTeamScore;

    // 全体の平均値
    private Double overallAverageScore;

    // コメントが一つでもあったか
    private boolean hasComment;
}

//     private Long evaluatorId;
//     private String name;
//     private Double skill;
//     private Double business;
//     private Double management;
//     private Double total;
//     private Boolean commentInput;

//     public EvaluationResponse(Long evaluatorId, String name, Double skill, Double business,
//                                 Double management, Double total, Boolean commentInput) {
//                 this.evaluatorId = evaluatorId;
//                 this.name = name;
//                 this.skill = skill;
//                 this.business = business;
//                 this.management = management;
//                 this.total = total;
//                 this.commentInput = commentInput;
//     }
// }