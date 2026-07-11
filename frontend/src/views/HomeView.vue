<template>
  <div class="login-page">
    <!-- 动态背景光斑 -->
    <div class="dynamic-background" aria-hidden="true">
      <span class="blur-blob blob-green"></span>
      <span class="blur-blob blob-orange"></span>
      <span class="blur-blob blob-pink"></span>

      <span
        v-for="dot in floatingDots"
        :key="dot.className"
        class="floating-dot"
        :class="dot.className"
      ></span>

      <!-- 底部流动波浪 -->
      <svg class="wave wave-back" viewBox="0 0 1440 360" preserveAspectRatio="none">
        <path d="M0,210 C180,120 330,290 520,225 C720,155 850,310 1040,215 C1200,135 1320,170 1440,115 L1440,360 L0,360 Z" />
      </svg>
      <svg class="wave wave-middle" viewBox="0 0 1440 360" preserveAspectRatio="none">
        <path d="M0,250 C220,165 345,325 560,245 C760,170 920,300 1100,235 C1250,180 1360,200 1440,165 L1440,360 L0,360 Z" />
      </svg>
      <svg class="wave wave-front" viewBox="0 0 1440 360" preserveAspectRatio="none">
        <path d="M0,285 C210,220 380,340 590,280 C810,215 955,320 1160,260 C1290,220 1380,225 1440,210 L1440,360 L0,360 Z" />
      </svg>
    </div>

    <!-- Logo -->
    <header class="brand">
      <img class="brand-logo" :src="logoMark" alt="中州养老" />
      <div class="brand-text">
        <strong>中州养老</strong>
        <span>ZHONG ZHOU YANG LAO</span>
      </div>
    </header>

    <main class="login-content">
      <!-- 左侧宣传区域 -->
      <section class="welcome-area">
        <p class="welcome-tag">ZHONGZHOU ELDERLY CARE</p>
        <h1>让照护更有温度</h1>
        <p class="welcome-description">智慧养老 · 安全守护 · 贴心服务</p>
        <div class="feature-list">
          <span><i class="feature-dot"></i>统一管理</span>
          <span><i class="feature-dot"></i>数据互通</span>
          <span><i class="feature-dot"></i>安全可靠</span>
        </div>
      </section>

      <!-- 登录卡片 -->
      <section class="login-card">
        <div class="card-header">
          <p>欢迎回来</p>
          <h2>账号密码登录</h2>
        </div>

        <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginRules"
          class="login-form"
          @keyup.enter="handleLogin"
        >
          <!-- 账号输入框 -->
          <el-form-item prop="account" class="custom-form-item">
            <div class="form-item">
              <span class="input-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8Zm7 8a7 7 0 0 0-14 0" />
                </svg>
              </span>
              <el-input
                v-model="loginForm.account"
                placeholder="请输入账号"
              />
            </div>
          </el-form-item>

          <!-- 密码输入框 -->
          <el-form-item prop="upwd" class="custom-form-item">
            <div class="form-item">
              <span class="input-icon">
                <svg viewBox="0 0 24 24" aria-hidden="true">
                  <path d="M7 10V8a5 5 0 0 1 10 0v2M6 10h12v10H6V10Zm6 4v2" />
                </svg>
              </span>
              <el-input
                v-model="loginForm.upwd"
                type="password"
                placeholder="请输入密码"
                show-password
              />
            </div>
          </el-form-item>

          <div class="form-options">
            <label class="remember-option">
              <input v-model="loginForm.remember" type="checkbox" />
              <span class="custom-checkbox"></span>
              <span>自动登录</span>
            </label>
          </div>

          <button
            class="login-button"
            type="button"
            :disabled="loading"
            @click="handleLogin"
          >
            <span v-if="loading" class="loading-icon"></span>
            {{ loading ? '正在登录...' : '登录' }}
          </button>
        </el-form>

        <p class="security-tip">请妥善保管账号信息，避免在公共设备上保存密码</p>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { login } from '@/api/admin'
import logoMark from '@/assets/zhyl-logo-mark.png'

const loginFormRef = ref(null)
const loading = ref(false)
const router = useRouter()

const loginForm = reactive({
  account: '',
  upwd: '',
  remember: false
})

const loginRules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  upwd: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const floatingDots = [
  { className: 'dot-1' }, { className: 'dot-2' }, { className: 'dot-3' },
  { className: 'dot-4' }, { className: 'dot-5' }, { className: 'dot-6' }
]

async function handleLogin() {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await login({ account: loginForm.account, upwd: loginForm.upwd })
    if (res.code === 200) {
      const storage = loginForm.remember ? localStorage : sessionStorage
      storage.setItem('rememberedAccount', loginForm.account)
      ElMessage.success('登录成功')
      router.replace('/MainIndex')
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (e) {
    // 错误已由 request.js 拦截器处理
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-page {
  --primary-blue: #2f91f3;
  --brand-green: #8fbd40;
  --brand-orange: #f4a13d;
  --brand-pink: #ec7c8b;
  position: relative;
  width: 100%;
  min-height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #f8fafc 0%, #fbfcf8 38%, #fff8f4 70%, #fff8fb 100%);
}

.dynamic-background { position: absolute; inset: 0; overflow: hidden; pointer-events: none; }

.blur-blob {
  position: absolute; border-radius: 50%; filter: blur(85px); opacity: 0.2;
  will-change: transform; animation: blob-float 18s ease-in-out infinite alternate;
}
.blob-green  { top: -160px; right: 3%; width: 440px; height: 440px; background: var(--brand-green); }
.blob-orange { bottom: -190px; left: 37%; width: 430px; height: 430px; background: var(--brand-orange); animation-delay: -6s; }
.blob-pink   { right: -160px; bottom: -60px; width: 500px; height: 500px; background: var(--brand-pink); animation-delay: -11s; }

@keyframes blob-float {
  0%   { transform: translate3d(-25px, -15px, 0) scale(1); }
  50%  { transform: translate3d(45px, 25px, 0) scale(1.08); }
  100% { transform: translate3d(-5px, 65px, 0) scale(0.96); }
}

.floating-dot { position: absolute; border-radius: 50%; opacity: 0.55; animation: dot-float 7s ease-in-out infinite; }
.dot-1 { top: 32%; left: 5%; width: 16px; height: 16px; background: rgba(244, 161, 61, 0.65); }
.dot-2 { top: 29%; left: 43%; width: 11px; height: 11px; background: rgba(143, 189, 64, 0.72); animation-delay: -2s; }
.dot-3 { top: 55%; left: 8%; width: 9px; height: 9px; background: rgba(236, 124, 139, 0.68); animation-delay: -4s; }
.dot-4 { top: 38%; right: 8%; width: 13px; height: 13px; background: rgba(236, 124, 139, 0.72); animation-delay: -1s; }
.dot-5 { bottom: 17%; right: 7%; width: 22px; height: 22px; background: rgba(236, 124, 139, 0.62); animation-delay: -5s; }
.dot-6 { top: 58%; left: 50%; width: 8px; height: 8px; background: rgba(244, 161, 61, 0.67); animation-delay: -3s; }

@keyframes dot-float {
  0%, 100% { transform: translateY(0) scale(1); }
  50%      { transform: translateY(-24px) scale(1.12); }
}

.wave {
  position: absolute; right: -2%; bottom: -2px; width: 104%; height: 41%;
  transform-origin: center bottom; will-change: transform; z-index: 1;
}
.wave path       { fill: currentColor; }
.wave-back   { color: rgba(143, 189, 64, 0.19); animation: wave-move-back 14s ease-in-out infinite alternate; }
.wave-middle { color: rgba(244, 161, 61, 0.13); animation: wave-move-middle 11s ease-in-out infinite alternate; }
.wave-front  { color: rgba(236, 124, 139, 0.15); animation: wave-move-front 16s ease-in-out infinite alternate; }

@keyframes wave-move-back   { from { transform: translateX(-12px) scaleY(1); } to { transform: translateX(18px) scaleY(1.07); } }
@keyframes wave-move-middle { from { transform: translateX(15px) scaleY(1.02); } to { transform: translateX(-18px) scaleY(0.95); } }
@keyframes wave-move-front  { from { transform: translateX(-8px); } to { transform: translateX(22px); } }

.brand {
  position: absolute; top: 44px; left: 5%; z-index: 3;
  display: flex; align-items: center; gap: 12px;
}
.brand-logo       { width: 62px; height: 62px; object-fit: contain; }
.brand-text       { display: flex; flex-direction: column; }
.brand-text strong { color: #353b37; font-family: "STSong","SimSun",serif; font-size: 28px; font-weight: 700; letter-spacing: 2px; }
.brand-text span   { margin-top: 2px; color: #727b76; font-size: 10px; letter-spacing: 0.4px; }

.login-content {
  position: relative; z-index: 2;
  display: grid; grid-template-columns: minmax(420px, 1fr) 430px;
  align-items: center; gap: 90px;
  width: min(1300px, 88%); min-height: 100vh;
  margin: 0 auto; padding: 125px 0 110px;
}

.welcome-area       { max-width: 670px; padding-left: 5%; transform: translateY(-15px); position: relative; z-index: 4; }
.welcome-tag        { margin: 0 0 17px; color: rgba(83,99,89,0.56); font-size: 12px; font-weight: 600; letter-spacing: 4px; }
.welcome-area h1 {
  margin: 0;
  background: linear-gradient(90deg, #e87686 0%, #ef9851 48%, #98b94f 100%);
  background-clip: text; -webkit-background-clip: text; color: transparent;
  font-family: "STKaiti","KaiTi",serif;
  font-size: clamp(46px, 5vw, 72px); font-weight: 500; letter-spacing: 4px; line-height: 1.25;
}
.welcome-description { margin: 24px 0 0; color: #626b66; font-size: 20px; letter-spacing: 5px; }
.feature-list        { display: flex; flex-wrap: wrap; gap: 22px; margin-top: 35px; color: #7a847e; font-size: 14px; }
.feature-list span   { display: flex; align-items: center; gap: 8px; }
.feature-dot {
  width: 7px; height: 7px; border-radius: 50%;
  background: linear-gradient(135deg, var(--brand-green), var(--brand-orange));
  box-shadow: 0 0 0 4px rgba(143,189,64,0.1);
}

.login-card {
  position: relative; width: 100%; padding: 42px 44px 34px;
  border: 1px solid rgba(255,255,255,0.86); border-radius: 24px;
  background: rgba(255,255,255,0.84);
  box-shadow: 0 28px 65px rgba(76,92,83,0.12), 0 3px 10px rgba(76,92,83,0.05);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
}
.login-card::before {
  position: absolute; inset: 0; border-radius: inherit;
  background: linear-gradient(145deg, rgba(255,255,255,0.65), rgba(255,255,255,0));
  pointer-events: none; content: "";
}

.card-header, .login-form, .security-tip { position: relative; z-index: 1; }
.card-header p  { margin: 0 0 5px; color: #98a19c; font-size: 14px; }
.card-header h2 { margin: 0 0 28px; color: #2d322f; font-size: 25px; font-weight: 700; }

/* ===== 统一输入框 ===== */
.custom-form-item {
  margin: 0 !important;
}
.custom-form-item :deep(.el-form-item__content) {
  display: block;
}
.custom-form-item :deep(.el-form-item__error) {
  padding-top: 4px; font-size: 13px; color: #e65e67;
}

.form-item {
  display: flex;
  align-items: center;
  height: 56px;
  padding: 0 18px;
  border: 1px solid #dce3e8;
  border-radius: 8px;
  background: rgba(255,255,255,0.7);
  transition: border-color 0.2s ease, box-shadow 0.2s ease, background 0.2s ease;
  box-sizing: border-box;
}
.form-item:focus-within {
  border-color: rgba(47,145,243,0.75); background: #fff;
  box-shadow: 0 0 0 4px rgba(47,145,243,0.1);
}

.input-icon {
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0; width: 20px; height: 20px; margin-right: 12px;
  color: #aeb8c2;
}
.input-icon svg {
  display: block; width: 18px; height: 18px;
  fill: none; stroke: currentColor;
  stroke-linecap: round; stroke-linejoin: round; stroke-width: 1.6;
}

/* Element Plus input inside form-item — strip all default styles */
.form-item :deep(.el-input) {
  flex: 1; height: 100%;
}
.form-item :deep(.el-input__wrapper) {
  border: 0 !important; box-shadow: none !important; background: transparent !important;
  padding: 0 !important; height: 100%;
}
.form-item :deep(.el-input__inner) {
  height: 100%; padding: 0; border: 0; background: transparent;
  font-size: 15px; color: #303632; line-height: normal;
}
.form-item :deep(.el-input__inner::placeholder) {
  color: #b1b9c1; opacity: 1;
}
.form-item :deep(.el-input--suffix) {
  height: 100%;
}
.form-item :deep(.el-input__suffix) {
  display: flex; align-items: center; height: 100%;
  color: #9ca6af; cursor: pointer;
}
.form-item :deep(.el-input__suffix:hover) { color: var(--primary-blue); }
.form-item :deep(.el-input__suffix-inner) {
  display: flex; align-items: center;
}

/* ===== 表单间距 ===== */
.login-form {
  display: flex; flex-direction: column;
}
.login-form > .custom-form-item + .custom-form-item {
  margin-top: 18px !important;
}

.form-options {
  display: flex; align-items: center;
  margin-top: 18px;
}
.remember-option {
  display: inline-flex; align-items: center; color: #59615d;
  cursor: pointer; font-size: 14px; user-select: none;
}
.remember-option input { position: absolute; width: 0; height: 0; opacity: 0; }
.custom-checkbox {
  position: relative; width: 17px; height: 17px; margin-right: 8px;
  border: 1px solid #cfd7df; border-radius: 4px; background: #fff;
  transition: all 0.2s ease;
}
.remember-option input:checked + .custom-checkbox {
  border-color: var(--primary-blue); background: var(--primary-blue);
}
.remember-option input:checked + .custom-checkbox::after {
  position: absolute; top: 2px; left: 5px; width: 4px; height: 8px;
  border: solid #fff; border-width: 0 2px 2px 0; content: ""; transform: rotate(45deg);
}

.login-button {
  display: flex; align-items: center; justify-content: center;
  width: 100%; height: 54px; margin-top: 26px; border: 0; border-radius: 8px;
  background: linear-gradient(100deg, #42a0f9 0%, #258cf1 100%);
  box-shadow: 0 10px 22px rgba(47,145,243,0.22); color: #fff;
  cursor: pointer; font-size: 17px; font-weight: 600; letter-spacing: 4px;
  transition: transform 0.2s ease, box-shadow 0.2s ease, filter 0.2s ease;
}
.login-button:hover:not(:disabled) {
  box-shadow: 0 14px 28px rgba(47,145,243,0.3); filter: brightness(1.03); transform: translateY(-2px);
}
.login-button:active:not(:disabled) { transform: translateY(0); }
.login-button:disabled { cursor: not-allowed; opacity: 0.72; }

.loading-icon {
  width: 17px; height: 17px; margin-right: 9px;
  border: 2px solid rgba(255,255,255,0.35); border-top-color: #fff; border-radius: 50%;
  animation: loading-rotate 0.75s linear infinite;
}
@keyframes loading-rotate { to { transform: rotate(360deg); } }

.security-tip { margin: 20px 0 0; color: #a0a8a3; font-size: 12px; line-height: 1.7; text-align: center; }

/* ===== 响应式 ===== */
@media (max-width: 1050px) {
  .login-content { grid-template-columns: minmax(330px, 1fr) 390px; gap: 45px; width: 90%; }
  .welcome-area { padding-left: 0; }
  .welcome-area h1 { font-size: 50px; }
  .welcome-description { font-size: 17px; letter-spacing: 3px; }
}

@media (max-width: 800px) {
  .brand { top: 24px; left: 24px; }
  .brand-logo { width: 50px; height: 50px; }
  .brand-text strong { font-size: 23px; }
  .login-content { display: flex; align-items: center; justify-content: center; width: 100%; padding: 115px 20px 90px; }
  .welcome-area { display: none; }
  .login-card { width: min(430px, 100%); }
  .wave { height: 33%; }
}

@media (max-width: 480px) {
  .brand { top: 20px; left: 20px; }
  .brand-logo { width: 45px; height: 45px; }
  .brand-text strong { font-size: 20px; }
  .brand-text span { font-size: 8px; }
  .login-content { padding-right: 16px; padding-left: 16px; }
  .login-card { padding: 34px 25px 28px; border-radius: 19px; }
  .card-header h2 { font-size: 22px; }
}

@media (prefers-reduced-motion: reduce) {
  .blur-blob, .floating-dot, .wave, .loading-icon { animation: none; }
}
</style>
