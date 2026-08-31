# 用户管理系统（User Management System）

一个基于 Spring Boot 的用户管理系统后端，实现了用户 CRUD、登录认证、JWT 令牌、基于角色的权限控制、分页搜索等功能。

## 技术栈

| 技术 | 说明 |
|------|------|
| Java 25 | 开发语言 |
| Spring Boot 4.1.0 | 后端框架 |
| Spring JDBC (JdbcTemplate) | 数据库访问 |
| MySQL | 关系型数据库 |
| JWT (jjwt) | 身份认证令牌 |
| BCrypt (jbcrypt) | 密码加密 |
| springdoc-openapi | 接口文档 |
| SLF4J | 日志 |

## 功能特性

- 用户注册、登录
- 基于 JWT 的无状态认证
- 基于角色的权限控制（admin / user）
- 用户增删改查（CRUD）
- 分页查询 + 模糊搜索
- 密码 BCrypt 加密存储
- 修改密码（需验证旧密码）
- 全局异常处理，统一响应格式
- 参数校验（Bean Validation）
- Swagger 接口文档

## 快速开始

### 1. 准备数据库

```sql
CREATE DATABASE user_manage_system DEFAULT CHARACTER SET utf8mb4;

USE user_manage_system;

CREATE TABLE sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL,
    role_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    status TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL
);

INSERT INTO sys_role (role_name, role_code, description) VALUES
('管理员', 'admin', '拥有全部权限'),
('普通用户', 'user', '只能管理自己的信息');
```

### 2. 修改配置

编辑 `src/main/resources/application.properties`，改成你自己的数据库账号密码：

```properties
spring.datasource.username=你的用户名
spring.datasource.password=你的密码
jwt.secret=换成你自己的密钥
```

### 3. 启动项目

```bash
./mvnw spring-boot:run
```

### 4. 访问接口文档

浏览器打开：http://localhost:8080/swagger-ui/index.html

## 项目结构

```
src/main/java/com/example/user_manage_system/
├── common/          # 通用类（状态码枚举）
├── config/          # 配置类（JWT、拦截器、CORS、Swagger）
├── controller/      # 控制层（接收请求）
├── dto/             # 数据传输对象
├── exception/       # 异常处理
├── model/           # 数据模型
├── repository/      # 数据访问层（JDBC）
├── service/         # 业务逻辑层
│   └── impl/        # 业务逻辑实现
└── util/            # 工具类（密码加密）
```

## API 接口

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | /api/auth/register | 用户注册 | 公开 |
| POST | /api/auth/login | 用户登录 | 公开 |
| GET | /api/users | 用户列表（分页+搜索） | admin |
| GET | /api/users/{id} | 用户详情 | admin / 本人 |
| POST | /api/users | 新增用户 | admin |
| PUT | /api/users/{id} | 修改用户 | admin / 本人 |
| PUT | /api/users/{id}/password | 修改密码 | 本人 |
| DELETE | /api/users/{id} | 删除用户 | admin |

## 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

| code | 含义 |
|------|------|
| 200 | 成功 |
| 400 | 业务错误 / 参数错误 |
| 401 | 未登录 / token 无效 |
| 500 | 服务器内部错误 |

## 认证方式

登录成功后获取 JWT token，后续请求在请求头携带：

```
Authorization: Bearer <token>
```
