package com.example.api.controller;

import com.example.api.dto.PastEvaluationResponse;
import com.example.api.service.PastEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PastEvaluationController {

    private final PastEvaluationService pastEvaluationService;

    public PastEvaluationController(PastEvaluationService pastEvaluationService) {
        this.pastEvaluationService = pastEvaluationService;
    }

    /**
     * 過去の評価履歴を取得します。
     * phaseIdと、JWTから取得した安全なuserIdを使用します。
     */
    @GetMapping("/past-evaluations")
    public ResponseEntity<PastEvaluationResponse> getPastEvaluations(
            @RequestParam("phase_id") Long phaseId, // ★ intからLongに変更
            Authentication authentication
    ) {
        // JWTから安全に本人IDを取得
        Long targetId = Long.parseLong(authentication.getName());
        
        // Serviceを呼び出す
        PastEvaluationResponse response = pastEvaluationService.getPastEvaluation(targetId, phaseId);
        
        return ResponseEntity.ok(response);
    }
}
