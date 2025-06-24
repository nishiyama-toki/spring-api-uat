package com.example.api.controller;

import com.example.api.dto.EvaluationResponseDTO;
import com.example.api.entity.Employee;
import com.example.api.entity.Evaluation;
import com.example.api.repository.EmployeeRepository;
import com.example.api.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationsController {

    private final EvaluationService evaluationService;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EvaluationsController(EvaluationService evaluationService,
                                 EmployeeRepository employeeRepository) {
        this.evaluationService = evaluationService;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public List<EvaluationResponseDTO> getEvaluations(
            @AuthenticationPrincipal UserDetails loginUser) {
        String email = loginUser.getUsername();
        Employee evaluator = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("社員が見つかりません"));
        Long evaluatorId = evaluator.getId();

        List<Evaluation> entities = evaluationService.getEvaluationsInPeriod(evaluatorId);
        return entities.stream().map(EvaluationResponseDTO::fromEntity).toList();
    }
}
