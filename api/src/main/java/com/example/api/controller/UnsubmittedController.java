package com.example.api.controller;

import com.example.api.dto.UnsubmittedResponse;
import com.example.api.service.UnsubmittedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UnsubmittedController {

    private final UnsubmittedService unsubmittedService;

    @GetMapping("/unsubmitted")
    public ResponseEntity<Map<String, List<UnsubmittedResponse>>> getUnsubmitted(
            @RequestParam("phase_id") Long phaseId) {
        List<UnsubmittedResponse> list = unsubmittedService.findUnsubmittedByPhase(phaseId);
        return ResponseEntity.ok(Map.of("unsubmitted_list", list));
    }
}
