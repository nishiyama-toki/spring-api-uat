package com.example.api.controller;

import com.example.api.dto.UserRegisterDto;
import com.example.api.dto.UserEditDto;
import com.example.api.entity.User;
import com.example.api.repository.UserRepository;
import com.example.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;  // ← これ追加！

    // ------------------------
    // 一覧取得
    // ------------------------
    @GetMapping("/user_management_DB")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ------------------------
    // ユーザー登録（パスワードをハッシュ化）
    // ------------------------
    @PostMapping("/user_management_register")
    public User registerUser(@RequestBody UserRegisterDto dto) {
        return userService.registerUser(dto); // ← Serviceに処理を委譲
    }

    // ------------------------
    // ユーザー更新
    // ------------------------
    @PutMapping("/user_management_edit")
    public User updateUser(@RequestBody UserEditDto dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setAdmin(dto.isAdmin());

        // パスワードは変更しない（setPassword は呼ばない）
        return userRepository.save(user);
}



    // ------------------------
    // ユーザー削除
    // ------------------------
    @DeleteMapping("/user_management_delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Integer> body) {
        Integer id = body.get("id");
        System.out.println("削除対象ID: " + id);

        if (id == null || !userRepository.existsById(id)) {
            System.out.println("ユーザーが存在しません: id=" + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ユーザーが存在しません");
        }

        userRepository.deleteById(id);
        System.out.println("削除成功: id=" + id);
        return ResponseEntity.ok().build();
    }



    // -------------------------------
    //トークン認証 
    // -------------------------------
    /*
    @GetMapping("/api/admin-only")
    public ResponseEntity<?> adminCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (!userDetails.isAdmin()) {
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }
    return ResponseEntity.ok("管理者アクセスOK");
    }
    */



}
