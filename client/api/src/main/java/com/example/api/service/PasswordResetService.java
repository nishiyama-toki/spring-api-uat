// password再設定処理
package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.entity.PasswordResetToken;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final EmployeeRepository employeeRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final ResetMailService mailService;

    /* ① 送信要求だけを受け取るメソッド */
    @Transactional
    public boolean requestReset(String email) {
        if (employeeRepo.findByEmail(email).isEmpty()) return false;
        mailService.sendResetMail(email);
        return true;
    }

    /* ② 実際にパスワードを更新するメソッド */
    @Transactional
    public boolean resetPassword(String token, String newPassword) {

        Optional<PasswordResetToken> opt = tokenRepo.findByToken(token);
        if (opt.isEmpty()) return false;

        PasswordResetToken resetToken = opt.get();

        /* Boolean 型なので getter は getUsed() になる */
        if (Boolean.TRUE.equals(resetToken.getUsed()) ||
            resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }

        /* email カラムを廃止 → employee 参照から直接取得 */
        Employee employee = resetToken.getEmployee();

        employee.setPassword(passwordEncoder.encode(newPassword));
        employee.setIsLocked(false);
        employee.setFailedCount(0);
        employee.setLockedAt(null);
        employeeRepo.save(employee);

        resetToken.setUsed(true);
        tokenRepo.save(resetToken);

        return true;
    }
}
