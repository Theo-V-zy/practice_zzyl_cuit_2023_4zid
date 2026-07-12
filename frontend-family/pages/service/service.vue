<template>
  <view class="page-shell service-page">
    <view class="search-bar">
      <input v-model.trim="keyword" confirm-type="search" placeholder="请输入服务名称关键字" @confirm="loadData" />
      <text @tap="loadData">搜索</text>
    </view>
    <view v-if="!list.length" class="empty-state"><text>暂无可选服务</text></view>
    <view class="service-grid">
      <view v-for="(item,index) in list" :key="item.id" class="service-card" @tap="goDetail(item.id)">
        <view class="service-image" :class="`service-image-${index%3}`"><image src="/static/logo-mark.png" mode="aspectFit" /></view>
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
    goDetail(id){uni.navigateTo({url:`/pages/service-detail/service-detail?id=${id}`})},
    formatPrice(v){return Number(v||0).toFixed(2)}
  }
}
</script>

<style scoped>
.service-page{padding-top:18rpx}.search-bar{height:76rpx;margin:0 24rpx 20rpx;display:flex;align-items:center;gap:18rpx}.search-bar input{flex:1;height:76rpx;padding:0 26rpx;background:#fff;border:1px solid #cfd3da;border-radius:38rpx}.search-bar text{color:#0052d9}.service-grid{display:grid;grid-template-columns:1fr 1fr;gap:18rpx;padding:0 24rpx 30rpx}.service-card{min-width:0;padding:16rpx;background:#fff;border-radius:10rpx}.service-image{height:210rpx;background:#edf2f7;display:flex;align-items:center;justify-content:center}.service-image-1{background:#f1eee8}.service-image-2{background:#e9f2ed}.service-image image{width:110rpx;height:110rpx;opacity:.45}.service-name{display:block;margin-top:16rpx;font-size:30rpx;font-weight:600}.service-desc{height:70rpx;display:-webkit-box;margin-top:8rpx;overflow:hidden;color:#6b717a;font-size:24rpx;line-height:1.45;-webkit-line-clamp:2;-webkit-box-orient:vertical}.service-bottom{margin-top:14rpx}.unit{color:#8f959e;font-size:22rpx}
</style>
