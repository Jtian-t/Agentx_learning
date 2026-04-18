-- 项目进度记录表
-- 用于存储每一步的设计思路、完成情况和长文本内容

CREATE TABLE project_progress (
    id SERIAL PRIMARY KEY,
    phase VARCHAR(50) NOT NULL,          -- 所属阶段 (如: 第一阶段, 第二阶段)
    step_name VARCHAR(255) NOT NULL,     -- 步骤名称 (如: 数据库设计, 登录功能实现)
    design_detail TEXT,                  -- 详细的设计思路 (长文本)
    completed_content TEXT,              -- 实际完成的代码或内容概要 (长文本)
    status VARCHAR(20) DEFAULT 'DONE',   -- 状态 (DONE, DOING, TODO)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 添加注释
COMMENT ON COLUMN project_progress.design_detail IS '详细的设计思路，支持长文本';
COMMENT ON COLUMN project_progress.completed_content IS '实际完成的内容或代码摘要';

-- 插入一条当前进度的记录作为初始化
INSERT INTO project_progress (phase, step_name, design_detail, completed_content) 
VALUES (
    '第一阶段', 
    '身份基石与环境搭建', 
    '统一使用 PostgreSQL + MyBatis Plus，并确立 Spring Security + JWT 的认证方案。', 
    '已完成 User 表设计，制定了 v1.1 复现规划文档。'
);
