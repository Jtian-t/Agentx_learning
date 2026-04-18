# JWT 身份校验后端实现技术方案 (Implementation Guide)

## 📌 核心目标
在 `Spring Boot` 框架下，基于 `Spring Security` 和 `jjwt` 实现无状态的身份验证体系，确保前后端分离模式下的安全联调。

---

## 🛠 核心组件清单

### 1. 统一响应封装 (Result Object)
- **原项目路径**: `org.xhy.infrastructure.common.Result`
- **核心字段**: `Integer code`, `T data`, `String msg`.

### 2. JWT 工具类 (JwtUtils)
- **原项目路径**: `org.xhy.infrastructure.utils.JwtUtils`
- **核心功能**: `generateToken`, `validateToken`, `getUsernameFromToken`.

### 3. 用户详情服务 (UserDetailsService Implementation)
- **原项目路径**: `org.xhy.domain.user.service.impl.UserDetailsServiceImpl`

### 4. JWT 过滤器 (JwtAuthenticationFilter)
- **原项目路径**: `org.xhy.infrastructure.security.JwtAuthenticationFilter`

### 5. 安全配置类 (SecurityConfig)
- **原项目路径**: `org.xhy.infrastructure.config.SecurityConfig`

---

## 📝 架构设计注解 (Annotations & Improvements)
> **注意**: 以下为针对原项目设计的改进建议，仅作参考，开发时优先遵循上述原厂路径。

1. **[包名优化]**: 原项目将 `JwtUtils` 放在 `utils` 下，从领域驱动设计（DDD）角度看，它更适合放在 `infrastructure.security` 包中，因为它是安全基础设施的一部分。
2. **[异常处理]**: 原项目的身份校验失败可能直接抛出了系统异常。建议在改进版中通过自定义 `AuthenticationEntryPoint` 返回更友好的 `Result` 错误信息，提升前端交互感。
3. **[常量抽取]**: 原项目可能将 `Bearer ` 等字符串硬编码在代码中。建议抽取到 `org.xhy.common.constant.AuthConstant` 中。

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
