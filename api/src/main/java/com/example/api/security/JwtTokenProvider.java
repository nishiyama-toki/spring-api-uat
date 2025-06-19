// JWTの発行処理
package com.example.api.security;

import com.example.api.entity.Employee;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // application.properties の jwt.secret を読み込む
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * JWTトークンを生成する
     * @param employee ユーザー情報
     * @return JWT文字列
     */
    public String generateToken(Employee employee) {
        Instant now = Instant.now();

        return Jwts.builder()
                .setSubject(String.valueOf(employee.getId()))           // トークンの識別子（ユーザーIDなど）
                .claim("role", employee.getPermission())               // 権限（admin等）をクレームに追加
                .setIssuedAt(Date.from(now))                           // 発行日時
                .setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))// 有効期限：1日後
                .signWith(SignatureAlgorithm.HS256, secretKey)        // HMAC SHA256で署名
                .compact();                                            // トークン文字列を完成
    }
}
