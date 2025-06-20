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
                .setSubject(employee.getEmail())                         // トークンのsubjectにemailを入れる
                .claim("id", employee.getId())                           // IDをclaimとして埋め込む
                .claim("role", employee.getPermission())                 // 権限をclaimに追加
                .setIssuedAt(Date.from(now))                             // 発行日時
                .setExpiration(Date.from(now.plus(1, ChronoUnit.DAYS)))  // 有効期限：1日
                .signWith(SignatureAlgorithm.HS256, secretKey)          // HMAC SHA256で署名
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

    /** トークンからユーザーID（Subject）を取得 */
    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject(); // setSubject に入れた値（email）
    }

    /** トークンから権限(role) を取り出す */
    public String extractRole(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(secretKey)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.get("role", String.class);
    }
}
