package com.example.api.controller;

import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.EvaluationRepository;
import com.example.api.repository.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class HomeApiController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    @GetMapping("/home")
    public ResponseEntity<?> getHome(@AuthenticationPrincipal Employee employee) {
        List<Phase> phases = phaseRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());
        Phase currentPhase = phases.isEmpty() ? null : phases.get(0);

        if (currentPhase == null) {
            return ResponseEntity.ok(Map.of(
                    "message", "現在有効な評価フェーズはありません。",
                    "alert_list", List.of(),
                    "not_submitted_list", List.of(),
                    "user_name", employee.getName(),
                    "is_admin", employee.getIsAdmin()
            ));
        }

        boolean hasSubmitted = evaluationRepository.existsByEvaluatorIdAndPhaseId(
                employee.getId().longValue(), currentPhase.getId().longValue()
        );

        List<Map<String, Object>> alertList = new ArrayList<>();
        if (!hasSubmitted && !Boolean.TRUE.equals(employee.getIsAdmin())) {
            alertList.add(Map.of(
                    "message", "あなたはまだ評価項目を提出していません",
                    "date", LocalDate.now().toString(),
                    "type", "warning"
            ));
        }

        List<Map<String, String>> notSubmittedList = null;
        if (Boolean.TRUE.equals(employee.getIsAdmin())) {
            List<Employee> unsubmitted = evaluationRepository.findEmployeesNotSubmitted(currentPhase.getId());
            notSubmittedList = unsubmitted.stream()
                    .map(e -> Map.of("name", e.getName(), "email", e.getEmail()))
                    .collect(Collectors.toList());
        }

        Map<String, Object> response = Map.of(
                "overview", "多面評価の概要テキストです。",
                "alert_list", alertList,
                "not_submitted_list", notSubmittedList != null ? notSubmittedList : List.of(),
                "user_name", employee.getName(),
                "is_admin", employee.getIsAdmin(),
                "current_phase", Map.of(
                        "name", currentPhase.getName(),
                        "start_date", currentPhase.getStartDate().toString(),
                        "end_date", currentPhase.getEndDate().toString(),
                        "self_eval_due", currentPhase.getSelfEvalDue() != null ? currentPhase.getSelfEvalDue().toString() : "",
                        "peer_eval_due", currentPhase.getPeerEvalDue() != null ? currentPhase.getPeerEvalDue().toString() : ""
                )
        );

        return ResponseEntity.ok(response);
    }
}
