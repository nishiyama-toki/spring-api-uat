// package com.example.api.service;

// import io.jsonwebtoken.Claims;
// import io.jsonwebtoken.Jwts;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import javax.crypto.SecretKey;
// import io.jsonwebtoken.security.Keys;
// import java.util.Date;
// import java.util.function.Function;

// @Service
// public class JwtService {
//     @Value("${jwt.secret}")
//     private String secretKey;

//     // トークンから特定のClaimを抽出する
//     private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//         final Claims claims = extractAllClaims(token);
//         return claimsResolver.apply(claims);
//     }

//     // トークンから全てのClaimを抽出する
//     private Claims extractAllClaims(String token) {
//         return Jwts.parserBuilder()
//             .setSigningKey(getSignInKey())
//             .build()
//             .parseClaimsJwt(token)
//             .getBody();
//     }

//     // トークンからユーザーID(Subject)を抽出する
//     public String extractUsername(String token) {
//         return extractClaim(token, Claims::getSubject);
//     }

//     // トークンの有効期限をチェックする
//     private boolean isTokenExpired(String token) {
//         return extractExpiration(token).before(new Data());
//     }

//     // トークンから有効期限を抽出する
//     private Date extractExpiration(String token) {
//         return extractClaim(token, Claims::getExpiration);
//     }

//     // トークンが有効か検証する
//     public boolean isTokenValid(String token) {
//         // ここでは有効期限のみチェック。DBでの失効チェックはFilter層で行う。
//         return !isTokenExpired(token);
//     }

//     // 秘密鍵を生成する
//     private SecretKey getSignInKey() {
//         return Keys.hmacShaKeyFor(secretKey.getBytes());
//     }
// }