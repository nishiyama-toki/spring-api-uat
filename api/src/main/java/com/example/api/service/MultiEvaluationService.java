package com.example.api.service;

import com.example.api.dto.MultiEvaluationDto;
import com.example.api.dto.PostEvaluationDto; // ← 修正ポイント！
import com.example.api.entity.Evaluation;
import com.example.api.phase.entity.Phase;
import com.example.api.repository.EvaluationRepository;
import com.example.api.exception.InvalidEvaluationPeriodException; 
import com.example.api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@Service
public class MultiEvaluationService {

    private static final Logger logger = LoggerFactory.getLogger(MultiEvaluationService.class);

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private JwtService jwtService;

    // ----------------------------
    //   多面評価を一括登録
    // ----------------------------
    public int registerEvaluations(MultiEvaluationDto dto, HttpServletRequest request) {
        // JWTトークンから evaluatorId を取得
        Integer evaluatorId = jwtService.extractUserId(request);

        int count = 0;

        logger.info("【DEBUG】受信したphaseId: {}", dto.getPhaseId());
        logger.info("【DEBUG】受信した評価数: {}", dto.getEvaluations().size());

        for (PostEvaluationDto eval : dto.getEvaluations()) {
            logger.info("【DEBUG】targetId={}, skillScore={}, businessScore={}, teamScore={}, comment={}",
                eval.getTargetId(),
                eval.getSkillScore(),
                eval.getBusinessScore(),
                eval.getTeamScore(),
                eval.getComment());
        }

        for (PostEvaluationDto eval : dto.getEvaluations()) {
            // スコアが null の場合はスキップ（バリデーションエラーを回避できたとしても保険として）
            if (eval.getSkillScore() == null || eval.getBusinessScore() == null || eval.getTeamScore() == null) {
                logger.warn("評価スコアが未入力のためスキップ: targetId={}", eval.getTargetId());
                continue;
            }

            // 既に評価済かどうかを参照
            Evaluation entity = evaluationRepository
                .findByEvaluatorIdAndTargetIdAndPhaseId(evaluatorId, eval.getTargetId(), dto.getPhaseId())
                .orElseGet(Evaluation::new);

            // エンティティに値をセット（キャメルケース準拠）
            entity.setEvaluatorId(evaluatorId);
            entity.setTargetId(eval.getTargetId());
            entity.setSkill_score(eval.getSkillScore());
            entity.setBusiness_score(eval.getBusinessScore());
            entity.setTeam_score(eval.getTeamScore());
            entity.setComment(eval.getComment());
            entity.setPhaseId(dto.getPhaseId());

            // ログ出力
            logger.info("評価保存: evaluatorId={}, targetId={}, phaseId={}",
                    evaluatorId, eval.getTargetId(), dto.getPhaseId());

            evaluationRepository.save(entity);
            count++;
        }

        logger.info("保存件数: {}", count);
        return count;
    }
}  
