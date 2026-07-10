# 中州养老家属端小程序

uni-app 项目，用 HBuilder X 编译为微信小程序。

## 环境准备

1. 下载安装 [HBuilder X](https://www.dcloud.io/hbuilderx.html)
2. 下载安装 [微信开发者工具](https://developers.weixin.qq.com/miniprogram/dev/devtools/download.html)

## 打开项目

HBuilder X → 文件 → 导入 → 从本地目录导入 → 选择 `frontend-family/`

## 启动调试

HBuilder X 工具栏：运行 → 运行到小程序模拟器 → 微信开发者工具

首次运行需要：
- 在微信开发者工具中开启"服务端口"（设置 → 安全 → 服务端口）
- 使用测试号 AppID 或自己的小程序 AppID

## 配置后端地址

编辑 `api/request.js`，修改 `BASE_URL`：
```js
const BASE_URL = 'http://localhost:8080'  // 改为实际后端地址
```

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
├── api/request.js      # API 请求封装（uni.request）
├── static/             # 图标等静态资源
├── pages.json          # 路由和 tabBar
├── manifest.json       # 小程序配置
└── App.vue             # 应用入口
```

## 测试账号
- 账号：family001
- 密码：123456
