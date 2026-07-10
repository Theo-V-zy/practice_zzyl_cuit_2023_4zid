<template>
  <view class="list-page">
    <view class="list-title">我的订单</view>
    <view v-if="list.length === 0" class="empty-hint">暂无订单记录</view>
    <view v-for="item in list" :key="item.id" class="list-card" @tap="goDetail(item.id)">
      <view class="card-row"><text class="label">订单编号</text><text>{{ item.orderNo || '-' }}</text></view>
      <view class="card-row"><text class="label">服务项目</text><text>{{ item.serviceName || '-' }}</text></view>
      <view class="card-row"><text class="label">金额</text><text class="price">¥{{ item.totalAmount || 0 }}</text></view>
      <view class="card-row"><text class="label">状态</text><text>{{ statusMap[item.orderStatus] || item.orderStatus }}</text></view>
      <view class="card-arrow">></view>
    </view>
  </view>
</template>

<script>
import { familyOrders } from '../../api/request'

export default {
  data() {
    return {
      list: [],
      statusMap: { CREATED: '已创建', PAID: '已支付', SERVING: '服务中', FINISHED: '已完成', CANCELED: '已取消', REFUNDED: '已退款' }
    }
  },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      try { const res = await familyOrders({ page: 1, pageSize: 50 }); if (res?.data) this.list = res.data } catch (e) {}
    },
    goDetail(id) { uni.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` }) }
  }
}
</script>

<style scoped>
.list-page { padding: 24rpx; min-height: 100vh; background: #f4f5f7; }
.list-title { font-size: 32rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; }
.empty-hint { padding: 120rpx 0; text-align: center; color: rgba(0,0,0,0.35); font-size: 28rpx; }
.list-card { padding: 24rpx; margin-bottom: 16rpx; background: #fff; border-radius: 12rpx; border: 1px solid #e7e9ed; position: relative; }
.card-row { display: flex; justify-content: space-between; padding: 8rpx 0; font-size: 28rpx; }
.label { color: rgba(0,0,0,0.5); }
.price { color: #0052d9; font-weight: 600; }
.card-arrow { position: absolute; right: 24rpx; top: 50%; transform: translateY(-50%); color: rgba(0,0,0,0.25); font-size: 32rpx; }
</style>
