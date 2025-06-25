package com.example.api.dto;

<<<<<<< HEAD
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
=======
import lombok.Data;
import java.time.LocalDate;

/** 提出依頼１件分をフロントへ返すシンプル DTO */
@Data
public class EvaluationResponseDTO {

    private Long      targetId;
    private String    targetName;
    private Integer   phaseNumber;
    private String    quarterName; // 追加: PhaseのperiodNameに対応
    private LocalDate startDate;
    private LocalDate endDate;
    private String    evaluationType; // 自己評価か多面評価かを区別するためのフィールド

    /** 可読性重視の static ファクトリ */
    public static EvaluationResponseDTO of(Long id,
                                           String name,
                                           Integer phaseNum,
                                           String quarterName, // 追加
                                           LocalDate start,
                                           LocalDate end,
                                           String type) { // typeを引数に追加

        EvaluationResponseDTO dto = new EvaluationResponseDTO();
        dto.targetId    = id;
        dto.targetName  = name;
        dto.phaseNumber = phaseNum;
        dto.quarterName = quarterName; // 設定
        dto.startDate   = start;
        dto.endDate     = end;
        dto.evaluationType = type; // タイプを設定
        return dto;
    }
}
>>>>>>> mizukami
