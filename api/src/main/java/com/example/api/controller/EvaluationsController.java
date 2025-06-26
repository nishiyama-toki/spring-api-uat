package com.example.api.controller;

import com.example.api.dto.EvaluationResponseDTO;
import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.PhaseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EvaluationsController {

    private final PhaseRepository    phaseRepo;
    private final EmployeeRepository employeeRepo;

    @GetMapping("/evaluations")
    public ResponseEntity<?> getEvaluations(@RequestParam Long evaluatorId) {

        Employee me = employeeRepo.findById(evaluatorId).orElse(null);
        if (me == null) {
            return ResponseEntity.badRequest().body("指定されたユーザーが存在しません");
        }

        LocalDate today = LocalDate.now();
        List<EvaluationResponseDTO> list = new ArrayList<>();

        for (Phase p : phaseRepo.findAll()) {

            if (p.getStartDate() != null && p.getEndDate() != null) {
                LocalDate start = p.getStartDate();
                LocalDate end   = p.getEndDate();

                if (!today.isBefore(start) && !today.isAfter(end)) {
                    // 自己評価
                    list.add(EvaluationResponseDTO.of(
                            p.getId(),       // phaseId
                            me.getId(),      // targetId
                            me.getName(),    // targetName
                            p.getPhaseNumber(),
                            p.getPeriodName(),
                            start,
                            end,
                            "SELF"
                    ));

                    // 多面評価
                    list.add(EvaluationResponseDTO.of(
                            p.getId(),       // phaseId
                            0L,              // targetId (ダミー)
                            "",              // targetName (ダミー)
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
