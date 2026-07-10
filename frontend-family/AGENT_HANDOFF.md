# 家属端小程序 Agent 开发任务书

家属端是 uni-app 微信小程序项目，由组长搭好框架、三个组员各自负责部分页面。

## 项目启动

```bash
cd frontend-family
npm install
npm run dev:mp-weixin
# 微信开发者工具 → 导入 → 选择 dist/dev/mp-weixin
```

后端地址在 `api/request.js` 的 `BASE_URL`，默认 `http://localhost:8080`。

## 页面分工

| 负责人 | 页面 | 路径 |
|--------|------|------|
| 组长 | 登录、我的 | `pages/login/` `pages/mine/` |
| 组员 A | 首页 | `pages/home/`（待创建） |
| 组员 B | 服务 | `pages/service/`（待创建） |
| 组员 C | 家人 | `pages/family/`（待创建） |

`pages.json` 中登记新页面，tabBar 可按需扩展。

## 组员 A Agent 提示词

```
请打开 frontend-family/AGENT_HANDOFF.md，我是组员 A，负责家属端首页。

先检查 git 状态，确认在 feature/member-a-resident-flow 分支。

我的任务：在 frontend-family/pages/ 下创建 home/ 目录，完成家属端首页。

要求：
- 使用 uni-app 原生组件（view/text/image/scroll-view）
- 样式用 rpx 单位，颜色 #0052d9 主色
- 网络请求用 ../../api/request.js 封装
- 后端接口前缀 /family/
- 参考原型：https://rp-java.itheima.net/zhyl/（家属端首页）

完成后在 pages.json 注册页面并添加到 tabBar。
```

## 组员 B Agent 提示词

```
请打开 frontend-family/AGENT_HANDOFF.md，我是组员 B，负责家属端服务。

先检查 git 状态，确认在 feature/member-b-service-order 分支。

我的任务：在 frontend-family/pages/ 下创建 service/ 目录，完成服务项目展示和下单页面。

要求：
- uni-app 原生组件，rpx 单位，#0052d9 主色
- 网络请求用 ../../api/request.js
- 后端接口前缀 /family/
- 参考原型服务页面

完成后在 pages.json 注册页面。
```

## 组员 C Agent 提示词

```
请打开 frontend-family/AGENT_HANDOFF.md，我是组员 C，负责家属端家人。

先检查 git 状态，确认在 feature/member-c-customer-monitor 分支。

我的任务：在 frontend-family/pages/ 下创建 family/ 目录，完成家人列表和绑定页面。

要求：
- uni-app 原生组件，rpx 单位，#0052d9 主色
- 网络请求用 ../../api/request.js
- 后端接口前缀 /family/
- 参考原型家人页面

完成后在 pages.json 注册页面。
```

## 开发规范

- **样式**：rpx 单位，主色 `#0052d9`，背景 `#f4f5f7`，圆角 12-20rpx
- **请求**：统一用 `api/request.js` 的 `request()` 函数
- **路由**：新增页面在 `pages.json` 的 `pages` 数组中注册
- **tabBar**：如需加底部导航，在 `pages.json` 的 `tabBar.list` 中添加
- **后端接口**：组长维护 `FamilyController.java`，组员需要新接口时告诉组长
- **不提交**：`node_modules/`、`dist/`、`unpackage/`

## 原型地址

```
https://rp-java.itheima.net/zhyl/#id=2dvxxe&p=%E7%99%BB%E5%BD%95_1&g=1
```

项目 ID 是 `2dvxxe`（家属端），不是 `ps9caw`（管理端）。
