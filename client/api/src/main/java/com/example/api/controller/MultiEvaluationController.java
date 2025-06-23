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

@CrossOrigin(
    origins = "http://localhost:3000",
    allowCredentials = "true" // ← これがないと credentials: 'include' に対応できない！
)
@RestController
@RequestMapping("/api/multi-evaluations")
public class MultiEvaluationController {

    @Autowired
    private MultiEvaluationService multiEvaluationService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    /**
     * 多面評価を一括登録
     */
    @PostMapping
    public Map<String, Object> submitEvaluations(
        @RequestBody MultiEvaluationDto dto,
        HttpServletRequest request
    ) {
        System.out.println("🔥 Controller: POST /api/multi-evaluations 到達");
        System.out.println("🔥 DTO 内容: " + dto);

        int count = multiEvaluationService.registerEvaluations(dto, request);
        return Map.of(
            "count", count,
            "message", "評価を登録しました。"
        );
    }

    /**
     * 多面評価対象者一覧＋既存評価を取得
     */
    @GetMapping("/targets")
    public List<TargetResponseDto> getTargetsWithEvaluations(HttpServletRequest request) {
        Integer evaluatorId = jwtService.extractUserId(request);
        return userService.getTargetsWithEvaluation(evaluatorId.longValue()); // ← Longに変換！
    }
} 
