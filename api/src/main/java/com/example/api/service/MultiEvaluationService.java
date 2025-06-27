package com.example.api.service;

import com.example.api.dto.MultiEvaluationDto;
import com.example.api.dto.PostEvaluationDto;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MultiEvaluationService {
    private static final Logger logger = LoggerFactory.getLogger(MultiEvaluationService.class);

    private final EvaluationRepository evaluationRepository;

    public int registerEvaluations(MultiEvaluationDto dto, Long evaluatorId) {
        int count = 0;

        for (PostEvaluationDto ev : dto.getEvaluations()) {
            logger.info("DEBUG eval: targetId={} skill={} biz={} team={}",
                    ev.getTargetId(), ev.getSkillScore(), ev.getBusinessScore(), ev.getTeamScore());

            boolean hasAnyScore = ev.getSkillScore() != null || ev.getBusinessScore() != null || ev.getTeamScore() != null;
            boolean hasComment = StringUtils.hasText(ev.getComment());

            if (!hasAnyScore && !hasComment) {
                continue;
            }

            Evaluation entity = evaluationRepository
                    .findByEvaluatorIdAndTargetIdAndPhaseId(evaluatorId, ev.getTargetId().longValue(), dto.getPhaseId().longValue())
                    .orElseGet(Evaluation::new);

            entity.setEvaluatorId(evaluatorId);
            entity.setTargetId(ev.getTargetId().longValue());
            entity.setPhaseId(dto.getPhaseId().longValue());

            // --- ▼▼▼ ここからロジックを修正 ▼▼▼ ---
            // フロントエンドから来た値で常に上書きする。
            // DTOの値がnullなら、エンティティのフィールドもnullで更新する。
            entity.setSkillScore(ev.getSkillScore() != null ? BigDecimal.valueOf(ev.getSkillScore()) : null);
            entity.setBusinessScore(ev.getBusinessScore() != null ? BigDecimal.valueOf(ev.getBusinessScore()) : null);
            entity.setTeamScore(ev.getTeamScore() != null ? BigDecimal.valueOf(ev.getTeamScore()) : null);
            entity.setComment(ev.getComment());
            // --- ▲▲▲ ここまで修正 ▲▲▲ ---

            evaluationRepository.save(entity);
            count++;
        }
        return count;
    }
}
