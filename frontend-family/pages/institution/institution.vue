<template>
  <view class="page-shell institution-page">
    <view class="institution-hero"><image src="/static/logo.png" mode="aspectFit" /><text>中州养老</text></view>
    <view class="panel"><text class="section-title">机构介绍</text><text class="copy">中州养老是一家集生活照料、健康管理、康复护理和文化娱乐于一体的综合养老机构。我们坚持以长者为中心，为每一位老人提供有温度、有尊严的照护服务。</text></view>
    <view class="panel details"><view><text class="label">机构类别</text><text>护理型养老机构</text></view><view><text class="label">床位总数</text><text>100张</text></view><view><text class="label">服务电话</text><text class="status-blue" @tap="call">0371-12345678</text></view><view><text class="label">机构地址</text><text>河南省郑州市中州大道100号</text></view></view>
    <button class="primary-btn appointment" @tap="appointment">预约参观</button>
  </view>
</template>

<script>export default{methods:{call(){uni.makePhoneCall({phoneNumber:'0371-12345678'})},appointment(){uni.navigateTo({url:'/pages/appointment-create/appointment-create?type=INTERVIEW'})}}}</script>
<style scoped>.institution-hero{height:320rpx;background:#dce9ff;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:12rpx}.institution-hero image{width:170rpx;height:130rpx}.institution-hero text{font-size:38rpx;font-weight:600}.copy{display:block;margin-top:20rpx;line-height:1.8;color:#5f6670}.details view{display:flex;padding:20rpx 0;border-bottom:1px solid #f0f1f2}.details view:last-child{border:0}.label{width:180rpx;color:#8f959e}.appointment{margin:30rpx 24rpx;width:calc(100% - 48rpx)}</style>
