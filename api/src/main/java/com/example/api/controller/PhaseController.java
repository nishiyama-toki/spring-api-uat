package com.example.api.controller;
import com.example.api.dto.PhaseResponse;
import com.example.api.service.PhaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PhaseController {
    private final PhaseService phaseService;
    // ① フェーズ一覧取得
    @GetMapping("/phases")
    public List<PhaseResponse> getPhases() {
        return phaseService.getPhases();
    }
    // ② フェーズ ID で 1件取得（追加）
    @GetMapping("/phases/{id}")
    public PhaseResponse getPhaseById(@PathVariable Long id) {
        return phaseService.getPhaseById(id);
    }
}
