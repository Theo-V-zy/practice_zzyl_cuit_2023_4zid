# 家属端 Agent 维护说明

家属端主体功能已经完成。后续 Agent 应先阅读本文件和 `README.md`，不要重新搭建工程或更换技术栈。

## 固定约束

- 技术栈：uni-app + Vue 3。
- 页面目录：`frontend-family/pages/`。
- 请求只能通过 `frontend-family/api/request.js`。
- 家属接口统一使用 `/family` 前缀和 `X-Family-Token`。
- 主导航固定为：首页、家人、服务、我的。
- 主色 `#0052d9`，背景 `#f5f5f5`，内容块白色，圆角不超过 `12rpx`。
- 原型项目 ID：`2dvxxe`。

## Agent 开始工作时

```text
请先阅读 frontend-family/README.md 和 frontend-family/AGENT_HANDOFF.md。
检查 git status，不覆盖其他成员未提交的改动。
修改后运行：cd frontend-family && npm run build:mp-weixin。
涉及后端时运行：cd backend && mvn -q -DskipTests package。
最后列出修改文件、验证结果和仍需人工测试的内容。
```

## 关键文件

- `pages.json`：页面与四栏导航。
- `api/request.js`：Token、后端地址和全部接口函数。
- `backend/.../FamilyController.java`：家属业务接口与数据权限。
- `backend/.../FamilyTokenService.java`：登录令牌生成和校验。
- `database/family_completion.sql`：家属端演示数据。

禁止把密码、Token 或正式服务器地址直接写进页面组件。
