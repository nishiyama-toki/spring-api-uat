package com.example.api.dto;

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
