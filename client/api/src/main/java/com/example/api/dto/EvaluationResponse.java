package com.example.api.dto;

import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;
import com.example.api.entity.Phase;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EvaluationResponse {

    private Long targetId;
    private String targetName;

    // フェーズ情報
    private Integer phaseNumber;
    private String phaseName; // name カラムに対応
    private LocalDate startDate;
    private LocalDate endDate;

    // 各項目の平均スコア
    private Double averageSkillScore;
    private Double averageBusinessScore;
    private Double averageTeamScore;

    private Double overallAverageScore;
    private boolean hasComment;

    public static EvaluationResponse fromEntity(Evaluation e) {
        EvaluationResponse dto = new EvaluationResponse();
        dto.setTargetId(e.getTargetId());
        Employee target = e.getTarget();
        dto.setTargetName(target != null ? target.getName() : null);

        Phase phase = e.getPhase();
        if (phase != null) {
            dto.setPhaseNumber(phase.getPhaseNumber());
            dto.setPhaseName(phase.getName());
            dto.setStartDate(phase.getStartDate());
            dto.setEndDate(phase.getEndDate());
        }

        BigDecimal s = e.getSkillScore();
        BigDecimal b = e.getBusinessScore();
        BigDecimal t = e.getTeamScore();

        if (s != null) dto.setAverageSkillScore(s.doubleValue());
        if (b != null) dto.setAverageBusinessScore(b.doubleValue());
        if (t != null) dto.setAverageTeamScore(t.doubleValue());

        if (s != null && b != null && t != null) {
            double avg = (s.doubleValue() + b.doubleValue() + t.doubleValue()) / 3.0;
            dto.setOverallAverageScore(Math.round(avg * 10.0) / 10.0);
        }
        dto.setHasComment(e.getComment() != null && !e.getComment().isBlank());
        return dto;
    }
}
