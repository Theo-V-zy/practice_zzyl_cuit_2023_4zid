<template>
  <view class="page-shell detail-page" v-if="detail.id">
    <view class="detail-image">
      <image src="/static/logo-mark.png" mode="aspectFit" />
      <view class="detail-type-tag" :class="detail.type==='plan'?'tag-plan':'tag-item'">
        {{ detail.type==='plan'?'护理套餐':'单项服务' }}
      </view>
    </view>

    <!-- 套餐包含项目 -->
    <view class="panel" v-if="detail.type==='plan' && detail.items && detail.items.length">
      <text class="section-title">套餐包含项目</text>
      <view class="plan-items">
        <view v-for="it in detail.items" :key="it.id" class="plan-item-row">
          <view class="plan-item-left">
            <text class="plan-item-name">{{ it.itemname }}</text>
            <text class="plan-item-desc">{{ it.description || '' }}</text>
          </view>
          <view class="plan-item-right">
            <text class="plan-item-price">¥{{ Number(it.price||0).toFixed(2) }}</text>
            <text class="plan-item-unit">{{ it.hlpc > 1 ? it.hlpc + '次/' : '' }}{{ it.unit }}</text>
          </view>
        </view>
      </view>
    </view>

    <view class="panel service-info">
      <text class="service-name">{{ detail.name }}</text>
      <view class="price-row">
        <text class="price">¥{{ formatPrice(detail.price) }}</text>
        <text class="unit">/{{ detail.unit }}</text>
      </view>
      <text class="description">{{ detail.description || '专业护理人员提供规范、温和的照护服务。' }}</text>
    </view>

    <view class="panel" v-if="detail.type==='plan'">
      <text class="section-title">套餐说明</text>
      <text class="description">本套餐包含以上护理项目，购买后护理人员将按计划频次为长者提供服务。服务过程会记录执行信息，您可在我的订单中查看进度。</text>
    </view>
    <view class="panel" v-else>
      <text class="section-title">服务说明</text>
      <text class="description">服务前护理人员将与您确认时间和长者身体情况。服务过程会记录执行信息，您可在我的订单中查看进度。</text>
    </view>

    <view class="bottom"><button class="primary-btn" @tap="order">立即下单</button></view>
  </view>
</template>

<script>
import { familyServiceDetail } from '../../api/request'
export default {
  data(){return{detail:{}}},
  onLoad(o){this.load(o.id)},
  methods:{
    async load(id){
      try{
        const r = await familyServiceDetail(id);
        this.detail = r.data || {}
      }catch(e){}
    },
    formatPrice(v){return Number(v||0).toFixed(2)},
    order(){
      uni.navigateTo({url:`/pages/order-confirm/order-confirm?serviceId=${this.detail.id}`})
    }
  }
}
</script>

<style scoped>
.detail-page{padding-bottom:120rpx}.detail-image{height:420rpx;background:#e9f0f7;display:flex;align-items:center;justify-content:center;position:relative}.detail-image image{width:180rpx;height:180rpx;opacity:.5}.detail-type-tag{position:absolute;top:20rpx;right:20rpx;padding:6rpx 16rpx;border-radius:8rpx;font-size:22rpx;color:#fff}.tag-item{background:#8bb7f0}.tag-plan{background:#f0a060}.service-info{display:flex;flex-direction:column;gap:18rpx}.service-name{font-size:36rpx;font-weight:600}.price-row{display:flex;align-items:baseline;gap:6rpx}.unit{color:#8f959e}.description{display:block;margin-top:18rpx;color:#5f6670;line-height:1.7}.section-title{font-size:30rpx;font-weight:600;margin-bottom:16rpx}.plan-items{display:flex;flex-direction:column;gap:14rpx}.plan-item-row{display:flex;justify-content:space-between;align-items:center;padding:16rpx;background:#f7f8fa;border-radius:8rpx}.plan-item-left{flex:1;min-width:0}.plan-item-name{font-size:28rpx;font-weight:500;display:block}.plan-item-desc{font-size:22rpx;color:#8f959e;display:block;margin-top:4rpx;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}.plan-item-right{text-align:right;margin-left:16rpx}.plan-item-price{font-size:28rpx;color:#f56c6c;font-weight:600;display:block}.plan-item-unit{font-size:20rpx;color:#8f959e;display:block}.bottom{position:fixed;left:0;right:0;bottom:0;padding:14rpx 24rpx;background:#fff;border-top:1px solid #eee}.bottom .primary-btn{width:100%}.panel{background:#fff;margin:18rpx 24rpx;padding:24rpx;border-radius:10rpx}
</style>
