package com.example.api.phase.controller;

import com.example.api.phase.dto.PhaseDto;
import com.example.api.phase.dto.PhaseEditDto;
import com.example.api.entity.Phase;
import com.example.api.repository.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AdminPhaseController {

    @Autowired
    private PhaseRepository phaseRepository;

    @PostMapping("/submission_period")
    public ResponseEntity<?> createPhase(@RequestBody PhaseDto dto) {
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return ResponseEntity.badRequest().body(Map.of("date_error", "開始日は終了日より前にしてください"));
        }
        if (phaseRepository.existsByPhaseNumberAndPeriodName(dto.getPhaseNumber(), dto.getPeriodName())) {
            return ResponseEntity.badRequest().body(Map.of("duplicate_error", "既に同じ評価期とクォーターが存在します"));
        }

        Phase phase = dto.toEntity();
        phaseRepository.save(phase);

        return ResponseEntity.ok(Map.of("message", "登録成功", "id", phase.getId()));
    }

    @PutMapping("/submission_period_edit")
    public ResponseEntity<?> editPhase(@RequestBody PhaseEditDto dto) {
        Phase target = phaseRepository.findById(dto.getId()).orElse(null);
        if (target == null) {
            return ResponseEntity.status(404).body(Map.of("not_found", "指定されたIDの評価期間が存在しません"));
        }

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            return ResponseEntity.badRequest().body(Map.of("date_error", "開始日は終了日より前にしてください"));
        }

        if (phaseRepository.existsByPhaseNumberAndPeriodNameAndIdNot(
                dto.getPhaseNumber(), dto.getPeriodName(), dto.getId())) {
            return ResponseEntity.badRequest().body(Map.of("duplicate_error", "既に同じ評価期とクォーターが存在します"));
        }

        target.setStartDate(dto.getStartDate());
        target.setEndDate(dto.getEndDate());
        target.setPeriodName(dto.getPeriodName());
        target.setPhaseNumber(dto.getPhaseNumber());
        phaseRepository.save(target);

        return ResponseEntity.ok(Map.of("message", "編集成功"));
    }

    @GetMapping("/submission_periods")
    public ResponseEntity<?> getAllPhases(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || userDetails.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }
        return ResponseEntity.ok(phaseRepository.findAll());
    }

    @GetMapping("/admin-only/phase")
    public ResponseEntity<?> adminCheck(@AuthenticationPrincipal UserDetails userDetails) {
        boolean isAdmin = userDetails != null &&
                userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) return ResponseEntity.status(403).body("管理者権限が必要です");

        return ResponseEntity.ok("管理者アクセスOK");
    }
}
