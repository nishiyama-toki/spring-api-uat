
package com.example.api.repository; // このファイルが所属するパッケージ（フォルダ構造）を示す

import com.example.api.dto.EvaluationResponseDTO; // クエリ結果を詰めるDTOクラス
import org.springframework.data.jpa.repository.Query; // カスタムクエリを定義するためのアノテーション
import org.springframework.data.repository.query.Param; // クエリ内のパラメータに名前をつけるためのアノテーション
import org.springframework.data.repository.CrudRepository; // CRUD操作を提供する基本インターフェース
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * EvaluationRepository は、評価データ（evaluationsテーブル）にアクセスするためのリポジトリである。
 * 
 * phases テーブルと JOIN して、phase_number や quarter名、提出期間も含めたデータを取得する。
 */
@Repository
public interface EvaluationRepository extends CrudRepository<com.example.api.entity.Evaluation, Long> {

    /**
     * 指定された evaluatorId に紐づく「提出期間中の評価依頼」をすべて取得する。
     * 
     * 条件：
     * - evaluations.evaluator_id = :evaluatorId
     * - 今日が phase の提出期間内であること（提出済みでも未提出でも含める）
     * 
     * 結合：
     * - phases テーブルと結合して、期（phase_number）・Q（name）・提出期間（startDate/endDate）を取得
     * 
     * DTOに詰める：
     * - target_id（評価対象者）の名前は仮に "ID文字列" として出力（本番では employees テーブルが必要になる）
     */
    
@Query("SELECT new com.example.api.dto.EvaluationResponseDTO(" +
       "e.targetId, p.phaseNumber, p.name, e.type, p.startDate, p.endDate) " +
       "FROM Evaluation e " +
       "JOIN Phase p ON e.phaseId = p.id " +
       "WHERE e.evaluatorId = :evaluatorId " +
       "AND CURRENT_DATE BETWEEN p.startDate AND p.endDate")
List<EvaluationResponseDTO> findEvaluationsInPeriod(@Param("evaluatorId") Long evaluatorId);


}
