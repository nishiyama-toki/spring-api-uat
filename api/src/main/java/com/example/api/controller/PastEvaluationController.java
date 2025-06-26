package com.example.api.controller;

import com.example.api.dto.PastEvaluationResponse;
import com.example.api.entity.Employee;
import com.example.api.service.PastEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PastEvaluationController {

    private final PastEvaluationService pastEvaluationService;

    @Autowired
    public PastEvaluationController(PastEvaluationService pastEvaluationService) {
        this.pastEvaluationService = pastEvaluationService;
    }

    @GetMapping("/past-evaluations")
    public ResponseEntity<PastEvaluationResponse> getPastEvaluations(
            @RequestParam("phase_id") Long phaseId,
            Authentication authentication
    ) {
        // authentication からログイン中の社員情報を取得
        Employee employee = (Employee) authentication.getPrincipal();
        Long targetId = employee.getId();

        PastEvaluationResponse response = pastEvaluationService.getPastEvaluation(targetId, phaseId);
        return ResponseEntity.ok(response);
    }
}
