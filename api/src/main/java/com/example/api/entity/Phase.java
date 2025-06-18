package com.example.api.phase.entity;

import jakarta.persistence.*;//アノテーションや機能をまとめるためのimport
import java.time.LocalDate;

@Entity
@Table(name= "phases")//DBのテーブル名

public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DBのAUTO_INCREMENT自動連番を使う
    private Long id;

    @Column(name = "name" , nullable = false) //クオーター名（プルダウンから数字が入る）
    private String periodName;

    @Column(name = "start_date", nullable = false) // 開始日
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false) // 終了日
    private LocalDate endDate;

    @Column(name = "phase_number", nullable = false) // 評価期（例：18）
    private Integer phaseNumber;

    // ----- Getter & Setter -----

    public Long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

   public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

     public Integer getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(Integer phaseNumber) {
        this.phaseNumber = phaseNumber;
    }
}


   



