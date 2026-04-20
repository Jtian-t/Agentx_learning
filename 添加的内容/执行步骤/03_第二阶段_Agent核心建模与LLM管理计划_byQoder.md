# 第二阶段实战计划：Agent 核心建模 + LLM 服务商管理

## 🎯 总体目标
完成 AgentX 平台的**核心骨架**——Agent 领域建模与 LLM 服务商/模型管理。这两个模块互为依存：Agent 工作区需要绑定模型配置，而模型来自 LLM 服务商。完成本阶段后，你将拥有"创建Agent → 配置模型 → 添加到工作区"的完整闭环。

---

## 📊 依赖关系全景图

```
第一阶段(已完成)          第二阶段(本次)              第三阶段(未来)
┌─────────────┐      ┌──────────────────────┐     ┌──────────────────┐
│  User/Auth   │─────▶│  2A: LLM Provider   │     │  Conversation    │
│  JWT 拦截器  │      │      + Model         │────▶│  Session/SSE     │
│  全局异常    │      ├──────────────────────┤     │  流式对话         │
│  Result封装  │      │  2B: Agent CRUD      │────▶│                  │
└─────────────┘      │      + Version        │     ├──────────────────┤
                     ├──────────────────────┤     │  Tool/MCP        │
                     │  2C: Agent Workspace │     │  工具市场         │
                     │    (Agent+LLM绑定)    │     ├──────────────────┤
                     └──────────────────────┘     │  RAG 知识库      │
                                                   └──────────────────┘
```

**为什么是 Agent + LLM 而不是先做对话？**
- Agent 是整个平台的**核心实体**，对话、工具、RAG 全部围绕 Agent 展开
- LLM Provider/Model 是 Agent 工作区的前置依赖，必须先有"模型可选"才能给 Agent 配模型
- 先搭好 Agent CRUD，后续每加一个模块（对话/工具/RAG）都是对 Agent 的增强，节奏清晰

---

## 🔧 前置修复：第一阶段遗留问题（约 0.5 天）

在开始第二阶段之前，必须先解决第一阶段的阻塞项：

- [ ] **F1. MyBatis-Plus 扫描配置**（🔴 阻塞项）
  - 在启动类上添加 `@MapperScan("com.jin.agentx_learning.domain.*.repository")`
  - 在 `application.yaml` 中配置 `mybatis-plus.mapper-locations`
  - 验证：项目能正常启动，无 Bean 注入失败

- [ ] **F2. UserDomainService 补全**（🟡 重要）
  - 实现 `getById(String userId)` 方法
  - 实现 `getByIds(List<String> userIds)` 方法
  - 实现 `findByEmail(String email)` 方法（LoginAppService 依赖）
  - 验证：register + login 流程可跑通

- [ ] **F3. 数据库表初始化**（🟡 重要）
  - 确保 `users` 表已创建，字段与 `UserEntity` 对齐
  - 确保 `user_settings` 表已创建（如果需要）
  - 验证：`getCaptcha` → `register` → `login` 全流程联调通过

> **提示**：F1 是硬性阻塞，F2/F3 影响第一阶段的完整验证但不阻塞第二阶段开发。建议优先修 F1，然后边修 F2/F3 边推进第二阶段。

---

## 📦 2A：LLM 服务商与模型管理（约 2-3 天）

### 领域定位
LLM 模块是平台的"AI引擎舱"——管理大模型服务商（如 OpenAI、Anthropic、DeepSeek）及其模型配置。它为 Agent 提供可选择的模型列表。

### 参考源码路径
```
reference/AgentX/AgentX/src/main/java/org/xhy/domain/llm/
├── model/
│   ├── ProviderEntity.java      # 服务商实体 (providers表)
│   ├── ModelEntity.java         # 模型实体 (models表)
│   ├── ProviderAggregate.java   # 服务商聚合根
│   ├── LLMRequest.java          # LLM请求封装
│   ├── HighAvailabilityResult.java  # 高可用结果
│   ├── config/ProviderConfig.java    # 服务商配置(API Key/URL)
│   └── enums/
│       ├── ProviderProtocol.java     # 协议枚举(OPENAI/ANTHROPIC等)
│       └── ModelType.java            # 模型类型枚举(CHAT/EMBEDDING等)
├── repository/
│   ├── ProviderRepository.java
│   └── ModelRepository.java
└── service/
    ├── LLMDomainService.java
    └── HighAvailabilityDomainService.java
```

### 任务清单

#### 2A-1: 领域模型与枚举（基础设施层）
- [ ] **ProviderProtocol 枚举**: `com.jin.agentx_learning.domain.llm.enums.ProviderProtocol`
  - 值: `OPENAI`, `ANTHROPIC`, `OLLAMA`, `CUSTOM`
  - 每种协议对应不同的 API 格式和鉴权方式
  - 参考源码: `org.xhy.infrastructure.llm.protocol.enums.ProviderProtocol`

- [ ] **ModelType 枚举**: `com.jin.agentx_learning.domain.llm.enums.ModelType`
  - 值: `CHAT`, `EMBEDDING`
  - 参考源码: `org.xhy.domain.llm.model.enums.ModelType`

- [ ] **ProviderConfig 值对象**: `com.jin.agentx_learning.domain.llm.model.config.ProviderConfig`
  - 字段: `apiKey`, `baseUrl`, `customHeaders`
  - 参考源码: `org.xhy.domain.llm.model.config.ProviderConfig`

- [ ] **类型转换器补全**:
  - `ProviderProtocolConverter`: `com.jin.agentx_learning.infrastructure.converter.ProviderProtocolConverter`
  - `ProviderConfigConverter`: `com.jin.agentx_learning.infrastructure.converter.ProviderConfigConverter`
  - `ModelTypeConverter`: `com.jin.agentx_learning.infrastructure.converter.ModelTypeConverter`
  - 作用: 让 MyBatis-Plus 能将枚举/复杂对象与 JSON 字段互转

#### 2A-2: 领域实体
- [ ] **ProviderEntity**: `com.jin.agentx_learning.domain.llm.model.ProviderEntity`
  - 表名: `providers`
  - 核心字段: `id`, `userId`, `protocol`, `name`, `description`, `config`(JSON), `isOfficial`, `status`
  - 业务方法: `isActive()` 验证是否启用, `isAvailable(userId)` 验证是否可访问
  - 参考源码: `org.xhy.domain.llm.model.ProviderEntity`

- [ ] **ModelEntity**: `com.jin.agentx_learning.domain.llm.model.ModelEntity`
  - 表名: `models`
  - 核心字段: `id`, `userId`, `providerId`, `modelId`, `name`, `description`, `modelEndpoint`, `isOfficial`, `type`, `status`
  - 业务方法: `isActive()` 验证是否启用, `isChatType()` 判断是否为对话模型
  - 参考源码: `org.xhy.domain.llm.model.ModelEntity`

#### 2A-3: 仓储层
- [ ] **ProviderRepository**: `com.jin.agentx_learning.domain.llm.repository.ProviderRepository`
  - 继承 `MyBatisPlusExtRepository<ProviderEntity>`

- [ ] **ModelRepository**: `com.jin.agentx_learning.domain.llm.repository.ModelRepository`
  - 继承 `MyBatisPlusExtRepository<ModelEntity>`

#### 2A-4: 领域服务
- [ ] **LLMDomainService**: `com.jin.agentx_learning.domain.llm.service.LLMDomainService`
  - `createProvider(ProviderEntity)` — 创建服务商
  - `updateProvider(ProviderEntity)` — 更新服务商配置
  - `getProviders(userId)` — 获取用户可用的服务商列表
  - `createModel(ModelEntity)` — 创建模型
  - `updateModel(ModelEntity)` — 更新模型
  - `getModels(providerId, userId)` — 获取某服务商下的模型列表
  - `getModelsByType(userId, ModelType)` — 按类型筛选模型
  - 参考源码: `org.xhy.domain.llm.service.LLMDomainService`

#### 2A-5: 数据库建表
- [ ] **providers 表**:
  ```sql
  CREATE TABLE providers (
    id VARCHAR(64) PRIMARY KEY,
    user_id VARCHAR(64) NOT NULL,
    protocol VARCHAR(32) NOT NULL,      -- OPENAI/ANTHROPIC/OLLAMA/CUSTOM
    name VARCHAR(128) NOT NULL,
    description TEXT,
    config JSONB,                        -- {apiKey, baseUrl, customHeaders}
    is_official BOOLEAN DEFAULT FALSE,
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
  );
  ```

- [ ] **models 表**:
  ```sql
  CREATE TABLE models (
    id VARCHAR(64) PRIMARY KEY,
    user_id VARCHAR(64) NOT NULL,
    provider_id VARCHAR(64) NOT NULL REFERENCES providers(id),
    model_id VARCHAR(128) NOT NULL,     -- 模型标识如 gpt-4o, claude-3-sonnet
    name VARCHAR(128) NOT NULL,
    description TEXT,
    model_endpoint VARCHAR(256),         -- 部署端点名称
    is_official BOOLEAN DEFAULT FALSE,
    type VARCHAR(32) DEFAULT 'CHAT',     -- CHAT/EMBEDDING
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
  );
  ```

#### 2A-6: 应用服务与 DTO
- [ ] **DTO**: 
  - `CreateProviderRequest`, `UpdateProviderRequest`, `ProviderResponse`
  - `CreateModelRequest`, `UpdateModelRequest`, `ModelResponse`

- [ ] **LLMAppService**: `com.jin.agentx_learning.application.llm.service.LLMAppService`
  - 编排 Provider/Model 的 CRUD 流程
  - DTO ↔ Entity 转换

#### 2A-7: 控制器
- [ ] **ProviderController**: `com.jin.agentx_learning.interfaces.api.portal.llm.ProviderController`
  - 路由前缀: `/api/llms/providers`
  - 端点: GET `/`, GET `/{id}`, POST `/`, PUT `/`, DELETE `/{id}`, PUT `/{id}/status`, GET `/protocols`

- [ ] **ModelController**: `com.jin.agentx_learning.interfaces.api.portal.llm.ModelController`
  - 路由前缀: `/api/llms/models`
  - 端点: GET `/`, GET `/default`, GET `/{id}`, POST `/`, PUT `/`, DELETE `/{id}`, PUT `/{id}/status`, GET `/types`

#### 2A-8: 验证
- [ ] 启动项目，确认无 Bean 注入失败
- [ ] 通过 Postman 测试 Provider CRUD
- [ ] 通过 Postman 测试 Model CRUD
- [ ] 测试模型类型筛选功能

---

## 📦 2B：Agent 核心建模与版本管理（约 3-4 天）

### 领域定位
Agent 是 AgentX 平台的灵魂——它代表一个 AI 助手，包含系统提示词、工具绑定、知识库关联等核心配置。版本管理支持 Agent 的发布与迭代。

### 参考源码路径
```
reference/AgentX/AgentX/src/main/java/org/xhy/domain/agent/
├── model/
│   ├── AgentEntity.java           # Agent实体 (agents表)
│   ├── AgentVersionEntity.java    # Agent版本实体 (agent_versions表)
│   ├── AgentWorkspaceEntity.java  # Agent工作区实体 (agent_workspace表)
│   ├── AgentWidgetEntity.java     # Agent小组件实体 (agent_widgets表)
│   └── LLMModelConfig.java        # 模型配置值对象
├── constant/
│   ├── PublishStatus.java         # 发布状态枚举
│   └── ...其他常量
├── repository/
│   ├── AgentRepository.java
│   ├── AgentVersionRepository.java
│   ├── AgentWorkspaceRepository.java
│   └── AgentWidgetRepository.java
└── service/
    ├── AgentDomainService.java         # Agent核心领域服务
    ├── AgentVersionDomainService.java  # 版本管理领域服务
    ├── AgentWorkspaceDomainService.java # 工作区领域服务
    └── AgentValidator.java             # Agent验证器
```

### 任务清单

#### 2B-1: 共享枚举与值对象
- [ ] **PublishStatus 枚举**: `com.jin.agentx_learning.domain.agent.constant.PublishStatus`
  - 值: `REVIEWING(1)`, `PUBLISHED(2)`, `REJECTED(3)`, `UNPUBLISHED(4)`
  - 参考源码: `org.xhy.domain.agent.constant.PublishStatus`

- [ ] **TokenOverflowStrategyEnum 枚举**: `com.jin.agentx_learning.domain.shared.enums.TokenOverflowStrategyEnum`
  - 值: `NONE`, `SLIDING_WINDOW`, `SUMMARY`
  - 参考源码: `org.xhy.domain.shared.enums.TokenOverflowStrategyEnum`

- [ ] **LLMModelConfig 值对象**: `com.jin.agentx_learning.domain.agent.model.LLMModelConfig`
  - 字段: `modelId`, `temperature`, `topP`, `topK`, `maxTokens`, `strategyType`, `reserveRatio`, `summaryThreshold`
  - 参考源码: `org.xhy.domain.agent.model.LLMModelConfig`

- [ ] **类型转换器补全**:
  - `ListStringConverter`: 已有参考，用于 `toolIds` / `knowledgeBaseIds`
  - `ListConverter`: 用于 `AgentVersionEntity` 的同类字段
  - `MapConverter`: 用于 `toolPresetParams`
  - `LLMModelConfigConverter`: 用于工作区的模型配置字段

#### 2B-2: 领域实体
- [ ] **AgentEntity**: `com.jin.agentx_learning.domain.agent.model.AgentEntity`
  - 表名: `agents`
  - 核心字段: `id`, `name`, `avatar`, `description`, `systemPrompt`, `welcomeMessage`, `toolIds`(JSON数组), `knowledgeBaseIds`(JSON数组), `publishedVersion`, `enabled`, `userId`, `toolPresetParams`(JSON对象), `multiModal`
  - 业务方法: `createNew()`, `updateBasicInfo()`, `enable()`, `disable()`, `publishVersion()`, `delete()`, `isEnable()`
  - 参考源码: `org.xhy.domain.agent.model.AgentEntity`

- [ ] **AgentVersionEntity**: `com.jin.agentx_learning.domain.agent.model.AgentVersionEntity`
  - 表名: `agent_versions`
  - 核心字段: `id`, `agentId`, `name`, `avatar`, `description`, `versionNumber`, `systemPrompt`, `welcomeMessage`, `toolIds`, `knowledgeBaseIds`, `changeLog`, `publishStatus`, `rejectReason`, `reviewTime`, `publishedAt`, `userId`, `toolPresetParams`, `multiModal`
  - 业务方法: `createFromAgent()`, `updatePublishStatus()`, `reject()`
  - 参考源码: `org.xhy.domain.agent.model.AgentVersionEntity`

#### 2B-3: 仓储层
- [ ] **AgentRepository**: `com.jin.agentx_learning.domain.agent.repository.AgentRepository`
- [ ] **AgentVersionRepository**: `com.jin.agentx_learning.domain.agent.repository.AgentVersionRepository`

#### 2B-4: 领域服务
- [ ] **AgentDomainService**: `com.jin.agentx_learning.domain.agent.service.AgentDomainService`
  - `createAgent(AgentEntity)` — 创建 Agent
  - `getAgent(agentId, userId)` — 获取单个 Agent（含权限校验）
  - `getUserAgents(userId, filterEntity)` — 获取用户的 Agent 列表（支持名称模糊搜索）
  - `updateAgent(AgentEntity)` — 更新 Agent 信息
  - `toggleAgentStatus(agentId)` — 切换启用/禁用
  - `deleteAgent(agentId, userId)` — 删除 Agent 及其版本
  - 参考源码: `org.xhy.domain.agent.service.AgentDomainService`

- [ ] **AgentVersionDomainService**:
  - `publishAgentVersion(agentId, versionEntity)` — 发布新版本
  - `getAgentVersions(agentId)` — 获取版本列表
  - `getLatestVersion(agentId)` — 获取最新版本
  - `getPublishedAgentsByName(name)` — 按名称搜索已发布 Agent（市场发现功能前置）
  - 参考源码: `org.xhy.domain.agent.service.AgentVersionDomainService`

#### 2B-5: 数据库建表
- [ ] **agents 表**:
  ```sql
  CREATE TABLE agents (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    avatar VARCHAR(512),
    description TEXT,
    system_prompt TEXT,
    welcome_message TEXT,
    tool_ids JSONB DEFAULT '[]',           -- 工具ID列表
    knowledge_base_ids JSONB DEFAULT '[]', -- 知识库ID列表
    published_version VARCHAR(64),
    enabled BOOLEAN DEFAULT TRUE,
    user_id VARCHAR(64) NOT NULL,
    tool_preset_params JSONB,              -- 预设工具参数
    multi_modal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    deleted_at TIMESTAMP                  -- 软删除
  );
  CREATE INDEX idx_agents_user_id ON agents(user_id);
  ```

- [ ] **agent_versions 表**:
  ```sql
  CREATE TABLE agent_versions (
    id VARCHAR(64) PRIMARY KEY,
    agent_id VARCHAR(64) NOT NULL REFERENCES agents(id),
    name VARCHAR(128) NOT NULL,
    avatar VARCHAR(512),
    description TEXT,
    version_number VARCHAR(32) NOT NULL,  -- 如 1.0.0
    system_prompt TEXT,
    welcome_message TEXT,
    tool_ids JSONB DEFAULT '[]',
    knowledge_base_ids JSONB DEFAULT '[]',
    change_log TEXT,
    publish_status INT DEFAULT 1,         -- 1审核中/2已发布/3拒绝/4已下架
    reject_reason TEXT,
    review_time TIMESTAMP,
    published_at TIMESTAMP,
    user_id VARCHAR(64) NOT NULL,
    tool_preset_params JSONB,
    multi_modal BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW()
  );
  CREATE INDEX idx_agent_versions_agent_id ON agent_versions(agent_id);
  ```

#### 2B-6: 应用服务与 DTO
- [ ] **DTO**:
  - `CreateAgentRequest`, `UpdateAgentRequest`, `AgentResponse`, `AgentDetailResponse`
  - `PublishVersionRequest`, `AgentVersionResponse`

- [ ] **AgentAppService**: `com.jin.agentx_learning.application.agent.service.AgentAppService`
  - 编排 Agent CRUD + 版本发布的完整流程

#### 2B-7: 控制器
- [ ] **AgentController**: `com.jin.agentx_learning.interfaces.api.portal.agent.AgentController`
  - 路由前缀: `/api/agents`
  - 核心端点:
    - GET `/user/{userId}` — 获取用户的 Agent 列表
    - GET `/{id}` — 获取 Agent 详情
    - POST `/` — 创建 Agent
    - PUT `/{id}` — 更新 Agent
    - DELETE `/{id}` — 删除 Agent
    - PUT `/{id}/toggle-status` — 切换启用/禁用
    - GET `/{id}/versions` — 获取版本列表
    - POST `/{id}/publish` — 发布新版本
    - GET `/published` — 获取已发布的 Agent 列表
  - 参考源码: `org.xhy.interfaces.api.portal.agent.AgentController`

#### 2B-8: 验证
- [ ] 创建 Agent → 列表查询 → 详情查询 → 更新 → 禁用/启用 → 删除
- [ ] 发布版本 → 查看版本列表 → 查看已发布 Agent
- [ ] 验证用户权限隔离：用户 A 无法操作用户 B 的 Agent

---

## 📦 2C：Agent 工作区管理（约 1-2 天）

### 领域定位
工作区是 Agent 的"运行时沙箱"——用户将 Agent 添加到工作区，并为它绑定具体的 LLM 模型和参数配置。这是 2A(LLM) 和 2B(Agent) 的交汇点。

### 参考源码路径
```
reference/AgentX/AgentX/src/main/java/org/xhy/domain/agent/
├── model/AgentWorkspaceEntity.java     # 工作区实体
├── repository/AgentWorkspaceRepository.java
└── service/AgentWorkspaceDomainService.java
```

### 任务清单

#### 2C-1: 领域实体与仓储
- [ ] **AgentWorkspaceEntity**: `com.jin.agentx_learning.domain.agent.model.AgentWorkspaceEntity`
  - 表名: `agent_workspace`
  - 核心字段: `id`, `agentId`, `userId`, `llmModelConfig`(JSON)
  - 参考源码: `org.xhy.domain.agent.model.AgentWorkspaceEntity`

- [ ] **AgentWorkspaceRepository**: `com.jin.agentx_learning.domain.agent.repository.AgentWorkspaceRepository`

#### 2C-2: 领域服务
- [ ] **AgentWorkspaceDomainService**: `com.jin.agentx_learning.domain.agent.service.AgentWorkspaceDomainService`
  - `addAgentToWorkspace(agentId, userId, llmModelConfig)` — 添加 Agent 到工作区
  - `getWorkspaceAgents(userId)` — 获取工作区中的 Agent 列表
  - `updateModelConfig(workspaceId, llmModelConfig)` — 更新模型配置
  - `removeFromWorkspace(agentId, userId)` — 从工作区移除 Agent
  - 参考源码: `org.xhy.domain.agent.service.AgentWorkspaceDomainService`

#### 2C-3: 数据库建表
- [ ] **agent_workspace 表**:
  ```sql
  CREATE TABLE agent_workspace (
    id VARCHAR(64) PRIMARY KEY,
    agent_id VARCHAR(64) NOT NULL REFERENCES agents(id),
    user_id VARCHAR(64) NOT NULL,
    llm_model_config JSONB,              -- {modelId, temperature, topP, ...}
    created_at TIMESTAMP DEFAULT NOW(),
    updated_at TIMESTAMP DEFAULT NOW(),
    UNIQUE(agent_id, user_id)            -- 同一用户不能重复添加同一Agent
  );
  CREATE INDEX idx_workspace_user_id ON agent_workspace(user_id);
  ```

#### 2C-4: 应用服务与控制器
- [ ] **AgentWorkspaceAppService**: 编排工作区操作
- [ ] **AgentWorkspaceController**: `com.jin.agentx_learning.interfaces.api.portal.agent.AgentWorkspaceController`
  - 路由前缀: `/api/agents/workspaces`
  - 端点:
    - GET `/` — 获取工作区 Agent 列表
    - POST `/{agentId}` — 添加 Agent 到工作区
    - DELETE `/{agentId}` — 从工作区移除 Agent
    - GET `/{agentId}/model-config` — 获取模型配置
    - PUT `/{agentId}/model/config` — 设置模型配置

#### 2C-5: 验证
- [ ] 创建 Agent → 添加到工作区 → 配置模型 → 查看工作区列表
- [ ] 验证模型配置与 LLM 模块的联动（只能选择已注册的模型）
- [ ] 验证移除 Agent 后工作区状态正确

---

## 📋 总体时间线与里程碑

| 阶段 | 内容 | 预计耗时 | 里程碑 |
|------|------|---------|--------|
| 前置修复 | F1-F3 遗留问题 | 0.5 天 | 项目可启动，登录流程通 |
| 2A | LLM Provider + Model | 2-3 天 | 可管理服务商和模型 |
| 2B | Agent CRUD + Version | 3-4 天 | 可创建/管理 Agent 及版本 |
| 2C | Agent Workspace | 1-2 天 | Agent 可绑定模型配置 |
| **合计** | | **7-10 天** | **Agent 全链路闭环** |

---

## 🔮 第二阶段完成后的项目全景

```
已完成:
  ✅ 用户认证 (JWT + 拦截器)
  ✅ LLM 服务商/模型管理
  ✅ Agent 核心 CRUD
  ✅ Agent 版本发布
  ✅ Agent 工作区 + 模型绑定

下一阶段（第三阶段）候选:
  🔜 对话与流式输出 (Session + Message + SSE)  ← 推荐，核心交互体验
  🔜 工具市场 (Tool Market + MCP)               ← Agent能力扩展
  🔜 RAG 知识库                                  ← Agent知识增强
```

---

## 💡 实施建议

1. **严格遵循 DDD 分层**：domain → application → interfaces，依赖方向不可逆转
2. **先建表再写代码**：每开始一个子模块，先根据 Entity 设计建好 SQL 表
3. **每个子模块完成后立即验证**：不要累积多个模块再测试
4. **参考源码但不照搬**：原项目包名为 `org.xhy`，你的包名为 `com.jin.agentx_learning`，注意替换
5. **复杂字段使用 JSONB**：`toolIds`、`knowledgeBaseIds`、`config`、`toolPresetParams` 等，配合 TypeHandler 实现自动序列化
6. **暂时跳过的功能**：
   - Agent Widget（小组件）—— 依赖对话系统，留到第三阶段
   - 高可用网关 —— 属于运维层，核心功能稳定后再接入
   - 代码自动生成系统提示词 (`/agents/generate-system-prompt`) —— 需要 LLM 调用能力，留到对话阶段
