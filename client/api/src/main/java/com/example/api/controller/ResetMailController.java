// メール送信用controller

package com.example.api.controller;

import com.example.api.dto.ResetMailRequest;
import com.example.api.service.ResetMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin; // ★これをインポート

//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true") // ★これを追加
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ResetMailController {

    private final ResetMailService resetMailService;

    @PostMapping("/reset-mail")
    public ResponseEntity<?> sendResetMail(@RequestBody ResetMailRequest request) {
        boolean result = resetMailService.sendResetMail(request.getEmail());

        if (result) {
            return ResponseEntity.ok("リセットメールを送信しました。");
        } else {
            return ResponseEntity.badRequest().body("入力されたメールアドレスは登録されていません。");
        }
    }
}
