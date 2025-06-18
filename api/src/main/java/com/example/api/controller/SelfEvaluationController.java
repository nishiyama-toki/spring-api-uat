package com.example.api.controller;

import com.example.api.dto.SelfEvaluationRequest;
import com.example.api.dto.SelfEvaluationResponseDTO;
import com.example.api.service.SelfEvaluationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication; // JWT認証で必要になるため、インポートは残します
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class SelfEvaluationController {

    private final SelfEvaluationService selfEvaluationService;

    public SelfEvaluationController(SelfEvaluationService selfEvaluationService) {
        this.selfEvaluationService = selfEvaluationService;
    }

    //------------------------------------------------------------------------------------------
    // データ取得 (GET)
    //------------------------------------------------------------------------------------------

    /**
     * ★★★ テスト用のJWT認証を使用しないバージョン (現在有効) ★★★
     * 自己評価データを取得します。
     * user_idをリクエストパラメータで直接受け取ります。
     */
    @GetMapping("/self-evaluations")
    public ResponseEntity<?> getSelfEvaluation(
            @RequestParam("phase_id") Integer phaseId,
            @RequestParam("user_id") Integer userId
    ) {
        Optional<SelfEvaluationResponseDTO> evaluationOpt = selfEvaluationService.getSelfEvaluation(phaseId, userId);
        
        return evaluationOpt
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /*
    // ★★★ 本番用のJWT認証を使用するバージョン ★★★
    // 結合テストの際は、こちらを有効化し、上のテスト用メソッドをコメントアウトしてください。
    @GetMapping("/self-evaluations")
    public ResponseEntity<?> getSelfEvaluation(
            @RequestParam("phase_id") Integer phaseId,
            Authentication authentication
    ) {
        // JWTの'sub'クレームからユーザーIDを取得します
        Integer userId = Integer.parseInt(authentication.getName());
        Optional<SelfEvaluationResponseDTO> evaluationOpt = selfEvaluationService.getSelfEvaluation(phaseId, userId);
        
        return evaluationOpt
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
    */


    //------------------------------------------------------------------------------------------
    // データ登録・更新 (POST)
    //------------------------------------------------------------------------------------------

    /**
     * ★★★ テスト用のJWT認証を使用しないバージョン (現在有効) ★★★
     * 自己評価データを登録または更新します。
     * 全てのIDをリクエストボディに含めて受け取ります。
     */
    @PostMapping("/self-evaluations")
    public ResponseEntity<Map<String, Object>> createOrUpdateSelfEvaluation(
            @RequestBody SelfEvaluationRequest request
    ) {
        selfEvaluationService.saveOrUpdateSelfEvaluation(request);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("count", 1);
        response.put("message", "評価を登録・更新しました。");
        
        return ResponseEntity.ok(response);
    }

    /*
    // ★★★ 本番用のJWT認証を使用するバージョン ★★★
    // 結合テストの際は、こちらを有効化し、上のテスト用メソッドをコメントアウトしてください。
    @PostMapping("/self-evaluations")
    public ResponseEntity<Map<String, Object>> createOrUpdateSelfEvaluation(
            @RequestBody SelfEvaluationRequest request,
            Authentication authentication
    ) {
        // JWTからユーザーIDを取得し、それを評価者・対象者IDとしてセットします
        Integer userId = Integer.parseInt(authentication.getName());
        request.setEvaluatorId(userId);
        request.setTargetId(userId);

        selfEvaluationService.saveOrUpdateSelfEvaluation(request);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("count", 1);
        response.put("message", "評価を登録・更新しました。");
        
        return ResponseEntity.ok(response);
    }
    */
}
