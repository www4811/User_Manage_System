package com.example.user_manage_system.service.impl;

import com.example.user_manage_system.config.JwtUtil;
import com.example.user_manage_system.dto.*;
import com.example.user_manage_system.exception.BusinessException;
import com.example.user_manage_system.model.UserRole;
import com.example.user_manage_system.repository.RoleRepository;
import com.example.user_manage_system.repository.UserRepository;
import com.example.user_manage_system.repository.UserRoleRepository;
import com.example.user_manage_system.service.UserService;
import com.example.user_manage_system.model.User;
import com.example.user_manage_system.model.Role;
import com.example.user_manage_system.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final JwtUtil jwtUtil;
     private static final Logger log= LoggerFactory.getLogger(UserServiceImpl.class);
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           UserRoleRepository userRoleRepository,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtUtil = jwtUtil;
    }
    @Override
    public String login(LoginRequest loginRequest){
        User user =userRepository.findByUsername(loginRequest.getUsername());

        if(user==null)
        {
            throw new BusinessException("用户名或密码错误");
        }
        if(!PasswordUtil.matches(loginRequest.getPassword(),user.getPassword())){
            throw new BusinessException("用户名或密码错误");
        }
        if(user.getStatus()==0){
            throw new BusinessException("账号已经封禁");
        }
        Role role = getUserRole(user.getId());
        log.info("用户登录成功：{}", user.getUsername());
        return jwtUtil.generateToken(user.getId(), role.getRoleCode());

    }

    @Override
    public void register(RegisterRequest registerRequest) {
       if(userRepository.findByUsername((registerRequest.getUserName()))!=null){
           throw new BusinessException("用户已存在");}
      User use = new User();
        use.setUsername(registerRequest.getUserName());
        use.setPassword(PasswordUtil.encode(registerRequest.getPassword()));
        use.setEmail(registerRequest.getEmail());
        use.setPhone(registerRequest.getPhoneNumber());
        use.setStatus(1);
        Long userId=userRepository.insert(use);
        Role role=roleRepository.findByCode("user");
        userRoleRepository.insert(userId,role.getId());
        log.info("用户注册成功：{}", use.getUsername());
    }

    @Override
    @CacheEvict(value="userList",allEntries=true)
    public void createUser(UserCreateRequest userCreateRequest) {
       if(userRepository.findByUsername(userCreateRequest.getUserName())!=null){
           throw  new BusinessException("用户名已存在");
       }
       User use=new User();
        use.setUsername(userCreateRequest.getUserName());
        use.setPassword(PasswordUtil.encode(userCreateRequest.getPassword()));
        use.setEmail(userCreateRequest.getEmail());
        use.setPhone(userCreateRequest.getPhone());

        use.setStatus(userCreateRequest.getStatus()!=null?userCreateRequest.getStatus():1);
              Long userId=userRepository.insert(use);
              Long roleId=userCreateRequest.getRoleId()!=null?userCreateRequest.getRoleId():2;
              userRoleRepository.insert(userId,roleId);
              log.info("管理员创建用户成功：{}", use.getUsername());
    }

    @Override
    @CacheEvict(value="userList",allEntries=true)
    public void updateUser(Long id, UserUpdateRequest userUpdateRequest) {
   User user=userRepository.findById(id);
       if(user==null){
           throw new BusinessException("用户不存在");
       }
       if(userUpdateRequest.getUserName()!=null){
           user.setUsername(userUpdateRequest.getUserName());
       }
       if(userUpdateRequest.getEmail()!=null){
           user.setEmail(userUpdateRequest.getEmail());
       }
       if(userUpdateRequest.getPhone()!=null){
           user.setPhone(userUpdateRequest.getPhone());
       }
       if(userUpdateRequest.getStatus()!=null){
           user.setStatus(userUpdateRequest.getStatus());
       }
       userRepository.update(user);
        if(userUpdateRequest.getRoleId()!=null){
            userRoleRepository.deleteByUserId(user.getId());
            userRoleRepository.insert(user.getId(), userUpdateRequest.getRoleId());
        }
    }

    @Override
    @CacheEvict(value="userList",allEntries=true)
    public void deleteUser(Long id) {
        userRoleRepository.deleteByUserId(id);
        userRepository.deleteById(id);
        log.info("删除用户成功：id={}", id);
    }

    @Override
    @Cacheable(value="userList",key="#page+':'+#size+':'+#keyword")
    public ApiResponse<PageResult<UserResponse>> listUsers(int page, int size, String keyword) {
        List<User> users = userRepository.findAll(page, size, keyword);
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(toUserResponse(user));
        }
        int total = userRepository.countFiltered(keyword);
        PageResult<UserResponse> pageResult = new PageResult<>(responses, total);
        return ApiResponse.success(pageResult);
    }
@Override
public void updatePassword(Long id, String newPassword ,String oldPassword) {
        User user=userRepository.findById(id);
        if(user==null){
            throw new BusinessException("查无此用户");
        }
        if(!PasswordUtil.matches(oldPassword,user.getPassword())){
            throw new BusinessException("旧密码输入错误");
        }
        userRepository.updatePassword(user,newPassword);
}
    @Override
    public UserResponse getUserById(Long id) {
        User user=userRepository.findById(id);
        if(user==null){
            throw new BusinessException("用户不存在");
        }
        return toUserResponse(user);
    }
    private  Role getUserRole(Long userid){
        List<UserRole> roles=userRoleRepository.findByUserId(userid);
        if(roles.isEmpty()){
            throw new BusinessException("用户未分配角色");
        }return roleRepository.findById(roles.get(0).getRoleId());
    }private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setStatus(user.getStatus());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        // 查角色名填进去
        Role role = getUserRole(user.getId());
        response.setRoleName(role.getRoleName());

        return response;
    }
}
