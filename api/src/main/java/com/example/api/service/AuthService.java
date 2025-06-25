<<<<<<< HEAD
// ロジック部分（認証・token生成・token保存）
=======
>>>>>>> mizukami
package com.example.api.service;

import com.example.api.dto.LoginRequest;
import com.example.api.dto.LoginResponse;
import com.example.api.entity.Employee;
import com.example.api.repository.EmployeeRepository;
import com.example.api.security.JwtTokenProvider;
<<<<<<< HEAD
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
=======
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
>>>>>>> mizukami
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
<<<<<<< HEAD
public class AuthService {

    private static final int LOCK_THRESHOLD = 5;              // ★ 失敗回数閾値
    private static final int LOCK_DURATION_MINUTES = 15;      // ★ ロック時間

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    public AuthService(
        AuthenticationManager authenticationManager,
        EmployeeRepository employeeRepository,
        JwtTokenProvider jwtTokenProvider,
        TokenService tokenService
    ) {
        this.authenticationManager = authenticationManager;
        this.employeeRepository = employeeRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest request) {
        // ---------- ユーザー存在確認 ----------
        Employee employee = employeeRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("認証失敗：ユーザーが存在しません"));

        // ---------- アカウントロック判定 ----------
        if (Boolean.TRUE.equals(employee.getIsLocked())) {
            LocalDateTime lockedAt = employee.getLockedAt();
            if (lockedAt != null &&
                lockedAt.plusMinutes(LOCK_DURATION_MINUTES).isAfter(LocalDateTime.now())) {

                throw new RuntimeException("アカウントがロックされています。"
                                          + LOCK_DURATION_MINUTES + "分後に再試行してください。");
            } else {
                // ロック解除（failedCount=4 にして「次の1回で即ロック」を実現）
                employee.setIsLocked(false);
                employee.setFailedCount(LOCK_THRESHOLD - 1);  // =4
                employee.setLockedAt(null);
                employeeRepository.save(employee);
            }
        }

        try {
            // ---------- 認証 ----------
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            // ---------- 認証成功処理 ----------
            employee.setFailedCount(0);       // ★ 成功時のみ完全リセット
            employee.setIsLocked(false);
            employee.setLockedAt(null);
            employeeRepository.save(employee);

            // トークン生成・保存
            String token = jwtTokenProvider.generateToken(employee);
            tokenService.saveToken(employee, token);

            return new LoginResponse(token, employee.getPermission());

        } catch (Exception ex) {
            // ---------- 認証失敗処理 ----------
            int failed = employee.getFailedCount() == null ? 0 : employee.getFailedCount();
            failed++;
            employee.setFailedCount(failed);

            if (failed >= LOCK_THRESHOLD) {   // ★ 閾値到達で即ロック
=======
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;

    public LoginResponse login(LoginRequest request) {
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
        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            int failedCount = employee.getFailedCount() + 1;
            employee.setFailedCount(failedCount);

            if (failedCount >= 5) {
>>>>>>> mizukami
                employee.setIsLocked(true);
                employee.setLockedAt(LocalDateTime.now());
            }

            employeeRepository.save(employee);
            throw new RuntimeException("メールアドレスまたはパスワードが正しくありません");
        }
<<<<<<< HEAD
=======

        // ログイン成功 → 失敗カウントをリセット
        employee.setFailedCount(0);
        employeeRepository.save(employee);

        // トークン生成・保存
        String token = jwtTokenProvider.generateToken(employee);
        tokenService.saveToken(employee, token);

        return new LoginResponse(token, employee.getPermission());
>>>>>>> mizukami
    }
}
