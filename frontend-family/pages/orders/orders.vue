<template>
  <view class="page">
    <!-- Tab 筛选 -->
    <view class="tabs">
      <view class="tab-item" :class="{ active: activeTab === 'ALL' }" @tap="switchTab('ALL')">全部</view>
      <view class="tab-item" :class="{ active: activeTab === 'UNPAID' }" @tap="switchTab('UNPAID')">待支付</view>
      <view class="tab-item" :class="{ active: activeTab === 'SERVING' }" @tap="switchTab('SERVING')">待执行</view>
      <view class="tab-item" :class="{ active: activeTab === 'FINISHED' }" @tap="switchTab('FINISHED')">已完成</view>
    </view>

    <view v-if="filteredList.length === 0" class="empty">暂无订单</view>

    <view v-for="item in filteredList" :key="item.id" class="order-card" @tap="goDetail(item.id)">
      <view class="card-top">
        <image class="card-img" src="/static/default-service.png" mode="aspectFit" />
        <view class="card-info">
          <text class="card-title">{{ item.serviceName || '服务项目' }}</text>
          <text class="card-price">¥{{ formatPrice(item.totalAmount) }}</text>
        </view>
        <text class="card-status" :class="statusClass(item.orderStatus)">{{ statusText(item.orderStatus) }}</text>
      </view>
      <view class="card-bottom">
        <text>服务家人：{{ item.elderName || '-' }}</text>
        <text>期望服务时间：{{ item.serviceTime || '-' }}</text>
      </view>
      <view class="card-actions" @tap.stop>
        <!-- 待支付 -->
        <button v-if="item.orderStatus === 'CREATED'" class="btn primary" size="mini" @tap="goPay(item)">去支付</button>
        <button v-if="item.orderStatus === 'CREATED'" class="btn" size="mini" @tap="cancelOrder(item)">取消订单</button>
        <!-- 待执行 -->
        <button v-if="item.orderStatus === 'PAID' || item.orderStatus === 'SERVING'" class="btn warn" size="mini" @tap="refundOrder(item)">退款</button>
        <!-- 已退款 -->
        <text v-if="item.orderStatus === 'REFUNDED'" class="tag-done">已退款</text>
        <!-- 已关闭 -->
        <text v-if="item.orderStatus === 'CANCELED'" class="tag-done">已关闭</text>
        <!-- 已完成 -->
        <button v-if="item.orderStatus === 'FINISHED'" class="btn" size="mini" @tap="deleteOrder(item)">删除</button>
        <!-- 已执行(超3个月)→已完成 -->
        <text v-if="item.orderStatus === 'DONE'" class="tag-done">已完成</text>
      </view>
    </view>

    <!-- 取消/退款原因弹窗 -->
    <view v-if="showReason" class="modal-mask" @tap="showReason = false">
      <view class="modal-box" @tap.stop>
        <view class="modal-title">{{ reasonTitle }}</view>
        <view class="reason-list">
          <view v-for="r in reasons" :key="r" class="reason-item" :class="{ selected: selectedReason === r }" @tap="selectedReason = r">
            <text>{{ r }}</text>
            <text v-if="selectedReason === r" class="check">✓</text>
          </view>
        </view>
        <button class="btn primary" @tap="confirmReason">确认提交</button>
      </view>
    </view>
  </view>
</template>

<script>
import { familyOrders, payFamilyOrder, cancelFamilyOrder, refundFamilyOrder, deleteFamilyOrder } from '../../api/request'

export default {
  data() {
    return {
      activeTab: 'ALL',
      list: [],
      showReason: false,
      reasonTitle: '选择原因',
      selectedReason: '',
      currentItem: null,
      reasons: ['不需要此项服务了', '费用有点贵', '临时有事，不方便服务', '信息填写错误', '重复下单']
    }
  },
  computed: {
    filteredList() {
      if (this.activeTab === 'ALL') return this.list
      return this.list.filter(i => {
        if (this.activeTab === 'UNPAID') return i.orderStatus === 'CREATED'
        if (this.activeTab === 'SERVING') return i.orderStatus === 'PAID' || i.orderStatus === 'SERVING'
        if (this.activeTab === 'FINISHED') return i.orderStatus === 'FINISHED' || i.orderStatus === 'DONE'
        return true
      })
    }
  },
  onShow() { this.loadData() },
  methods: {
    async loadData() {
      try { const res = await familyOrders({ page: 1, pageSize: 50 }); if (res?.data) this.list = res.data } catch (e) {}
    },
    switchTab(tab) { this.activeTab = tab },
    goDetail(id) { uni.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` }) },
    goPay(item) { uni.showModal({ title: '模拟支付', content: `确认支付 ¥${this.formatPrice(item.totalAmount)}？`, success: async res => { if (!res.confirm) return; try { await payFamilyOrder(item.id); uni.showToast({ title: '支付成功', icon: 'success' }); this.loadData() } catch (e) {} } }) },
    cancelOrder(item) { this.currentItem = item; this.reasonTitle = '取消订单'; this.selectedReason = ''; this.showReason = true },
    refundOrder(item) { this.currentItem = item; this.reasonTitle = '申请退款'; this.selectedReason = ''; this.showReason = true },
    async confirmReason() {
      if (!this.selectedReason) { uni.showToast({ title: '请选择原因', icon: 'none' }); return }
      this.showReason = false
      try {
        if (this.reasonTitle === '取消订单') await cancelFamilyOrder(this.currentItem.id, this.selectedReason)
        else await refundFamilyOrder(this.currentItem.id, this.selectedReason)
        uni.showToast({ title: this.reasonTitle === '取消订单' ? '取消成功' : '退款已提交', icon: 'success' })
        this.loadData()
      } catch (e) {}
    },
    deleteOrder(item) {
      uni.showModal({ title: '提示', content: '确定删除此订单？', success: async res => { if (!res.confirm) return; try { await deleteFamilyOrder(item.id); uni.showToast({ title: '已删除', icon: 'success' }); this.loadData() } catch (e) {} } })
    },
    statusText(s) {
      const m = { CREATED: '待支付', PAID: '待执行', SERVING: '待执行', FINISHED: '已完成', DONE: '已完成', REFUNDED: '已退款', CANCELED: '已关闭' }
      return m[s] || s
    },
    statusClass(s) {
      if (s === 'CREATED') return 's-unpaid'
      if (s === 'PAID' || s === 'SERVING') return 's-serving'
      if (s === 'FINISHED' || s === 'DONE') return 's-done'
      return 's-other'
    },
    formatPrice(v) { return v ? Number(v).toFixed(2) : '0.00' }
  }
}
</script>

<style scoped>
.page { padding: 0; background: #f4f5f7; min-height: 100vh; }
.tabs { display: flex; background: #fff; padding: 0 24rpx; border-bottom: 1px solid #e7e9ed; }
.tab-item { flex: 1; text-align: center; padding: 24rpx 0; font-size: 28rpx; color: rgba(0,0,0,0.6); position: relative; }
.tab-item.active { color: #0052d9; font-weight: 600; }
.tab-item.active::after { content: ''; position: absolute; bottom: 0; left: 30%; right: 30%; height: 4rpx; background: #0052d9; border-radius: 2rpx; }
.empty { padding: 120rpx 0; text-align: center; color: rgba(0,0,0,0.35); }
.order-card { margin: 16rpx 24rpx; padding: 24rpx; background: #fff; border-radius: 12rpx; }
.card-top { display: flex; align-items: flex-start; gap: 16rpx; }
.card-img { width: 120rpx; height: 120rpx; border-radius: 8rpx; background: #f5f7fa; }
.card-info { flex: 1; }
.card-title { font-size: 30rpx; font-weight: 600; color: #333; display: block; }
.card-price { font-size: 32rpx; color: #e34d59; font-weight: 600; margin-top: 8rpx; display: block; }
.card-status { font-size: 24rpx; padding: 4rpx 12rpx; border-radius: 4rpx; }
.s-unpaid { color: #e37318; }
.s-serving { color: #0052d9; }
.s-done { color: #2ba471; }
.s-other { color: rgba(0,0,0,0.4); }
.card-bottom { margin-top: 16rpx; padding-top: 16rpx; border-top: 1px solid #f5f7fa; font-size: 26rpx; color: rgba(0,0,0,0.5); display: flex; flex-direction: column; gap: 4rpx; }
.card-actions { margin-top: 16rpx; display: flex; justify-content: flex-end; gap: 16rpx; }
.btn { font-size: 24rpx; padding: 8rpx 24rpx; border-radius: 32rpx; background: #f5f7fa; color: #333; border: 1px solid #e7e9ed; line-height: 1.5; }
.btn.primary { background: #0052d9; color: #fff; border-color: #0052d9; }
.btn.warn { background: #fff; color: #e34d59; border-color: #e34d59; }
.tag-done { font-size: 24rpx; color: rgba(0,0,0,0.35); padding: 8rpx 0; }
.modal-mask { position: fixed; inset: 0; background: rgba(0,0,0,0.5); display: flex; align-items: flex-end; z-index: 100; }
.modal-box { width: 100%; background: #fff; border-radius: 24rpx 24rpx 0 0; padding: 32rpx; }
.modal-title { font-size: 32rpx; font-weight: 600; text-align: center; margin-bottom: 24rpx; }
.reason-list { display: flex; flex-direction: column; gap: 0; }
.reason-item { padding: 28rpx 16rpx; font-size: 30rpx; border-bottom: 1px solid #f5f7fa; display: flex; justify-content: space-between; }
.reason-item .check { color: #0052d9; font-weight: 600; }
</style>
