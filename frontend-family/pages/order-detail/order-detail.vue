<template>
  <view class="detail-page">
    <view class="list-title">订单详情</view>
    <view v-if="detail" class="detail-card">
      <view class="card-row"><text class="label">订单编号</text><text>{{ detail.orderNo || '-' }}</text></view>
      <view class="card-row"><text class="label">服务项目</text><text>{{ detail.serviceName || '-' }}</text></view>
      <view class="card-row"><text class="label">数量</text><text>{{ detail.quantity || 1 }}</text></view>
      <view class="card-row"><text class="label">总金额</text><text class="price">¥{{ detail.totalAmount || 0 }}</text></view>
      <view class="card-row"><text class="label">实付金额</text><text class="price">¥{{ detail.payAmount || 0 }}</text></view>
      <view class="card-row"><text class="label">状态</text><text>{{ detail.orderStatus || '-' }}</text></view>
      <view class="card-row"><text class="label">创建时间</text><text>{{ detail.createTime || '-' }}</text></view>
    </view>
    <view v-else class="empty-hint">加载中...</view>
  </view>
</template>

<script>
import { familyOrderDetail } from '../../api/request'

export default {
  data() { return { detail: null } },
  onLoad(options) {
    if (options.id) this.loadDetail(options.id)
  },
  methods: {
    async loadDetail(id) {
      try { const res = await familyOrderDetail(id); if (res?.data) this.detail = res.data } catch (e) {}
    }
  }
}
</script>

<style scoped>
.detail-page { padding: 24rpx; min-height: 100vh; background: #f4f5f7; }
.list-title { font-size: 32rpx; font-weight: 600; color: #333; margin-bottom: 20rpx; }
.empty-hint { padding: 120rpx 0; text-align: center; color: rgba(0,0,0,0.35); font-size: 28rpx; }
.detail-card { padding: 28rpx; background: #fff; border-radius: 12rpx; border: 1px solid #e7e9ed; }
.card-row { display: flex; justify-content: space-between; padding: 12rpx 0; font-size: 28rpx; border-bottom: 1px solid #f5f7fa; }
.card-row:last-child { border-bottom: 0; }
.label { color: rgba(0,0,0,0.5); }
.price { color: #0052d9; font-weight: 600; }
</style>
