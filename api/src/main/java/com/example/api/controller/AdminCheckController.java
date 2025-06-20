package com.example.api.controller;

import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminCheckController {

    private final EmployeeRepository employeeRepository;

    public AdminCheckController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/api/admin-only")
    public ResponseEntity<?> checkAdmin(@RequestParam("email") String email) {
        Employee user = employeeRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(404).body("ユーザーが見つかりません");
        }

        if (!user.getIsAdmin()) {
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }

        return ResponseEntity.ok("管理者アクセスOK");
    }
}
