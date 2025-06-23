package com.example.api.controller;

import com.example.api.dto.PastEvaluationResponse;
import com.example.api.service.PastEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PastEvaluationController {

    private final PastEvaluationService pastEvaluationService;

    public PastEvaluationController(PastEvaluationService pastEvaluationService) {
        this.pastEvaluationService = pastEvaluationService;
    }

    @GetMapping("/past-evaluations")
    public ResponseEntity<PastEvaluationResponse> getPastEvaluations(
            @RequestParam("phase_id") Long phaseId,
            Authentication authentication
    ) {
        Long targetId = Long.parseLong(authentication.getName());
        PastEvaluationResponse response = pastEvaluationService.getPastEvaluation(targetId, phaseId);
        return ResponseEntity.ok(response);
    }
}
