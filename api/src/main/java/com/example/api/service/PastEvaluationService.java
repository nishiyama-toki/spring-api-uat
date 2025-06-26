package com.example.api.service;

import com.example.api.dto.CommentDTO;
import com.example.api.dto.EvaluationRawDTO;
import com.example.api.dto.PastEvaluationResponse;
import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.EvaluationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PastEvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final EmployeeRepository employeeRepository;

    public PastEvaluationService(EvaluationRepository evaluationRepository, EmployeeRepository employeeRepository) {
        this.evaluationRepository = evaluationRepository;
        this.employeeRepository = employeeRepository;
    }

    public PastEvaluationResponse getPastEvaluation(Long targetId, Long phaseId) {
        // 対象者の名前を取得
        Employee target = employeeRepository.findById(targetId)
            .orElseThrow(() -> new EntityNotFoundException("対象者が見つかりません: " + targetId));

        // ★ Repositoryに新しく定義した、正しいメソッドを呼び出す
        List<Evaluation> evaluations = evaluationRepository.findByTarget_IdAndPhase_Id(targetId, phaseId);

        if (evaluations.isEmpty()) {
            return new PastEvaluationResponse(); // データがない場合は空のオブジェクトを返す
        }

        // --- 平均スコアを計算 ---
        long count = evaluations.stream().filter(e -> e.getSkillScore() != null).count();
        if (count == 0) count = 1; // 0除算を避ける

        BigDecimal avgSkillScore = evaluations.stream()
            .map(Evaluation::getSkillScore)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP);
        
        // (他のスコアも同様に計算)
        BigDecimal avgBusinessScore = evaluations.stream()
            .map(Evaluation::getBusinessScore).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP);

        BigDecimal avgTeamScore = evaluations.stream()
            .map(Evaluation::getTeamScore).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(count), 1, RoundingMode.HALF_UP);

        // --- コメントと生データを抽出 ---
        List<CommentDTO> comments = evaluations.stream()
            .filter(e -> e.getComment() != null && !e.getComment().isBlank())
            .map(e -> new CommentDTO(e.getEvaluator().getName(), e.getComment()))
            .collect(Collectors.toList());
        
        List<EvaluationRawDTO> rawEvaluations = evaluations.stream()
            .map(e -> new EvaluationRawDTO(
                e.getSkillScore() != null ? e.getSkillScore().floatValue() : 0.0f,
                e.getBusinessScore() != null ? e.getBusinessScore().floatValue() : 0.0f,
                e.getTeamScore() != null ? e.getTeamScore().floatValue() : 0.0f,
                e.getComment(),
                e.getEvaluator().getName()
            ))
            .collect(Collectors.toList());

        // --- 最終的なレスポンスオブジェクトを組み立てる ---
        PastEvaluationResponse response = new PastEvaluationResponse();
        response.setName(target.getName());
        response.setAverageSkillScore(avgSkillScore);
        response.setAverageBusinessScore(avgBusinessScore);
        response.setAverageTeamScore(avgTeamScore);
        response.setComments(comments);
        response.setRawEvaluations(rawEvaluations);
        
        return response;
    }
}
