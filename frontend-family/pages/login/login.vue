<template>
  <view class="login-container">
    <view class="login-card">
      <view class="login-logo-area">
        <image class="login-logo" src="/static/logo.png" mode="aspectFit" />
        <text class="login-title">中州养老</text>
        <text class="login-subtitle">家属端</text>
      </view>

      <view class="login-form">
        <view class="form-item">
          <input class="form-input" v-model="account" placeholder="请输入账号" />
        </view>
        <view class="form-item">
          <input class="form-input" v-model="password" type="password" placeholder="请输入密码" />
        </view>
        <button class="login-btn" :loading="loading" @tap="handleLogin">
          {{ loading ? '登录中...' : '登 录' }}
        </button>
      </view>

      <text class="login-tip">默认测试账号：family001 / 123456</text>
    </view>
  </view>
</template>

<script>
import { familyLogin } from '../../api/request'

export default {
  data() {
    return {
      account: '',
      password: '',
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      if (!this.account) {
        uni.showToast({ title: '请输入账号', icon: 'none' })
        return
      }
      if (!this.password) {
        uni.showToast({ title: '请输入密码', icon: 'none' })
        return
      }
      this.loading = true
      try {
        const res = await familyLogin({ account: this.account, password: this.password })
        if (res.code === 200 && res.data) {
          getApp().globalData.token = 'true'
          getApp().globalData.userInfo = res.data
          uni.showToast({ title: '登录成功', icon: 'success' })
          uni.switchTab({ url: '/pages/home/home' })
        }
      } catch (e) { /* error handled by request */ }
      finally { this.loading = false }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #0052d9, #3370ff);
  padding: 40rpx;
}
.login-card {
  width: 100%;
  max-width: 600rpx;
  padding: 60rpx 50rpx;
  background: #fff;
  border-radius: 20rpx;
  box-shadow: 0 16rpx 64rpx rgba(0,0,0,0.12);
}
.login-logo-area {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 60rpx;
}
.login-logo { width: 100rpx; height: 100rpx; margin-bottom: 20rpx; }
.login-title { font-size: 40rpx; font-weight: 600; color: #333; }
.login-subtitle { font-size: 26rpx; color: rgba(0,0,0,0.45); margin-top: 8rpx; }
.login-form { margin-top: 20rpx; }
.form-item { margin-bottom: 24rpx; }
.form-input {
  width: 100%;
  height: 88rpx;
  padding: 0 24rpx;
  border: 1px solid #e7e9ed;
  border-radius: 12rpx;
  font-size: 30rpx;
  background: #f5f7fa;
  box-sizing: border-box;
}
.login-btn {
  width: 100%;
  height: 88rpx;
  line-height: 88rpx;
  margin-top: 20rpx;
  background: #0052d9;
  color: #fff;
  border-radius: 12rpx;
  font-size: 32rpx;
  letter-spacing: 8rpx;
  border: none;
}
.login-tip {
  display: block;
  margin-top: 40rpx;
  text-align: center;
  color: rgba(0,0,0,0.3);
  font-size: 24rpx;
}
</style>
