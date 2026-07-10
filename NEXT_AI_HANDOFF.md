# 下一位 AI 项目交接书

## 你现在要做什么

你接手的是一个尚未完成的智慧养老课程项目。请不要只做分析、列计划或生成静态假页面；先检查仓库现状，然后持续完成组长负责的前后端功能，直到能够实际启动、操作、保存和查询数据。

当前首要目标：在 `feature/leader-base-auth` 分支上完成组长负责的管理端和家属端功能，并严格遵循老师提供的 Axure 原型与 PRD 说明。

开始工作后直接执行以下流程：

1. 读取本文件和下方列出的必读文件。
2. 检查 `git status`、当前分支、已有代码和数据库结构。
3. 打开对应 Axure 原型页面，先看页面，再看该页面右侧或下方的 PRD 规则。
4. 按优先级连续实现前端、后端、数据库和联调，不要停在方案阶段。
5. 每完成一组功能就运行构建并实际操作页面。
6. 只提交本次相关文件，推送到当前功能分支，并更新指向 `dev` 的 PR。

## 用户的硬性要求

- 必须按原型实现，不能擅自把界面改成常见后台模板。
- 原型中的页面结构是：顶部一级功能模块、左侧当前模块的二三级菜单、右侧业务内容区。
- 原型页面上的文字说明和交互规则同样属于需求，不能只看截图。
- 后台公共布局已经做好，除非原型证据证明有问题，否则不要重写。
- 页面宽度要适配常用桌面尺寸，不能让整个页面出现难以操作的横向滚动。
- 前端不能只展示假数据；列表、新增、编辑、状态修改、分页和查询必须尽量连接真实后端与 MySQL。
- 遵循老师现有 `com.soft` 分层和 MyBatis Plus 写法，不要突然引入另一套架构。
- 不要覆盖其他组员负责的业务模块，不要删除用户或其他 Agent 已有修改。

## 仓库与环境

| 项目 | 信息 |
| --- | --- |
| 本地仓库 | `/Users/ziyao_with_u/实训文档/zzyl-team-env` |
| GitHub | `git@github.com:Theo-V-zy/practice_zzyl_cuit_2023_4zid.git` |
| 开发主干 | `dev` |
| 组长分支 | `feature/leader-base-auth` |
| 当前 Draft PR | `https://github.com/Theo-V-zy/practice_zzyl_cuit_2023_4zid/pull/1` |
| 后端 | Spring Boot 3、MyBatis Plus、Maven、JDK 17+ |
| 前端 | Vue 3、Vue CLI、Element Plus、Vue Router、Axios |
| 数据库 | MySQL 8，数据库名 `zzyl` |
| 本机默认账号 | `root` |
| 本机默认密码 | `we005218`，只能作为本地开发默认值，不能再添加新的明文密钥 |
| 后端地址 | `http://localhost:8080` |
| 前端地址 | `http://localhost:5173`，端口占用时以终端实际输出为准 |
| 后台测试账号 | `20260023 / 222222` |

启动命令：

```bash
mysql -uroot -pwe005218 < database/zzyl.sql

cd backend
MYSQL_PASSWORD=we005218 mvn spring-boot:run

cd frontend
npm install
npm run serve
```

## 原型来源

总入口：

```text
https://rp-java.itheima.net/zhyl/#id=ps9caw&p=%E6%95%B4%E4%BD%93%E4%BB%8B%E7%BB%8D&g=1
```

Axure 单页可以用页面中文名直接打开，例如：

```text
https://rp-java.itheima.net/zhyl/工作台.html
https://rp-java.itheima.net/zhyl/预约登记.html
```

不要凭聊天记录或记忆复刻。每做一个页面，都要重新打开对应原型，采集页面结构、字段、按钮、状态、弹窗和 PRD 规则。

## 必读文件

开始编码前按顺序阅读：

1. `README.md`
2. `TEAM_README.md`
3. `docs/组长负责模块功能拆分.md`
4. `docs/数据库核心设计-v1.md`
5. `docs/前端统一布局规范.md`
6. `docs/小组协作规范.md`
7. `database/zzyl.sql`
8. `frontend/src/config/adminNavigation.js`
9. `frontend/src/layouts/AdminLayout.vue`
10. `frontend/src/router/index.js`

视觉复刻依据和检查记录位于：

```text
docs/design-evidence/
design-qa.md
```

## 当前已经完成的内容

### 工程环境

- 已整理老师风格的前后端初始环境。
- 已配置后端数据库环境变量、CORS、MyBatis Plus 和文件上传基础能力。
- 已配置前端请求地址、开发端口和 Element Plus。
- 前后端曾分别通过 `mvn -q -DskipTests package` 和 `npm run build`。

### 数据库

- `database/zzyl.sql` 是当前正式初始化脚本，共 21 张核心表。
- 保留老师已有的 `t_user`、`t_menu`、`t_nursimg_item`、`t_nursing_plain`、`t_plain_item`。
- 业务关系主要使用逻辑外键和索引，只有老师原有护理计划关系保留物理外键。
- 不允许各模块私自创建重复表；确实不够用时先更新统一设计文档和正式 SQL。

### 后端

目前真正已有的后端功能有限：

- 登录：`/login`
- 动态菜单：`/sysMenus`
- 当前用户：`/loadInfo`、`/showInfo`
- 修改个人资料：`/updateUser`
- 修改密码：`/updatePwd`
- 文件上传：`/upload`
- 护理项目和护理计划的部分接口

其余 21 张表对应的实体、Mapper、Service、Controller 和 DTO 大部分尚未实现。

### 前端

- `AdminLayout.vue` 已按原型改成顶部 11 个一级模块和左侧当前模块子菜单。
- `adminNavigation.js` 保存完整原型菜单层级。
- 路由已经为各模块准备占位入口。
- 老师已有登录、个人信息、修改密码、护理项目、护理计划页面。
- 大量业务路由当前仍使用 `ModulePlaceholder.vue`，这不算功能完成。

### 当前已知技术债

- 前端菜单目前带有原型静态配置作为兜底；`database/zzyl.sql` 中的 `t_menu` 初始化数据还没有完全调整成同一套三级菜单结构。后续实现权限菜单时要统一两者。
- 当前登录依赖 Session，但前端还没有完整路由守卫和统一 Axios 响应拦截。
- 当前退出登录主要清理前端状态，后端还应补正式 `/logout`。
- 家属端布局、路由和登录体系尚未建立。
- 密码目前是课程代码的明文方式，不要在不兼容老师数据的情况下贸然全量改造；可以在文档中记录后续安全改进。

## 功能分工边界

| 负责人 | 管理端 | 家属端 | 分支 |
| --- | --- | --- | --- |
| 组长 | 登录、工作台、个人中心、消息中心、权限配置 | 登录、我的 | `feature/leader-base-auth` |
| 组员 A | 来访管理、入退管理、在住管理 | 首页 | `feature/member-a-resident-flow` |
| 组员 B | 服务管理、订单管理、财务管理 | 服务 | `feature/member-b-service-order` |
| 组员 C | 客户管理、协同工作、智能监测 | 家人 | `feature/member-c-customer-monitor` |

你在组长分支工作时，不要代替组员实现他们的完整业务，只维护必要的公共设施、占位路由和跨模块接口约定。

## 组长功能实施顺序

### P0：公共基础与登录闭环

- 封装 Axios 请求实例、错误提示和 Session 失效处理。
- 增加后台路由守卫、登录状态恢复和正式退出接口。
- 修复管理端登录页，使视觉和交互符合原型。
- 建立 `views/admin`、`views/family`、`api` 等清晰目录，但不要无意义重构老师已有代码。
- 建立家属端路由和 `FamilyLayout.vue`，按原型实现移动端底部导航。

### P1：权限配置

- 用户信息：分页、查询、新增、编辑、启禁用、重置密码、角色分配。
- 角色管理：菜单权限和数据权限。
- 菜单管理：树形展示及增删改。
- 部门管理：树形部门及增删改。
- 职位管理：分页、查询及增删改。
- 同步修正 `t_menu` 初始化数据、后端菜单树和前端导航配置。

主要表：`t_user`、`t_role`、`t_menu`、`t_department`、`t_post`。

### P2：个人中心和消息中心

- 完善个人资料、头像上传、表单校验和修改密码。
- 消息分页、查询、详情、已读状态、批量已读和删除。

主要表：`t_user`、`t_message`。

### P3：工作台

严格按 `工作台.html` 和页面 PRD 实现：

- 我的信息。
- 老人、床位、员工、收入、服务单数据概览。
- 收益、入退、服务统计切换。
- 快捷方式。
- 待办事项。
- 预约总览。
- 老人等级、年龄和养老服务能力统计。

统计数据可以先由真实 SQL 聚合和少量课程测试数据支撑，不能整页写死成无法联调的常量。

### P4：家属端登录和“我的”

- 家属登录和独立登录状态。
- `我的`首页。
- 我的合同、我的预约、我的订单、订单详情、我的账单。
- 数据必须按当前家属和绑定老人过滤。

主要表：`t_family_user`、`t_family_elder`、`t_contract`、`t_visit`、`t_order`、`t_bill`。

## 后端实现约定

- 包名继续使用 `com.soft`。
- 每个模块按 `pojo/entity -> dto -> mapper -> service -> service.impl -> controller` 分层。
- 分页优先使用 MyBatis Plus `Page`。
- Controller 统一返回项目已有响应结构；如果现有结构不足，先建立一个小型通用响应类，再逐步统一，不能同一个项目出现多种随意格式。
- 查询参数和写入参数不要直接全部使用实体类，复杂页面使用 DTO。
- 新增和修改要做后端校验，不能只依赖前端。
- 删除业务数据前检查引用关系；核心业务优先使用状态禁用或逻辑删除。
- 所有金额使用 `BigDecimal`，时间字段使用合适的 Java 时间类型。

## 前端实现约定

- 不要重写 `AdminLayout.vue`、顶部模块和左侧公共导航。
- 每个业务页面只负责查询区、表格、分页、表单、抽屉或弹窗。
- 优先使用 Element Plus 组件和现有图标库。
- 页面根节点宽度不得超过内容区，表格用 `width: 100%`。
- 路由必须有唯一 `path`、`name` 和 `meta.title`。
- 页面路径必须和 `adminNavigation.js` 及后端菜单路径一致。
- 不要用五颜六色的装饰，不要发明与原型不同的卡片和大标题。
- 原型已有按钮、字段、状态和弹窗必须实现；原型没有的功能不要擅自添加到首屏。

## 每个功能的完成标准

一个功能只有同时满足以下条件才算完成：

- 页面结构、文字和交互符合对应原型及 PRD。
- 前端不是占位页，表单和按钮可操作。
- 后端有真实接口，能够读写 MySQL。
- 分页、查询、空数据、失败提示和表单校验正常。
- 刷新页面后数据仍然存在。
- 不影响其他模块和公共布局。
- `npm run build` 通过。
- `mvn -q -DskipTests package` 通过。
- 浏览器实际操作主流程成功，控制台没有新增错误。
- 修改数据库时同步更新 `database/zzyl.sql` 和设计文档。

## Git 工作规则

开始前：

```bash
git status -sb
git branch --show-current
git pull
```

当前应在：

```text
feature/leader-base-auth
```

提交时只添加本次相关文件，不要使用不加检查的全量提交。禁止提交：

```text
node_modules/
frontend/dist/
backend/target/
.idea/
.env
```

提交示例：

```bash
git add 具体文件
git commit -m "完成权限用户管理"
git push
```

PR 方向始终是：

```text
feature/leader-base-auth -> dev
```

## 你第一次接手时的回复和行动

不要再次向用户询问仓库路径、技术栈、原型地址或分支。先用一句话说明你已经读完交接书，然后立即检查仓库并开始 P0。

推荐开场：

```text
我已经读完交接书，当前目标是在 feature/leader-base-auth 上按 Axure 原型完成组长模块。我会先核对工作区和现有登录链路，然后直接实现请求封装、路由守卫、正式退出和后台登录闭环，完成后运行前后端构建并给你可检查的页面。
```

