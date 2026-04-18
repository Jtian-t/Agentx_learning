# JWT 身份校验后端实现技术方案 (Implementation Guide)

## 📌 核心目标
在 `Spring Boot` 框架下，基于 `Spring Security` 和 `jjwt` 实现无状态的身份验证体系，确保前后端分离模式下的安全联调。

---

## 🛠 核心组件清单

### 1. 统一响应封装 (Result Object)
- **路径建议**: `com.jin.agentx_learning.infrastructure.common.Result`
- **核心字段**: `Integer code` (200, 401, 500), `T data`, `String msg`.
- **作用**: 规范前后端交互协议。

### 2. JWT 工具类 (JwtUtils)
- **路径建议**: `com.jin.agentx_learning.infrastructure.security.JwtUtils`
- **核心功能**:
  - `generateToken(String username)`: 生成加密字符串（包含有效期、签发者）。
  - `validateToken(String token)`: 验证 Token 是否合法且未过期。
  - `getUsernameFromToken(String token)`: 解析 Token 还原用户标识。

### 3. 用户详情服务 (UserDetailsService Implementation)
- **路径建议**: `com.jin.agentx_learning.domain.user.service.impl.UserDetailsServiceImpl`
- **逻辑**: 实现 `UserDetailsService` 接口，根据 `email` 从 `users` 表中查询用户信息，并封装为 `UserDetails` 对象。

### 4. JWT 过滤器 (JwtAuthenticationFilter)
- **路径建议**: `com.jin.agentx_learning.infrastructure.security.JwtAuthenticationFilter`
- **逻辑**: 
  - 继承 `OncePerRequestFilter`。
  - 从 `Authorization: Bearer <token>` 中提取 Token。
  - 验证成功后，通过 `SecurityContextHolder.getContext().setAuthentication(...)` 手动设置登录态。

### 5. 安全配置类 (SecurityConfig)
- **路径建议**: `com.jin.agentx_learning.infrastructure.config.SecurityConfig`
- **配置要点**:
  - **CSRF**: 禁用（无状态 API 不需要）。
  - **Session**: 设置为 `SessionCreationPolicy.STATELESS`。
  - **拦截规则**: 放行 `/api/auth/login` 等公开接口，其余接口通过 `.authenticated()` 拦截。
  - **密码加密**: 注入 `BCryptPasswordEncoder`。
  - **跨域 (CORS)**: 配置允许前端域名访问。

---

## 🚀 执行顺序建议

1.  **基础设施**: 先写 `Result` 和 `JwtUtils`。
2.  **用户对接**: 实现 `UserDetailsService` 确保能查到库里的用户。
3.  **安全核心**: 编写 `SecurityConfig` 建立防御骨架。
4.  **动态拦截**: 编写 `JwtAuthenticationFilter` 实现 Token 自动转换。
5.  **业务接入**: 编写 `AuthController` 的登录逻辑。

---

## 💡 联调注意事项 (Tips for Collaboration)

- **HTTP Header**: 前端在登录成功后，必须在后续每个请求的 Header 中添加 `Authorization: Bearer <token>`。
- **错误代码**: 当 Token 过期时，后端应返回 `401 Unauthorized`，前端拦截器将据此跳转回登录页。
- **用户信息**: 登录接口建议直接返回 `UserDTO`（含头像、昵称），提升页面加载的首屏感。
