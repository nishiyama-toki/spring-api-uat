package com.example.api.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    // 本番用のコードはコメントアウトして保存
    /*
    private final String secretKey = "your-secret-key"; // TODO: application.properties に移動推奨

    public Integer extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing or invalid");
        }

        String token = authHeader.substring(7); // "Bearer " を除去

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.get("userId", Integer.class); // トークンに "userId" を含めておくこと
    }
    */

    // テスト用：常に evaluator_id = 1 を返す
    public Integer extractUserId(HttpServletRequest request) {
        return 1;
    }
}
