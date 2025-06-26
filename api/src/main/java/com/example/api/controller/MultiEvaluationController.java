package com.example.api.controller;

import com.example.api.entity.Employee;
import com.example.api.dto.MultiEvaluationDto;
import com.example.api.dto.TargetResponseDto;
import com.example.api.service.MultiEvaluationService;
import com.example.api.service.UserService;
import com.example.api.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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

    @GetMapping("/multi-evaluations/targets")
    public List<TargetResponseDto> getTargetsWithEvaluations(
        HttpServletRequest request,
        @RequestParam(name = "phase") Long phaseId, // 'phase' パラメータを受け取る (必須)
        @RequestParam(name = "target_id", required = false) Long targetId, // 'target_id' パラメータを受け取る (任意)
        Authentication authentication
    ) {
        // ★ ここで userId を明示的に定義する
        Employee principal = (Employee) authentication.getPrincipal();
        Long userId = principal.getId();

        List<TargetResponseDto> targets = userService.getTargetsWithEvaluation(userId, phaseId);

        if (targetId != null && targetId > 0) {
            return targets.stream()
                        .filter(t -> t.getId() != null && t.getId().equals(targetId))
                        .toList();
        } else {
            return targets;
        }
    }
}