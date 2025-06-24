package com.example.api.service;

import com.example.api.dto.MultiEvaluationDto;
import com.example.api.dto.PostEvaluationDto;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EvaluationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MultiEvaluationService {
    private static final Logger logger = LoggerFactory.getLogger(MultiEvaluationService.class);

    @Autowired
    private EvaluationRepository evaluationRepository;
    @Autowired
    private JwtService jwtService;

    public int registerEvaluations(MultiEvaluationDto dto, HttpServletRequest request) {
        Long evaluatorId = jwtService.extractUserId(request).longValue();
        int count = 0;

        for (PostEvaluationDto ev : dto.getEvaluations()) {
            logger.info("DEBUG eval: targetId={} skill={} biz={} team={}",
                    ev.getTargetId(), ev.getSkillScore(), ev.getBusinessScore(), ev.getTeamScore());

            if (ev.getSkillScore() == null || ev.getBusinessScore() == null || ev.getTeamScore() == null) continue;

            Evaluation entity = evaluationRepository
                    .findByEvaluatorIdAndTargetIdAndPhaseId(evaluatorId, ev.getTargetId().longValue(), dto.getPhaseId().longValue())
                    .orElseGet(Evaluation::new);

            entity.setEvaluatorId(evaluatorId);
            entity.setTargetId(ev.getTargetId().longValue());
            entity.setSkillScore(BigDecimal.valueOf(ev.getSkillScore()));
            entity.setBusinessScore(BigDecimal.valueOf(ev.getBusinessScore()));
            entity.setTeamScore(BigDecimal.valueOf(ev.getTeamScore()));
            entity.setComment(ev.getComment());
            entity.setPhaseId(dto.getPhaseId().longValue());

            evaluationRepository.save(entity);
            count++;
        }
        return count;
    }
}
