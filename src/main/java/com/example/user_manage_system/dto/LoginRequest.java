package com.example.user_manage_system.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
      @NotBlank(message = "no username")
    public String username;
    @NotBlank(message = "no password")
      public String password;
public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}
