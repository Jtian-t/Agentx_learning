# PostgreSQL 常见命令与 MyBatis-Plus 使用

## 一、PostgreSQL 常见命令（psql）

### 1. 连接数据库

```bash
# 方式一：docker exec 进入容器连接
docker exec -it postgres-container_AgentX_Learn psql -U AgentX_Learn -d AgentX_Learn

# 方式二：如果本地有 psql 客户端
psql -h localhost -p 5433 -U AgentX_Learn -d AgentX_Learn
```

### 2. 常用 psql 元命令

```sql
-- 列出所有数据库
\l

-- 列出所有表
\dt

-- 列出所有用户
\du

-- 查看表结构
\d 表名

-- 查看某个表的详细结构
\d+ 表名

-- 切换数据库
\c 数据库名

-- 查看当前连接信息
\conninfo

-- 显示历史命令
\s

-- 退出 psql
\q
```

### 3. SQL 基础操作

```sql
-- 创建数据库
CREATE DATABASE mydb;

-- 删除数据库
DROP DATABASE IF EXISTS mydb;

-- 创建表
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    age INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 删除表
DROP TABLE IF EXISTS users;

-- 插入数据
INSERT INTO users (name, email, age) 
VALUES ('张三', 'zhangsan@example.com', 25);

-- 批量插入
INSERT INTO users (name, email, age) 
VALUES 
('李四', 'lisi@example.com', 30),
('王五', 'wangwu@example.com', 28);

-- 查询所有数据
SELECT * FROM users;

-- 条件查询
SELECT * FROM users WHERE age > 25;

-- 更新数据
UPDATE users SET age = 26 WHERE name = '张三';

-- 删除数据
DELETE FROM users WHERE name = '王五';

-- 模糊查询
SELECT * FROM users WHERE name LIKE '张%';

-- 排序
SELECT * FROM users ORDER BY age DESC;

-- 分页
SELECT * FROM users ORDER BY id LIMIT 10 OFFSET 0;

-- 统计
SELECT COUNT(*) FROM users;
SELECT AVG(age) FROM users;
SELECT MAX(age), MIN(age) FROM users;

-- 分组
SELECT age, COUNT(*) FROM users GROUP BY age;
```

---

## 二、Java 项目接入（JDBC 示例）

### 1. Maven 依赖

```xml
<dependencies>
    <!-- PostgreSQL 驱动 -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.1</version>
    </dependency>
</dependencies>
```

### 2. JDBC 连接工具类

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresJDBC {
    
    private static final String URL = "jdbc:postgresql://localhost:5433/AgentX_Learn";
    private static final String USER = "AgentX_Learn";
    private static final String PASSWORD = "AgentX_Learn";
    
    public static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    // 查询用户列表
    public static List<User> queryUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setAge(rs.getInt("age"));
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}

// 用户实体类
class User {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    
    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
}
```

---

## 三、MyBatis-Plus 使用

### 1. Maven 依赖

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- MyBatis-Plus -->
    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-boot-starter</artifactId>
        <version>3.5.5</version>
    </dependency>
    
    <!-- PostgreSQL 驱动 -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Lombok（可选） -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
</dependencies>
```

### 2. application.yml 配置

```yaml
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost:5433/AgentX_Learn
    username: AgentX_Learn
    password: AgentX_Learn

mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # 打印 SQL
  global-config:
    db-config:
      id-type: auto  # 主键自增
```

### 3. 实体类

```java
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String email;
    
    private Integer age;
    
    private LocalDateTime createdAt;
}
```

### 4. Mapper 接口

```java
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 继承 BaseMapper，自动拥有 CRUD 方法
}
```

### 5. Service 层

```java
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    // 新增用户
    public boolean addUser(User user) {
        return save(user);
    }
    
    // 根据 ID 查询
    public User getUserById(Long id) {
        return getById(id);
    }
    
    // 查询所有用户
    public List<User> getAllUsers() {
        return list();
    }
    
    // 条件查询：根据姓名模糊查询
    public List<User> getUsersByName(String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getName, name);
        return list(wrapper);
    }
    
    // 条件查询：年龄大于指定值
    public List<User> getUsersByAgeGreaterThan(Integer age) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.gt(User::getAge, age);
        return list(wrapper);
    }
    
    // 更新用户
    public boolean updateUser(User user) {
        return updateById(user);
    }
    
    // 删除用户
    public boolean deleteUser(Long id) {
        return removeById(id);
    }
    
    // 分页查询
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> 
            getUsersByPage(int pageNum, int pageSize) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> page = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        return page(page);
    }
}
```

### 6. Controller 层

```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // 新增用户
    @PostMapping
    public boolean addUser(@RequestBody User user) {
        return userService.addUser(user);
    }
    
    // 根据 ID 查询
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    // 查询所有用户
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
    
    // 根据姓名模糊查询
    @GetMapping("/search")
    public List<User> getUsersByName(@RequestParam String name) {
        return userService.getUsersByName(name);
    }
    
    // 更新用户
    @PutMapping
    public boolean updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }
    
    // 删除用户
    @DeleteMapping("/{id}")
    public boolean deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
```

### 7. MyBatis-Plus 分页插件配置

如果需要使用分页功能，需要添加分页插件配置：

```java
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {
    
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加 PostgreSQL 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.POSTGRE_SQL));
        return interceptor;
    }
}
```

### 8. 复杂查询示例

```java
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    // 复杂条件查询
    public List<User> complexQuery(String name, Integer minAge, Integer maxAge) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        
        // 姓名模糊查询（可选）
        if (name != null && !name.isEmpty()) {
            wrapper.like(User::getName, name);
        }
        
        // 年龄范围查询（可选）
        if (minAge != null) {
            wrapper.ge(User::getAge, minAge);
        }
        if (maxAge != null) {
            wrapper.le(User::getAge, maxAge);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(User::getCreatedAt);
        
        return list(wrapper);
    }
    
    // 批量删除
    public boolean deleteByIds(List<Long> ids) {
        return removeByIds(ids);
    }
    
    // 批量插入
    public boolean saveBatchUsers(List<User> users) {
        return saveBatch(users);
    }
    
    // 统计数量
    public long countUsers() {
        return count();
    }
    
    // 条件统计
    public long countUsersByAge(Integer age) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getAge, age);
        return count(wrapper);
    }
}
```

---

## 四、数据库初始化脚本（init.sql）

```sql
-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    age INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO users (name, email, age) VALUES 
('张三', 'zhangsan@example.com', 25),
('李四', 'lisi@example.com', 30),
('王五', 'wangwu@example.com', 28),
('赵六', 'zhaoliu@example.com', 35);

-- 创建索引（可选）
CREATE INDEX IF NOT EXISTS idx_users_name ON users(name);
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
```

---

## 五、PostgreSQL 数据类型对应 Java 类型

| PostgreSQL 类型 | Java 类型 |
|---------------|----------|
| SERIAL / BIGSERIAL | Long / Integer |
| VARCHAR / TEXT | String |
| INTEGER | Integer / int |
| BIGINT | Long / long |
| BOOLEAN | Boolean / boolean |
| DATE | LocalDate |
| TIMESTAMP | LocalDateTime |
| NUMERIC / DECIMAL | BigDecimal |
| JSONB | String / JSONObject |
