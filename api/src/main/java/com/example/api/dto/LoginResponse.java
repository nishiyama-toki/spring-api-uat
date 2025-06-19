// ログイン後にJWTトークンとロールをフロントに返す
package com.example.api.dto;

public class LoginResponse {
    private String token;
    private String permission;

    public LoginResponse(String token, String permission) {
        this.token = token;
        this.permission = permission;
    }

    // ゲッター
    public String getToken() {
        return token;
    }

    public String getPermission() {
        return permission;
    }
}

