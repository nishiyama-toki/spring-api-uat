package com.example.api.phase.controller;

import com.example.api.phase.dto.PhaseDto;
import com.example.api.phase.dto.PhaseEditDto;
import com.example.api.entity.Phase;
import com.example.api.repository.PhaseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.api.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.Map;

@RestController
@RequestMapping("/api")
// Reactアプリ（http://localhost:3000）からの全メソッドのアクセスを許可
@CrossOrigin(origins = "http://localhost:3000")
public class AdminPhaseController {

    @Autowired
    private PhaseRepository phaseRepository;

    // -------------------------------
    // POST /api/submission_period
    // 評価期間の新規登録処理
    // -------------------------------
    @PostMapping("/submission_period")
    public ResponseEntity<?> createPhase(@RequestBody PhaseDto dto) {

        // バリデーション：開始日が終了日より後ならエラー
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return ResponseEntity.badRequest().body(
                Map.of("date_error", "開始日は終了日よりも前にしてください")
            );
        }

        // バリデーション：既に同じフェーズ・期間名のデータがある場合
        if (phaseRepository.existsByPhaseNumberAndPeriodName(dto.getPhaseNumber(), dto.getPeriodName())) {
            return ResponseEntity.badRequest().body(
                Map.of("duplicate_error", "既に同じ評価期とクォーターが存在します")
            );
        }

        // 登録処理
        Phase phase = dto.toEntity();
        phaseRepository.save(phase);

        return ResponseEntity.ok(Map.of(
            "message", "登録成功",
            "id", phase.getId() // 自動採番IDを返す
        ));
    }

    // -------------------------------
    // PUT /api/submission_period_edit
    // 評価期間の編集処理
    // -------------------------------
    @PutMapping("/submission_period_edit")
    public ResponseEntity<?> editPhase(@RequestBody PhaseEditDto dto) {

        // ID存在チェック
        Phase target = phaseRepository.findById(dto.getId()).orElse(null);
        if (target == null) {
            return ResponseEntity.status(404).body(
                Map.of("not_found", "指定されたIDの評価期間が存在しません")
            );
        }

        // 日付バリデーション
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return ResponseEntity.badRequest().body(
                Map.of("date_error", "開始日は終了日よりも前にしてください")
            );
        }

        // 自分以外に同一フェーズ・期間が存在するか
        if (phaseRepository.existsByPhaseNumberAndPeriodNameAndIdNot(
                dto.getPhaseNumber(), dto.getPeriodName(), dto.getId())) {
            return ResponseEntity.badRequest().body(
                Map.of("duplicate_error", "既に同じ評価期とクォーターが存在します")
            );
        }

        // 更新処理
        target.setStartDate(dto.getStartDate());
        target.setEndDate(dto.getEndDate());
        target.setPeriodName(dto.getPeriodName());
        target.setPhaseNumber(dto.getPhaseNumber());

        phaseRepository.save(target);

        return ResponseEntity.ok(Map.of("message", "編集成功"));
    }

    // -------------------------------
    // GET /api/submission_periods
    // 評価期間一覧取得API
    // -------------------------------
    @GetMapping("/submission_periods")
    public ResponseEntity<?> getAllPhases(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        System.out.println("認証情報: " + userDetails); // ←ログ出力

        if (userDetails == null || !userDetails.isAdmin()) {
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }

        return ResponseEntity.ok(phaseRepository.findAll());
    }
    
    // -------------------------------
    // トークン認証 
    // -------------------------------
    @GetMapping("/admin-only/phase")
    public ResponseEntity<?> adminCheck(@AuthenticationPrincipal(expression = "this") UserDetailsImpl userDetails) {
        if (userDetails == null || !userDetails.isAdmin()) {
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }

        return ResponseEntity.ok("管理者アクセスOK");
    }
}
