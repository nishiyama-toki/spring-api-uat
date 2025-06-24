package com.example.api.controller;

import com.example.api.dto.EvaluationResponseDTO;
import com.example.api.dto.TargetResponseDto; // UserServiceから受け取るDTO
import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.PhaseRepository;
import com.example.api.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Optionalを使用するために追加

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EvaluationsController {

    private final PhaseRepository    phaseRepo;
    private final EmployeeRepository employeeRepo;
    private final UserService        userService;

    @GetMapping("/evaluations")
    public ResponseEntity<?> getEvaluations(@RequestParam Long evaluatorId) {

        Employee me = employeeRepo.findById(evaluatorId).orElse(null);
        if (me == null) {
            return ResponseEntity.badRequest().body("指定されたユーザーが存在しません");
        }

        LocalDate today = LocalDate.now();
        List<EvaluationResponseDTO> list = new ArrayList<>();

        /* ==================== 全フェーズを走査 ==================== */
        for (Phase p : phaseRepo.findAll()) {

            if (p.getStartDate() != null && p.getEndDate() != null) {
                LocalDate start = p.getStartDate();
                LocalDate end   = p.getEndDate();

                if (!today.isBefore(start) && !today.isAfter(end)) {
                    // 自己評価 (評価対象は自分自身)
                    list.add(EvaluationResponseDTO.of(
                            me.getId(), // targetId は自分
                            me.getName(), // targetName は自分の名前
                            p.getPhaseNumber(),
                            p.getPeriodName(),
                            start,
                            end,
                            "SELF"
                    ));

                    // 多面評価 (特定の個人を指定せず、汎用的なリンクを生成)
                    // この画面では具体的な対象者を特定しない
                    // targetIdとtargetNameは、nullまたは汎用的なダミー値を設定
                    list.add(EvaluationResponseDTO.of(
                            0L,   // ダミーのtargetId (nullはDTOでLongだとエラーになる場合があるので0L)
                            "",   // ダミーのtargetName (空文字列)
                            p.getPhaseNumber(),
                            p.getPeriodName(),
                            start,
                            end,
                            "PEER"
                    ));
                }
            }
        }

        return ResponseEntity.ok(list);
    }
}