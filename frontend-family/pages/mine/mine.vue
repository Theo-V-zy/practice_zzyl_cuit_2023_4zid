<template>
  <view class="page">
    <!-- 用户信息卡片 -->
    <view class="user-card">
      <image class="avatar" :src="userInfo.avatar || '/static/default-avatar.png'" mode="aspectFill" />
      <view class="user-text">
        <text class="name">{{ userInfo.name || '家属用户' }}</text>
        <text class="phone">{{ userInfo.phone || '-' }}</text>
      </view>
      <text class="edit-btn" @tap="editProfile">编辑资料</text>
    </view>

    <!-- 我的家人 -->
    <view class="section">
      <view class="section-title">我的家人</view>
      <scroll-view scroll-x class="family-scroll" v-if="elders.length > 0">
        <view v-for="e in elders" :key="e.id" class="elder-item">
          <image class="elder-avatar" :src="e.avatar || '/static/default-avatar.png'" mode="aspectFill" />
          <text class="elder-name">{{ e.name }}</text>
          <text class="elder-relation">{{ e.relation }}</text>
        </view>
      </scroll-view>
      <text v-else class="no-data">暂无绑定家人</text>
    </view>

    <!-- 功能入口 -->
    <view class="section">
      <view class="section-title">常用功能</view>
      <view class="menu-grid">
        <view class="menu-item" @tap="navigateTo('/pages/contracts/contracts')">
          <view class="menu-icon-box icon-contracts">
            <text class="menu-emoji">📋</text>
          </view>
          <text class="menu-label">我的合同</text>
        </view>
        <view class="menu-item" @tap="navigateTo('/pages/appointments/appointments')">
          <view class="menu-icon-box icon-appointments">
            <text class="menu-emoji">📅</text>
          </view>
          <text class="menu-label">我的预约</text>
        </view>
        <view class="menu-item" @tap="navigateTo('/pages/orders/orders')">
          <view class="menu-icon-box icon-orders">
            <text class="menu-emoji">📦</text>
          </view>
          <text class="menu-label">我的订单</text>
        </view>
        <view class="menu-item" @tap="navigateTo('/pages/bills/bills')">
          <view class="menu-icon-box icon-bills">
            <text class="menu-emoji">💰</text>
          </view>
          <text class="menu-label">我的账单</text>
        </view>
      </view>
    </view>

    <!-- 退出登录 -->
    <view class="logout-area">
      <button class="logout-btn" @tap="handleLogout">退出登录</button>
    </view>
  </view>
</template>

<script>
import { familyProfile, familyLogout, familyMine } from '../../api/request'

export default {
  data() {
    return {
      userInfo: {},
      elders: []
    }
  },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      try {
        const res = await familyProfile()
        if (res && res.name) this.userInfo = res
      } catch (e) {}
      try {
        const res = await familyMine()
        if (res?.data) {
          if (res.data.elders) this.elders = res.data.elders
        }
      } catch (e) {}
    },
    navigateTo(url) { uni.navigateTo({ url }) },
    editProfile() { uni.showToast({ title: '编辑资料', icon: 'none' }) },
    handleLogout() {
      uni.showModal({
        title: '提示', content: '确定退出登录吗？',
        success: (res) => {
          if (res.confirm) {
            familyLogout().finally(() => {
              getApp().globalData.token = ''
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
.page { padding: 24rpx; background: #f4f5f7; min-height: 100vh; padding-bottom: 100rpx; }

.user-card { display: flex; align-items: center; padding: 32rpx 24rpx; background: linear-gradient(135deg, #0052d9, #3370ff); border-radius: 16rpx; color: #fff; }
.avatar { width: 90rpx; height: 90rpx; border-radius: 50%; border: 3rpx solid rgba(255,255,255,0.4); }
.user-text { flex: 1; margin-left: 20rpx; }
.name { font-size: 34rpx; font-weight: 600; display: block; }
.phone { font-size: 26rpx; opacity: 0.85; margin-top: 4rpx; display: block; }
.edit-btn { font-size: 24rpx; padding: 8rpx 20rpx; border: 1px solid rgba(255,255,255,0.5); border-radius: 24rpx; }

.section { margin-top: 28rpx; }
.section-title { font-size: 30rpx; font-weight: 600; color: #333; margin-bottom: 16rpx; }
.no-data { font-size: 26rpx; color: rgba(0,0,0,0.35); }

.family-scroll { white-space: nowrap; }
.elder-item { display: inline-flex; flex-direction: column; align-items: center; margin-right: 28rpx; }
.elder-avatar { width: 80rpx; height: 80rpx; border-radius: 50%; background: #f5f7fa; }
.elder-name { font-size: 26rpx; color: #333; margin-top: 8rpx; }
.elder-relation { font-size: 22rpx; color: rgba(0,0,0,0.4); }

.menu-grid { display: flex; flex-wrap: wrap; }
.menu-item { width: 25%; display: flex; flex-direction: column; align-items: center; padding: 20rpx 0; }
.menu-icon-box { width: 88rpx; height: 88rpx; border-radius: 20rpx; display: flex; align-items: center; justify-content: center; margin-bottom: 8rpx; }
.icon-contracts { background: #eef4ff; }
.icon-appointments { background: #fef0e6; }
.icon-orders { background: #eefaf3; }
.icon-bills { background: #fff7e6; }
.menu-emoji { font-size: 40rpx; }
.menu-label { font-size: 24rpx; color: #333; }

.logout-area { margin-top: 60rpx; }
.logout-btn { width: 100%; height: 88rpx; line-height: 88rpx; background: #fff; color: #e34d59; border-radius: 12rpx; font-size: 30rpx; border: 1px solid #e7e9ed; }
</style>
