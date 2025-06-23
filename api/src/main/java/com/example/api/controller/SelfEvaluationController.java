package com.example.api.controller;

import com.example.api.dto.SelfEvaluationRequest;
import com.example.api.dto.SelfEvaluationResponseDTO;
import com.example.api.service.SelfEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SelfEvaluationController {

    private final SelfEvaluationService selfEvaluationService;

    public SelfEvaluationController(SelfEvaluationService selfEvaluationService) {
        this.selfEvaluationService = selfEvaluationService;
    }

    /**
     * JWTを使用して自己評価を取得
     */
    @GetMapping("/self-evaluations")
    public ResponseEntity<?> getSelfEvaluation(
            @RequestParam("phase_id") Long phaseId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName()); // トークンからログインユーザーIDを取得
        Optional<SelfEvaluationResponseDTO> evaluationOpt = selfEvaluationService.getSelfEvaluation(phaseId, userId);
        return evaluationOpt
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * JWTを使用して自己評価を登録・更新
     */
    @PostMapping("/self-evaluations")
    public ResponseEntity<Map<String, Object>> createOrUpdateSelfEvaluation(
            @RequestBody SelfEvaluationRequest request,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        request.setEvaluatorId(userId);
        request.setTargetId(userId);

        selfEvaluationService.saveOrUpdateSelfEvaluation(request);

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("count", 1);
        response.put("message", "評価を登録・更新しました。");

        return ResponseEntity.ok(response);
    }
}
