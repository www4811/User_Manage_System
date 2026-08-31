package com.example.user_manage_system.controller;

import com.example.user_manage_system.dto.ApiResponse;
import com.example.user_manage_system.dto.LoginRequest;
import com.example.user_manage_system.dto.RegisterRequest;
import com.example.user_manage_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ApiResponse<Void> register(@Valid @RequestBody RegisterRequest registerRequest) {
        userService.register(registerRequest);
        return ApiResponse.success(null);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.login(loginRequest);
        return ApiResponse.success(token);
    }
}
