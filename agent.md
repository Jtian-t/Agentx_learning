# AgentX 复现项目 - 核心协作契约 (v2.1)

## 🎯 项目愿景 (The Goal)
基于 `lucky-aeon/AgentX` 的最终形态进行 100% 像素级复现。

---

## 👥 协作职责 (Roles)
- **用户 (Lead Backend)**: 负责核心业务实现、数据库建模、API 架构。
- **Agent (Frontend & Architect)**: 负责高交互感前端开发、SSE 交互逻辑，并利用图谱提供“上帝视角”的架构咨询。

---

## 📜 四大神圣执行协议 (Sacred Protocols)

### 1. 图谱先行协议 (Graph-First Discovery) - *NEW*
- **准则**: 严禁在未查询图谱的情况下进行大规模“盲目扫描”。
- **动作**: 任何关于原厂类关系、路径定位、架构依赖的查询，必须优先执行 `graphify query` 或 `graphify explain`。图谱是项目的“雷达”。

### 2. 参考优先协议 (Reference-First Protocol)
- **准则**: 严禁凭空想象代码。
- **动作**: 任何代码编写前，必须强制基于图谱定位到 `reference/AgentX` 下的原厂实现。

### 3. 数字标识协议 (Sequential Identity Protocol)
- **准则**: 每个文件夹内的数字前缀独立编号（从 `01_` 开始）。
- **动作**: 所有的“执行步骤”采用 TODO 格式。Git 提交统一格式 `[Step XX]`。

### 4. 像素级还原协议 (Pixel-Perfect Restoration)
- **准则**: 统一使用 `org.xhy` 作为包名前缀，还原原厂“血统”。
- **原则**: “原厂优先，注解改进”。

---

## 🎯 具体设计要求 (Captured Requirements)
1. **[Auth]**: 采用 Spring Security + JWT 实现身份校验。
2. **[Git]**: 提交信息必须带有 `[Step XX]` 标识。
3. **[Doc]**: 文件夹内独立编号，执行步骤采用 TODO 形式。
4. **[Efficiency]**: Agent 必须优先使用知识图谱进行逻辑定位。

---

## 🛠 技术底座 (Tech Stack)
- **后端**: Java 17, Spring Boot 3.2.3, MyBatis Plus 3.5.11, Spring Security + JWT.
- **前端**: React + TypeScript + Vanilla CSS.

---

## 📌 演进轨迹与状态
- **[Step 07-16]**: 源码克隆、图谱构建、拦截器方案修正、LoginController 路径确认。
- **[Step 17]**: 固化“图谱先行协议”。

## 🚀 当前任务 (Next Action)
- **后端**: 实现 `LoginController` 及其依赖的 `LoginAppService`。
- **前端**: 准备开启 React 工程化脚手架。
