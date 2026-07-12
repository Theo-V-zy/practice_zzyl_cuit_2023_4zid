# 中州养老家属端微信小程序

技术栈：uni-app + Vue 3，后端为仓库中的 Spring Boot 服务。

## 已实现功能

- 家属账号登录与 7 天 Token 登录态
- 首页、家人、服务、我的四个主 Tab
- 探访预约、参观预约、预约记录与取消
- 绑定/解绑家人、老人详情和健康数据
- 服务搜索、服务详情、选择家人和服务时间下单
- 订单查询、模拟支付、取消、退款和删除
- 合同、账单、个人资料与养老院介绍

## 测试账号

- 账号：`family001`
- 密码：`123456`

## 启动后端

```bash
cd backend
mvn spring-boot:run
```

MySQL 默认连接 `zzyl` 数据库，账号 `root`，密码 `we005218`。

## 构建微信小程序

```bash
cd frontend-family
npm install
npm run build:mp-weixin
```

编译结果位于：

```text
frontend-family/dist/build/mp-weixin
```

在微信开发者工具中选择“导入项目”，目录选择上述路径即可。

开发者工具开启自动化端口后，可执行登录和四个主 Tab 的冒烟测试：

```bash
WECHAT_AUTOMATION_ENDPOINT=ws://127.0.0.1:9420 npm run test:smoke
```

## 后端地址

模拟器默认请求 `http://127.0.0.1:8080`。真机调试时，需要在登录前通过控制台设置电脑局域网地址：

```js
wx.setStorageSync('familyApiBaseUrl', 'http://电脑局域网IP:8080')
```

微信开发者工具本地调试时，需在“详情 → 本地设置”中勾选“不校验合法域名、web-view、TLS 版本以及 HTTPS 证书”。正式发布时应配置 HTTPS 合法域名。

## 数据初始化

完整建库使用 `database/zzyl.sql`。已有数据库补充家属端演示数据：

```bash
mysql -uroot -pwe005218 < database/family_completion.sql
```

## 原型

家属端项目 ID 为 `2dvxxe`：

```text
https://rp-java.itheima.net/zhyl/#id=2dvxxe&p=登录_1&g=1
```
