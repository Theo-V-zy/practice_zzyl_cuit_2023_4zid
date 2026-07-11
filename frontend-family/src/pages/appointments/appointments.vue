<template>
  <view class="page">
    <view class="list-title">我的预约</view>
    <!-- Tab -->
    <view class="tabs">
      <view class="tab-item" :class="{ active: activeTab === 'ALL' }" @tap="switchTab('ALL')">全部</view>
      <view class="tab-item" :class="{ active: activeTab === 'VISIT' }" @tap="switchTab('VISIT')">参观预约</view>
      <view class="tab-item" :class="{ active: activeTab === 'INTERVIEW' }" @tap="switchTab('INTERVIEW')">探访预约</view>
    </view>

    <view v-if="filteredList.length === 0" class="empty">暂无预约记录</view>

    <view v-for="item in filteredList" :key="item.id" class="apt-card">
      <view class="apt-header">
        <text class="apt-date">日期：{{ item.appointmentDate || item.appointmentTime || '-' }}</text>
        <text class="apt-status" :class="statusClass(item.status)">{{ statusText(item.status) }}</text>
      </view>
      <view class="apt-body">
        <text class="apt-time">{{ item.appointmentTime || '-' }}</text>
        <text>预约人：{{ item.visitorName || '-' }}</text>
        <text>家人：{{ item.elderName || '-' }}</text>
        <text>电话：{{ item.visitorPhone || '-' }}</text>
      </view>
      <view class="apt-footer" v-if="item.status === 'PENDING'">
        <button class="btn" size="mini" @tap="cancelApt(item)">取消</button>
      </view>
    </view>

    <!-- 取消确认弹窗 -->
    <view v-if="showCancel" class="modal-mask" @tap="showCancel = false">
      <view class="modal-box" @tap.stop>
        <view class="modal-title">取消预约</view>
        <text class="modal-desc">{{ cancelMsg }}</text>
        <view class="modal-btns">
          <button class="btn" @tap="showCancel = false">再想想</button>
          <button class="btn primary" @tap="confirmCancel">继续</button>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { familyAppointments } from '../../api/request'

export default {
  data() {
    return {
      activeTab: 'ALL',
      list: [],
      cancelCount: 0,
      showCancel: false,
      currentItem: null
    }
  },
  computed: {
    filteredList() {
      if (this.activeTab === 'ALL') return this.list
      return this.list.filter(i => i.visitType === this.activeTab)
    },
    cancelMsg() {
      const left = 3 - this.cancelCount
      if (left <= 1) return `该账号今日已不可进行预约，是否还要继续？`
      return `取消${4 - left}次后，该用户账号今日不可进行预约。是否还要继续？`
    }
  },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      try { const res = await familyAppointments({ page: 1, pageSize: 50 }); if (res?.data) this.list = res.data } catch (e) {}
    },
    switchTab(tab) { this.activeTab = tab },
    statusText(s) {
      const m = { PENDING: '待上门', COMPLETED: '已完成', EXPIRED: '已过期', CANCELLED: '已取消' }
      return m[s] || s
    },
    statusClass(s) {
      if (s === 'PENDING') return 's-pending'
      if (s === 'COMPLETED') return 's-done'
      return 's-expired'
    },
    cancelApt(item) { this.currentItem = item; this.showCancel = true },
    confirmCancel() {
      this.cancelCount++
      this.showCancel = false
      uni.showToast({ title: '取消成功', icon: 'success' })
      this.loadData()
    }
  }
}
</script>

<style scoped>
.page { padding: 0; background: #f4f5f7; min-height: 100vh; }
.list-title { padding: 24rpx 24rpx 0; font-size: 32rpx; font-weight: 600; color: #333; }
.tabs { display: flex; background: #fff; padding: 0 24rpx; border-bottom: 1px solid #e7e9ed; margin-bottom: 16rpx; margin-top: 16rpx; }
.tab-item { flex: 1; text-align: center; padding: 24rpx 0; font-size: 28rpx; color: rgba(0,0,0,0.6); position: relative; }
.tab-item.active { color: #0052d9; font-weight: 600; }
.tab-item.active::after { content: ''; position: absolute; bottom: 0; left: 30%; right: 30%; height: 4rpx; background: #0052d9; border-radius: 2rpx; }
.empty { padding: 120rpx 0; text-align: center; color: rgba(0,0,0,0.35); }
.apt-card { margin: 0 24rpx 16rpx; padding: 24rpx; background: #fff; border-radius: 12rpx; }
.apt-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12rpx; }
.apt-date { font-size: 28rpx; font-weight: 600; color: #333; }
.apt-status { font-size: 22rpx; padding: 4rpx 12rpx; border-radius: 4rpx; }
.s-pending { background: #eef4ff; color: #0052d9; }
.s-done { background: #eefaf3; color: #2ba471; }
.s-expired { background: #f5f5f5; color: rgba(0,0,0,0.35); }
.apt-body { display: flex; flex-direction: column; gap: 6rpx; font-size: 26rpx; color: rgba(0,0,0,0.6); }
.apt-time { font-size: 36rpx; color: #333; font-weight: 600; }
.apt-footer { margin-top: 16rpx; display: flex; justify-content: flex-end; }
.btn { font-size: 24rpx; padding: 8rpx 28rpx; border-radius: 32rpx; background: #f5f7fa; color: #333; border: 1px solid #e7e9ed; line-height: 1.5; }
.btn.primary { background: #0052d9; color: #fff; border-color: #0052d9; }

.modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal-box { width: 560rpx; padding: 40rpx; background: #fff; border-radius: 16rpx; text-align: center; }
.modal-title { font-size: 32rpx; font-weight: 600; margin-bottom: 16rpx; }
.modal-desc { font-size: 26rpx; color: rgba(0,0,0,0.6); display: block; margin-bottom: 24rpx; line-height: 1.6; }
.modal-btns { display: flex; gap: 16rpx; }
.modal-btns .btn { flex: 1; height: 72rpx; line-height: 72rpx; }
</style>
