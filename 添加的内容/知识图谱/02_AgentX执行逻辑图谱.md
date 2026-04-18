# AgentX 知识图谱 - 02 执行逻辑
**核心流程**: The Agentic Loop (Reasoning → Action → Observation)

## 🔄 核心执行闭环 (The Loop)
不同于普通的 Chat 模式，AgentX 遵循严格的工程循环：
1. **Plan (计划)**: Orchestrator 拆解任务，生成 Step-by-step 计划。
2. **Execute (执行)**: 指定角色 Agent 调用 Skill Library 中的工具执行任务。
3. **Iterate (迭代)**: 针对执行中的错误进行自我纠正。
4. **Review (评审)**: 由专门的 Reviewer Agent 检查代码质量与合规性。
5. 
5. **Validate (验证)**: 通过自动化测试套件验证功能正确性。

## 🛠 MCP 交互流 (MCP Flow)
- **Trigger**: LLM 识别出需要外部能力（如查询数据库、读取文件）。
- **Request**: 后端解析 MCP 指令并路由到特定的 **MCP Tool Server**。
- **Response**: 工具执行结果返回 LLM，作为下一轮推理的上下文。

## 🧠 记忆与上下文
- **Short-term**: 基于 Redis 的会话窗口。
- **Long-term**: 总结算法（Summarization）与 RAG 检索。
