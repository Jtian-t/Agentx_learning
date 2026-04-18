# AgentX 知识图谱 - 01 架构拓扑
**参考项目**: Jtian-t/AgentX / lucky-aeon/AgentX

## 🏗 核心架构：Harness-Oriented Architecture (HOA)
项目不依赖单一模型，而是构建了一个“结构化开发团队”。

### 1. 核心模块组成
- **Orchestrator (编排层)**: 负责任务拆解与角色分配。
- **Agent Team (角色层)**: 包含 20+ 专业角色（Architect, PM, DevOps, Security, Tester 等）。
- **Skills Library (技能层)**: 69+ 种标准生产技能，如 `api-design`, `rag-pipelines`, `security-audit`。
- **Workspace Runtime (运行层)**: 管理 `.agentx/` 目录下的计划、进度、评审记录与学习心得。

### 2. 核心配置文件
- **`AGENTS.md`**: 顶层导航，定义路由规则。
- **`Skills.md`**: 技能索引，指导模型如何调用工具。
- **`WORKFLOW.md`**: 定义各角色间的交付协议（Handoffs）。

## 📦 技术选型 (Tech Stack)
- **后端**: Java 17+, Spring Boot, MyBatis Plus (数据持久化).
- **前端**: React/TS (管理后台), VS Code Extension (开发插件).
- **AI 协议**: MCP (Model Context Protocol) 核心驱动。
- **数据库**: PostgreSQL (主存), Redis (缓存), Vector DB (RAG).
