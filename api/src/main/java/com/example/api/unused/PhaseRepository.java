
package com.example.api.repository; // フォルダ構造上の位置を指定

import com.example.api.entity.Phase; // Phaseエンティティ（phasesテーブルに対応）
import org.springframework.data.jpa.repository.JpaRepository; // JPA標準のリポジトリインターフェース
import org.springframework.data.jpa.repository.Query; // JPQLを書くためのアノテーション
import org.springframework.data.repository.query.Param; // JPQLのパラメータ指定用
import org.springframework.stereotype.Repository; // リポジトリとしてSpringに認識させる

import java.time.LocalDate; // 今日の日付を扱うための型

@Repository // このインターフェースがリポジトリ層であることをSpringに伝える
public interface PhaseRepository extends JpaRepository<Phase, Long> {

    // 本日が含まれている評価期間（フェーズ）を1件取得する。
    // SQL的には「SELECT * FROM phases WHERE today BETWEEN start_date AND end_date」
    @Query("SELECT p FROM Phase p WHERE :today BETWEEN p.startDate AND p.endDate")
    Phase findCurrentPhase(@Param("today") LocalDate today);
}
