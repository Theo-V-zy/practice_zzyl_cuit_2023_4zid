# 组员开发使用说明

这份说明给每个组员使用。照着做就能拉代码、切自己的分支、启动环境、提交代码、推送到 GitHub，并让 agent 辅助管理代码。

仓库地址：

```bash
git@github.com:Theo-V-zy/practice_zzyl_cuit_2023_4zid.git
```

如果 SSH 不能用，可以改用 HTTPS：

```bash
https://github.com/Theo-V-zy/practice_zzyl_cuit_2023_4zid.git
```

## 一、先安装环境

每个人电脑上需要有：

- JDK 17 或以上
- Maven 3.8 或以上
- Node.js 18 LTS
- MySQL 8.0
- Git
- IDEA 或 VS Code

检查命令：

```bash
java -version
mvn -v
node -v
npm -v
git --version
mysql --version
```

## 二、第一次拉主干环境

任选一个工作目录，执行：

```bash
git clone git@github.com:Theo-V-zy/practice_zzyl_cuit_2023_4zid.git
cd practice_zzyl_cuit_2023_4zid
git switch dev
git pull origin dev
```

如果 SSH 没配置成功，就用 HTTPS：

```bash
git clone https://github.com/Theo-V-zy/practice_zzyl_cuit_2023_4zid.git
cd practice_zzyl_cuit_2023_4zid
git switch dev
git pull origin dev
```

`dev` 是大家共同联调的开发主干，不要直接在 `dev` 上写代码。

## 三、切到自己的分支

先看自己负责哪个分支：

| 负责人 | 管理端功能 | 家属端功能 | 分支 |
| --- | --- | --- | --- |
| 组长 | 登录、工作台、个人中心、消息中心、权限配置 | 登录、我的 | `feature/leader-base-auth` |
| 组员 A | 来访管理、入退管理、在住管理 | 首页 | `feature/member-a-resident-flow` |
| 组员 B | 服务管理、订单管理、财务管理 | 服务 | `feature/member-b-service-order` |
| 组员 C | 客户管理、协同工作、智能监测 | 家人 | `feature/member-c-customer-monitor` |

第一次切自己的分支，用：

```bash
git fetch origin
git switch --track origin/feature/你的分支名
```

例如组员 A：

```bash
git fetch origin
git switch --track origin/feature/member-a-resident-flow
```

例如组员 B：

```bash
git fetch origin
git switch --track origin/feature/member-b-service-order
```

以后已经有本地分支了，就这样切：

```bash
git switch feature/你的分支名
git pull
```

## 四、第一次启动项目

### 1. 初始化数据库

如果你的 MySQL 密码是 `we005218`：

```bash
mysql -uroot -pwe005218 < database/zzyl.sql
```

如果你的 MySQL 密码不是 `we005218`，把命令里的密码换成自己的：

```bash
mysql -uroot -p你的MySQL密码 < database/zzyl.sql
```

### 2. 启动后端

```bash
cd backend
MYSQL_PASSWORD=你的MySQL密码 mvn spring-boot:run
```

如果你的 MySQL 密码是 `we005218`：

```bash
cd backend
MYSQL_PASSWORD=we005218 mvn spring-boot:run
```

后端默认地址：

```text
http://localhost:8080
```

### 3. 启动前端

另开一个终端，回到项目根目录：

```bash
cd frontend
npm install
npm run serve
```

前端默认地址：

```text
http://localhost:5173/
```

默认登录账号：

```text
账号：20260023
密码：222222
```

## 五、每天写代码前先更新

每次开始写代码前，先确认自己在自己的分支：

```bash
git branch
```

看到当前分支前面有 `*`。如果不是自己的分支，先切回去：

```bash
git switch feature/你的分支名
```

然后拉最新代码：

```bash
git pull
```

如果组长通知大家同步 `dev` 的最新公共代码，可以执行：

```bash
git fetch origin
git merge origin/dev
```

有冲突就先别乱改，找组长一起处理。

## 六、写完代码后上传

先看改了哪些文件：

```bash
git status
```

确认没有把这些文件放进去：

```text
node_modules/
dist/
target/
.idea/
.env
frontend/.env.development
```

添加本次修改：

```bash
git add .
```

提交：

```bash
git commit -m "完成某某功能"
```

推送到自己的远程分支：

```bash
git push
```

如果第一次推送提示没有上游分支，用：

```bash
git push -u origin 当前分支名
```

查看当前分支名：

```bash
git branch --show-current
```

## 七、提交 Pull Request

代码 push 后，打开 GitHub 仓库：

```text
https://github.com/Theo-V-zy/practice_zzyl_cuit_2023_4zid
```

点击 `Compare & pull request`。

PR 方向必须是：

```text
base: dev
compare: 自己的 feature 分支
```

PR 标题建议：

```text
完成来访管理页面和接口
```

PR 描述建议写：

```text
本次完成：
1. 新增来访管理列表页面
2. 新增预约登记弹窗
3. 对接来访查询接口

测试情况：
1. 前端 npm run serve 正常
2. 后端 mvn spring-boot:run 正常
3. 页面新增和查询已测试
```

不要自己把代码直接合并到 `main`。只向 `dev` 提 PR，由组长检查后合并。

## 八、提交规范

提交信息用中文，简单说清楚做了什么：

```bash
git commit -m "完成入住管理列表"
git commit -m "修复护理服务表单校验"
git commit -m "新增家属端首页接口"
git commit -m "调整权限菜单路由"
```

不要写太随意的提交信息，例如：

```text
改了一下
111
测试
更新
```

每次提交尽量只做一类事情：

- 写页面就只提交页面相关代码
- 写接口就只提交接口相关代码
- 改 SQL 就说明改了哪张表
- 不要一次提交一大堆不相关内容

## 九、文件规范

不要提交：

```text
node_modules/
frontend/dist/
backend/target/
.idea/
*.iml
.env
frontend/.env.development
```

可以提交：

```text
backend/src/
backend/pom.xml
frontend/src/
frontend/package.json
frontend/package-lock.json
database/zzyl.sql
docs/
README.md
TEAM_README.md
```

如果新增了数据库表或字段，必须同步修改：

```text
database/zzyl.sql
```

## 十、怎么让 agent 辅助提交代码

可以把下面这段话发给 agent：

```text
请先检查当前 git 状态，确认我在自己的 feature 分支上。
帮我总结本次修改，排除 node_modules、dist、target、.idea、.env 等不该提交的文件。
然后帮我 git add、git commit，并 push 到当前远程分支。
不要合并 main 或 dev，不要删除别人的代码。
```

如果要让 agent 帮你同步最新代码，可以说：

```text
请帮我确认当前分支，然后从 origin 拉取最新代码。
如果有冲突，请先告诉我冲突文件，不要自动乱改。
```

如果要让 agent 帮你提 PR，可以说：

```text
请帮我把当前 feature 分支推送到 GitHub，并提醒我在 GitHub 上向 dev 分支提交 Pull Request。
PR 标题和描述请根据本次修改帮我整理。
```

## 十一、常见问题

### 1. 端口被占用

如果后端 `8080` 被占用，先关闭原来的后端进程，或者改端口：

```bash
BACKEND_PORT=8081 MYSQL_PASSWORD=你的MySQL密码 mvn spring-boot:run
```

前端接口地址也要改为：

```bash
VUE_APP_API_BASE_URL=http://localhost:8081 npm run serve
```

### 2. MySQL 密码不一样

启动后端时指定自己的密码：

```bash
MYSQL_PASSWORD=你的MySQL密码 mvn spring-boot:run
```

### 3. npm install 很慢

可以换 npm 镜像：

```bash
npm config set registry https://registry.npmmirror.com
npm install
```

### 4. 不小心在 dev 上写了代码

先不要提交，执行：

```bash
git status
```

然后找组长或让 agent 帮你把修改转移到自己的 feature 分支。

### 5. 代码写完 push 失败

先拉最新代码：

```bash
git pull
```

如果出现冲突，不要乱删，先找组长处理。

## 十二、最短流程

第一次：

```bash
git clone git@github.com:Theo-V-zy/practice_zzyl_cuit_2023_4zid.git
cd practice_zzyl_cuit_2023_4zid
git switch --track origin/feature/自己的分支名
mysql -uroot -p自己的MySQL密码 < database/zzyl.sql
cd backend
MYSQL_PASSWORD=自己的MySQL密码 mvn spring-boot:run
```

另开终端：

```bash
cd frontend
npm install
npm run serve
```

每天：

```bash
git switch feature/自己的分支名
git pull
```

写完：

```bash
git status
git add .
git commit -m "完成某某功能"
git push
```

最后去 GitHub 提 PR 到 `dev`。
