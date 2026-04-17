-- User 表建表语句
-- 对应实体: com.jin.agentx_learning.domain.user.model.UserEntity

CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    nickname VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(50),
    password VARCHAR(255),
    github_id VARCHAR(255),
    github_login VARCHAR(255),
    avatar_url VARCHAR(500),
    login_platform VARCHAR(50),
    is_admin BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP
);

-- 创建索引以提高查询性能
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_github_id ON users(github_id);
CREATE INDEX idx_users_deleted_at ON users(deleted_at);

-- 字段说明:
-- id: 用户ID，UUID格式
-- nickname: 用户昵称
-- email: 邮箱
-- phone: 手机号
-- password: 密码（加密存储）
-- github_id: GitHub用户ID
-- github_login: GitHub登录名
-- avatar_url: 头像URL
-- login_platform: 登录平台（github/normal等）
-- is_admin: 是否为管理员
-- created_at: 创建时间
-- updated_at: 更新时间
-- deleted_at: 删除时间（软删除）
