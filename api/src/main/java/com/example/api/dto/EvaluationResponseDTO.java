package com.example.api.dto;

import com.example.api.entity.Evaluation;
import java.time.LocalDate;

public class EvaluationResponseDTO {

    /** 評価対象者の氏名 */
    private final String targetName;
    /** 評価期番号 */
    private final Integer phaseNumber;
    /** フェーズ開始日 */
    private final LocalDate startDate;
    /** フェーズ終了日 */
    private final LocalDate endDate;

    public EvaluationResponseDTO(String targetName,
                                 Integer phaseNumber,
                                 LocalDate startDate,
                                 LocalDate endDate) {
        this.targetName = targetName;
        this.phaseNumber = phaseNumber;
        this.startDate   = startDate;
        this.endDate     = endDate;
    }

    /** Entity → DTO 変換 */
    public static EvaluationResponseDTO fromEntity(Evaluation e) {
        return new EvaluationResponseDTO(
                e.getTarget().getName(),          // 対象者名
                e.getPhase().getPhaseNumber(),    // 期番号
                e.getPhase().getStartDate(),      // 開始日
                e.getPhase().getEndDate()         // 終了日
        );
    }

    /* ======= getter ======= */
    public String   getTargetName() { return targetName; }
    public Integer  getPhaseNumber() { return phaseNumber; }
    public LocalDate getStartDate()  { return startDate; }
    public LocalDate getEndDate()    { return endDate; }
}
