package com.example.user_manage_system.repository;

import com.example.user_manage_system.model.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepository {
    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private final RowMapper<Role> rowMapper = (rs, rowNum)->{
    Role role = new Role();
    role.setId(rs.getLong("id"));
    role.setRoleName(rs.getString("role_name"));
    role.setDescription(rs.getString("description"));
    role.setRoleCode(rs.getString("role_code"));
    role.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
    return role;
    };
    public List<Role> findAll() {
   return jdbcTemplate.query("select * from sys_role", rowMapper);
    }
    public Role findById(Long id) {
        return jdbcTemplate.queryForObject("select * from sys_role where id = ?", rowMapper, id);
    }
    public Role findByCode(String roleCode) {
        return jdbcTemplate.queryForObject("select * from sys_role where role_code = ?", rowMapper, roleCode);
    }
}
