package com.example.api.phase.dto;

import com.example.api.entity.Phase; // Phase エンティティのインポート
import java.time.LocalDate;

public class PhaseDto {

    private String periodName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer phaseNumber;

    // --- エンティティ変換 ---
    public Phase toEntity() {
        Phase p = new Phase(); // 空の Phase インスタンスを生成
        p.setPeriodName(this.periodName);       // フェーズ名
        p.setStartDate(this.startDate);         // 開始日
        p.setEndDate(this.endDate);             // 終了日
        p.setPhaseNumber(this.phaseNumber);     // フェーズ番号
        return p;
    }

    // --- Getter & Setter ---
    public String getPeriodName() {
        return periodName;
    }

    public void setPe
}