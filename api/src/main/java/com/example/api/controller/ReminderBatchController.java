package com.example.api.controller;

import com.example.api.entity.Employee;
import com.example.api.entity.Phase;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.EvaluationRepository;
import com.example.api.repository.PhaseRepository;
import com.example.api.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class ReminderBatchController {

    @Autowired
    private PhaseRepository phaseRepository;

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("/api/reminder/batch")
    public ResponseEntity<?> sendReminderBatch() {
        // 現在の評価フェーズ取得（修正版）
        Phase currentPhase = phaseRepository
            .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate.now(), LocalDate.now())
            .orElse(null);

        if (currentPhase == null) {
            return ResponseEntity.badRequest().body(Map.of("結果", "failure", "エラーメッセージ", "現在有効な評価フェーズがありません"));
        }

        // 未提出者一覧取得
        List<Employee> unsubmitted = evaluationRepository.findEmployeesNotSubmitted(currentPhase.getId());

        // メール送信
        int sendCount = 0;
        List<Map<String, String>> sentList = new ArrayList<>();
        for (Employee e : unsubmitted) {
            boolean result = mailService.sendReminder(
                e.getEmail(),
                e.getName(),
                currentPhase.getName(),
                currentPhase.getSelfEvalDue().toString()
            );
            if (result) {
                sendCount++;
                sentList.add(Map.of("name", e.getName(), "email", e.getEmail()));
            }
        }

        // レスポンス
        Map<String, Object> response = Map.of(
            "送信件数", sendCount,
            "実行日", LocalDateTime.now().toString(),
            "送信先リスト", sentList,
            "結果", "success"
        );
        return ResponseEntity.ok(response);
    }
}
