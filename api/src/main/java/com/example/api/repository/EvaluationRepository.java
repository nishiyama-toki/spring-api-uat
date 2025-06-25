package com.example.api.repository;

import com.example.api.entity.Evaluation;
import com.example.api.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByPhaseId(Long phaseId);
    List<Evaluation> findByTargetIdAndPhaseIdAndCommentIsNotNull(Long targetId, Long phaseId);
    Optional<Evaluation> findByEvaluatorIdAndTargetIdAndPhaseId(Long evaluatorId, Long targetId, Long phaseId);
    Optional<Evaluation> findByEvaluatorIdAndTargetId(Long evaluatorId, Long targetId);

    boolean existsByEvaluatorIdAndPhaseId(Long evaluatorId, Long phaseId);
    List<Evaluation> findByTarget_IdAndPhase_Id(Long targetId, Long phaseId);
    List<Evaluation> findByEvaluator_IdAndTarget_IdAndPhase_Id(Long evaluatorId, Long targetId, Long phaseId);

    @Query("""
        SELECT e FROM Evaluation e
        WHERE e.evaluatorId = :evaluatorId
          AND CURRENT_DATE BETWEEN e.phase.startDate AND e.phase.endDate
    """)
    List<Evaluation> findEvaluationsByEvaluatorIdInCurrentPeriod(@Param("evaluatorId") Long evaluatorId);

    @Query("SELECT ev FROM Evaluation ev WHERE ev.phaseId = :phaseId")
    List<Evaluation> findEvaluationsInPeriod(@Param("phaseId") Long phaseId);

    @Query("SELECT DISTINCT ev.evaluatorId FROM Evaluation ev WHERE ev.phaseId = :phaseId")
    List<Long> findEvaluatorIdsByPhaseId(@Param("phaseId") Long phaseId);

    @Query("""
        SELECT emp FROM Employee emp
        WHERE emp.id NOT IN (
            SELECT ev.targetId FROM Evaluation ev WHERE ev.phaseId = :phaseId
        )
    """)
    List<Employee> findEmployeesNotSubmitted(@Param("phaseId") Long phaseId);
    List<Evaluation> findByEvaluatorId(Long evaluatorId);

}
