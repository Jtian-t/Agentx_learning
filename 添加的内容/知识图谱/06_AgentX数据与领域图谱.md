# AgentX 知识图谱 - 03 数据与领域
**核心实体关系 (ER Mapping)**

## 👤 用户域 (User Domain)
- **UserEntity**: id, email, password, nickname, isAdmin.
- **LoginPlatform**: GitHub, Normal, Google.
- **关联**: 一个 User 可以拥有多个 Agent。

## 🤖 Agent 域 (Agent Domain)
- **AgentEntity**: name, prompt_template, avatar, model_config.
- **SkillAssociation**: Agent 关联的工具/技能列表。
- **ContextWindow**: 针对不同模型的上下文裁剪配置。

## 💬 对话域 (Conversation Domain)
- **Conversation**: session_id, user_id, agent_id, status.
- **Message**: content, role (user/assistant/tool/system), tokens_used.
- **Metadata**: 存储工具调用的原始请求与响应。

## 📂 存储分布
- **Structured (PostgreSQL)**: 用户信息、Agent 配置、历史消息、任务状态。
- **Unstructured (Vector DB)**: 知识库文档向量。
- **Ephemeral (Redis)**: 热点会话缓存、分布式锁。
