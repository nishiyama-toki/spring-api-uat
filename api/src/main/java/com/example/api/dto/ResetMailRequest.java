// フロントエンドから受け取る email 情報を受け取る
package com.example.api.dto;

public class ResetMailRequest {

    private String email;

    // デフォルトコンストラクタ（シリアライズ／デシリアライズに必須）
    public ResetMailRequest() {}

    // 引数付きコンストラクタ（テストや直接生成用）
    public ResetMailRequest(String email) {
        this.email = email;
    }

    // Getter
    public String getEmail() {
        return email;
    }

    // Setter
    public void setEmail(String email) {
        this.email = email;
    }
}
