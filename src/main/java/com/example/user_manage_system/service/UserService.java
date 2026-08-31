package com.example.user_manage_system.service;

import com.example.user_manage_system.dto.*;

public interface UserService {
    String login(LoginRequest loginRequest);

    void register(RegisterRequest registerRequest);

    void createUser(UserCreateRequest userCreateRequest);

    void updateUser(Long id, UserUpdateRequest userUpdateRequest);

    void deleteUser(Long id);
     void updatePassword(Long id, String newPassword, String oldPassword);
    ApiResponse<PageResult<UserResponse>> listUsers(int page, int size, String keyword);

    UserResponse getUserById(Long id);
}
