package com.example.api.service;

import com.example.api.dto.LoginRequest;
import com.example.api.dto.LoginResponse;
import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import com.example.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
<<<<<<< HEAD

        // ▼▼▼ ログの仕込み ▼▼▼
        System.out.println("ログイン処理を開始します。 email: " + request.getEmail());
        // ▲▲▲▲▲▲▲▲▲▲▲▲▲▲


        try {//ログイン認証ができない原因を探るためのデバック
=======
>>>>>>> finaltest-from-develop
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail(request.getEmail());

        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("メールアドレスまたはパスワードが正しくありません");
        }

        Employee employee = optionalEmployee.get();

        // アカウントがロックされている場合の処理
        if (employee.getIsLocked()) {
            if (employee.getLockedAt() != null &&
                employee.getLockedAt().plusMinutes(15).isBefore(LocalDateTime.now())) {

                // 15分経過 → ロック解除し、failedCount を -1 に設定
                employee.setIsLocked(false);
                employee.setFailedCount(-1);
                employee.setLockedAt(null);
                employeeRepository.save(employee);

            } else {
                // まだ15分経っていない → ロック中
                throw new RuntimeException("アカウントがロックされています。15分後に再度お試しください。");
            }
        }

        // パスワード不一致 → 失敗カウントをインクリメント
        // if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
        //     int failedCount = employee.getFailedCount() + 1;
        //     employee.setFailedCount(failedCount);

        //     if (failedCount >= 5) {
        //         employee.setIsLocked(true);
        //         employee.setLockedAt(LocalDateTime.now());
        //     }

        //     employeeRepository.save(employee);
        //     throw new RuntimeException("メールアドレスまたはパスワードが正しくありません");
        // }

        // ログ仕込み（この3行を追加）
        System.out.println("入力パスワード: " + request.getPassword());
        System.out.println("DBのハッシュ: " + employee.getPassword());
        System.out.println("一致するか？→ " + passwordEncoder.matches(request.getPassword(), employee.getPassword()));
        System.out.println("エンコーダーの種類: " + passwordEncoder.getClass().getName());

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            int failedCount = (employee.getFailedCount() != null ? employee.getFailedCount() : 0) + 1;
            employee.setFailedCount(failedCount);

            if (failedCount >= 5) {
                employee.setIsLocked(true);
                employee.setLockedAt(LocalDateTime.now());
            }

            employeeRepository.save(employee);
            throw new RuntimeException("メールアドレスまたはパスワードが正しくありません");
        }

        // ログイン成功 → 失敗カウントをリセット
        employee.setFailedCount(0);
        employeeRepository.save(employee);

        // トークン生成・保存
        String token = jwtTokenProvider.generateToken(employee);
        tokenService.saveToken(employee, token);

        return new LoginResponse(token, employee.getPermission());

        } catch (Exception e) {
            System.out.println("▼▼▼ 例外が発生 ▼▼▼");
            e.printStackTrace(); // ← これが超重要
            System.out.println("▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲▲");
            throw e;
    }

}

    
}
