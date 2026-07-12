<template>
  <view class="page">
    <view class="list-title">我的合同</view>
    <view v-if="list.length === 0" class="empty">
      <image class="empty-img" src="/static/empty-contract.png" mode="aspectFit" />
      <text>您暂时没有合同信息哦~</text>
    </view>
    <view v-for="item in list" :key="item.id" class="contract-card">
      <view class="card-header">
        <text class="card-name">{{ item.contractName || item.contractNo }}</text>
        <text class="card-status" :class="statusClass(item.status)">{{ statusText(item.status) }}</text>
      </view>
      <view class="card-body">
        <text class="card-row">家人：{{ item.elderName || '-' }}</text>
        <text class="card-row">编号：{{ item.contractNo }}</text>
        <text class="card-row">有效期：{{ item.startDate }}~{{ item.endDate }}</text>
        <text class="card-row">签约日期：{{ item.signDate || '-' }}</text>
      </view>
      <view class="card-footer">
        <button class="btn" size="mini" @tap="downloadContract(item)">下载</button>
        <button class="btn primary" size="mini" @tap="viewContract(item)">查看</button>
      </view>
    </view>
  </view>
</template>

<script>
import { familyContracts } from '../../api/request'

export default {
  data() { return { list: [] } },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      try { const res = await familyContracts(); if (res?.data) this.list = res.data } catch (e) {}
    },
    statusText(s) {
      const m = { ACTIVE: '生效中', PENDING: '未生效', EXPIRED: '已过期', CANCELLED: '已失效' }
      return m[s] || s
    },
    statusClass(s) {
      if (s === 'ACTIVE') return 's-active'
      if (s === 'PENDING') return 's-pending'
      return 's-expired'
    },
    downloadContract() { uni.showToast({ title: '电子合同下载暂未接入', icon: 'none' }) },
    viewContract(item) { uni.showModal({ title: item.contractNo, content: `${item.elderName || '家人'}\n${item.startDate} 至 ${item.endDate}\n月费 ¥${Number(item.monthlyFee || 0).toFixed(2)}`, showCancel: false }) }
  }
}
</script>

<style scoped>
.page { padding: 24rpx; background: #f4f5f7; min-height: 100vh; }
.list-title { font-size: 32rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; }
.empty { display: flex; flex-direction: column; align-items: center; padding: 120rpx 0; color: rgba(0,0,0,0.35); font-size: 28rpx; }
.empty-img { width: 200rpx; height: 200rpx; margin-bottom: 20rpx; opacity: 0.6; }
.contract-card { padding: 24rpx; margin-bottom: 16rpx; background: #fff; border-radius: 12rpx; border: 1px solid #e7e9ed; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16rpx; }
.card-name { font-size: 30rpx; font-weight: 600; color: #333; }
.card-status { font-size: 22rpx; padding: 6rpx 14rpx; border-radius: 4rpx; }
.s-active { background: #eef4ff; color: #0052d9; }
.s-pending { background: #fff7e6; color: #e37318; }
.s-expired { background: #f5f5f5; color: rgba(0,0,0,0.35); }
.card-body { display: flex; flex-direction: column; gap: 8rpx; }
.card-row { font-size: 26rpx; color: rgba(0,0,0,0.6); }
.card-footer { display: flex; justify-content: flex-end; gap: 16rpx; margin-top: 16rpx; }
.btn { font-size: 24rpx; padding: 8rpx 28rpx; border-radius: 32rpx; background: #f5f7fa; color: #333; border: 1px solid #e7e9ed; line-height: 1.5; }
.btn.primary { background: #0052d9; color: #fff; border-color: #0052d9; }
</style>
