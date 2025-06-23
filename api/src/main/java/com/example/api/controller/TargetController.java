package com.example.api.controller;

import com.example.api.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/targets")
public class TargetController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public List<EmployeeProjection> getTargets() {
        return employeeRepository.findAllProjectedBy();
    }

    public interface EmployeeProjection {
        Integer getId();
        String getName();
        String getRole();
    }
}
