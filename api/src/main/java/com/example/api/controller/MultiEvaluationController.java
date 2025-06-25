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
<<<<<<< HEAD
=======
import java.util.ArrayList;
>>>>>>> mizukami

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
<<<<<<< HEAD
    public List<TargetResponseDto> getTargetsWithEvaluations(HttpServletRequest request) {
        Integer evaluatorId = jwtService.extractUserId(request);
        return userService.getTargetsWithEvaluation(evaluatorId.longValue());
    }
}
=======
    public List<TargetResponseDto> getTargetsWithEvaluations(
        HttpServletRequest request,
        @RequestParam(name = "phase") Long phaseId, // 'phase' パラメータを受け取る (必須)
        @RequestParam(name = "target_id", required = false) Long targetId // 'target_id' パラメータを受け取る (任意)
    ) {
        Integer evaluatorId = jwtService.extractUserId(request);
        if (evaluatorId == null) {
            throw new IllegalArgumentException("Evaluator ID not found in JWT.");
        }

        // UserServiceから評価者が評価すべき対象者を取得
        // UserServiceはすでにevaluatorIdとphaseIdに基づいて絞り込みを行っているはず
        // （例：ログインユーザーが評価可能な特定の対象者のみを返すロジック）
        List<TargetResponseDto> targets = userService.getTargetsWithEvaluation(evaluatorId.longValue(), phaseId);

        // targetIdがURLで指定されている場合のみ、そのターゲットに絞り込む
        // 今回のフローでは、targetIdは初回ロード時には渡さない想定なので、
        // このフィルタリングは通常実行されないか、特定の既存評価読み込み時に使用される
        if (targetId != null && targetId > 0) {
            return targets.stream()
                          .filter(t -> t.getId() != null && t.getId().equals(targetId))
                          .toList();
        } else {
            // targetIdが指定されていない場合（初回ロード時）は、
            // UserServiceが返した評価可能な全ての対象者をそのまま返す
            return targets;
        }
    }
}
>>>>>>> mizukami
