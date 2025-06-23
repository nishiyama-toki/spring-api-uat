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
@CrossOrigin(origins = "http://localhost:3000")
public class SelfEvaluationController {

    private final SelfEvaluationService selfEvaluationService;

    public SelfEvaluationController(SelfEvaluationService selfEvaluationService) {
        this.selfEvaluationService = selfEvaluationService;
    }

    // --- テスト用 ---
    @GetMapping("/self-evaluations")
    public ResponseEntity<?> getSelfEvaluation(
            @RequestParam("phase_id") Long phaseId, // ★ IntegerからLongに変更
            @RequestParam("user_id") Long userId   // ★ IntegerからLongに変更
    ) {
        Optional<SelfEvaluationResponseDTO> evaluationOpt = selfEvaluationService.getSelfEvaluation(phaseId, userId);
        
        return evaluationOpt
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/self-evaluations")
    public ResponseEntity<Map<String, Object>> createOrUpdateSelfEvaluation(
            @RequestBody SelfEvaluationRequest request
    ) {
        selfEvaluationService.saveOrUpdateSelfEvaluation(request);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("count", 1);
        response.put("message", "評価を登録・更新しました。");
        
        return ResponseEntity.ok(response);
    }


    /*
    // --- JWT認証を使用する本番用 ---
    @GetMapping("/self-evaluations")
    public ResponseEntity<?> getSelfEvaluation(
            @RequestParam("phase_id") Long phaseId,
            Authentication authentication
    ) {
        Long userId = Long.parseLong(authentication.getName());
        Optional<SelfEvaluationResponseDTO> evaluationOpt = selfEvaluationService.getSelfEvaluation(phaseId, userId);
        
        return evaluationOpt
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

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
    */
}
