-- User 表测试数据
-- 插入两个用户：1个管理员 + 1个普通用户

-- 管理员用户
INSERT INTO users (id, nickname, email, phone, password, github_id, github_login, avatar_url, login_platform, is_admin, created_at, updated_at, deleted_at)
VALUES (
    'user-001-admin',
    '系统管理员',
    'admin@agentx.ai',
    NULL,
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E',
    NULL,
    NULL,
    'https://api.dicebear.com/7.x/avataaars/svg?seed=admin',
    'normal',
    TRUE,
    NOW(),
    NOW(),
    NULL
);

-- 普通用户
INSERT INTO users (id, nickname, email, phone, password, github_id, github_login, avatar_url, login_platform, is_admin, created_at, updated_at, deleted_at)
VALUES (
    'user-002-normal',
    '测试用户',
    'test@example.com',
    '13800138000',
    '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5E',
    '12345678',
    'testuser',
    'https://api.dicebear.com/7.x/avataaars/svg?seed=testuser',
    'github',
    FALSE,
    NOW(),
    NOW(),
    NULL
);

-- 说明:
-- 管理员账号: admin@agentx.ai / admin123
-- 普通用户账号: test@example.com / 任意密码 (示例密码哈希仅供演示)
-- 密码使用 BCrypt 加密，实际使用时请生成真实的密码哈希
