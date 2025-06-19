package com.example.api.repository;

import com.example.api.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

// ★ 主キーの型をLongに変更
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> { 
    
    /**
     * 自己評価の取得・更新用メソッド (IDはすべてLong)
     */
    Optional<Evaluation> findByEvaluator_IdAndTarget_IdAndPhase_Id(Long evaluatorId, Long targetId, Long phaseId);

    /**
     * 過去の評価履歴取得用メソッド (IDはすべてLong)
     */
    List<Evaluation> findByTarget_IdAndPhase_Id(Long targetId, Long phaseId);
}
