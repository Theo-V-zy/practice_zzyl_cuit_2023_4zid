# 中州养老家属端小程序

uni-app 项目，编译为微信小程序运行。

## 快速开始

### 1. 安装依赖
```bash
cd frontend-family
npm install
```

### 2. 启动开发
```bash
# 编译为微信小程序
npm run dev:mp-weixin
```

### 3. 导入微信开发者工具
- 打开微信开发者工具
- 导入项目 → 选择 `frontend-family/dist/dev/mp-weixin`
- AppID 使用测试号或你自己的

### 4. 配置后端地址
编辑 `api/request.js`，修改 `BASE_URL` 为实际后端地址。

## 项目结构
```
frontend-family/
├── pages/
│   ├── login/          # 家属登录
│   ├── mine/           # 我的（首页）
│   ├── contracts/      # 我的合同
│   ├── appointments/   # 我的预约
│   ├── orders/         # 我的订单
│   ├── order-detail/   # 订单详情
│   └── bills/          # 我的账单
├── api/request.js      # API 请求封装
├── static/             # 静态资源（图标等）
├── pages.json          # 路由和 tabBar 配置
├── manifest.json       # 小程序配置
└── App.vue             # 应用入口
```

## 页面说明

| 页面 | 路径 | 说明 |
|------|------|------|
| 登录 | pages/login/login | 家属账号密码登录 |
| 我的 | pages/mine/mine | 个人信息、绑定家人、功能入口 |
| 我的合同 | pages/contracts/contracts | 合同卡片列表、状态标签、下载/查看 |
| 我的预约 | pages/appointments/appointments | Tab筛选（全部/参观/探访）、取消预约 |
| 我的订单 | pages/orders/orders | Tab筛选（全部/待支付/待执行/已完成）、状态驱动操作按钮 |
| 订单详情 | pages/order-detail/order-detail | 订单完整信息、支付倒计时、状态流转、养老院介绍 |
| 我的账单 | pages/bills/bills | 账单列表、金额/已付/状态 |

## 后端接口
所有接口以 `/family/` 为前缀，详见后端 `FamilyController.java`。

## 测试账号
- 账号：family001
- 密码：123456
