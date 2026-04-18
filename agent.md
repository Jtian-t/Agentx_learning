# AgentX 复现项目 - 协作契约 (agent.md)

## 🎯 项目目标
从零复现 `lucky-aeon/AgentX`（智能 Agent 构建平台），重点对标其 **MCP 协议集成**、**高可用网关**和**流式交互体验**。

## 👥 协作分工
- **用户 (You)**: 后端主开发。负责业务逻辑实现、数据库设计、API 编写。
- **Agent (Me)**: 前端主开发 + 架构顾问。负责高交互感界面实现、SSE 交互逻辑、前端状态管理，并在用户请求时协助排查后端 Bug。

## 🚀 核心工作流 (Core Workflow) - 强制执行
1. **参考优先 (Reference-First)**: 任何代码编写或架构建议，必须首先检索 `reference/AgentX` 目录下的原厂实现。
2. **像素级还原**: 除非用户明确要求改进，否则包名（`org.xhy`）、类名、方法签名必须与原厂一致。
3. **数字标识 (Sequential Identity)**: 所有新创建的文档、SQL、重要记录必须带有两位数字前缀（如 `04_xxx`），以体现任务先后顺序。

## 📦 Git 演进方案 (Numerical Git Protocol)
为了在 GitHub 提交历史中清晰展现进度，所有提交必须遵循：
- **格式**: `[Step XX] <Type>: <Description>`
- **示例**: `[Step 04] feat: implement UserEntity and Result class`
- **标签**: 每完成一个关键里程碑（如完成第一阶段），必须打上对应的 Git Tag（如 `v01-auth-complete`）。

## 📌 当前状态
- **阶段**: 第一阶段（身份基石）.
- **已完成**: 
  - 01-03: 规划书、JWT 指南、进度表.
  - 04-06: 架构图谱、执行逻辑、数据模型.
  - 07: 已克隆原厂源码至 `reference/AgentX` 并完成后端全量索引.
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
