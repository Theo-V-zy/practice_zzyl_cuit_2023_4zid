# 组员 Agent 提示词 v2

> 使用前提：组长已将 `feature/leader-base-auth` 合并到 `dev`。
> 以下提示词直接复制给 B、C 对应组员的 AI Agent。

---

## 组员 B：服务、订单、财务 + 家属端服务

```text
你是组员 B 的 AI Agent。请严格按以下步骤执行，不要跳过任何一步。

## 第 1 步：新建分支

```bash
git fetch origin
git checkout -b feature/member-b-v2 origin/dev
```

旧分支 feature/member-b-service-order 作废，不再使用。

## 第 2 步：阅读规范

必须阅读以下文件再开始写代码：
- TEAM_README.md
- TEAM_AGENT_TASKS.md（只看组员 B 的部分）
- frontend-family/README.md
- database/zzyl.sql（确认 t_nursimg_item、t_nursing_plain、t_plain_item、t_nursing_task、t_order、t_bill、t_elder、t_family_user 表结构）

原型入口：https://rp-java.itheima.net/zhyl/#id=ps9caw&p=%E6%95%B4%E4%BD%93%E4%BB%8B%E7%BB%8D&g=1

## 第 3 步：完成以下全部模块

按顺序实现，每完成一个就验证数据库和页面效果：

**服务管理：**
- 护理等级：CRUD + 页面（/NursingLevel，替换 ModulePlaceholder）
- 护理计划：完善已有 NursingPlain，支持关联多个护理项目
- 护理项目：完善已有 NursingItem，支持启停
- 负责老人：CRUD + 页面（/ElderAssignment）
- 任务安排：为老人生成护理任务，支持状态变更（/NursingTask）

**订单管理：**
- 订单列表 + 详情（/Order）
- 退款管理（/Refund）

**财务管理：**
- 入账列表 + 账单详情（/Bill）
- 欠费老人（/Arrears）
- 预缴款充值（/Prepay）
- 余额查询（/Balance）

**家属端服务（frontend-family/）：**
- 服务项目展示页面
- 为绑定老人下单
- 订单状态查看

## 第 4 步：集成规则

- 后端新建 Entity → Mapper → Service → ServiceImpl → Controller，风格对齐已有代码
- 返回格式统一 {code: 200/400, msg, data, total}
- 前端新建页面放在 frontend/src/views/，替换 router 中对应 ModulePlaceholder
- admin.js 新增 API 函数，不删不改已有的
- **禁止修改**：frontend/src/layouts/、router 中非自己的路由、其他组员的 Controller 和页面
- 不新建重复表（只用 t_nursimg_item、t_nursing_plain、t_plain_item、t_nursing_task、t_order、t_bill）
- 老师代码中的 Nursimg/Plain 拼写必须保留兼容

## 第 5 步：完成检查

全部完成后运行：
```bash
cd frontend && npm run build
cd ../backend && mvn clean compile
```

然后汇报：
- 已完成页面和接口清单
- 使用到的数据表
- 是否修改了公共文件
- 前端和后端构建结果
- 尚未完成或需要组长协调的问题
```

---

## 组员 C：客户、协同、智能监测 + 家属端家人

```text
你是组员 C 的 AI Agent。请严格按以下步骤执行，不要跳过任何一步。

## 第 1 步：新建分支

```bash
git fetch origin
git checkout -b feature/member-c-v2 origin/dev
```

旧分支 feature/member-c-customer-monitor 作废，不再使用。

## 第 2 步：阅读规范

必须阅读以下文件再开始写代码：
- TEAM_README.md
- TEAM_AGENT_TASKS.md（只看组员 C 的部分）
- frontend-family/README.md
- database/zzyl.sql（确认 t_customer、t_apply、t_device、t_elder、t_family_user、t_family_elder、t_message 表结构）

原型入口：https://rp-java.itheima.net/zhyl/#id=ps9caw&p=%E6%95%B4%E4%BD%93%E4%BB%8B%E7%BB%8D&g=1

## 第 3 步：完成以下全部模块

按顺序实现，每完成一个就验证数据库和页面效果：

**客户管理：**
- 客户列表 + 新增/编辑 + 查询（/Customer，替换 ModulePlaceholder）
- 意向等级、来源字段
- 跟进记录：跟进内容 + 下次跟进时间

**协同工作：**
- 我的待办：展示 t_apply 中当前用户的待处理申请（/Todo）
- 我的申请：展示当前用户发起的申请列表（/Application）
- 审批操作：更新申请状态、审批人、审批时间、意见
- 入住/退住/请假流程的审批处理

**智能监测：**
- 设备管理：设备列表 + 新增/编辑/启停（/Device）
- 设备可绑定老人或床位
- 报警数据展示（/Alert）
- 报警规则配置（/AlertRule）

**家属端家人（frontend-family/）：**
- 家人列表页面
- 绑定家人（通过老人编号或约定信息）
- 查看绑定老人的基础和健康信息

## 第 4 步：集成规则

- 后端新建 Entity → Mapper → Service → ServiceImpl → Controller，风格对齐已有代码
- 返回格式统一 {code: 200/400, msg, data, total}
- 前端新建页面放在 frontend/src/views/，替换 router 中对应 ModulePlaceholder
- admin.js 新增 API 函数，不删不改已有的
- **禁止修改**：frontend/src/layouts/、router 中非自己的路由、其他组员的 Controller 和页面
- **禁止新建**：t_todo、t_application、t_alert_record、t_alert_rule 等重复表，统一使用 t_apply 和 t_device 现有字段
- 涉及入住/退住/请假申请时只处理审批和协同，不覆盖组员 A 的业务办理代码

## 第 5 步：完成检查

全部完成后运行：
```bash
cd frontend && npm run build
cd ../backend && mvn clean compile
```

然后汇报：
- 已完成页面和接口清单
- 使用到的数据表
- 是否修改了公共文件
- 前端和后端构建结果
- 尚未完成或需要组长协调的问题
```
