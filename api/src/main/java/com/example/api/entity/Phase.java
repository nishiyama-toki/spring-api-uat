
package com.example.api.entity; // このファイルが属するパッケージ（フォルダ構造）を指定

// --- 必要なインポート ---
import jakarta.persistence.*; // JPA用アノテーションを使うためのインポート
import java.time.LocalDate;  // 日付型（DATE）を扱うためのクラス

@Entity // このクラスがDBのテーブルと対応することを示す
@Table(name = "phases", schema = "evaluation") // 対応するテーブル名とスキーマ名を指定
public class Phase {

    @Id // 主キーであることを示す
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動採番（SERIAL）を使う設定
    @Column(name = "id") // 対応するカラム名を指定
    private Long id;

    @Column(name = "phase_number") // 評価期の番号（例：2024年度第1Q → 1）
    private Integer phaseNumber;

    @Column(name = "name") // Qの名前（例：Q1, Q2など）
    private String name;

    @Column(name = "start_date") // 評価の開始日
    private LocalDate startDate;

    @Column(name = "end_date") // 評価の終了日
    private LocalDate endDate;

    @Column(name = "self_eval_due") // 自己評価の締切日
    private LocalDate selfEvalDue;

    @Column(name = "peer_eval_due") // 多面評価の締切日
    private LocalDate peerEvalDue;

    /**
     * 今日の日付がこのフェーズの期間内（start_date ～ end_date）に含まれているかを判定する。
     * このメソッドは現在フェーズが有効かどうかの判断で使用される。
     */
    public boolean isWithinSubmissionPeriod() {
        LocalDate today = LocalDate.now();
        return (today.isEqual(startDate) || today.isAfter(startDate))
            && (today.isBefore(endDate) || today.isEqual(endDate));
    }

    // --- 以下 getter/setter メソッド（各フィールドの読み書きを行う） ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(Integer phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
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

    public LocalDate getSelfEvalDue() {
        return selfEvalDue;
    }

    public void setSelfEvalDue(LocalDate selfEvalDue) {
        this.selfEvalDue = selfEvalDue;
    }

    public LocalDate getPeerEvalDue() {
        return peerEvalDue;
    }

    public void setPeerEvalDue(LocalDate peerEvalDue) {
        this.peerEvalDue = peerEvalDue;
    }
}
