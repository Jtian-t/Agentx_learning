# AgentX 复现项目 - 核心协作契约 (v2.4)

## 🎯 项目愿景 (The Goal)
基于 `lucky-aeon/AgentX` 的最终形态进行 100% 像素级复现。

---

## 📜 七大神圣执行协议 (Sacred Protocols)

### 1. 原子化任务分解协议 (Atomic Task Specification) - *NEW*
- **准则**: 计划即代码，描述即逻辑。
- **动作**: 任何新增的类任务，必须严格遵循以下结构描述：
  - `[类名]`: 完整的 `org.xhy...` 路径。
  - `[职责]`: 一句话描述其核心功能。
  - `[前置依赖]`: 显式列出其 import 链中所有属于本项目的新增类。
  - `[关键注解/配置]`: 标注如 `@Data`, `@RestController`, `@NotBlank` 等核心工程细节。

### 2. 经验沉淀协议 (Experience Anchoring)
- **准则**: 记录 Bug 与面试谈资，同步更新 `问题记录` 文件夹。

### 3. 内容保护协议 (Content Preservation)
- **准则**: 严禁删除用户笔记，采用 `> **By Gemini:**` 进行增量补充。

### 4. 依赖透明协议 (Dependency Transparency)
- **准则**: 在 Controller 或复杂 Service 出现前，必须先在计划中“预报”其所有依赖。

### 5. 图谱先行协议 (Graph-First Discovery)
- **准则**: 优先图谱定位，拒绝盲目扫描，提高路径准确率。

### 6. 参考优先协议 (Reference-First Protocol)
- **准则**: 像素级还原，严禁凭空想象。

### 7. 数字标识与还原协议 (Identity & Restoration)
- **准则**: 独立编号；包名还原。

---

## 🛠 协作指令集 (Instruction Sets)
- **"分析架构"**: Agent 必须调用 `graphify` 提供全局视图。
- **"实现某功能"**: Agent 必须先检查并补完对应的执行计划 MD，获得用户确认后再编码。
- **"总结问题"**: Agent 必须按照面试标准更新 `01_开发问题与解决方案记录.md`。

---

## 📌 演进轨迹
- **[Step 25]**: 升级“原子化任务分解协议”，确立高质量计划标准。
