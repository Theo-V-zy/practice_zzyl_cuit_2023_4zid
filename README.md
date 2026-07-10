# ZZYL 小组初始环境

这个仓库按老师 day07 的代码风格整理：后端保持 `Spring Boot + MyBatis Plus + com.soft` 分层，前端保持 `Vue 3 + Vue CLI + Element Plus`，数据库使用老师提供的 `zzyl.sql`。

组员开发、拉分支、提交代码、提 PR，请先看：[TEAM_README.md](TEAM_README.md)。前端页面统一遵循：[前端统一布局规范](docs/前端统一布局规范.md)；数据库表结构统一遵循：[数据库核心设计 v1](docs/数据库核心设计-v1.md)。

## 目录

- `backend/`：Spring Boot 后端，默认端口 `8080`
- `frontend/`：Vue 3 前端，默认端口 `5173`
- `database/zzyl.sql`：MySQL 初始化脚本
- `docs/`：小组协作和开发规范

## 本地启动

1. 准备环境：JDK 17 或以上、Maven 3.8+、Node.js 18 LTS、MySQL 8.0。
2. 可选：复制 `.env.example` 为 `.env`，按自己的 MySQL 配置修改。
   如果要调整前端请求地址，复制 `frontend/.env.development.example` 为 `frontend/.env.development`。
3. 初始化数据库：

```bash
mysql -uroot -pwe005218 < database/zzyl.sql
```

也可以用 Docker：

```bash
docker compose up -d mysql
```

4. 启动后端：

```bash
cd backend
MYSQL_PASSWORD=we005218 mvn spring-boot:run
```

5. 启动前端：

```bash
cd frontend
npm install
npm run serve
```

访问：`http://localhost:5173/`

默认账号：`20260023`  
默认密码：`222222`

## 推到 GitHub

```bash
git init
git add .
git commit -m "init zzyl teacher-style environment"
git branch -M main
git remote add origin <你的仓库地址>
git push -u origin main
```

## 注意

- 不提交 `.env`、`frontend/.env.development`、`node_modules/`、`target/`、`.idea/`。
- 老师代码里已有 `Nursimg`、`Plain` 等拼写，初始环境先保留，避免影响已有表名和接口。
- 文件上传依赖阿里云 OSS：需要在本机配置 `OSS_ACCESS_KEY_ID` 和 `OSS_ACCESS_KEY_SECRET`，否则上传功能不可用。
