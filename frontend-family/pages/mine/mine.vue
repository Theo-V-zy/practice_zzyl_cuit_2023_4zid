<template>
  <view class="page-shell mine-page">
    <view class="profile" @tap="go('/pages/profile/profile')">
      <image :src="user.avatar || '/static/default-avatar.png'" mode="aspectFill" class="avatar" />
      <view><text class="name">{{user.name||'家属用户'}}</text><text class="phone">{{maskedPhone}}</text></view>
      <text class="arrow">›</text>
    </view>
    <view class="menu panel">
      <view @tap="go('/pages/contracts/contracts')"><text class="menu-mark">合</text><text>我的合同</text><text class="arrow">›</text></view>
      <view @tap="go('/pages/appointments/appointments')"><text class="menu-mark">约</text><text>我的预约</text><text class="arrow">›</text></view>
      <view @tap="go('/pages/orders/orders')"><text class="menu-mark">单</text><text>我的订单</text><text class="arrow">›</text></view>
      <view @tap="go('/pages/bills/bills')"><text class="menu-mark">账</text><text>我的账单</text><text class="arrow">›</text></view>
    </view>
    <view class="menu panel"><view @tap="call"><text class="menu-mark">电</text><text>联系客服</text><text class="muted">0371-12345678</text></view></view>
    <button class="logout" @tap="logout">退出登录</button>
  </view>
</template>

<script>
import{familyProfile,familyLogout,clearSession}from'../../api/request'
export default{
  data(){return{user:{}}},computed:{maskedPhone(){const p=this.user.phone||'';return p.length===11?`${p.slice(0,3)}****${p.slice(7)}`:p}},onShow(){this.load()},
  methods:{
    async load(){try{const r=await familyProfile();this.user=r.data||{}}catch(e){}},
    go(url){uni.navigateTo({url})},
    call(){uni.makePhoneCall({phoneNumber:'0371-12345678'})},
    logout(){uni.showModal({title:'退出登录',content:'确定退出当前账号吗？',success:async r=>{if(!r.confirm)return;try{await familyLogout()}catch(e){}clearSession();uni.reLaunch({url:'/pages/login/login'})}})}
  }
}
</script>

<style scoped>
.mine-page{padding-top:24rpx}.profile{margin:0 24rpx;padding:34rpx 28rpx;background:#fff;border-radius:12rpx;display:flex;align-items:center}.avatar{width:106rpx;height:106rpx;border-radius:50%;background:#edf1f5}.profile>view{flex:1;margin-left:24rpx;display:flex;flex-direction:column;gap:10rpx}.name{font-size:34rpx;font-weight:600}.phone{color:#8f959e}.arrow{font-size:46rpx;color:#a1a6ad}.menu{padding:0 28rpx}.menu>view{min-height:96rpx;display:flex;align-items:center;border-bottom:1px solid #f0f1f2}.menu>view:last-child{border:0}.menu>view>text:nth-child(2){flex:1}.menu-mark{width:54rpx;height:54rpx;line-height:54rpx;margin-right:22rpx;text-align:center;border-radius:10rpx;background:#e8f1ff;color:#0052d9}.logout{margin:50rpx 24rpx;width:calc(100% - 48rpx);height:84rpx;line-height:84rpx;background:#fff;color:#d54941;border-radius:8rpx}
</style>
