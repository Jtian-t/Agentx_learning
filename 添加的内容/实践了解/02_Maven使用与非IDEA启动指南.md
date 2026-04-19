# Maven 实战指南：构建、管理与非 IDEA 启动

## 1. Maven 是什么？（核心作用）

Maven 是 Java 项目的**项目管理工具**。你可以把它理解为项目的“管家”，它主要负责三件事：

### 📦 依赖管理 (Dependency Management)
- **以前**: 你需要手动去官网下载 `jar` 包，放进 `lib` 文件夹，还要担心版本冲突。
- **现在**: 你只需要在 `pom.xml` 中写上坐标（GroupId, ArtifactId, Version），Maven 会自动从中央仓库下载，并自动处理“依赖的依赖”。

### 🛠️ 生命周期管理 (Lifecycle)
Maven 定义了一套标准的构建流程，你只需要运行简单的命令即可：
- `mvn clean`: 清理旧的编译文件。
- `mvn compile`: 编译源代码。
- `mvn test`: 运行单元测试。
- `mvn package`: 将项目打包成 `jar` 或 `war` 文件。
- `mvn install`: 将打好的包安装到本地仓库，供其他项目使用。

### 🏗️ 项目对象模型 (POM)
通过 `pom.xml` 文件，统一了项目的目录结构、编译环境和插件配置，实现“一次配置，到处运行”。

---

## 2. 不使用 IDEA，如何启动项目？

即使没有 IDEA，你也可以通过以下三种主流方式启动 Spring Boot 项目。

### 方法 A：直接运行（开发常用）
在项目根目录下（`pom.xml` 所在位置），打开命令行执行：
```bash
mvn spring-boot:run
```
> **原理**: Maven 会利用插件直接拉起主类的 `main` 方法。

### 方法 B：打包后运行（生产环境常用）
这是最标准的运行方式，分为两步：
1. **打包**:
   ```bash
   mvn clean package -DskipTests
   ```
   执行完后，会在 `target` 目录下生成一个 `AgentX_learning-0.0.1-SNAPSHOT.jar`。
2. **启动**:
   ```bash
   java -jar target/AgentX_learning-0.0.1-SNAPSHOT.jar
   ```

### 方法 C：使用 Maven Wrapper (mvnw)
如果你的环境中没有安装 Maven，可以使用项目自带的包装器（如果有）：
```powershell
.\mvnw.cmd spring-boot:run
```

---

## 3. 如何配置全局 Maven (Windows)

由于 Agent 无法直接修改你的系统变量，请按照以下步骤手动操作，或运行我提供的脚本。

### 手动步骤：
1. **下载**: 从 [maven.apache.org](https://maven.apache.org/download.cgi) 下载 Binary zip 并解压（如 `D:\maven`）。
2. **设置 M2_HOME**:
   - 右键“此电脑” -> 属性 -> 高级系统设置 -> 环境变量。
   - 新建系统变量：变量名 `M2_HOME`，变量值 `D:\maven`（你的安装路径）。
3. **添加到 PATH**:
   - 找到系统变量中的 `Path`，点击编辑。
   - 新增一行：`%M2_HOME%\bin`。
4. **验证**: 打开新的命令行输入 `mvn -v`。

---

## 4. 为什么 Agent 之前运行失败？
1. **路径缺失**: 系统找不到 `mvn` 命令，说明环境变量未配置。
2. **语法差异**: Windows PowerShell 不支持 `&&` 连接符，必须原子化执行。
3. **Wrapper 缺失**: 根目录下没有 `mvnw.cmd` 文件，无法借助包装器运行。

> **建议**: 以后遇到启动报错，优先尝试 `mvn clean spring-boot:run`，这能排除大部分因为缓存引起的玄学问题。
