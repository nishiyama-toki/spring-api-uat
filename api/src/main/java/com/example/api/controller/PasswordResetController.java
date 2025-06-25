package com.example.api.controller;

import com.example.api.dto.ResetPasswordRequest;
import com.example.api.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
<<<<<<< HEAD
=======
        System.out.println("TOKEN: " + request.getToken());
        System.out.println("NEW PASSWORD: " + request.getNewPassword());

>>>>>>> mizukami
        boolean success = passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("無効または期限切れのトークンです");
        }
    }
}
