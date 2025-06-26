package com.example.api.controller;

import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.EvaluationRepository;
import com.example.api.repository.PhaseRepository;
import com.example.api.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ReminderBatchController {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("/reminder/batch")
    public ResponseEntity<?> sendReminderBatch() {
        List<Phase> phases = phaseRepository
                .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now());
        Phase currentPhase = phases.isEmpty() ? null : phases.get(0);

        if (currentPhase == null) {
            return ResponseEntity.badRequest().body(Map.of("結果", "failure", "エラーメッセージ", "現在有効な評価フェーズがありません"));
        }

        List<Employee> unsubmitted = evaluationRepository.findEmployeesNotSubmitted(currentPhase.getId());

        int sendCount = 0;
        List<Map<String, String>> sentList = new ArrayList<>();
        for (Employee e : unsubmitted) {
            // currentPhase.getSelfEvalDue().toString() を currentPhase.getEndDate().toString() に変更
            // もし endDate も null の可能性があるなら、nullチェックを追加
            String dueDateString = (currentPhase.getEndDate() != null) ? currentPhase.getEndDate().toString() : "期限不明";

            boolean result = mailService.sendReminder(
                    e.getEmail(),
                    e.getName(),
                    currentPhase.getName(),
                    dueDateString // 修正後の期限文字列を使用
            );
            if (result) {
                sendCount++;
                sentList.add(Map.of("name", e.getName(), "email", e.getEmail()));
            }
        }

        Map<String, Object> response = Map.of(
                "送信件数", sendCount,
                "実行日", LocalDateTime.now().toString(),
                "送信先リスト", sentList,
                "結果", "success"
        );
        return ResponseEntity.ok(response);
    }
}