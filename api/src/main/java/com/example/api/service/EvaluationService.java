package com.example.api.service;

import com.example.api.dto.EvaluationResponse;
import com.example.api.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    /** フェーズIDで全評価一覧取得 */
    public List<EvaluationResponse> getAllEvaluations(Long phaseId) {
        return evaluationRepository.findByPhaseId(phaseId)
                .stream()
                .map(EvaluationResponse::fromEntity)
                .toList();
    }

    /** 指定ターゲットのコメント付き評価取得 */
    public List<EvaluationResponse> getCommentsForTarget(Long targetId, Long phaseId) {
        return evaluationRepository.findByTargetIdAndPhaseIdAndCommentIsNotNull(targetId, phaseId)
                .stream()
                .map(EvaluationResponse::fromEntity)
                .toList();
    }

    /** 今期 evaluator が行った評価一覧取得 */
    public List<EvaluationResponse> getEvaluationsInPeriod(Long evaluatorId) {
        return evaluationRepository.findEvaluationsByEvaluatorIdInCurrentPeriod(evaluatorId)
                .stream()
                .map(EvaluationResponse::fromEntity)
                .toList();
    }
}