<template>
  <view class="page">
    <view class="list-title">我的账单</view>
    <view v-if="list.length === 0" class="empty">暂无账单记录</view>
    <view v-for="item in list" :key="item.id" class="bill-card">
      <view class="card-left">
        <text class="bill-name">{{ item.feeName || '费用' }}</text>
        <text class="bill-no">{{ item.billNo || '-' }}</text>
        <text class="bill-month">{{ item.billMonth || '-' }}</text>
      </view>
      <view class="card-right">
        <text class="bill-amount">¥{{ formatPrice(item.totalAmount) }}</text>
        <text class="bill-paid">已付 ¥{{ formatPrice(item.paidAmount) }}</text>
        <text class="bill-status" :class="item.status === 'UNPAID' ? 'text-danger' : 'text-success'">
          {{ item.status === 'UNPAID' ? '未支付' : item.status === 'PAID' ? '已支付' : item.status }}
        </text>
      </view>
    </view>
  </view>
</template>

<script>
import { familyBills } from '../../api/request'

export default {
  data() { return { list: [] } },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      try { const res = await familyBills(); if (res?.data) this.list = res.data } catch (e) {}
    },
    formatPrice(v) { return v ? Number(v).toFixed(2) : '0.00' }
  }
}
</script>

<style scoped>
.page { padding: 24rpx; background: #f4f5f7; min-height: 100vh; }
.list-title { font-size: 32rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; }
.empty { padding: 120rpx 0; text-align: center; color: rgba(0,0,0,0.35); }
.bill-card { display: flex; justify-content: space-between; padding: 24rpx; margin-bottom: 16rpx; background: #fff; border-radius: 12rpx; border: 1px solid #e7e9ed; }
.card-left { display: flex; flex-direction: column; gap: 8rpx; }
.bill-name { font-size: 30rpx; font-weight: 600; color: #333; }
.bill-no { font-size: 24rpx; color: rgba(0,0,0,0.4); }
.bill-month { font-size: 24rpx; color: rgba(0,0,0,0.4); }
.card-right { display: flex; flex-direction: column; align-items: flex-end; gap: 6rpx; }
.bill-amount { font-size: 32rpx; font-weight: 600; color: #e34d59; }
.bill-paid { font-size: 24rpx; color: rgba(0,0,0,0.5); }
.bill-status { font-size: 24rpx; }
.text-danger { color: #e34d59; }
.text-success { color: #2ba471; }
</style>
