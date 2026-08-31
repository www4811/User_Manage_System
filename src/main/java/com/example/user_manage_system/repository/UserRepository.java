package com.example.user_manage_system.repository;

import com.example.user_manage_system.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> rowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setStatus(rs.getInt("status"));
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return user;
    };

    // 按用户名查（登录、注册校验用）
    public User findByUsername(String username) {
        List<User> list = jdbcTemplate.query(
            "SELECT * FROM sys_user WHERE username = ?", rowMapper, username
        );
        return list.isEmpty() ? null : list.get(0);
    }
    public User findById(Long id) {
        User user=jdbcTemplate.queryForObject("SELECT * FROM sys_user WHERE id = ?", rowMapper, id);
        return user;
    }
    // 分页 + 模糊搜索
    public List<User> findAll(int page, int size, String keyword) {
        int offset = (page - 1) * size;   // page从1开始，offset从0开始
        return jdbcTemplate.query(
                "SELECT * FROM sys_user WHERE username LIKE ? ORDER BY id ASC LIMIT ?, ?",
                rowMapper, "%" + keyword + "%", offset, size
        );
    }
    public int countFiltered(String keyword) {
        Integer a = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM sys_user WHERE username LIKE ?", Integer.class, "%" + keyword + "%"
        );
        return a != null ? a : 0;
    }
    public Long insert(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO sys_user (username, password, email, phone, status) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.setInt(5, user.getStatus());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }
    // 修改用户（只改传了值的字段）
   public void updatePassword(User user, String newPassword) {
        jdbcTemplate.update("UPDATE sys_user SET password = ? WHERE id=?",newPassword,user.getId());
   }
    public void update(User user) {
        jdbcTemplate.update(
                "UPDATE sys_user SET username = ?, email = ?, phone = ?, status = ? WHERE id = ?",
                user.getUsername(), user.getEmail(), user.getPhone(), user.getStatus(), user.getId()
        );
    }// 删除用户
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM sys_user WHERE id = ?", id);
    }
}
