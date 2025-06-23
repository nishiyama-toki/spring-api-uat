package com.example.api.service;

import com.example.api.dto.EvaluationResponse;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {

    private final EvaluationRepository evaluationRepository;

    /** フェーズ ID で全評価一覧取得（管理画面など） */
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

    /** “いま提出対象” の評価依頼を **Entity のまま** 返す  */
    public List<Evaluation> getEvaluationsInPeriod(Long evaluatorId) {
        // Repository は元々 Entity を返す想定なので、そのまま渡すだけ
        return evaluationRepository.findEvaluationsByEvaluatorIdInCurrentPeriod(evaluatorId);
    }
}
