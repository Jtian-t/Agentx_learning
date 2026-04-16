# IDEA 连接 PostgreSQL 数据源

## 连接步骤：

### 1. 打开 Database 工具窗口
- 菜单：View → Tool Windows → Database
- 或者点击右侧边栏的 Database 图标

### 2. 添加数据源
- 点击 `+` 号 → Data Source → PostgreSQL

### 3. 填写连接信息（以你的配置为例）：
- **Host:** `localhost`
- **Port:** `5433`（你使用的端口）
- **User:** `AgentX_Learn`
- **Password:** `AgentX_Learn`
- **Database:** `AgentX_Learn`
- **URL:** `jdbc:postgresql://localhost:5433/AgentX_Learn`

### 4. 测试连接
- 点击 "Test Connection" 按钮
- 显示 "Succeeded" 表示连接成功

### 5. 点击 OK 保存

---

## 注意事项：
- 确保 PostgreSQL 容器正在运行：`docker ps` 查看容器状态
- 检查端口是否正确映射（你使用的是 5433）
- 如果连接失败，先检查容器日志：`docker logs postgres-container_AgentX_Learn`

 docker run --name postgres-container_AgentX_Learn -e POSTGRES_USER=AgentX_Learn -e POSTGRES_PASSWORD=AgentX_Learn -e POSTGRES_DB=AgentX_Learn -p 5433:5433 -v postgres_data:/var/lib/postgresql -d postgres:latest
