package com.example.api.controller;

import com.example.api.dto.UserRegisterDto;
import com.example.api.dto.UserEditDto;
import com.example.api.entity.User;
import com.example.api.entity.Employee;
import com.example.api.repository.UserRepository;
import com.example.api.repository.EmployeeRepository;
import com.example.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/user_management_DB")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/user_management_register")
    public User registerUser(@RequestBody UserRegisterDto dto) {
        return userService.registerUser(dto);
    }

    @PutMapping("/user_management_edit")
    public User updateUser(@RequestBody UserEditDto dto) {
        User user = userRepository.findById(dto.getId()).orElseThrow();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setAdmin(dto.isAdmin());
        user.setPermission(dto.getPermission());
        return userRepository.save(user);
    }

    @DeleteMapping("/user_management_delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Integer> body) {
        Integer id = body.get("id");
        if (id == null || !userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ユーザーが存在しません");
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin-only")
    public ResponseEntity<?> adminCheck() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Employee emp = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        if (!Boolean.TRUE.equals(emp.getIsAdmin())) {
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }
        return ResponseEntity.ok("管理者アクセスOK");
    }
}
