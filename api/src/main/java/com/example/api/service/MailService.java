package com.example.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendReminder(String email, String userName, String phaseName, String deadline) {
        String subject = "【重要】多面評価の提出リマインド";
        String body = String.format(
            "%sさん\n\n" +
            "現在実施中の多面評価の提出がまだ完了していません。\n\n" +
            "【評価フェーズ】%s\n" +
            "【提出期限】%s\n\n" +
            "下記よりログインの上、評価のご提出をお願いいたします。\n" +
            "https://your-system-url/home\n\n" +
            "※本メールはシステムから自動送信されています。",
            userName, phaseName, deadline
        );

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("your-gmail@gmail.com"); // 差出人を自分のメールに

            mailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

