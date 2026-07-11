<template>
  <view class="home-page">
    <!-- 顶部banner -->
    <view class="banner">
      <image class="banner-bg" src="/static/banner-bg.png" mode="aspectFill" />
      <view class="banner-overlay">
        <text class="banner-title">{{ nursingHome.name }}</text>
        <text class="banner-subtitle">专业养老服务 · 温馨如家</text>
      </view>
    </view>

    <!-- 快捷入口 -->
    <view class="quick-actions">
      <view class="action-card" @tap="navigateTo('/pages/appointments/appointments')">
        <view class="action-icon icon-appointment"></view>
        <text class="action-label">预约参观</text>
        <text class="action-desc">在线预约探访</text>
      </view>
      <view class="action-card" @tap="navigateTo('/pages/contracts/contracts')">
        <view class="action-icon icon-contract"></view>
        <text class="action-label">我的合同</text>
        <text class="action-desc">查看合同信息</text>
      </view>
      <view class="action-card" @tap="navigateTo('/pages/orders/orders')">
        <view class="action-icon icon-order"></view>
        <text class="action-label">服务下单</text>
        <text class="action-desc">选择护理服务</text>
      </view>
    </view>

    <!-- 绑定老人 -->
    <view class="section" v-if="elders.length > 0">
      <view class="section-header">
        <text class="section-title">我的家人</text>
        <text class="section-count">{{ elders.length }}位老人</text>
      </view>
      <view class="elder-list">
        <view class="elder-card" v-for="elder in elders" :key="elder.id">
          <view class="elder-avatar">
            <text class="elder-avatar-text">{{ elder.name[0] }}</text>
          </view>
          <view class="elder-info">
            <text class="elder-name">{{ elder.name }}</text>
            <text class="elder-detail">编号: {{ elder.elderNo }} · {{ elder.relation }}</text>
            <text class="elder-status" :class="statusClass(elder.status)">
              {{ statusLabel(elder.status) }}
            </text>
          </view>
          <view class="elder-arrow">></view>
        </view>
      </view>
    </view>

    <!-- 养老院介绍 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">养老院介绍</text>
      </view>
      <view class="intro-card">
        <text class="intro-text">{{ nursingHome.introduction }}</text>
        <view class="intro-details">
          <view class="intro-row">
            <text class="intro-label">地址</text>
            <text class="intro-value">{{ nursingHome.address }}</text>
          </view>
          <view class="intro-row">
            <text class="intro-label">电话</text>
            <text class="intro-value phone">{{ nursingHome.phone }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-if="loading" class="loading-more">加载中...</view>
  </view>
</template>

<script>
import { request } from '../../api/request.js'

export default {
  data() {
    return {
      loading: true,
      nursingHome: {
        name: '中州养老',
        address: '河南省郑州市中州大道100号',
        phone: '0371-12345678',
        introduction: ''
      },
      elders: []
    }
  },
  onShow() {
    this.loadHomeData()
  },
  methods: {
    async loadHomeData() {
      this.loading = true
      try {
        const res = await request({
          url: '/family/home',
          method: 'GET'
        })
        if (res.code === 200) {
          this.nursingHome = res.nursingHome || this.nursingHome
          this.elders = res.elders || []
        }
      } catch (e) {
        // 如果后端不可用，显示默认数据
      }
      this.loading = false
    },
    navigateTo(url) {
      uni.navigateTo({ url })
    },
    statusLabel(status) {
      const map = {
        'IN': '在住',
        'WAIT_CHECKIN': '待入住',
        'LEAVE': '请假中',
        'CHECKED_OUT': '已退住'
      }
      return map[status] || status
    },
    statusClass(status) {
      if (status === 'IN') return 'status-in'
      if (status === 'LEAVE') return 'status-leave'
      return 'status-other'
    }
  }
}
</script>

<style scoped>
.home-page {
  min-height: 100vh;
  background: #f4f5f7;
  padding-bottom: 20rpx;
}

.banner {
  position: relative;
  width: 100%;
  height: 340rpx;
  overflow: hidden;
}

.banner-bg {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #0052d9, #4a90e2);
}

.banner-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: rgba(0, 0, 0, 0.15);
}

.banner-title {
  font-size: 48rpx;
  font-weight: bold;
  color: #fff;
}

.banner-subtitle {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.85);
  margin-top: 12rpx;
}

.quick-actions {
  display: flex;
  margin: -40rpx 24rpx 24rpx;
  position: relative;
  z-index: 2;
}

.action-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24rpx 12rpx;
  background: #fff;
  border-radius: 16rpx;
  margin: 0 8rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.06);
}

.action-icon {
  width: 64rpx;
  height: 64rpx;
  border-radius: 32rpx;
  margin-bottom: 12rpx;
}

.icon-appointment { background: #e8f5f2; }
.icon-contract { background: #eef4ff; }
.icon-order { background: #fff7e6; }

.action-label {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.action-desc {
  font-size: 22rpx;
  color: #999;
  margin-top: 6rpx;
}

.section {
  margin: 24rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: 700;
  color: #333;
}

.section-count {
  font-size: 24rpx;
  color: #999;
}

.elder-list {
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.elder-card {
  display: flex;
  align-items: center;
  padding: 24rpx;
  background: #fff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
}

.elder-avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 40rpx;
  background: #0052d9;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20rpx;
}

.elder-avatar-text {
  font-size: 36rpx;
  color: #fff;
  font-weight: 600;
}

.elder-info {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.elder-name {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.elder-detail {
  font-size: 24rpx;
  color: #999;
  margin-top: 4rpx;
}

.elder-status {
  font-size: 22rpx;
  margin-top: 4rpx;
}

.status-in { color: #67c23a; }
.status-leave { color: #e6a23c; }
.status-other { color: #999; }

.elder-arrow {
  font-size: 28rpx;
  color: #ccc;
}

.intro-card {
  padding: 24rpx;
  background: #fff;
  border-radius: 16rpx;
  box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.04);
}

.intro-text {
  font-size: 26rpx;
  color: #666;
  line-height: 42rpx;
}

.intro-details {
  margin-top: 20rpx;
  border-top: 1rpx solid #f0f0f0;
  padding-top: 20rpx;
}

.intro-row {
  display: flex;
  align-items: center;
  margin-bottom: 12rpx;
}

.intro-label {
  font-size: 24rpx;
  color: #999;
  width: 80rpx;
}

.intro-value {
  font-size: 26rpx;
  color: #333;
}

.phone {
  color: #0052d9;
}

.loading-more {
  text-align: center;
  padding: 32rpx;
  color: #999;
  font-size: 24rpx;
}
</style>
