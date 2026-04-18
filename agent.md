# AgentX 复现项目 - 核心协作契约 (v2.0)

## 🎯 项目愿景 (The Goal)
基于 `lucky-aeon/AgentX` 的最终形态进行 100% 像素级复现。
**核心突破点**: 深入理解并重构 MCP (Model Context Protocol) 网关、模型高可用调度及流式交互引擎。

---

## 👥 协作职责 (Roles)
- **用户 (Lead Backend)**: 负责核心业务实现、数据库建模、API 架构。
- **Agent (Frontend & Architect)**: 
  - **职责**: 负责高交互感前端开发、SSE 流式交互逻辑、CSS 视觉实现。
  - **角色**: 架构咨询师。在用户开发后端时，负责提供原厂代码参考、Review 逻辑并确保对齐。

---

## 📜 三大神圣执行协议 (Sacred Protocols)

### 1. 参考优先协议 (Reference-First Protocol)
- **准则**: 严禁凭空想象代码。
- **动作**: 任何代码编写前，必须强制读取 `reference/AgentX` 下的原厂源码。
- **知识库**: 利用 `graphify-out/graph.json` 进行类关系追踪，确保逻辑链路一致。

### 2. 数字标识协议 (Sequential Identity Protocol)
- **准则**: 确保项目演进路径 100% 可追溯。
- **动作**: 
  - **文件命名**: 所有规划文档、SQL 脚本必须带两位数字前缀（如 `01_xxx.md`, `08_xxx.sql`）。
  - **Git 提交**: 统一格式 `[Step XX] <Type>: <Description>`。
  - **进度同步**: 每次任务完成，必须将详细记录写入 PostgreSQL 的 `project_progress` 表。

### 3. 像素级还原协议 (Pixel-Perfect Restoration)
- **准则**: 包结构与类名必须具备“原厂血统”。
- **规范**: 
  - 统一使用 `org.xhy` 作为基础包名前缀。
  - **原则**: “原厂优先，注解改进”。若原厂设计存在瑕疵，严禁直接改动类结构，必须先还原，再通过代码注解或文档记录优化建议。

---

## 🛠 技术底座 (Tech Stack)
- **后端**: Java 17, Spring Boot 3.2.3, MyBatis Plus 3.5.11, Spring Security + JWT.
- **前端**: React + TypeScript + Vanilla CSS (追求现代暗黑风格与打字机动效).
- **存储**: PostgreSQL (主存), Redis (缓存), RabbitMQ (消息队列).

---

## 📌 演进轨迹 (Timeline & Status)
- **[Step 01-03]**: 规划蓝图、JWT 身份指南、进度表 SQL。
- **[Step 04-06]**: 架构/执行/数据 三大维度的知识图谱构建。
- **[Step 07]**: 原厂源码全量克隆与后端知识索引构建。
- **[Step 08]**: 协作契约 2.0 固化。

## 🚀 当前任务 (Next Action)
- **后端**: 按照 `02_JWT身份校验实现指南` 实现 `org.xhy.infrastructure.common.Result` 与安全拦截链。
- **前端**: 待后端认证逻辑完成后，开启 React 工程化脚手架搭建。

---

## 💡 交互感核心要求 (UX Mandates)
1. **打字机效果 (SSE)**: AI 输出必须具备流畅的流式实时感。
2. **状态透明化**: 所有的 MCP 调用、模型搜索必须有明确的中间态动效（如：`Agent is thinking...`）。
3. **极简主义**: 界面简洁、色彩深邃、反馈即时。
