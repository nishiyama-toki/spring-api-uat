package com.example.api.controller;

import com.example.api.dto.PhaseResponse;
import com.example.api.service.PhaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PhaseController {

    private final PhaseService phaseService;

    public PhaseController(PhaseService phaseService) {
        this.phaseService = phaseService;
    }

    @GetMapping("/phases")
    public List<PhaseResponse> getPhases() {
        return phaseService.getPhases();
    }
}
