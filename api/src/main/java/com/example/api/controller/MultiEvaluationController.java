package com.example.api.controller;

// --- ▼▼▼ ここから不足しているimport文を追加 ▼▼▼ ---
import com.example.api.dto.MultiEvaluationDto;
import com.example.api.service.MultiEvaluationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
// --- ▲▲▲ ここまで追加 ▲▲▲ ---

import com.example.api.dto.TargetResponseDto;
import com.example.api.entity.Employee;
import com.example.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multi-evaluations")
@RequiredArgsConstructor
public class MultiEvaluationController {

    private final UserService userService;
    // --- ▼▼▼ MultiEvaluationService を利用可能にするため追加 ▼▼▼ ---
    private final MultiEvaluationService multiEvaluationService;
    // --- ▲▲▲ 追加 ▲▲▲ ---

    @GetMapping("/targets")
    public List<TargetResponseDto> getTargetsWithEvaluations(
            Authentication authentication,
            @RequestParam("phase_id") Long phaseId,
            @RequestParam(name = "target_id", required = false) Long targetId
    ) {
        Employee loginUser = (Employee) authentication.getPrincipal();
        Long evaluatorId = loginUser.getId();

        List<TargetResponseDto> targets = userService.getTargetsWithEvaluation(evaluatorId, phaseId);

        if (targetId != null && targetId > 0) {
            return targets.stream()
                    .filter(t -> t.getId().equals(targetId))
                    .toList();
        } else {
            return targets;
        }
    }

    /**
     * POST /api/multi-evaluations
     * 評価内容を登録・更新する
     */
    @PostMapping
    public ResponseEntity<?> registerEvaluations(
            @Valid @RequestBody MultiEvaluationDto evaluationDto,
            Authentication authentication
    ) {
        // 認証情報から評価者IDを取得
        Employee loginUser = (Employee) authentication.getPrincipal();
        Long evaluatorId = loginUser.getId();

        // サービスを呼び出して評価を登録
        int registeredCount = multiEvaluationService.registerEvaluations(evaluationDto, evaluatorId);

        // JSON形式でレスポンスを返す
        return ResponseEntity.ok().body(
            "{\"message\": \"" + registeredCount + "件の評価を登録しました\"}"
        );
    }
}
