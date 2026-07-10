# 组员 Agent 开发任务书

这份文件分别交给三位组员及其 Agent。Agent 必须先确认组员身份和分支，只实现对应模块。

## 项目结构说明

```
zzyl-team-env/
├── backend/                  # Spring Boot 后端
├── frontend/                 # Vue 3 管理端
├── frontend-family/          # uni-app 家属端小程序（多组员共用）
├── database/zzyl.sql         # 统一数据库脚本
└── docs/                     # 文档和规范
```

家属端 `frontend-family/` 是 uni-app 项目，编译为微信小程序。组员开发家属端页面时：
- 用 `npm run dev:mp-weixin` 编译
- 用微信开发者工具导入 `dist/dev/mp-weixin`
- 页面路径在 `pages.json` 中登记
- API 封装在 `api/request.js`，后端前缀 `/family/`

## 开工前统一条件

组长先将公共环境 PR 合并到 `dev`。组员收到“可以同步”的通知后执行：

```bash
git fetch origin
git switch dev
git pull origin dev
git switch 自己的feature分支
git merge origin/dev
```

如果出现冲突，Agent 只能列出冲突文件并分析原因，不能直接删除公共布局、数据库脚本或其他组员代码。

所有 Agent 必须阅读：

```text
TEAM_README.md
docs/小组协作规范.md
docs/数据库核心设计-v1.md
docs/前端统一布局规范.md
database/zzyl.sql
frontend/src/config/adminNavigation.js
frontend/src/router/index.js
```

原型入口：

```text
https://rp-java.itheima.net/zhyl/#id=ps9caw&p=%E6%95%B4%E4%BD%93%E4%BB%8B%E7%BB%8D&g=1
```

实现每个页面前，Agent 必须打开对应 Axure 页面，查看原型画面和页面 PRD，不能凭常见后台样式自行设计。

## 所有组员 Agent 的共同规则

- 先检查当前分支，绝对不能在 `main` 或 `dev` 上直接开发。
- 不修改 `frontend/src/layouts/AdminLayout.vue`、Logo、顶栏和侧栏整体结构。
- 不删除或改名其他组员路由。
- 不私自创建重复表，优先使用统一的 21 张核心表。
- 确实缺字段时，先列出字段、用途和影响模块，交给组长确认。
- 前端业务页要替换对应的 `ModulePlaceholder.vue` 路由，不能另造一套布局。
- 后端继续使用 `com.soft` 分层、Spring Boot 和 MyBatis Plus。
- 列表必须支持查询、分页、空状态和失败提示。
- 新增、编辑、状态修改和删除必须连接真实后端与 MySQL。
- 不提交 `node_modules`、`dist`、`target`、`.idea`、`.env`。
- 完成后运行 `npm run build` 和 `mvn -q -DskipTests package`。
- 只把自己的功能分支 PR 提交到 `dev`，不直接合并 `main`。

## 组员 A：来访、入退、在住和家属端首页

### 分支

```text
feature/member-a-resident-flow
```

### 管理端范围

- 来访管理：预约登记、来访登记、到院确认、状态查询。
- 入退管理：入住办理、退住办理。
- 在住管理：合同跟踪、合同详情、床位房型、智能床位、房型设置、请假管理。

### 家属端范围

- 首页。
- 预约入口。
- 养老院介绍。

### 主要数据表

```text
t_visit
t_elder
t_bed
t_contract
t_apply
t_family_user
t_family_elder
```

不得再建 `t_room`、`t_room_type`、`t_visit_record` 等重复表。预约和来访登记统一使用 `t_visit.visit_stage`；入住、退住和请假统一使用 `t_apply.apply_type`。

### 必须完成的主流程

1. 新建预约并在预约列表查到。
2. 确认到院后更新预约状态和到院时间。
3. 创建入住申请，审批完成后生成或更新老人档案、床位占用和合同。
4. 创建退住申请，完成后释放床位并更新老人、合同状态。
5. 请假申请和返回状态可查询。
6. 家属端首页可以读取养老院信息、预约入口和当前绑定老人摘要。

### 直接交给组员 A Agent 的提示词

```text
请阅读仓库根目录 TEAM_AGENT_TASKS.md，我是组员 A。

先检查 git 状态，确认当前分支是 feature/member-a-resident-flow；如果不是，只告诉我正确切换命令，不要在 dev 或 main 上修改代码。

我的范围是管理端来访管理、入退管理、在住管理，以及家属端首页。请严格打开老师 Axure 中对应页面并阅读页面说明，复用现有 AdminLayout.vue，不要修改公共顶栏、侧栏和其他组员模块。

请先核对 database/zzyl.sql 中的 t_visit、t_elder、t_bed、t_contract、t_apply、t_family_user、t_family_elder，然后按 com.soft 现有风格实现实体、DTO、Mapper、Service、Controller，再完成 Vue 页面、路由和接口联调。不能只做静态页面，也不要私自新建重复表。

按“预约登记和到院 -> 入住办理 -> 在住合同与床位 -> 请假 -> 退住 -> 家属端首页”的顺序连续实现。每完成一个流程就实际验证数据库变化。最后运行前端和后端构建，汇报已完成页面、接口、数据表、测试结果和剩余问题；确认无误后只提交到当前分支并 push，不要合并 dev 或 main。
```

## 组员 B：服务、订单、财务和家属端服务

### 分支

```text
feature/member-b-service-order
```

### 管理端范围

- 服务管理：护理等级、护理计划、护理项目、负责老人、任务安排及任务状态。
- 订单管理：订单列表、订单详情、退款管理。
- 财务管理：入账列表、账单详情、欠费老人、预缴款充值、余额查询。

### 家属端范围

- 服务项目展示。
- 为绑定老人下单。
- 查看服务订单状态。

### 主要数据表

```text
t_nursimg_item
t_nursing_plain
t_plain_item
t_nursing_task
t_order
t_bill
t_elder
t_family_user
```

老师代码中的 `Nursimg`、`Plain` 和数据库表名拼写必须兼容，不能为了改拼写破坏已有功能。

### 必须完成的主流程

1. 护理项目和护理计划可分页查询、新增、编辑和启停。
2. 护理计划可以关联多个护理项目。
3. 为老人生成护理任务，任务支持待执行、已执行、已取消及详情。
4. 家属可为绑定老人选择服务并创建订单。
5. 订单支付或服务状态变化后可生成、更新账单。
6. 退款、欠费、预缴款和余额能够通过统一状态字段完成课程演示。

### 直接交给组员 B Agent 的提示词

```text
请阅读仓库根目录 TEAM_AGENT_TASKS.md，我是组员 B。

先检查 git 状态，确认当前分支是 feature/member-b-service-order；如果不是，只告诉我正确切换命令，不要在 dev 或 main 上修改代码。

我的范围是管理端服务管理、订单管理、财务管理，以及家属端服务。请严格打开老师 Axure 中对应页面并阅读页面说明，复用现有 AdminLayout.vue，不要修改公共顶栏、侧栏和其他组员模块。

请先检查老师已有的护理项目和护理计划代码，保持 t_nursimg_item、t_nursing_plain、t_plain_item 以及 Nursimg/Plain 拼写兼容。在此基础上完成 t_nursing_task、t_order、t_bill 对应的实体、DTO、Mapper、Service、Controller，再完成 Vue 页面、路由和真实接口联调。不能只做静态页面，也不要私自拆出重复的订单明细或账单表。

按“护理项目和计划 -> 护理任务 -> 家属端服务下单 -> 订单 -> 退款 -> 账单和预存”的顺序连续实现。最后运行前端和后端构建，汇报已完成页面、接口、数据表、测试结果和剩余问题；确认无误后只提交到当前分支并 push，不要合并 dev 或 main。
```

## 组员 C：客户、协同、智能监测和家属端家人

### 分支

```text
feature/member-c-customer-monitor
```

### 管理端范围

- 客户管理：客户信息、意向等级、来源、跟进内容和下次跟进时间。
- 协同工作：我的待办、我的申请、入住/退住/请假流程查看和处理。
- 智能监测：设备管理、设备详情、运行状态、事件、报警数据和报警规则。

### 家属端范围

- 家人列表。
- 绑定家人。
- 查看绑定老人基础和健康信息。

### 主要数据表

```text
t_customer
t_apply
t_device
t_elder
t_family_user
t_family_elder
t_message
```

不要再建 `t_todo`、`t_application`、`t_alert_record`、`t_alert_rule`。协同流程统一使用 `t_apply`，设备和报警演示统一使用 `t_device` 当前字段。

### 必须完成的主流程

1. 客户可新增、编辑、查询和记录跟进。
2. `t_apply` 按当前用户和状态展示待办、已处理、我的申请。
3. 审批操作会更新申请状态、审批人、审批时间和意见。
4. 设备可新增、编辑、启停并绑定老人或床位。
5. 设备详情可展示最近运行数据、事件和报警状态。
6. 家属可以通过老人编号或约定信息绑定老人，并只能查看自己的绑定关系。

### 直接交给组员 C Agent 的提示词

```text
请阅读仓库根目录 TEAM_AGENT_TASKS.md，我是组员 C。

先检查 git 状态，确认当前分支是 feature/member-c-customer-monitor；如果不是，只告诉我正确切换命令，不要在 dev 或 main 上修改代码。

我的范围是管理端客户管理、协同工作、智能监测，以及家属端家人。请严格打开老师 Axure 中对应页面并阅读页面说明，复用现有 AdminLayout.vue，不要修改公共顶栏、侧栏和其他组员模块。

请使用 database/zzyl.sql 中现有的 t_customer、t_apply、t_device、t_elder、t_family_user、t_family_elder、t_message，按 com.soft 现有风格完成实体、DTO、Mapper、Service、Controller，再完成 Vue 页面、路由和真实接口联调。不能只做静态页面，不要新建重复的待办、申请、报警数据或报警规则表。

按“客户信息 -> 我的待办和我的申请 -> 设备管理和报警 -> 家属绑定老人”的顺序连续实现。涉及入住、退住、请假申请时只维护审批和协同部分，不要覆盖组员 A 的业务办理代码。最后运行前端和后端构建，汇报已完成页面、接口、数据表、测试结果和剩余问题；确认无误后只提交到当前分支并 push，不要合并 dev 或 main。
```

## Agent 完成后的统一检查

每位 Agent 必须向组员提供以下报告：

```text
当前分支：
本次提交：
完成页面：
新增或修改接口：
使用的数据表：
是否修改公共文件：
前端构建结果：
后端构建结果：
实际操作过的主流程：
尚未完成或需要组长协调的问题：
PR 地址：
```

提交命令由 Agent 在检查改动后执行：

```bash
git status
git diff --check
git add 本次相关文件
git commit -m "完成对应功能"
git push -u origin 当前分支
```

PR 必须提交到：

```text
base: dev
compare: 自己的 feature 分支
```

