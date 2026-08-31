package com.example.user_manage_system.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank(message = "no userName")
    public String userName;
    @NotBlank(message = "blank password")
    public String password;
    public String phoneNumber;
    public String email;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
