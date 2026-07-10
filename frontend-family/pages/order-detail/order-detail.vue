<template>
  <view class="page">
    <!-- 状态头部 -->
    <view class="status-header" :class="statusHeaderClass">
      <text class="status-title">{{ statusText }}</text>
      <text class="status-desc">{{ statusDesc }}</text>
      <text v-if="detail.orderStatus === 'CREATED'" class="countdown">请在{{ countdown }}内完成支付，超时将取消订单</text>
    </view>

    <!-- 服务信息 -->
    <view class="section">
      <view class="section-title">服务信息</view>
      <view class="info-card">
        <image class="service-img" :src="detail.image || '/static/default-service.png'" mode="aspectFill" />
        <view class="service-info">
          <text class="service-name">{{ detail.serviceName || '-' }}</text>
          <text class="service-price">¥{{ formatPrice(detail.totalAmount) }}</text>
          <text class="service-label">实付款</text>
        </view>
      </view>
      <view class="info-row"><text class="lbl">备注</text><text>{{ detail.remark || '无' }}</text></view>
      <view class="info-row"><text class="lbl">期望服务时间</text><text>{{ detail.serviceTime || '-' }}</text></view>
      <view class="info-row"><text class="lbl">服务家人</text><text>{{ detail.elderName || '-' }}</text></view>
      <view class="info-row"><text class="lbl">订单编号</text><text>{{ detail.orderNo || '-' }}</text></view>
      <view class="info-row"><text class="lbl">下单时间</text><text>{{ detail.createTime || '-' }}</text></view>
    </view>

    <!-- 退款信息（如有） -->
    <view v-if="detail.refundReason" class="section">
      <view class="section-title">退款信息</view>
      <view class="info-row"><text class="lbl">退款原因</text><text>{{ detail.refundReason }}</text></view>
      <view class="info-row"><text class="lbl">申请时间</text><text>{{ detail.refundTime || '-' }}</text></view>
      <view class="info-row"><text class="lbl">退款金额</text><text class="price">¥{{ formatPrice(detail.refundAmount) }}</text></view>
    </view>

    <!-- 执行记录（已执行/已完成） -->
    <view v-if="detail.executor" class="section">
      <view class="section-title">执行记录</view>
      <view class="info-row"><text class="lbl">执行人</text><text>{{ detail.executor }}</text></view>
      <view class="info-row"><text class="lbl">执行时间</text><text>{{ detail.executeTime || '-' }}</text></view>
      <scroll-view scroll-x class="exec-images" v-if="detail.execImages">
        <image v-for="(img, i) in detail.execImages" :key="i" :src="img" mode="aspectFill" class="exec-img" @tap="previewImage(img)" />
      </scroll-view>
    </view>

    <!-- 养老院介绍 -->
    <view class="section">
      <view class="section-title">养老院介绍</view>
      <view class="nursing-info">
        <image class="nursing-img" :src="detail.nursingImage || '/static/default-place.png'" mode="aspectFill" @tap="previewImage(detail.nursingImage)" />
        <text class="nursing-name">中州养老</text>
        <text class="nursing-addr">北京市昌平区西三旗街道138号</text>
        <view class="nursing-tags">
          <text>机构类别：护理型养老机构</text>
          <text>占地面积(m²)：15000</text>
          <text>床位总数：100</text>
          <text>建立日期：2022.09.01</text>
        </view>
        <button class="contact-btn" size="mini">致电咨询</button>
      </view>
    </view>

    <!-- 底部操作 -->
    <view class="bottom-actions" v-if="detail.orderStatus === 'CREATED'">
      <button class="btn outline" @tap="cancelOrder">取消订单</button>
      <button class="btn primary" @tap="goPay">去支付</button>
    </view>
    <view class="bottom-actions" v-if="detail.orderStatus === 'PAID' || detail.orderStatus === 'SERVING'">
      <button class="btn outline" @tap="refundOrder">退款</button>
    </view>
    <view class="bottom-actions" v-if="detail.orderStatus === 'FINISHED' || detail.orderStatus === 'DONE'">
      <button class="btn outline" @tap="contactService">联系客服</button>
    </view>
  </view>
</template>

<script>
import { familyOrderDetail } from '../../api/request'

export default {
  data() {
    return {
      detail: {},
      countdown: '15:00',
      timer: null
    }
  },
  computed: {
    statusText() {
      const m = { CREATED: '订单待支付', PAID: '订单待服务', SERVING: '订单待服务', FINISHED: '订单已完成', DONE: '订单已完成', REFUNDED: '订单已退款', CANCELED: '订单已关闭' }
      return m[this.detail.orderStatus] || '订单详情'
    },
    statusDesc() {
      const m = { CREATED: '', PAID: '中州工作人员将竭诚为您服务', SERVING: '中州工作人员将竭诚为您服务', FINISHED: '', DONE: '', REFUNDED: '', CANCELED: '' }
      return m[this.detail.orderStatus] || ''
    },
    statusHeaderClass() {
      if (this.detail.orderStatus === 'CREATED') return 'h-unpaid'
      if (this.detail.orderStatus === 'PAID' || this.detail.orderStatus === 'SERVING') return 'h-serving'
      return 'h-done'
    }
  },
  onLoad(options) {
    if (options.id) this.loadDetail(options.id)
  },
  onUnload() { if (this.timer) clearInterval(this.timer) },
  methods: {
    async loadDetail(id) {
      try { const res = await familyOrderDetail(id); if (res?.data) this.detail = res.data } catch (e) {}
    },
    goPay() { uni.showToast({ title: '跳转支付', icon: 'none' }) },
    cancelOrder() { uni.showToast({ title: '取消成功', icon: 'success' }) },
    refundOrder() { uni.showToast({ title: '退款申请已提交', icon: 'success' }) },
    contactService() { uni.showToast({ title: '请联系客服', icon: 'none' }) },
    previewImage(url) { if (url) uni.previewImage({ urls: [url] }) },
    formatPrice(v) { return v ? Number(v).toFixed(2) : '0.00' }
  }
}
</script>

<style scoped>
.page { background: #f4f5f7; min-height: 100vh; padding-bottom: 100rpx; }
.status-header { padding: 48rpx 32rpx; text-align: center; color: #fff; }
.h-unpaid { background: linear-gradient(135deg, #e37318, #f0a050); }
.h-serving { background: linear-gradient(135deg, #0052d9, #3370ff); }
.h-done { background: linear-gradient(135deg, #2ba471, #5dbe8a); }
.status-title { font-size: 36rpx; font-weight: 600; display: block; }
.status-desc { font-size: 26rpx; margin-top: 8rpx; display: block; opacity: 0.85; }
.countdown { font-size: 24rpx; margin-top: 16rpx; display: block; opacity: 0.9; }

.section { margin: 16rpx 24rpx; padding: 24rpx; background: #fff; border-radius: 12rpx; }
.section-title { font-size: 30rpx; font-weight: 600; color: #333; margin-bottom: 16rpx; }
.info-card { display: flex; gap: 16rpx; margin-bottom: 16rpx; }
.service-img { width: 160rpx; height: 160rpx; border-radius: 8rpx; background: #f5f7fa; }
.service-info { flex: 1; }
.service-name { font-size: 30rpx; font-weight: 600; display: block; }
.service-price { font-size: 36rpx; color: #e34d59; font-weight: 600; display: block; margin-top: 12rpx; }
.service-label { font-size: 24rpx; color: rgba(0,0,0,0.4); }

.info-row { display: flex; justify-content: space-between; padding: 12rpx 0; font-size: 26rpx; border-bottom: 1px solid #f5f7fa; }
.lbl { color: rgba(0,0,0,0.5); }
.price { color: #e34d59; font-weight: 600; }

.exec-images { white-space: nowrap; margin-top: 12rpx; }
.exec-img { width: 160rpx; height: 160rpx; border-radius: 8rpx; margin-right: 12rpx; display: inline-block; background: #f5f7fa; }

.nursing-info { display: flex; flex-direction: column; gap: 12rpx; }
.nursing-img { width: 100%; height: 320rpx; border-radius: 8rpx; background: #f5f7fa; }
.nursing-name { font-size: 30rpx; font-weight: 600; }
.nursing-addr { font-size: 24rpx; color: rgba(0,0,0,0.5); }
.nursing-tags { font-size: 24rpx; color: rgba(0,0,0,0.5); }
.nursing-tags text { display: block; padding: 4rpx 0; }
.contact-btn { margin-top: 12rpx; font-size: 26rpx; background: #f5f7fa; border: 1px solid #e7e9ed; border-radius: 32rpx; }

.bottom-actions { position: fixed; bottom: 0; left: 0; right: 0; padding: 16rpx 32rpx; background: #fff; border-top: 1px solid #e7e9ed; display: flex; gap: 16rpx; }
.btn { flex: 1; height: 80rpx; line-height: 80rpx; border-radius: 40rpx; font-size: 30rpx; text-align: center; }
.btn.primary { background: #0052d9; color: #fff; border: none; }
.btn.outline { background: #fff; color: #333; border: 1px solid #e7e9ed; }
</style>
