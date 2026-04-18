# 用户认证系统 - 开发路线规划

**规划者**: Claude  
**创建时间**: 2026-04-17  
**当前进度**: 已完成 UserEntity 实体和 users 表

---

## 📋 路线总览

本路线以**功能为导向**，注重前后端联调，让每一步都有可测试的成果。

---

## 🎯 第一阶段：基础设施完善

**目标**: 搭建好基础框架，让项目具备开发条件

### 1.1 完善 pom.xml 依赖
- [ ] JWT（jjwt 或 java-jwt）- Token 生成与解析
- [ ] Spring Security / Sa-Token - 权限控制（推荐 Sa-Token，更简单）
- [ ] Validation - 参数校验（hibernate-validator）
- [ ] Lombok - 简化 getter/setter
- [ ] MyBatis-Plus Spring Boot Starter
- [ ] Swagger / Knife4j - API 文档

### 1.2 完善 application.yaml 配置
```yaml
server:
  port: 8080

spring:
  datasource:
    # 已配置好 PostgreSQL
  jpa:
    # 已配置好

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

jwt:
  secret: your-secret-key-at-least-256-bits
  expiration: 86400000  # 24小时
```

### 1.3 统一响应结构
- [ ] 创建 `Result<T>` 类
- [ ] 创建 `ResultCode` 枚举（错误码）

---

## 🔐 第二阶段：用户注册功能

**目标**: 实现用户注册，能在数据库写入用户数据

### 2.1 Repository 层
- [ ] 创建 `UserMapper` 接口，继承 `BaseMapper<UserEntity>`

### 2.2 DTO 定义
- [ ] `RegisterRequest` - 注册请求（邮箱/手机号、密码、昵称）
- [ ] `RegisterResponse` - 注册响应（用户ID）

### 2.3 Service 层
- [ ] 创建 `UserService` 接口
- [ ] 创建 `UserServiceImpl` 实现类
- [ ] 密码加密（BCrypt）
- [ ] 邮箱/手机号唯一性校验
- [ ] 调用 UserEntity.valid() 验证

### 2.4 Controller 层
- [ ] 创建 `AuthController`
- [ ] `POST /api/auth/register` - 注册接口

### 2.5 测试
- [ ] 创建建表 SQL（已完成）
- [ ] 创建测试数据 SQL（已完成）
- [ ] Postman 测试注册接口

---

## ✅ 第三阶段：用户登录 + JWT

**目标**: 实现登录，返回 token，这是前后端联调的关键

### 3.1 JWT 工具类
- [ ] 创建 `JwtUtil` 工具类
  - `generateToken(userId, email)` - 生成 token
  - `parseToken(token)` - 解析 token
  - `validateToken(token)` - 校验 token 是否有效

### 3.2 登录 DTO
- [ ] `LoginRequest` - 登录请求（账号、密码）
- [ ] `LoginResponse` - 登录响应（token、用户信息）

### 3.3 Service 层
- [ ] `login(account, password)` 方法
  - 根据邮箱/手机号查询用户
  - BCrypt 密码校验
  - 生成 JWT token

### 3.4 Controller 层
- [ ] `POST /api/auth/login` - 登录接口

### 3.5 测试
- [ ] Postman 测试登录接口
- [ ] 验证返回的 token

---

## 🔒 第四阶段：权限校验（保护接口）

**目标**: 给需要登录的接口加上权限校验

### 4.1 用户上下文
- [ ] 创建 `UserContext` 类（ThreadLocal 持有当前用户）
- [ ] 创建 `UserContextHolder` 工具类
  - `setCurrentUser(user)`
  - `getCurrentUser()`
  - `clear()`

### 4.2 自定义注解
- [ ] `@RequireLogin` - 需要登录才能访问
- [ ] `@RequireAdmin` - 需要管理员权限

### 4.3 拦截器
- [ ] 创建 `JwtInterceptor`
  - 从 Header 中获取 token
  - 解析 token 获取 userId
  - 查询用户信息
  - 设置到 UserContext
  - 检查注解权限

### 4.4 拦截器配置
- [ ] 创建 `WebMvcConfig`
  - 注册拦截器
  - 配置排除路径（登录、注册等）

### 4.5 测试接口
- [ ] `GET /api/user/info` - 获取当前用户信息（需登录）
- [ ] `PUT /api/user/info` - 更新用户信息（需登录）

---

## 🎨 第五阶段：前后端联调准备

**目标**: 让前端可以方便对接

### 5.1 API 文档
- [ ] 集成 Knife4j（Swagger 增强版）
- [ ] 给 Controller 方法添加 `@ApiOperation` 注解
- [ ] 给 DTO 添加 `@ApiModel` 注解
- [ ] 访问 `http://localhost:8080/doc.html` 查看文档

### 5.2 跨域配置
- [ ] 创建 `CorsConfig`
  - 允许前端域名访问
  - 允许必要的 Header（Authorization）
  - 允许必要的 Method

### 5.3 全局异常处理
- [ ] 创建 `GlobalExceptionHandler`
  - 捕获 `BusinessException`
  - 捕获 `MethodArgumentNotValidException`
  - 统一返回 `Result.error()`

### 5.4 测试完整流程
- [ ] 注册新用户
- [ ] 登录获取 token
- [ ] 使用 token 调用需要登录的接口

---

## 📝 配套文件

### 已创建
- [x] `添加的内容/建表方案/user表.sql` - users 表建表语句
- [x] `添加的内容/建表方案/user表数据.sql` - users 表测试数据

### 待创建
- [ ] `添加的内容/问题记录/` - 各阶段遇到的问题
- [ ] `添加的内容/学习笔记/` - 重要知识点记录

---

## 🎯 下一步行动

从 **第一阶段：基础设施完善** 开始，先把 pom.xml 和基础类建好！

---

*本路线会根据实际进度动态调整*
