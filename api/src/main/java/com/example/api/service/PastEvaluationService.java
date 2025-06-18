// /src/main/java/com/example/api/service/PastEvaluationService.java
package com.example.api.service;

import com.example.api.dto.CommentDTO;
import com.example.api.dto.EvaluationRawDTO;
import com.example.api.dto.PastEvaluationResponse;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EvaluationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PastEvaluationService {

    private final EvaluationRepository evaluationRepository;

    public PastEvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public PastEvaluationResponse getPastEvaluation(Integer phaseId, Integer targetId) {
        // (1) DB から全レコードを取得 (より確実な@Query付きメソッドを呼び出す)
        List<Evaluation> evaluations =
            evaluationRepository.findByPhaseAndTarget(phaseId, targetId);

        // (2) データベースから対応する評価が見つからなかった場合の処理
        if (evaluations.isEmpty()) {
            PastEvaluationResponse res = new PastEvaluationResponse();
            res.setPhaseNumber(0);
            res.setName("");
            res.setSkillScore(0f);
            res.setBusinessScore(0f);
            res.setTeamScore(0f);
            res.setComments(Collections.emptyList());
            res.setRawEvaluations(Collections.emptyList());
            return res;
        }

        // (3) 生データをそのまま詰める (nullを0fに変換する)
        List<EvaluationRawDTO> rawList = evaluations.stream()
            .map(e -> {
                float skillScore = (e.getSkillScore() != null) ? e.getSkillScore().floatValue() : 0f;
                float businessScore = (e.getBusinessScore() != null) ? e.getBusinessScore().floatValue() : 0f;
                float teamScore = (e.getTeamScore() != null) ? e.getTeamScore().floatValue() : 0f;

                return new EvaluationRawDTO(
                    skillScore,
                    businessScore,
                    teamScore,
                    e.getComment(),
                    e.getEvaluator().getName()
                );
            })
            .collect(Collectors.toList());

        // (4) コメント一覧だけ抽出
        List<CommentDTO> commentList = evaluations.stream()
            .map(e -> new CommentDTO(
                e.getEvaluator().getName(),
                e.getComment()
            ))
            .collect(Collectors.toList());

        // (5) レスポンス組み立て
        PastEvaluationResponse res = new PastEvaluationResponse();
        res.setPhaseNumber(evaluations.get(0).getPhase().getPhaseNumber());
        res.setName(evaluations.get(0).getPhase().getName());
        res.setSkillScore(0f); // 平均値はフロントエンドで計算するため0fをセット
        res.setBusinessScore(0f);
        res.setTeamScore(0f);
        res.setComments(commentList);
        res.setRawEvaluations(rawList);

        return res;
    }
}