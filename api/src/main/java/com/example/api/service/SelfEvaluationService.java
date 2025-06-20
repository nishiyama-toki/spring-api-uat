package com.example.api.service;

import com.example.api.dto.SelfEvaluationRequest;
import com.example.api.dto.SelfEvaluationResponseDTO;
import com.example.api.entity.Evaluation;
import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EvaluationRepository;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.PhaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SelfEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EmployeeRepository employeeRepository;
    private final PhaseRepository phaseRepository;

    public SelfEvaluationService(EvaluationRepository er, EmployeeRepository empRepo, PhaseRepository pr) {
        this.evaluationRepository = er;
        this.employeeRepository = empRepo;
        this.phaseRepository = pr;
    }

    @Transactional(readOnly = true)
    public Optional<SelfEvaluationResponseDTO> getSelfEvaluation(Long phaseId, Long userId) {
        return evaluationRepository
                .findByEvaluator_IdAndTarget_IdAndPhase_Id(userId, userId, phaseId)
                .stream()
                .findFirst()
                .map(SelfEvaluationResponseDTO::new);
    }

    @Transactional
    public Evaluation saveOrUpdateSelfEvaluation(SelfEvaluationRequest request) {
        Optional<Evaluation> existingEvaluationOpt = evaluationRepository
            .findByEvaluator_IdAndTarget_IdAndPhase_Id(
                request.getEvaluatorId(),
                request.getTargetId(),
                request.getPhaseId()
            );

        Evaluation evaluation;
        if (existingEvaluationOpt.isPresent()) {
            evaluation = existingEvaluationOpt.get();
        } else {
            evaluation = new Evaluation();

            Employee evaluator = employeeRepository.findById(request.getEvaluatorId())
                .orElseThrow(() -> new EntityNotFoundException("評価者が見つかりません: " + request.getEvaluatorId()));

            Employee target = employeeRepository.findById(request.getTargetId())
                .orElseThrow(() -> new EntityNotFoundException("対象者が見つかりません: " + request.getTargetId()));

            Phase phase = phaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new EntityNotFoundException("フェーズが見つかりません: " + request.getPhaseId()));

            evaluation.setEvaluator(evaluator);
            evaluation.setTarget(target);
            evaluation.setPhase(phase);
            evaluation.setCreatedAt(LocalDateTime.now());
        }

        evaluation.setSkillScore(request.getSkillScore());
        evaluation.setBusinessScore(request.getBusinessScore());
        evaluation.setTeamScore(request.getTeamScore());
        evaluation.setComment(request.getComment());
        evaluation.setUpdatedAt(LocalDateTime.now());

        return evaluationRepository.save(evaluation);
    }
}
