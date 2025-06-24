// email と password をフロントから受け取る
package com.example.api.dto;

public class LoginRequest {
    private String email;
    private String password;

    // デフォルトコンストラクタ
    public LoginRequest() {}

    // ゲッター・セッター
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

