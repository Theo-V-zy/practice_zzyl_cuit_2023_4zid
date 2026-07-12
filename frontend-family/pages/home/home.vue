<template>
  <view class="page-shell home-page">
    <view class="hero">
      <image src="/static/logo.png" mode="aspectFit" class="hero-logo" />
      <text class="hero-copy">把温暖送给父母，让情爱陪伴家人</text>
    </view>

    <view class="consult panel">
      <image src="/static/logo-mark.png" mode="aspectFit" class="consult-icon" />
      <view class="consult-copy">
        <text class="consult-title">入住咨询</text>
        <text class="muted">贴心服务 欢迎咨询</text>
      </view>
      <button class="small-btn" @tap="callInstitution">立即咨询</button>
    </view>

    <view class="appointment-row">
      <view class="appointment-card" @tap="goAppointment('VISIT')">
        <text class="card-title">探访预约</text>
        <text class="muted">老人家属预约拜访</text>
      </view>
      <view class="appointment-card" @tap="goAppointment('INTERVIEW')">
        <text class="card-title">参观预约</text>
        <text class="muted">预约参观养老院</text>
      </view>
    </view>

    <view class="quick-grid panel">
      <view class="quick-item" @tap="go('/pages/institution/institution')"><text class="quick-mark">院</text><text>养老院介绍</text></view>
      <view class="quick-item" @tap="go('/pages/bind-family/bind-family')"><text class="quick-mark">家</text><text>绑定家人</text></view>
      <view class="quick-item" @tap="switchTab('/pages/service/service')"><text class="quick-mark">服</text><text>我的服务</text></view>
      <view class="quick-item" @tap="go('/pages/bills/bills')"><text class="quick-mark">账</text><text>我的账单</text></view>
    </view>

    <view class="room-section">
      <view class="section-head"><text class="section-title">热门房型</text><text class="muted">舒适安心</text></view>
      <view v-for="(room,index) in roomTypes" :key="room.name" class="room-card">
        <view class="room-photo" :class="`room-photo-${index % 3}`">
          <image src="/static/logo-mark.png" mode="aspectFit" />
        </view>
        <text class="room-name">{{ room.name }}</text>
        <text class="room-desc">{{ room.description }}</text>
        <text class="room-price">¥{{ room.price || 0 }} 起/月</text>
      </view>
    </view>
  </view>
</template>

<script>
import { familyHome, getToken } from '../../api/request'

export default {
  data() { return { roomTypes: [] } },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      if (!getToken()) return
      try {
        const res = await familyHome()
        this.roomTypes = res.roomTypes || []
      } catch (e) {}
    },
    requireLogin() {
      if (getToken()) return true
      uni.navigateTo({ url: '/pages/login/login' })
      return false
    },
    go(url) { if (this.requireLogin()) uni.navigateTo({ url }) },
    switchTab(url) { if (this.requireLogin()) uni.switchTab({ url }) },
    goAppointment(type) {
      if (this.requireLogin()) uni.navigateTo({ url: `/pages/appointment-create/appointment-create?type=${type}` })
    },
    callInstitution() {
      uni.showModal({ title: '入住咨询', content: '0371-12345678', confirmText: '呼叫', success: res => {
        if (res.confirm) uni.makePhoneCall({ phoneNumber: '0371-12345678' })
      } })
    }
  }
}
</script>

<style scoped>
.home-page { padding-bottom: 28rpx; }
.hero { height: 250rpx; margin: 0 24rpx; background: #dce9ff; display: flex; flex-direction: column; align-items: center; justify-content: center; }
.hero-logo { width: 150rpx; height: 110rpx; }
.hero-copy { margin-top: 12rpx; font-size: 28rpx; color: #344563; }
.consult { display: flex; align-items: center; margin-top: 20rpx; }
.consult-icon { width: 72rpx; height: 72rpx; margin-right: 20rpx; }
.consult-copy { flex: 1; display: flex; flex-direction: column; gap: 8rpx; }
.consult-title,.card-title { font-size: 30rpx; font-weight: 600; }
.small-btn { margin: 0; padding: 0 24rpx; height: 64rpx; line-height: 64rpx; background: #fff; border: 1px solid #d8dbe2; border-radius: 6rpx; font-size: 26rpx; }
.appointment-row { display: flex; gap: 18rpx; margin: 0 24rpx; }
.appointment-card { flex: 1; min-height: 120rpx; padding: 24rpx; background: #fff; border-radius: 12rpx; display: flex; flex-direction: column; gap: 10rpx; }
.quick-grid { display: grid; grid-template-columns: repeat(4,1fr); padding: 24rpx 12rpx; }
.quick-item { display: flex; flex-direction: column; align-items: center; gap: 12rpx; font-size: 24rpx; }
.quick-mark { width: 64rpx; height: 64rpx; line-height: 64rpx; text-align: center; background: #e8f1ff; color: #0052d9; border-radius: 50%; font-weight: 600; }
.room-section { margin: 30rpx 24rpx; }
.section-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20rpx; }
.room-card { margin-bottom: 20rpx; padding: 20rpx; background: #fff; border-radius: 12rpx; }
.room-photo { height: 270rpx; display: flex; align-items: center; justify-content: center; background: #eef3f7; }
.room-photo-1 { background: #e9f2ed; }
.room-photo-2 { background: #f2eee7; }
.room-photo image { width: 150rpx; height: 150rpx; opacity: .42; }
.room-name { display: block; margin-top: 18rpx; font-size: 30rpx; font-weight: 600; }
.room-desc { display: block; margin-top: 10rpx; color: #6b717a; line-height: 1.6; }
.room-price { display: block; margin-top: 12rpx; color: #d54941; }
</style>
