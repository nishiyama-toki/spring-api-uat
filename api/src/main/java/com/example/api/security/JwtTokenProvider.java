package com.example.api.security;

import com.example.api.entity.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // application.properties に設定した secret を読み込む
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * JWTトークンを生成する
     * @param employee ログインユーザー情報
     * @return JWT文字列
     */
    public String generateToken(Employee employee) {
        Instant now = Instant.now();
        Instant expiry = now.plus(30, ChronoUnit.MINUTES); // 有効期限30分（スライディング）

        return Jwts.builder()
                .setSubject(String.valueOf(employee.getId()))                // 主体（ユーザーID）
                .claim("email", employee.getEmail())                         // 任意の情報
                .claim("role", employee.getPermission())                     // 権限（admin, employeeなど）
                .setIssuedAt(Date.from(now))                                 // 発行時間
                .setExpiration(Date.from(expiry))                            // 有効期限
                .signWith(key, SignatureAlgorithm.HS256)                          // 署名アルゴリズム
                .compact();
    }

    /** トークンの署名と有効期限を検証する */
    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** トークンからユーザーIDを取得（subに埋め込んだ値） */
    public String extractUserId(String token) {
        return parseClaims(token).getSubject();
    }

    /** トークンからロールを取得（claim） */
    public String extractRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    /** トークンの有効期限（exp）を取得 */
    public Instant extractExpiration(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }

    /** 内部で使う共通のClaims抽出処理 */
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }
}
