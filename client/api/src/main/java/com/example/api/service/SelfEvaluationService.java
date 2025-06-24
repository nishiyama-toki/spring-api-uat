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

    public SelfEvaluationService(EvaluationRepository er,
                                 EmployeeRepository empRepo,
                                 PhaseRepository pr) {
        this.evaluationRepository = er;
        this.employeeRepository = empRepo;
        this.phaseRepository = pr;
    }

    // ────────────── 取得 ──────────────
    @Transactional(readOnly = true)
    public Optional<SelfEvaluationResponseDTO> getSelfEvaluation(Long phaseId, Long userId) {
        return evaluationRepository
                .findByEvaluatorIdAndTargetIdAndPhaseId(userId, userId, phaseId)   // Optional<Evaluation>
                .map(SelfEvaluationResponseDTO::new);
    }

    // ────────────── 登録／更新 ──────────────
    @Transactional
    public Evaluation saveOrUpdateSelfEvaluation(SelfEvaluationRequest req) {

        Optional<Evaluation> opt = evaluationRepository
                .findByEvaluatorIdAndTargetIdAndPhaseId(
                        req.getEvaluatorId(),
                        req.getTargetId(),
                        req.getPhaseId());

        Evaluation ev = opt.orElseGet(Evaluation::new);

        if (opt.isEmpty()) {
            Employee evaluator = employeeRepository.findById(req.getEvaluatorId())
                    .orElseThrow(() -> new EntityNotFoundException("評価者が見つかりません"));
            Employee target = employeeRepository.findById(req.getTargetId())
                    .orElseThrow(() -> new EntityNotFoundException("対象者が見つかりません"));
            Phase phase = phaseRepository.findById(req.getPhaseId())
                    .orElseThrow(() -> new EntityNotFoundException("フェーズが見つかりません"));

            ev.setEvaluator(evaluator);
            ev.setTarget(target);
            ev.setPhase(phase);
            ev.setCreatedAt(LocalDateTime.now());
        }

        ev.setSkillScore(req.getSkillScore());
        ev.setBusinessScore(req.getBusinessScore());
        ev.setTeamScore(req.getTeamScore());
        ev.setComment(req.getComment());
        ev.setUpdatedAt(LocalDateTime.now());

        return evaluationRepository.save(ev);
    }
}
