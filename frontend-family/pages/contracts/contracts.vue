<template>
  <view class="list-page">
    <view class="list-title">我的合同</view>
    <view v-if="list.length === 0" class="empty-hint">暂无合同记录</view>
    <view v-for="item in list" :key="item.id" class="list-card">
      <view class="card-row"><text class="label">合同编号</text><text>{{ item.contractNo || '-' }}</text></view>
      <view class="card-row"><text class="label">老人</text><text>{{ item.elderName || '-' }}</text></view>
      <view class="card-row"><text class="label">月费</text><text class="price">¥{{ item.monthlyFee || 0 }}</text></view>
      <view class="card-row"><text class="label">状态</text><text :class="item.status === 'ACTIVE' ? 'text-success' : ''">{{ item.status || '-' }}</text></view>
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
      try { const res = await familyContracts({ page: 1, pageSize: 50 }); if (res?.data) this.list = res.data } catch (e) {}
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
.text-success { color: #2ba471; }
</style>
