// JWTの発行処理
// JWTの発行・検証処理
package com.example.api.security;

import com.example.api.entity.Employee;
import io.jsonwebtoken.Claims;
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
                .setSubject(String.valueOf(employee.getId()))                  // ユーザーID
                .claim("role", employee.getPermission())                       // 権限
                .setIssuedAt(Date.from(now))                                   // 発行日時
                .setExpiration(Date.from(now.plus(30, ChronoUnit.MINUTES)))    // 30分で期限切れ
                .signWith(SignatureAlgorithm.HS256, secretKey)                 // 署名
                .compact();
    }

    /* =====  追加メソッド ===== */

    /** トークンの署名・期限を検証 */
    public boolean isValid(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false; // 署名不正・期限切れなど
        }
    }

    /** トークンからユーザーID(Sub) を取り出す */
    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject(); // setSubject に入れた ID
    }

    /** トークンから権限(role) を取り出す（必要なら） */
    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.get("role", String.class);
    }
}

