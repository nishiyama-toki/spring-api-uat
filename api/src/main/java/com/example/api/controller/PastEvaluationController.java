package com.example.api.controller;

import com.example.api.dto.PastEvaluationResponse;
import com.example.api.service.PastEvaluationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class PastEvaluationController {

    private final PastEvaluationService service;

    public PastEvaluationController(PastEvaluationService service) {
        this.service = service;
    }

    @GetMapping("/record")
    public PastEvaluationResponse getRecord(
      @RequestParam("phase_id")  int phaseId,
      @RequestParam("target_id") int targetId
    ) {
        return service.getPastEvaluation(phaseId, targetId);
    }
}
