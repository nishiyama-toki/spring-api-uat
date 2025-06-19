package com.example.api.dto;
import com.fasterxml.jackson.annotation.JsonProperty;


public class BaseUserDto {
    private String name;
    private String email;
    private String password;

    @JsonProperty("isAdmin")
    private boolean isAdmin;
    private String role;

    // --- Getter ---
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isAdmin() { return isAdmin; }
    public String getRole() { return role; }

    // --- Setter ---
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    @JsonProperty("isAdmin")
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public void setRole(String role) { this.role = role; }
}
