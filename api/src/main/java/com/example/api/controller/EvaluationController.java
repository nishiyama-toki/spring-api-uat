package com.example.api.controller;

import com.example.api.dto.EvaluationResponse;
import com.example.api.service.EvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EvaluationController {

    private final EvaluationService evaluationService;

    public EvaluationController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/all_evaluations")
    public ResponseEntity<?> getAllEvaluations(@RequestParam("phase_id") Long phaseId) {
        if (phaseId == null) {
            return ResponseEntity.badRequest().body(Map.of("validation_error", "期・Qが未選択です"));
        }
        List<EvaluationResponse> evaluations = evaluationService.getAllEvaluations(phaseId);
        return ResponseEntity.ok(Map.of("employees", evaluations));
    }

    @GetMapping("/employees/{targetId}/comments")
    public ResponseEntity<List<EvaluationResponse>> getComments(
            @PathVariable Long targetId,
            @RequestParam("phase_id") Long phaseId) {
        List<EvaluationResponse> comments = evaluationService.getCommentsForTarget(targetId, phaseId);
        return ResponseEntity.ok(comments);
    }
}
