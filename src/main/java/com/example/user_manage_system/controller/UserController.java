package com.example.user_manage_system.controller;

import com.example.user_manage_system.dto.*;
import com.example.user_manage_system.exception.BusinessException;
import com.example.user_manage_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<PageResult<UserResponse>> getUser(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String keyword, @RequestAttribute String role) {
        if (!"admin".equals(role)) {
            throw new BusinessException("无权分页查询搜索");
        }
        return userService.listUsers(page, size, keyword);

    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable Long id, @RequestAttribute Long userId, @RequestAttribute String role) {
        if (!"admin".equals(role) && !id.equals(userId)) {
            throw new BusinessException("无权查看他人信息");
        }
        return ApiResponse.success(userService.getUserById(id));
    }

    @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody UserCreateRequest userCreateRequest, @RequestAttribute String role) {
        if (!"admin".equals(role)) {
            throw new BusinessException("无权限执行此操作");
        }
        userService.createUser(userCreateRequest);
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest, @PathVariable Long id, @RequestAttribute Long userId, @RequestAttribute String role) {
        if (!"admin".equals(role) && !id.equals(userId)) {
            throw new BusinessException("无权修改他人信息");
        }
        userService.updateUser(id, userUpdateRequest);
        return ApiResponse.success(null);

    }

@PutMapping("/{id}/password")
public ApiResponse<Void> updatePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest, @RequestAttribute Long userId,@PathVariable Long id) {
        if (!id.equals(userId)) {throw new BusinessException("不存在该用户");}
       userService.updatePassword(userId,changePasswordRequest.getNewPassword(),changePasswordRequest.getOldPassword());
            return ApiResponse.success(null);

}
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, @RequestAttribute String role) {
        if (!"admin".equals(role)) {
            throw new BusinessException("无权限执行此操作");
        }
        userService.deleteUser(id);
        return ApiResponse.success(null);
    }
}



