package com.example.api.service;

import com.example.api.dto.EvaluationResponse;
import com.example.api.dto.CommentResponse;
import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EvaluationRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
// ★↓確認用
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EvaluationService {
    
    private final EvaluationRepository evaluationRepository;
    // ★確認用
    private static final Logger logger = LoggerFactory.getLogger(EvaluationService.class);

    public EvaluationService(EvaluationRepository evaluationRepository) {
        this.evaluationRepository = evaluationRepository;
    }

    public List<EvaluationResponse> getAllEvaluations(Long periodId) {

        // ★確認用
        logger.info("Service: getAllEvaluations が呼び出されました。渡された periodId: {}", periodId);

        // 指定された期の評価をすべて取得
        List<Evaluation> allEvaluations = evaluationRepository.findByPhaseId(periodId);

        // ★確認用
        logger.info("DB検索結果: {} 件の評価データを取得しました。", allEvaluations.size());

        // 評価される人(target)ごとに、評価のリストをグループ化する
        Map<Employee, List<Evaluation>> groupedByTarget = allEvaluations.stream()
            .collect(Collectors.groupingBy(Evaluation::getTarget));
            
        // 社員ごとに平均値を計算していく
        List<EvaluationResponse> responseList = new ArrayList<>();
        groupedByTarget.forEach((targetEmployee, evaluationsForEmployee) -> {
            
            double totalSkill = 0, totalBusiness = 0, totalTeam = 0;
            int countSkill = 0, countBusiness = 0, countTeam = 0;
            boolean hasComment = false;

            for (Evaluation eval : evaluationsForEmployee) {
                if (eval.getSkillScore() != null) {
                    totalSkill += eval.getSkillScore();
                    countSkill++;
                }
                if (eval.getBusinessScore() != null) {
                    totalBusiness += eval.getBusinessScore();
                    countBusiness++;
                }
                if (eval.getTeamScore() != null) {
                    totalTeam += eval.getTeamScore();
                    countTeam++;
                }
                if (eval.getComment() != null && !eval.getComment().isEmpty()) {
                    hasComment = true;
                }
            }

            // 各項目の平均値を計算
            Double avgSkill = (countSkill > 0) ? totalSkill / countSkill : null;
            Double avgBusiness = (countBusiness > 0) ? totalBusiness / countBusiness : null;
            Double avgTeam = (countTeam > 0) ? totalTeam / countTeam : null;

            // 全体の平均値を計算
            double overallTotalScore = totalSkill + totalBusiness + totalTeam;
            int overallItemCount = countSkill + countBusiness + countTeam;
            Double overallAvg = (overallItemCount > 0) ? overallTotalScore / overallItemCount : 0.0;

            // 4. 計算結果をレスポンスDTOに詰める
            EvaluationResponse response = new EvaluationResponse();
            response.setTargetId(targetEmployee.getId());
            response.setTargetName(targetEmployee.getName());
            response.setAverageSkillScore(avgSkill);
            response.setAverageBusinessScore(avgBusiness);
            response.setAverageTeamScore(avgTeam);
            response.setOverallAverageScore(overallAvg);
            response.setHasComment(hasComment);

            responseList.add(response);
        });

        return responseList;
    }

    public List<CommentResponse> getCommentsForTarget(Long targetId, Long phaseId) {
        List<Evaluation> evaluations = evaluationRepository.findByTargetIdAndPhaseIdAndCommentIsNotNull(targetId, phaseId);

        return evaluations.stream()
            .map(evaluation -> new CommentResponse(
                evaluation.getEvaluator().getName(),
                evaluation.getComment()
            ))
            .collect(Collectors.toList());
    }
}