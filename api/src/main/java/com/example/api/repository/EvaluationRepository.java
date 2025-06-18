package com.example.api.repository;

import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
    boolean existsByEvaluatorIdAndPhaseId(Integer evaluatorId, Integer phaseId);

    // evaluatorIdで判定するのが「未提出者」
    @Query("SELECT e FROM Employee e WHERE NOT EXISTS " +
           "(SELECT 1 FROM Evaluation ev WHERE ev.evaluatorId = e.id AND ev.phaseId = :phaseId)")
    List<Employee> findEmployeesNotSubmitted(Integer phaseId);
}
