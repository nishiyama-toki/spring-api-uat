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
                .findByEvaluatorIdAndTargetIdAndPhaseId(userId, userId, phaseId)
                .map(SelfEvaluationResponseDTO::new);
    }

    // ────────────── 登録／更新 ──────────────
    @Transactional
    public Evaluation saveOrUpdateSelfEvaluation(SelfEvaluationRequest req) {

        // フロントエンドから渡されたIDで、既存の評価データを探す
        Optional<Evaluation> opt = evaluationRepository
                .findByEvaluatorIdAndTargetIdAndPhaseId(
                        req.getEvaluatorId(),
                        req.getTargetId(),
                        req.getPhaseId());

        // 既存データがあればそれを使い、なければ新しい空のEvaluationオブジェクトを用意する
        Evaluation ev = opt.orElseGet(Evaluation::new);

        // ★★★★★ ここからが修正の核心 ★★★★★
        // もし評価が「新規作成」の場合 (optが空だった場合)
        if (opt.isEmpty()) {
            // IDを使って、関連するオブジェクトをDBから取得する
            Employee evaluator = employeeRepository.findById(req.getEvaluatorId())
                    .orElseThrow(() -> new EntityNotFoundException("評価者が見つかりません"));
            Employee target = employeeRepository.findById(req.getTargetId())
                    .orElseThrow(() -> new EntityNotFoundException("対象者が見つかりません"));
            Phase phase = phaseRepository.findById(req.getPhaseId())
                    .orElseThrow(() -> new EntityNotFoundException("フェーズが見つかりません"));

            // --- IDとオブジェクトの両方を、新しいEvaluationオブジェクトにセットする ---
            ev.setEvaluator(evaluator);
            ev.setTarget(target);
            ev.setPhase(phase);
            
            // このIDセットが抜けていたことが、エラーの根本原因です
            ev.setEvaluatorId(evaluator.getId());
            ev.setTargetId(target.getId());
            ev.setPhaseId(phase.getId());
            
            ev.setCreatedAt(LocalDateTime.now());
        }
        // ★★★★★ ここまで ★★★★★

        // スコアとコメントをセットする（これは新規・更新の両方で実行）
        ev.setSkillScore(req.getSkillScore());
        ev.setBusinessScore(req.getBusinessScore());
        ev.setTeamScore(req.getTeamScore());
        ev.setComment(req.getComment());
        ev.setUpdatedAt(LocalDateTime.now());

        // すべての情報がセットされた状態で、データベースに保存する
        return evaluationRepository.save(ev);
    }
}