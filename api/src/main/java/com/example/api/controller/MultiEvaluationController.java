package com.example.api.controller;

import com.example.api.dto.MultiEvaluationDto;
import com.example.api.dto.TargetResponseDto;
import com.example.api.service.MultiEvaluationService;
import com.example.api.service.UserService;
import com.example.api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/multi-evaluations")
public class MultiEvaluationController {

    @Autowired
    private MultiEvaluationService multiEvaluationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @PostMapping
    public Map<String, Object> submitEvaluations(
            @RequestBody MultiEvaluationDto dto,
            HttpServletRequest request
    ) {
        int count = multiEvaluationService.registerEvaluations(dto, request);
        return Map.of(
                "count", count,
                "message", "評価を登録しました。"
        );
    }

    @GetMapping("/targets")
    public List<TargetResponseDto> getTargetsWithEvaluations(HttpServletRequest request) {
        Integer evaluatorId = jwtService.extractUserId(request);
        return userService.getTargetsWithEvaluation(evaluatorId.longValue());
    }
}
