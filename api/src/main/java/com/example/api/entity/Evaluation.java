
package com.example.api.entity; // このクラスが属するパッケージ（フォルダ）を示す

import jakarta.persistence.Entity; // このクラスがエンティティ（DBのテーブルに対応）であることを示す
import jakarta.persistence.Id; // 主キー（id）を表すためのアノテーション
import jakarta.persistence.Column; // カラム名を指定するためのアノテーション
import jakarta.persistence.Table; // 対応するテーブル名を指定するアノテーション
import jakarta.persistence.GeneratedValue; // 自動採番用アノテーション
import jakarta.persistence.GenerationType; // 自動採番の戦略指定
import java.time.LocalDateTime; // 日時を扱うためのJava標準クラス

@Entity // このクラスはJPAエンティティ（DBのテーブル）であると指定する
@Table(name = "evaluations") // 対応するDB上のテーブル名を明示的に書く
public class Evaluation {

    @Id // 主キーとして指定（JPAで必須）
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB側でIDを自動採番するよう指定
    private Long id; // テーブルの id カラム（主キー）

    @Column(name = "evaluator_id", nullable = false) // 評価する人のID（null不可）
    private Long evaluatorId;

    @Column(name = "target_id", nullable = false) // 評価対象者のID（null不可）
    private Long targetId;

    @Column(name = "phase_id", nullable = false) // どの評価期間（フェーズ）かを表すID（null不可）
    private Long phaseId;

    @Column(name = "type", nullable = false) // 評価の種類（"SELF" or "PEER"）（null不可）
    private String type;

    @Column(name = "skill_score") // スキルの評価スコア（未評価ならnull）
    private Integer skillScore;

    @Column(name = "business_score") // ビジネス面の評価スコア（未評価ならnull）
    private Integer businessScore;

    @Column(name = "team_score") // チームワークの評価スコア（未評価ならnull）
    private Integer teamScore;

    @Column(name = "comment", length = 1000) // コメント（任意入力、最大1000文字）
    private String comment;

    @Column(name = "created_at", nullable = false) // レコード作成日時（null不可）
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false) // レコード更新日時（null不可）
    private LocalDateTime updatedAt;

    /**
     * この評価が未提出かどうかを判定する。
     * 提出済みとみなす条件：
     * ・スキル、ビジネス、チームスコアのいずれか1つ以上に値が入っている
     * ・もしくはコメントに何かしらの文字が入力されている
     * → いずれも入力がなければ「未提出」と判定
     */
    public boolean isNotSubmitted() {
        return skillScore == null
            && businessScore == null
            && teamScore == null
            && (comment == null || comment.trim().isEmpty());
    }

    // --- ゲッター（Serviceクラスで使用される）---

    public Long getId() {
        return id;
    }

    public Long getEvaluatorId() {
        return evaluatorId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // ※ セッターなどは必要に応じて追加可能
}
