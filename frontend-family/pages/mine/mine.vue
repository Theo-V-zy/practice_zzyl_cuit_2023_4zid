<template>
  <view class="mine-page">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <image class="user-avatar" :src="userInfo.avatar || '/static/default-avatar.png'" mode="aspectFill" />
      <view class="user-info">
        <text class="user-name">{{ userInfo.name || '家属用户' }}</text>
        <text class="user-phone">{{ userInfo.phone || '-' }}</text>
      </view>
    </view>

    <!-- 菜单入口 -->
    <view class="menu-grid">
      <view class="menu-item" @tap="navigateTo('/pages/contracts/contracts')">
        <text class="menu-icon">📋</text>
        <text class="menu-label">我的合同</text>
      </view>
      <view class="menu-item" @tap="navigateTo('/pages/appointments/appointments')">
        <text class="menu-icon">📅</text>
        <text class="menu-label">我的预约</text>
      </view>
      <view class="menu-item" @tap="navigateTo('/pages/orders/orders')">
        <text class="menu-icon">📦</text>
        <text class="menu-label">我的订单</text>
      </view>
      <view class="menu-item" @tap="navigateTo('/pages/bills/bills')">
        <text class="menu-icon">💰</text>
        <text class="menu-label">我的账单</text>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-area">
      <button class="logout-btn" @tap="handleLogout">退出登录</button>
    </view>
  </view>
</template>

<script>
import { familyProfile, familyLogout } from '../../api/request'

export default {
  data() {
    return {
      userInfo: {}
    }
  },
  onShow() {
    this.loadUserInfo()
  },
  methods: {
    async loadUserInfo() {
      try {
        const res = await familyProfile()
        if (res && res.name) this.userInfo = res
      } catch (e) { /* ignore */ }
    },
    navigateTo(url) {
      uni.navigateTo({ url })
    },
    handleLogout() {
      uni.showModal({
        title: '提示',
        content: '确定退出登录吗？',
        success(res) {
          if (res.confirm) {
            familyLogout().finally(() => {
              getApp().globalData.token = ''
              getApp().globalData.userInfo = null
              uni.showToast({ title: '已退出', icon: 'success' })
              uni.reLaunch({ url: '/pages/login/login' })
            })
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.mine-page { padding: 24rpx; min-height: 100vh; background: #f4f5f7; }
.user-card {
  display: flex;
  align-items: center;
  padding: 36rpx 28rpx;
  background: linear-gradient(135deg, #0052d9, #3370ff);
  border-radius: 20rpx;
  color: #fff;
}
.user-avatar { width: 100rpx; height: 100rpx; border-radius: 50%; border: 4rpx solid rgba(255,255,255,0.4); }
.user-info { margin-left: 24rpx; }
.user-name { font-size: 36rpx; font-weight: 600; }
.user-phone { font-size: 26rpx; margin-top: 8rpx; opacity: 0.85; }
.menu-grid { display: flex; flex-wrap: wrap; margin-top: 32rpx; }
.menu-item {
  width: calc(50% - 12rpx);
  margin: 0 24rpx 24rpx 0;
  padding: 40rpx 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: #fff;
  border-radius: 16rpx;
  border: 1px solid #e7e9ed;
}
.menu-item:nth-child(2n) { margin-right: 0; }
.menu-icon { font-size: 48rpx; margin-bottom: 12rpx; }
.menu-label { font-size: 28rpx; color: #333; }
.logout-area { position: fixed; bottom: 60rpx; left: 24rpx; right: 24rpx; }
.logout-btn { width: 100%; height: 88rpx; line-height: 88rpx; background: #fff; color: #e34d59; border-radius: 12rpx; font-size: 30rpx; border: 1px solid #e7e9ed; }
</style>
