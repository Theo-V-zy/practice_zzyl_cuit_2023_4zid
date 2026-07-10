<template>
  <view class="list-page">
    <view class="list-title">我的账单</view>
    <view v-if="list.length === 0" class="empty-hint">暂无账单记录</view>
    <view v-for="item in list" :key="item.id" class="list-card">
      <view class="card-row"><text class="label">账单编号</text><text>{{ item.billNo || '-' }}</text></view>
      <view class="card-row"><text class="label">费用名称</text><text>{{ item.feeName || '-' }}</text></view>
      <view class="card-row"><text class="label">总金额</text><text class="price">¥{{ item.totalAmount || 0 }}</text></view>
      <view class="card-row"><text class="label">已付</text><text>¥{{ item.paidAmount || 0 }}</text></view>
      <view class="card-row">
        <text class="label">状态</text>
        <text :class="item.status === 'UNPAID' ? 'text-danger' : 'text-success'">
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
      try { const res = await familyBills({ page: 1, pageSize: 50 }); if (res?.data) this.list = res.data } catch (e) {}
    }
  }
}
</script>

<style scoped>
.list-page { padding: 24rpx; min-height: 100vh; background: #f4f5f7; }
.list-title { font-size: 32rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; }
.empty-hint { padding: 120rpx 0; text-align: center; color: rgba(0,0,0,0.35); font-size: 28rpx; }
.list-card { padding: 24rpx; margin-bottom: 16rpx; background: #fff; border-radius: 12rpx; border: 1px solid #e7e9ed; }
.card-row { display: flex; justify-content: space-between; padding: 8rpx 0; font-size: 28rpx; }
.label { color: rgba(0,0,0,0.5); }
.price { color: #0052d9; font-weight: 600; }
.text-danger { color: #e34d59; }
.text-success { color: #2ba471; }
</style>
