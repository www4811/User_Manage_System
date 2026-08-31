package com.example.user_manage_system.repository;

import com.example.user_manage_system.model.UserRole;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRoleRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<UserRole> rowMapper = (rs, rowNum) -> {
        UserRole ur = new UserRole();
        ur.setId(rs.getLong("id"));
        ur.setUserId(rs.getLong("user_id"));
        ur.setRoleId(rs.getLong("role_id"));
        return ur;
    };

    // 给用户分配角色
    public void insert(Long userId, Long roleId) {
        jdbcTemplate.update(
                "INSERT INTO sys_user_role (user_id, role_id) VALUES (?, ?)",
                userId, roleId
        );
    }

    // 查某个用户的角色列表
    public List<UserRole> findByUserId(Long userId) {
        return jdbcTemplate.query(
                "SELECT * FROM sys_user_role WHERE user_id = ?",
                rowMapper, userId
        );
    }

    // 删掉用户的所有角色（修改角色时先删再加）
    public void deleteByUserId(Long userId) {
        jdbcTemplate.update(
                "DELETE FROM sys_user_role WHERE user_id = ?", userId
        );
    }
}