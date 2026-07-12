<template>
  <view class="page-shell detail-page" v-if="detail.id">
    <view class="detail-image"><image src="/static/logo-mark.png" mode="aspectFit" /></view>
    <view class="panel service-info"><text class="service-name">{{detail.name}}</text><view><text class="price">¥{{formatPrice(detail.price)}}</text><text class="unit">/{{detail.unit}}</text></view><text class="description">{{detail.description||'专业护理人员提供规范、温和的照护服务。'}}</text></view>
    <view class="panel"><text class="section-title">服务说明</text><text class="description">服务前护理人员将与您确认时间和长者身体情况。服务过程会记录执行信息，您可在我的订单中查看进度。</text></view>
    <view class="bottom"><button class="primary-btn" @tap="order">立即下单</button></view>
  </view>
</template>

<script>
import { familyServiceDetail } from '../../api/request'
export default{data(){return{detail:{}}},onLoad(o){this.load(o.id)},methods:{async load(id){try{const r=await familyServiceDetail(id);this.detail=r.data||{}}catch(e){}},formatPrice(v){return Number(v||0).toFixed(2)},order(){uni.navigateTo({url:`/pages/order-confirm/order-confirm?serviceId=${this.detail.id}`})}}}
</script>

<style scoped>
.detail-page{padding-bottom:120rpx}.detail-image{height:420rpx;background:#e9f0f7;display:flex;align-items:center;justify-content:center}.detail-image image{width:180rpx;height:180rpx;opacity:.5}.service-info{display:flex;flex-direction:column;gap:18rpx}.service-name{font-size:36rpx;font-weight:600}.unit{color:#8f959e}.description{display:block;margin-top:18rpx;color:#5f6670;line-height:1.7}.bottom{position:fixed;left:0;right:0;bottom:0;padding:14rpx 24rpx;background:#fff;border-top:1px solid #eee}.bottom .primary-btn{width:100%}
</style>
