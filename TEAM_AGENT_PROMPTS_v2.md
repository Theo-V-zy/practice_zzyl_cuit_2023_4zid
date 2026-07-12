# 组员 Agent 拉项目提示词

把下面这段话发给你的 AI Agent，它就能帮你把项目拉到本地跑起来。

---

```text
请帮我拉取并启动智慧养老管理系统项目。

## 环境要求

确保本机已安装：JDK 17+、Maven 3.8+、Node.js 18 LTS、MySQL 8.0、Git。

本机 MySQL 的 root 密码如果不确定，先帮我查一下。

## 拉取项目

git clone git@github.com:Theo-V-zy/practice_zzyl_cuit_2023_4zid.git
cd practice_zzyl_cuit_2023_4zid
git fetch origin
git checkout feature/leader-base-auth
git pull origin feature/leader-base-auth

如果 SSH 不通，改用 HTTPS：
git clone https://github.com/Theo-V-zy/practice_zzyl_cuit_2023_4zid.git

## 初始化数据库

mysql -uroot -p你的MySQL密码 < database/zzyl.sql

## 启动后端

cd backend
MYSQL_PASSWORD=你的MySQL密码 mvn spring-boot:run

## 启动前端（另开终端）

cd frontend
npm install
npm run serve

默认前端地址：http://localhost:5173
管理员账号：123 / We123456
家属端测试账号：family001 / 123456

## 验证

启动后帮我验证：
1. 浏览器打开 http://localhost:5173 能看到登录页
2. 用 123/We123456 登录成功进入工作台
3. 左侧菜单点击几个模块确认页面不报错

如果任何一步失败，告诉我错误信息，不要自行修改代码。
```
