# AgentX 项目复现全周期规划书 (v1.1)
**制定人**: Gemini CLI 
**目标**: 100% 还原 lucky-aeon/AgentX 核心技术栈，并在此基础上提升交互感与工程化水平。

---

## 📅 第一阶段：身份基石与安全通信 (Identity & Auth)
**当前进度**: 已完成用户表设计（SQL）。
**核心任务**: 建立与原项目完全一致的认证体系。

1. **技术栈对齐 (Tech Alignment)**:
   - **ORM**: 彻底移除 JPA，统一使用 **MyBatis Plus 3.5.x**。 
   - **Security**: 采用 **Spring Security + JWT (jjwt)** 实现无状态认证。
   - **Utils**: 引入 **Lombok**、**Hutool** 简化开发。
2. **认证层实现**:
   - 实现 `JwtUtil` 用于 Token 的生成与解析。
   - 实现 `SecurityConfig` 过滤链，配置 `/api/auth/**` 匿名访问。
   - 实现 `UserDetailServiceImpl` 对接 `users` 表进行登录校验。
3. **交互体验提升**:
   - **统一响应体**: 实现 `Result<T>` 类，确保前后端数据契约一致。
   - **全局异常处理**: 针对权限不足、登录失败提供精确的 JSON 反馈。

---

## 🧠 第二阶段：Agent 建模与灵魂定义 (Agent & Prompt)
**目标**: 实现 Agent 的核心配置管理。

1. **领域模型复刻**:
   - 建立 `agents` 数据表与实体类。
   - 实现符合原项目规范的 Service 层逻辑（CRUD）。
2. **多租户安全**:
   - 利用 Spring Security 的 Context 获取当前登录用户，确保数据操作的安全性。
3. **交互体验提升**:
   - 设计 **Prompt 调试界面** 原型，支持实时占位符替换预览。

---

## 💬 第三阶段：实时对话与流式输出 (Dialogue & SSE)
**目标**: 实现流式交互，复刻 AI 对话的实时打字机感。

1. **LLM 适配层**:
   - 参照原项目接入大模型 API，支持 Openai 协议。
2. **SSE 流式通讯**:
   - 后端使用 `SseEmitter` 异步推送消息碎片。
3. **上下文管理**:
   - 实现基于 Redis 的会话持久化，复刻原项目的记忆窗口逻辑。

---

## 🛠️ 第四阶段：MCP 协议与工具集成 (MCP & Tools)
**目标**: 攻克 AgentX 的技术壁垒——MCP 协议。

1. **MCP Client 实现**:
   - 解析并分发工具调用指令。
2. **Function Calling**:
   - 实现“模型预测工具 -> 后端执行 -> 结果喂回”的闭环逻辑。
3. **交互体验提升**:
   - **思考过程可视化**: 像原项目一样展示 Agent 的思维链或工具调用状态。

---

## 🌍 第五阶段：网关与高可用部署 (Gateway & Ops)
**目标**: 达到工业级交付标准。

1. **API Gateway**:
   - 实现模型负载均衡与 Key 管理。
2. **RAG 增强**:
   - 接入向量数据库，实现本地知识检索。

---

## 📝 近期行动计划 (Action Items)
1. **同步 pom.xml**: 剔除 JPA，引入 Spring Security、JWT、Lombok。
2. **基础框架搭建**: 创建 `common` 包，实现 `Result` 类与 `BusinessException`。
3. **安全配置**: 编写 `SecurityConfig` 与 `JwtFilter`。
4. **登录联调**: 实现 `AuthController` 登录接口并完成前后端调通。
