<template>
  <div class="family-login-container">
    <div class="family-login-card">
      <div class="login-logo-area">
        <img :src="logoMark" alt="中州养老" class="login-logo-img" />
        <h2>中州养老</h2>
        <p>家属端</p>
      </div>

      <el-form ref="formRef" :model="loginForm" :rules="rules" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="account">
          <el-input v-model="loginForm.account" placeholder="请输入账号" :prefix-icon="User" clearable />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" :loading="loading" @click="handleLogin">
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <p class="login-tip">默认测试账号：family001 / 123456</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { familyLogin } from '@/api/family'
import logoMark from '@/assets/zhyl-logo-mark.png'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)

const loginForm = reactive({ account: '', password: '' })
const rules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

async function handleLogin() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await familyLogin({ account: loginForm.account, password: loginForm.password })
    if (res.code === 200) {
      localStorage.setItem('familyToken', 'true')
      if (res.data) localStorage.setItem('familyUser', JSON.stringify(res.data))
      ElMessage.success('登录成功')
      router.replace('/family/mine')
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
.family-login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0052d9 0%, #3370ff 100%);
  padding: 20px;
}
.family-login-card {
  width: 360px;
  padding: 40px 30px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.12);
}
.login-logo-area {
  text-align: center;
  margin-bottom: 32px;
}
.login-logo-img {
  width: 56px;
  height: 56px;
  object-fit: contain;
}
.login-logo-area h2 {
  margin: 10px 0 4px;
  font-size: 22px;
  color: #333;
}
.login-logo-area p {
  margin: 0;
  color: rgba(0,0,0,0.45);
  font-size: 14px;
}
.login-btn {
  width: 100%;
  height: 44px;
  letter-spacing: 4px;
  border-radius: 6px;
}
.login-tip {
  text-align: center;
  color: rgba(0,0,0,0.3);
  font-size: 12px;
  margin: 16px 0 0;
}
</style>
