<template>
  <view class="page-shell" v-if="elder.id">
    <view class="person panel">
      <image :src="elder.avatar || '/static/default-avatar.png'" mode="aspectFill" class="avatar" />
      <view><text class="name">{{ elder.name }}</text><text class="relation">{{ elder.relation }}</text><text class="muted">床位 {{ bedText }}</text></view>
    </view>
    <view class="health-grid">
      <view v-for="item in elder.health" :key="item.name" class="health-card">
        <view class="health-head"><text>{{ item.name }}</text><text class="normal">{{ item.status }}</text></view>
        <text class="health-value">{{ item.value }}</text><text class="health-time">{{ item.time }}</text>
      </view>
    </view>
    <view class="panel knowledge"><text class="section-title">健康知识</text><text>规律监测血压、心率、血氧和体温，有助于及时了解长者身体变化。如数据持续异常，请及时联系养老院护理人员。</text></view>
  </view>
</template>

<script>
import { familyElderDetail } from '../../api/request'
export default {
  data(){return{elder:{health:[]}}},
  computed:{bedText(){return [this.elder.building,this.elder.floor,this.elder.roomNo,this.elder.bedNo].filter(Boolean).join(' ')||'暂未分配'}},
  onLoad(options){this.load(options.id)},
  methods:{async load(id){try{const res=await familyElderDetail(id);this.elder=res.data||{health:[]}}catch(e){}}}
}
</script>

<style scoped>
.person { display:flex;align-items:center;gap:22rpx; }
.avatar { width:100rpx;height:100rpx;border-radius:8rpx;background:#eef1f5; }
.person>view { display:flex;flex-direction:column;gap:8rpx; }
.name { font-size:32rpx;font-weight:600; }.relation{position:absolute;margin-left:110rpx;margin-top:2rpx;color:#d54941;border:1px solid #d54941;padding:2rpx 10rpx;font-size:22rpx;}
.health-grid{display:grid;grid-template-columns:1fr 1fr;gap:16rpx;margin:20rpx 24rpx;}.health-card{padding:24rpx;background:#fff;border:1px solid #dfe2e6;border-radius:8rpx;}.health-head{display:flex;justify-content:space-between}.normal{color:#00a870;font-size:22rpx}.health-value{display:block;margin-top:24rpx;font-size:34rpx;font-weight:600}.health-time{display:block;margin-top:12rpx;color:#8f959e;font-size:22rpx}.knowledge{line-height:1.7}.knowledge .section-title{display:block;margin-bottom:16rpx}
</style>
