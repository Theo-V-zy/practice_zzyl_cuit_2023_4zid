<template>
  <view class="page-shell service-page">
    <view class="search-bar">
      <input v-model.trim="keyword" confirm-type="search" placeholder="请输入服务名称关键字" @confirm="loadData" />
      <text @tap="loadData">搜索</text>
    </view>
    <view v-if="!list.length" class="empty-state"><text>暂无可选服务</text></view>
    <view class="service-grid">
      <view v-for="(item,index) in list" :key="item.type+'-'+item.id" class="service-card" @tap="goDetail(item)">
        <view class="service-image" :class="`service-image-${index%3}`">
          <image src="/static/logo-mark.png" mode="aspectFit" />
          <view class="type-badge" :class="item.type==='plan'?'type-plan':'type-item'">
            {{ item.type==='plan'?'套餐':'单项' }}
          </view>
        </view>
        <text class="service-name">{{ item.name }}</text>
        <text class="service-desc">{{ item.description || '专业护理服务，让长者更舒适安心。' }}</text>
        <view class="service-bottom"><text class="price">¥{{ formatPrice(item.price) }}</text><text class="unit">/{{ item.unit }}</text></view>
      </view>
    </view>
  </view>
</template>

<script>
import { familyServices } from '../../api/request'
export default {
  data(){return{keyword:'',list:[]}},
  onShow(){this.loadData()},
  methods:{
    async loadData(){try{const res=await familyServices(this.keyword);this.list=res.data||[]}catch(e){}},
    goDetail(item){uni.navigateTo({url:`/pages/service-detail/service-detail?id=${item.id}&type=${item.type}`})},
    formatPrice(v){return Number(v||0).toFixed(2)}
  }
}
</script>

<style scoped>
.service-page{padding-top:18rpx}.search-bar{height:76rpx;margin:0 24rpx 20rpx;display:flex;align-items:center;gap:18rpx}.search-bar input{flex:1;height:76rpx;padding:0 26rpx;background:#fff;border:1px solid #cfd3da;border-radius:38rpx}.search-bar text{color:#0052d9}.service-grid{display:grid;grid-template-columns:1fr 1fr;gap:18rpx;padding:0 24rpx 30rpx}.service-card{min-width:0;padding:16rpx;background:#fff;border-radius:10rpx;position:relative}.service-image{height:210rpx;background:#edf2f7;display:flex;align-items:center;justify-content:center;position:relative}.service-image-1{background:#f1eee8}.service-image-2{background:#e9f2ed}.service-image image{width:110rpx;height:110rpx;opacity:.45}.type-badge{position:absolute;top:10rpx;right:10rpx;padding:4rpx 12rpx;border-radius:8rpx;font-size:20rpx;color:#fff}.type-item{background:#8bb7f0}.type-plan{background:#f0a060}.service-name{display:block;margin-top:16rpx;font-size:30rpx;font-weight:600}.service-desc{height:70rpx;display:-webkit-box;margin-top:8rpx;overflow:hidden;color:#6b717a;font-size:24rpx;line-height:1.45;-webkit-line-clamp:2;-webkit-box-orient:vertical}.service-bottom{margin-top:14rpx}.unit{color:#8f959e;font-size:22rpx}
</style>
