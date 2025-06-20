// mail送信処理
package com.example.api.service;

import com.example.api.entity.Employee;
import com.example.api.entity.PasswordResetToken;
import com.example.api.repository.EmployeeRepository;
import com.example.api.repository.PasswordResetTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResetMailService {

    private final EmployeeRepository employeeRepo;
    private final PasswordResetTokenRepository tokenRepo;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    @Value("${app.frontend.url}")          // 例: http://localhost:3000
    private String frontendUrl;

    private static final int EXPIRE_HOURS = 1;

    @Transactional
    public boolean sendResetMail(String email) {

        /* 1. ユーザー存在チェック */
        Optional<Employee> opt = employeeRepo.findByEmail(email);
        if (opt.isEmpty()) return false;

        Employee employee = opt.get();

        /* 2. トークン生成 & 保存 */
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmployee(employee);  //email は持たせない
        resetToken.setEmail(employee.getEmail());
        resetToken.setExpiresAt(now.plusHours(EXPIRE_HOURS));
        resetToken.setUsed(false);
        resetToken.setCreatedAt(now);
        tokenRepo.save(resetToken);

        /* 3. URL 生成 */
        String link = UriComponentsBuilder
                .fromHttpUrl(frontendUrl)
                .path("/reset_password")
                .queryParam("token", token)
                .toUriString();

        /* 4. メール作成 & 送信 */
        String subject = "【評価システム】パスワード再設定のご案内";
        String body = """
                %s 様

                パスワードの再設定をご希望とのことで、本メールをお送りしております。
                下記のリンクをクリックして、新しいパスワードを設定してください。

                【パスワード再設定リンク】
                %s

                ※このリンクは %d 時間のみ有効です。
                ※このメールに心当たりがない場合は破棄してください。
                """.formatted(employee.getName(), link, EXPIRE_HOURS);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(employee.getEmail());
        msg.setSubject(subject);
        msg.setText(body);
        mailSender.send(msg);

        return true;
    }
}
