<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <img class="login-logo" :src="logoMark" alt="中州养老" />
        <h1 class="system-title">智慧养老服务平台</h1>
        <p class="system-subtitle">关爱老人 · 用心服务</p>
      </div>

      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          size="large"
          @keyup.enter="handleLogin"
      >
        <el-form-item prop="account">
          <el-input
              v-model="loginForm.account"
              placeholder="请输入账号"
              :prefix-icon="User"
              clearable
          />
        </el-form-item>

        <el-form-item prop="upwd">
          <el-input
              v-model="loginForm.upwd"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              class="login-btn"
              :loading="loading"
              @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <span>默认账号：20260023 / 222222</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { login, loadInfo } from '@/api/admin'
import logoMark from '@/assets/zhyl-logo-mark.png'

const loginFormRef = ref(null)
const loading = ref(false)
const router = useRouter()

const loginForm = reactive({
  account: '',
  upwd: ''
})

const loginRules = {
  account: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  upwd: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

async function handleLogin() {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login({ account: loginForm.account, upwd: loginForm.upwd })
    if (res.code === 200) {
      ElMessage.success('登录成功')
      router.replace('/MainIndex')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (e) {
    ElMessage.error('登录失败，请检查网络')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #0052d9 0%, #3370ff 50%, #85b0ff 100%);
  padding: 20px;
}

.login-card {
  width: 420px;
  padding: 50px 40px;
  background: #ffffff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
}

.login-header {
  text-align: center;
  margin-bottom: 36px;
}

.login-logo {
  width: 64px;
  height: 64px;
  margin-bottom: 12px;
  object-fit: contain;
}

.system-title {
  font-size: 24px;
  font-weight: 600;
  color: #333333;
  margin: 0 0 8px 0;
}

.system-subtitle {
  font-size: 14px;
  color: rgba(0, 0, 0, 0.45);
  margin: 0;
}

.login-form {
  margin-top: 10px;
}

.login-btn {
  width: 100%;
  height: 44px;
  font-size: 16px;
  letter-spacing: 4px;
  border-radius: 6px;
  margin-top: 8px;
}

.login-footer {
  text-align: center;
  margin-top: 24px;
  color: rgba(0, 0, 0, 0.35);
  font-size: 13px;
}
</style>
