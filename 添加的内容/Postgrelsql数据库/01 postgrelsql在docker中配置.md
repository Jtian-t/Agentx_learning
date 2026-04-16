# PostgreSQL 使用指南

## Docker 中配置 PostgreSQL

### 1. 拉取 PostgreSQL 镜像
```bash
docker pull postgres:latest
```
> **注解说明：**
> - 是的，**任意打开一个终端**都可以执行此命令，不需要在特定目录下
> - 镜像会自动存储到 Docker 配置的存储空间中（通常是 Docker Desktop 设置的镜像存储位置）
>
> **Docker 默认存储位置：**
> - **Windows (Docker Desktop):** `C:\Users\你的用户名\AppData\Local\Docker\wsl\`
> - **可在 Docker Desktop 设置中查看和修改：** Settings → Resources → Disk image location
> - AppData 默认是隐藏项目，需要在资源管理器中显示隐藏项目（Ctrl + Shift + 3 等）
>
> wsl文件夹下看不到显示的镜像文件
> **如何查看已下载的镜像：**
> - 镜像存储在 WSL2 虚拟磁盘（`.vhdx`）内部，**不会在文件夹中直接显示为单独文件**
> - 使用命令查看已下载镜像：
>   ```bash
>   docker images
>   ```
> - `CREATED` 列显示的是**镜像在仓库中构建发布的时间**，不是你下载到本地的时间

### 2. 运行 PostgreSQL 容器
```bash
docker run --name postgres-container \
  -e POSTGRES_USER=your_username \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_DB=your_database \
  -p 5432:5432 \
  -v postgres_data:/var/lib/postgresql/data \
  -d postgres:latest
```

```bash 我个人的参数命名
docker run --name postgres-container_AgentX_Learn \
  -e POSTGRES_USER=AgentX_Learn \
  -e POSTGRES_PASSWORD=AgentX_Learn \
  -e POSTGRES_DB=AgentX_Learn \
  -p 5433:5433 \ # 因为之前的Agenx项目已经使用了5432端口,所以这里使用5433端口 这个就算端口冲突了也会创建容器.只是启动会报错
  -v postgres_data_new:/var/lib/postgresql \
  -d postgres:latest
  
  docker run --name postgres-container_AgentX_Learn -e POSTGRES_USER=AgentX_Learn -e POSTGRES_PASSWORD=AgentX_Learn -e POSTGRES_DB=AgentX_Learn -p 5433:5433 -v postgres_data:/var/lib/postgresql -d postgres:latest
```
1. 为什么端口冲突了，容器还会被创建？
   Docker 执行 run 命令是分两步走的：
   第一步：先创建容器（分配名字、配置、卷挂载）
   第二步：尝试启动容器 + 绑定端口
   如果第二步端口绑定失败 → 容器已经创建好了，只是状态是【创建成功，但启动失败】
   所以你会遇到：
   端口报错了
   但名字已经被占用了
   下次再运行就提示 名字重复


参数说明：
- `--name`: 容器名称
- `-e POSTGRES_USER`: 数据库用户名
- `-e POSTGRES_PASSWORD`: 数据库密码
- `-e POSTGRES_DB`: 默认数据库名称
- `-p 5432:5432`: 端口映射（主机端口:容器端口）
- `-v postgres_data:/var/lib/postgresql/data`: 数据卷持久化

> **数据卷持久化解注：**
> - **挂载含义：** 将容器内的 `/var/lib/postgresql/data` 目录（PostgreSQL 数据存储位置）映射到主机上的一个存储位置
> - **为什么需要持久化：** 容器删除后，容器内的数据会丢失，使用数据卷后数据保存在主机上，容器删除重建数据不丢失
> - **本地存储位置：** 
>   - 命名卷 `postgres_data` 由 Docker 管理，默认存储在 Docker 的存储目录中
>   - Windows: `C:\Users\你的用户名\AppData\Local\Docker\wsl\distro\...\mnt\wsl\docker-desktop-data\data\docker\volumes\postgres_data\_data`
>   - 也可以使用主机路径挂载如：`-v D:/docker/postgres_data:/var/lib/postgresql/data`
>
> **⚠️ Postgres 18+ 数据卷挂载注意事项：**
> - Postgres 18 之后存储目录规则有变化，直接挂载 `/var/lib/postgresql/data` 会出现警告
> - **解决办法：** 改为挂载上层目录 `/var/lib/postgresql`
> - 如果之前用旧方式创建过，需要先删除旧卷再重新创建：
>   ```bash
>   # 1. 停止并删除旧容器
>   docker stop postgres-container
>   docker rm postgres-container
>   # 2. 删除旧数据卷
>   docker volume rm postgres_data
>   # 3. 重新创建容器（挂载上层目录）
>   docker run --name postgres-container -e POSTGRES_USER=your_username -e POSTGRES_PASSWORD=your_password -e POSTGRES_DB=your_database -p 5432:5432 -v postgres_data:/var/lib/postgresql -d postgres:latest
>   ```

### 3. 常用容器命令
```bash
# 查看容器状态
docker ps

# 查看日志
docker logs postgres-container
docker logs postgres-container_AgentX_Learn  //个人版

# 进入容器
docker exec -it postgres-container psql -U your_username -d your_database
docker exec -it postgres-container_AgentX_Learn psql -U AgentX_Learn -d AgentX_Learn
  
# 停止容器
docker stop postgres-container
docker stop postgres-container_AgentX_Learn  //个人版

# 启动容器
docker start postgres-container
docker start postgres-container_AgentX_Learn  //个人版
```

### 4. 使用 Docker Compose（推荐）

#### docker-compose.yml 文件作用
- **编排工具：** 用一个文件管理多个容器的配置、启动、停止
- **替代 docker run：** 把复杂的 `docker run` 参数写成配置文件，更易维护
- **一键启动：** 一条命令启动所有相关服务

#### 在哪里创建
- 在你的**项目根目录**下创建
- 例如：`D:\claude_program\Agentx\Agentx_learning\docker-compose.yml`
- 文件名必须是 `docker-compose.yml`（或 `compose.yaml`）

#### 创建方式
1. 在项目根目录新建一个文本文件
2. 命名为 `docker-compose.yml`
3. 把下面的配置内容复制进去
4. 根据需要修改用户名、密码等配置

#### 配置文件详细说明：

```yaml
version: '3.8'  # Docker Compose 文件格式版本

services:       # 定义要运行的服务（容器）
  postgres:     # 服务名称，可以自定义
    image: postgres:latest                    # 使用的镜像
    container_name: postgres-container        # 容器名称
    environment:                               # 环境变量
      POSTGRES_USER: your_username
      POSTGRES_PASSWORD: your_password
      POSTGRES_DB: your_database
    ports:                                     # 端口映射
      - "5432:5432"
    volumes:                                   # 数据卷挂载
      - postgres_data:/var/lib/postgresql     # 数据持久化（Postgres 18+ 用这个）
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql  # 初始化脚本
    networks:                                  # 网络配置
      - app-network
    restart: unless-stopped                    # 自动重启策略：除非手动停止，否则总是重启

volumes:       # 定义数据卷（在 services 中引用）
  postgres_data:  # 会自动创建名为 postgres_data 的命名卷

networks:      # 定义网络
  app-network:
    driver: bridge  # 桥接网络，容器之间可以通过服务名互相访问
```

#### 常用命令：

```bash
# ⭐ 核心命令：根据 docker-compose.yml 启动容器（后台运行）
# 注意：需要在 docker-compose.yml 所在的目录下执行
docker-compose up -d

# 停止服务
docker-compose down

# 停止服务并删除数据卷（谨慎使用！）
docker-compose down -v

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs

# 查看特定服务日志
docker-compose logs postgres
```

---

## IDEA 连接 PostgreSQL 数据源

### 连接步骤：

1. **打开 Database 工具窗口**
   - 菜单：View → Tool Windows → Database
   - 或者点击右侧边栏的 Database 图标

2. **添加数据源**
   - 点击 `+` 号 → Data Source → PostgreSQL

3. **填写连接信息**（以你的配置为例）：
   - **Host:** `localhost`
   - **Port:** `5433`（你使用的端口）
   - **User:** `AgentX_Learn`
   - **Password:** `AgentX_Learn`
   - **Database:** `AgentX_Learn`
   - **URL:** `jdbc:postgresql://localhost:5433/AgentX_Learn`

4. **测试连接**
   - 点击 "Test Connection" 按钮
   - 显示 "Succeeded" 表示连接成功

5. **点击 OK 保存**

### 注意事项：
- 确保 PostgreSQL 容器正在运行：`docker ps` 查看容器状态
- 检查端口是否正确映射（你使用的是 5433）
- 如果连接失败，先检查容器日志：`docker logs postgres-container_AgentX_Learn`
