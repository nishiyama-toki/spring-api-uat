package com.example.api.repository;

import com.example.api.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByPhaseId(Long phaseId);
    List<Evaluation> findByTargetIdAndPhaseIdAndCommentIsNotNull(Long targetId, Long phaseId);

    @Query("SELECT DISTINCT e.evaluator.id FROM Evaluation e WHERE e.phase.id = :phaseId")
    List<Long> findEvaluatorIdsByPhaseId(@Param("phaseId") Long phaseId);
}

import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // 再送のため
    Optional<Evaluation> findByEvaluatorIdAndTargetIdAndPhaseId(Integer evaluatorId, Integer targetId, Integer phaseId);

    // フロント初期表示で既存スコア取得用
    Optional<Evaluation> findByEvaluatorIdAndTargetId(Integer evaluatorId, Integer targetId);
}
