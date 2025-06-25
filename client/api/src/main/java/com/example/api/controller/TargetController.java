package com.example.api.controller;

import com.example.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(
    origins = "http://localhost:3000",  // フロントのURLを明示
    allowCredentials = "true"           // credentials: 'include' に対応
)
@RestController
@RequestMapping("/api/targets")
public class TargetController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public List<EmployeeProjection> getTargets() {
        // evaluator_id 除外などが必要ならここで絞り込める
        return employeeRepository.findAllProjectedBy();
    }

    // --- フロントに返すプロジェクション（id, name, role） ---
    public interface EmployeeProjection {
        Integer getId();
        String getName();
        String getRole();
    }
}
