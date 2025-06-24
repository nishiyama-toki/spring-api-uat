package com.example.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // 🔑 Base64でエンコードされたSECRET_KEYを復号してKeyとして使用
    private Key getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(keyBytes, 0, keyBytes.length, "HmacSHA256");
    }

    // トークンからemail（=subject）を抽出
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // -------------------------------
    // トークンから userId を抽出（←追加！）
    // -------------------------------
    public Integer extractUserId(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        String token = authHeader.substring(7);
        Claims claims = extractAllClaims(token);
        return claims.get("id", Integer.class);  // ← トークンに id を埋め込んでる前提
    }

    // トークンの有効性を検証
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // 🔧 修正：Base64デコードしたキーを使用
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // トークン生成処理（id を含める）
    public String generateToken(UserDetails userDetails, Integer userId) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // email など
                .claim("id", userId) // ← ここで id を埋め込む！
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10時間有効
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // 🔧 修正：同じキーで署名
                .compact();
    }
}
