package com.example.api.dto;

import java.time.LocalDate;

/**
 * EvaluationResponseDTO は、評価依頼一覧画面で1件分の評価情報を保持するDTOクラスである。
 * 画面には対象者名の代わりに、仮でIDを文字列化したものを表示する。
 */
public class EvaluationResponseDTO {

    private String targetName;     // 対象者名（仮：targetIdをString化したもの）
    private Integer phaseNumber;   // 期（○○期）
    private String quarterName;    // Q（1Q〜4Q）
    private String type;           // 種別（"SELF" または "PEER"）
    private LocalDate startDate;   // 提出開始日
    private LocalDate endDate;     // 提出終了日

    /**
     * コンストラクタ：Long型の targetId を受け取り、文字列として保持する。
     */
    public EvaluationResponseDTO(Long targetId, Integer phaseNumber, String quarterName, String type,
                                  LocalDate startDate, LocalDate endDate) {
        this.targetName = String.valueOf(targetId); // ← 仮の表示名としてIDを文字列化
        this.phaseNumber = phaseNumber;
        this.quarterName = quarterName;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // --- Getter（必要に応じて追加） ---
    public String getTargetName() { return targetName; }
    public Integer getPhaseNumber() { return phaseNumber; }
    public String getQuarterName() { return quarterName; }
    public String getType() { return type; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
}
