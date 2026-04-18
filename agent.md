# AgentX 复现项目 - 协作契约 (agent.md)

## 🎯 项目目标
从零复现 `lucky-aeon/AgentX`（智能 Agent 构建平台），重点对标其 **MCP 协议集成**、**高可用网关**和**流式交互体验**。

## 👥 协作分工
- **用户 (You)**: 后端主开发。负责业务逻辑实现、数据库设计、API 编写。
- **Agent (Me)**: 前端主开发 + 架构顾问。负责高交互感界面实现、SSE 交互逻辑、前端状态管理，并在用户请求时协助排查后端 Bug。

## 🛠 技术栈
- **后端**: Java 17, Spring Boot, MyBatis Plus, Spring Security, JWT, PostgreSQL.
- **前端**: React + TypeScript + Vanilla CSS (追求极致的交互与现代视觉感).
- **通讯**: RESTful API + SSE (Server-Sent Events) 流式推送.

## 📌 当前状态
- **阶段**: 第一阶段（身份基石）.
- **已完成**: 
  - `pom.xml` 依赖对齐（Security, JWT, MyBatis Plus）.
  - 数据库 `users` 表与 `project_progress` 表设计.
  - 项目全局规划书 v1.1 已同步 GitHub.
- **待执行**:
  - 后端：实现 `Result<T>`、`LoginController` 与 `SecurityConfig`.
  - 前端：搭建 React 工程化基础，实现炫酷的登录/注册界面.

## 交互感要求 (Core UX)
1. **实时感**: 所有的 AI 回复必须支持打字机效果 (SSE).
2. **状态透明**: 所有的后端长耗时操作（如 MCP 工具调用）在前端必须有明确的状态动画（如：Thinking... Calling GitHub...）.
3. **视觉风格**: 现代、简洁、暗色系优先，响应式布局.

## 🚀 自动化流程
- 每完成一个功能点，必须：
  1. 将详细说明写入 PostgreSQL 的 `project_progress` 表。
  2. 提交代码并推送到 GitHub (master 分支)。
