# AgentX 复现项目 - 核心协作契约 (v2.6)

## 🎯 项目愿景 (The Goal)
基于 `lucky-aeon/AgentX` 的最终形态进行 100% 像素级复现。

---

## 📜 八大神圣执行协议 (Sacred Protocols)

### 1. 上下文锚定与内容保护协议 (Contextual Anchoring) - *NEW*
- **准则**: 笔记即现场。
- **动作**: 严禁将用户在特定任务（如 2.1 节）下的笔记、链接、感悟移动到其他位置或进行全局总结。
- **要求**: Agent 的补充建议必须以 `> **By Gemini:**` 开头，且必须紧贴在对应的任务条目之后，确保“问题-解决方案-感悟”三位一体，严禁切断上下文。

### 2. 经验沉淀与分包协议 (Experience Anchoring & Partitioning)
- **准则**: 让每一个 Bug 都有价值。
- **动作**: 当单个记录文件过大时，必须开启新文档（如 `02_xxx.md`）进行分层存储。

### 3. 原子化任务分解协议 (Atomic Task Specification) - *RECOVERED*
- **准则**: 计划即代码，描述即逻辑。
- **动作**: 任何新增的类任务，必须严格遵循以下结构描述（严禁简化）：
  - **[类名]**: 完整的 `org.xhy...` 路径。
  - **[职责]**: 一句话描述该类在 DDD 架构中的核心功能。
  - **[前置依赖]**: 显式列出其 import 链中所有属于本项目的新增类。
  - **[关键注解/配置]**: 标注如 `@Data`, `@RestController`, `@NotBlank`, `@Email` 等核心细节。

### 4. 依赖透明协议 (Dependency Transparency)
- **准则**: 在实现 Controller 或复杂 Service 前，必须在计划中“预报”其所有依赖类（DTO/Utils）。

### 5. 图谱先行协议 (Graph-First Discovery)
- **准则**: 优先图谱定位，拒绝盲目扫描。

### 6. 参考优先协议 (Reference-First Protocol)
- **准则**: 像素级还原原厂实现。

### 7. 数字标识与还原协议 (Identity & Restoration)
- **准则**: 独立编号（从 `01_` 开始）；包名还原。

---

## 📌 演进轨迹
- **[Step 27]**: 深度恢复原子任务细节，新增“上下文锚定”规则，严禁挪动用户笔记。
