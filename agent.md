# AgentX 复现项目 - 核心协作契约 (v2.2)

## 🎯 项目愿景 (The Goal)
基于 `lucky-aeon/AgentX` 的最终形态进行 100% 像素级复现。

---

## 👥 协作职责 (Roles)
- **用户 (Lead Backend)**: 负责核心业务实现、数据库建模、API 架构。
- **Agent (Frontend & Architect)**: 负责高交互感前端开发、SSE 交互逻辑，并利用图谱提供“上帝视角”的架构咨询。

---

## 📜 五大神圣执行协议 (Sacred Protocols)

### 1. 内容保护与补完协议 (Content Preservation) - *NEW*
- **准则**: 严禁删除或覆盖用户在计划文档、笔记中撰写的任何内容。
- **动作**: Agent 的补充建议必须以“增量”方式进行，且必须通过 `> **By Gemini:**` 等明显标识进行区分，确保用户思考痕迹 100% 保留。

### 2. 依赖透明协议 (Dependency Transparency) - *NEW*
- **准则**: 凡事预则立。
- **动作**: 在计划文档中新增任何核心功能类（如 Controller）时，必须强制在同一计划项下穷举并描述其所有“前置依赖类”（DTO、Service、Utils 等），确保护航开发链路无死角。

### 3. 图谱先行协议 (Graph-First Discovery)
- **准则**: 严禁在未查询图谱的情况下进行大规模“盲目扫描”。
- **动作**: 任何关于原厂类关系、路径定位、架构依赖的查询，优先执行 `graphify query`。

### 4. 参考优先协议 (Reference-First Protocol)
- **准则**: 严禁凭空想象代码。
- **动作**: 任何代码编写前，必须强制基于图谱定位到 `reference/AgentX` 下的原厂实现。

### 5. 数字标识与还原协议 (Identity & Restoration)
- **准则**: 每个文件夹前缀独立编号；统一使用 `org.xhy` 包名还原原厂“血统”。

---

## 🎯 具体设计要求 (Captured Requirements)
1. **[Auth]**: 采用 Spring Security + JWT 实现身份校验。
2. **[Git]**: 提交信息必须带有 `[Step XX]` 标识。
3. **[Efficiency]**: 优先使用知识图谱；增量式更新计划文档，标注前置依赖。

---

## 📌 演进轨迹与状态
- **[Step 17-19]**: 固化图谱协议、修正拦截器、添加 Validation 预警。
- **[Step 20]**: 固化“内容保护”与“依赖透明”双重协议。

## 🚀 当前任务 (Next Action)
- **后端**: 按照补完后的计划实现 `LoginRequest` 等 DTO 族。
