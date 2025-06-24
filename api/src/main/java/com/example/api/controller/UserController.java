package com.example.api.controller;

import com.example.api.dto.UserRegisterDto;
import com.example.api.dto.UserEditDto;
// import com.example.api.entity.User; // Userエンティティは使用しない方向でコメントアウト
import com.example.api.entity.Employee; // Employeeエンティティを使用
// import com.example.api.repository.UserRepository; // UserRepositoryは使用しない方向でコメントアウト
import com.example.api.repository.EmployeeRepository; // EmployeeRepositoryを使用
import com.example.api.service.UserService; // UserServiceを使用
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional; // Optionalを使用するために追加

@RestController
@RequestMapping("/api")
public class UserController {

    // @Autowired // UserRepositoryは使用しない方向でコメントアウト
    // private UserRepository userRepository;

    @Autowired // EmployeeRepositoryを使用
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserService userService; // UserServiceを使用

    @GetMapping("/user_management_DB")
    public List<Employee> getAllUsers() { // User -> Employee に変更
        return employeeRepository.findAll(); // userRepository -> employeeRepository に変更
    }

    @PostMapping("/user_management_register")
    public Employee registerUser(@RequestBody UserRegisterDto dto) { // User -> Employee に変更
        // UserService.registerUser の戻り値が Employee であるため、そのまま返す
        return userService.registerUser(dto);
    }

    @PutMapping("/user_management_edit")
    public Employee updateUser(@RequestBody UserEditDto dto) { // User -> Employee に変更
        // User -> Employee に変更して、employeeRepository を使用
        Employee employee = employeeRepository.findById(dto.getId().longValue()) // intをLongに変換 (dto.getId()がIntegerの場合)
                                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません")); // NotFoundExceptionなどを推奨

        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        // dtoにsetRole, setAdmin, setPermissionがUserEditDtoに存在すると仮定
        employee.setRole(dto.getRole());
        employee.setIsAdmin(dto.isAdmin()); // isAdmin()はbooleanを返すはずなので、そのままセット
        employee.setPermission(dto.getPermission());
        
        return employeeRepository.save(employee); // userRepository -> employeeRepository に変更
    }

    @DeleteMapping("/user_management_delete")
    public ResponseEntity<?> deleteUser(@RequestBody Map<String, Integer> body) {
        Integer id = body.get("id");
        if (id == null) { // idがnullの場合のチェックを追加
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IDが指定されていません");
        }
        // userRepository -> employeeRepository に変更し、idをLongに変換
        if (!employeeRepository.existsById(id.longValue())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ユーザーが存在しません");
        }
        employeeRepository.deleteById(id.longValue()); // idをLongに変換
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin-only")
    public ResponseEntity<?> adminCheck() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // employeeRepository を使用
        Employee emp = employeeRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        if (!Boolean.TRUE.equals(emp.getIsAdmin())) { // Boolean型なのでBoolean.TRUE.equals()が安全
            return ResponseEntity.status(403).body("管理者権限が必要です");
        }
        return ResponseEntity.ok("管理者アクセスOK");
    }
}