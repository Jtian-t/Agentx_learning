# JWT 身份校验后端实现技术方案 (v1.2) - 原厂还原版

## 📌 核心目标
在 `Spring Boot` 框架下，基于 `Spring Security` 和 `jjwt` 实现无状态的身份验证体系。**要求：包结构必须与 lucky-aeon/AgentX 原始代码 100% 一致。**

---

## 🛠 核心组件清单 (严格对照原厂路径)

### 1. 统一响应封装 (Result Object)
- **原厂路径**: `org.xhy.infrastructure.common.Result`
- **核心字段**: `Integer code`, `T data`, `String msg`.
- **说明**: 用于前后端数据契约对齐。

### 2. JWT 工具类 (JwtUtils)
- **原厂路径**: `org.xhy.infrastructure.utils.JwtUtils`
- **核心功能**: 
  - `generateToken(String username)`
  - `validateToken(String token)`
  - `getUsernameFromToken(String token)`

### 3. 用户详情服务 (UserDetailsService)
- **原厂路径**: `org.xhy.domain.user.service.impl.UserDetailsServiceImpl`
- **逻辑**: 实现 `UserDetailsService` 接口，通过 `email` 查询数据库。

### 4. JWT 过滤器 (JwtAuthenticationFilter)
- **原厂路径**: `org.xhy.infrastructure.security.JwtAuthenticationFilter`
- **逻辑**: 继承 `OncePerRequestFilter`，拦截请求并验证 Header 中的 Bearer Token。

### 5. 安全配置类 (SecurityConfig)
- **原厂路径**: `org.xhy.infrastructure.config.SecurityConfig`
- **配置**: 禁用 CSRF、Session 改为 Stateless、配置权限放行路径。

---

## 📝 架构设计注解 (Annotations & Improvements)
> **注意**: 复现时请优先使用上述【原厂路径】。以下改进建议仅供后续优化参考。

1. **[包名优化]**: 
   - `JwtUtils` 在原项目中位于 `utils` 包。
   - *改进建议*: 从安全架构角度看，将其移至 `infrastructure.security.utils` 更符合职责单一原则。
2. **[异常响应]**: 
   - 原项目在 `Filter` 层抛出的异常可能无法被 `@RestControllerAdvice` 捕获。
   - *改进建议*: 自定义实现 `AuthenticationEntryPoint`，确保 Token 过期时能返回统一格式的 `Result` JSON。
3. **[权限颗粒度]**: 
   - 原项目可能使用了硬编码的 API 路径。
   - *改进建议*: 使用 `@PreAuthorize` 注解配合权限表实现动态鉴权。

---

## 🚀 执行顺序建议 (Implementation Path)

1.  **基础设施搭建**: 优先创建 `org.xhy.infrastructure.common.Result` 和 `org.xhy.infrastructure.utils.JwtUtils`。
2.  **数据对接**: 实现 `org.xhy.domain.user.service.impl.UserDetailsServiceImpl`，确保 Spring Security 能识别你的 `users` 表。
3.  **防御架构**: 编写 `org.xhy.infrastructure.config.SecurityConfig`。
4.  **动态校验**: 编写 `org.xhy.infrastructure.security.JwtAuthenticationFilter` 挂载到 Security 过滤链中。
5.  **入口实现**: 编写 `org.xhy.web.controller.AuthController` (原厂命名习惯) 处理登录逻辑。

---

## 💡 联调注意事项
- **Header 格式**: `Authorization: Bearer <token>` (注意 Bearer 后有空格)。
- **CORS 跨域**: 必须在 `SecurityConfig` 中显式开启 `http.cors()`。
