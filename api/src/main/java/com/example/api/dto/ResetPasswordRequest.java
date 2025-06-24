// token と newPasswordをフロントから受け取る
package com.example.api.dto;

public class ResetPasswordRequest {
    // mailに含まれる再設定用token
    private String token;
    // ユーザーが新しく決めたpassword
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
