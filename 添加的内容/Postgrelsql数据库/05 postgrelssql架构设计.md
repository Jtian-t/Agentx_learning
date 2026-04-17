# PostgreSQL 架构设计与核心概念

## 一、PostgreSQL 独特之处

### 1. PostgreSQL 是什么

PostgreSQL 是一个**对象关系型数据库管理系统（ORDBMS），有以下独特特点：

- **开源免费：** 完全开源，社区驱动
- **功能强大：** 支持复杂查询、事务、外键、触发器、视图
- **可扩展性强：** 支持自定义数据类型、函数、操作符
- **ACID 兼容：** 完全支持事务的原子性、一致性、隔离性、持久性
- **多版本并发控制（MVCC）：** 读不阻塞写，写不阻塞读
- **支持 JSON/JSONB：** 原生支持半结构化数据
- **支持空间数据：** PostGIS 扩展让它成为地理信息系统首选

---

## 二、PostgreSQL 的三层架构

当你连接到 PostgreSQL 时，会看到这样的结构：

```
实例（Instance）
  └── 数据库（Database）
        └── Schema（模式）
              └── 数据库对象（表、视图、索引等）
```

### 1. 第一层：实例（Instance）

- **实例** 是 PostgreSQL 服务器运行的进程
- 一个实例可以包含多个数据库
- 你在 Docker 里运行的一个容器就是一个实例

### 2. 第二层：数据库（Database）

- **数据库** 是数据的物理容器
- 一个实例下可以有多个独立的数据库
- **默认数据库：**
  - `postgres` - 默认的管理数据库
  - `template0`、`template1` - 模板数据库

**为什么有个默认的 postgres 数据库？**
- 当你不指定数据库连接时，默认连到 `postgres`
- 用于管理操作（创建其他数据库）
- 不要在这个数据库里存业务数据！

**创建数据库：
```sql
-- 查看所有数据库
\l

-- 创建数据库
CREATE DATABASE mydb;

-- 删除数据库
DROP DATABASE mydb;

-- 连接到指定数据库
\c mydb
```

### 3. 第三层：Schema（模式）

**Schema 是什么？**
- Schema 是数据库内的**命名空间**，用于组织和管理数据库对象
- 类似文件系统里的文件夹
- 一个数据库下可以有多个 Schema

**为什么要用 Schema？**

1. **组织管理：** 把相关的表放在一起
2. **权限控制：** 可以给不同用户不同 Schema 的权限
3. **多租户：** 一个数据库多个应用，用不同 Schema 隔离
4. **避免命名冲突：** 不同 Schema 下可以有同名的表

**例子：**
```
mydb（数据库）
  ├── public（Schema）- 默认
  ├── auth（Schema）- 用户认证相关表
  ├── order（Schema）- 订单相关表
  └── report（Schema）- 报表相关表
```

**Schema 操作：**
```sql
-- 查看当前数据库的所有 Schema
\dn

-- 创建 Schema
CREATE SCHEMA my_schema;

-- 在指定 Schema 下创建表
CREATE TABLE my_schema.users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

-- 查询指定 Schema 的表
SELECT * FROM my_schema.users;

-- 设置搜索路径（默认优先找的 Schema）
SET search_path TO my_schema, public;

-- 删除 Schema
DROP SCHEMA my_schema CASCADE;
```

---

## 三、PostgreSQL 默认的三个 Schema

### 1. public（默认 Schema）

- **作用：** 用户创建的对象默认都在这里
- **特点：**
  - 你不指定 Schema 时，表就创建在这里
  - 所有人都有权限
  - 是默认的搜索路径

```sql
-- 下面两个语句等价，都在 public 下创建
CREATE TABLE users (...);
CREATE TABLE public.users (...);
```

### 2. pg_catalog（系统表 Schema）

- **作用：** 存储系统表和内置函数
- **特点：**
  - PostgreSQL 自己用的，不要手动改
  - 存了所有表、字段、类型的定义
  - 总是在搜索路径最前面

**里面有什么：**
- `pg_tables` - 所有表的信息
- `pg_indexes` - 所有索引的信息
- `pg_user` - 所有用户的信息
- `pg_database` - 所有数据库的信息

```sql
-- 查询所有表
SELECT * FROM pg_tables WHERE schemaname = 'public';
```

### 3. information_schema（信息 Schema）

- **作用：** 提供标准的元数据访问接口
- **特点：**
  - SQL 标准定义的，不是 PostgreSQL 特有的
  - 视图形式提供，更友好
  - 跨数据库可移植

**常用视图：**
- `tables` - 所有表
- `columns` - 所有列
- `views` - 所有视图
- `schemata` - 所有 Schema

```sql
-- 查询当前数据库所有表
SELECT table_name FROM information_schema.tables 
WHERE table_schema = 'public';

-- 查询某个表的所有列
SELECT column_name, data_type 
FROM information_schema.columns 
WHERE table_name = 'users';
```

---

## 四、序列（Sequence）

### 什么是序列？

序列是 PostgreSQL 中用于**生成自增数字**的数据库对象。

### 为什么要用序列？

- 给主键生成唯一 ID
- 比自增列更灵活

### 序列的使用

```sql
-- 创建序列
CREATE SEQUENCE user_id_seq;

-- 使用序列获取下一个值
SELECT nextval('user_id_seq');

-- 获取当前值
SELECT currval('user_id_seq');

-- 设置序列起始值
ALTER SEQUENCE user_id_seq RESTART WITH 100;

-- 删除序列
DROP SEQUENCE user_id_seq;
```

### SERIAL 类型（自动使用序列）

```sql
-- 使用 SERIAL，PostgreSQL 会自动创建序列
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100)
);

-- 上面的语句等价于：
CREATE SEQUENCE users_id_seq;
CREATE TABLE users (
    id INTEGER PRIMARY KEY DEFAULT nextval('users_id_seq'),
    name VARCHAR(100)
);
ALTER SEQUENCE users_id_seq OWNED BY users.id;
```

### 现代方式：IDENTITY（PostgreSQL 10+）

```sql
-- 推荐使用 GENERATED AS IDENTITY
CREATE TABLE users (
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100)
);
```

---

## 五、数据库对象（Database Objects）

### 什么是数据库对象？

数据库对象是数据库里存储的各种东西：

| 对象类型 | 说明 | 例子 |
|---------|------|------|
| TABLE | 表，存数据的地方 | users, orders |
| VIEW | 视图，虚拟表 | v_user_orders |
| INDEX | 索引，加速查询 | idx_users_email |
| SEQUENCE | 序列，生成自增ID | users_id_seq |
| FUNCTION | 函数 | get_user(1) |
| TRIGGER | 触发器 | 插入前自动更新时间 |
| TYPE | 自定义类型 | address_type |
| SCHEMA | 模式，命名空间 | auth, order |

### 常用数据库对象操作

```sql
-- 查看当前 Schema 下的所有对象
\d

-- 查看所有表
\dt

-- 查看所有视图
\dv

-- 查看所有序列
\ds

-- 查看所有索引
\di

-- 查看所有函数
\df

-- 查看表的索引
\d users
```

---

## 六、PostgreSQL 的架构图总结

```
┌─────────────────────────────────────────┐
│   PostgreSQL 实例（Instance）          │
│  (Docker 容器里运行的服务)       │
└──────────────┬──────────────────────┘
               │
    ┌──────────┼──────────┐
    │          │          │
┌───▼───┐ ┌──▼───┐ ┌──▼────┐
│postgres│ │ mydb │ │ other │
│(默认)  │ │(业务) │ │ 数据库  │
└───┬───┘ └───┬───┘ └────────┘
    │          │
    │    ┌─────┼─────┐
    │    │     │     │
┌───▼──┐┌▼───┐┌▼────┐┌▼───────┐
│public││auth││order││report │
│Schema││    ││     ││Schema  │
└───┬──┘└──┬─┘└──┬──┘└───┬───┘
    │       │      │        │
    │   ┌───┴───┐  │        │
    │   │ 表    │  │        │
    │   │ 视图  │  │        │
    │   │ 索引  │  │        │
    │   │ 序列  │  │        │
    │   └───────┘  │        │
    │              │        │
    └──────────────┴────────┘
```

---

## 七、常见问题

### Q1: 为什么要分三层？不能直接建表吗？

**A:** 可以直接建表，默认会在 `public` Schema 下。但分层次的好处是：
- 更好的组织管理
- 权限控制更细
- 避免命名冲突

### Q2: public、pg_catalog、information_schema 的区别？

| Schema | 用途 | 谁用 |
|--------|------|------|
| public | 默认，用户数据 | 我们 |
| pg_catalog | 系统表 | PostgreSQL |
| information_schema | 标准元数据 | 查询用 |

### Q3: 什么时候该创建新数据库还是新 Schema？

- **新数据库：** 完全隔离，不能跨库查询
- **新 Schema：** 同一数据库下，方便关联查询

### Q4: MySQL 和 PostgreSQL 的对比？

| 特点 | MySQL | PostgreSQL |
|-------|-------|-----------|
| Schema | 相当于数据库 | 命名空间 |
| 自增 | AUTO_INCREMENT | SERIAL / IDENTITY |
| 系统表 | information_schema | pg_catalog + information_schema |
