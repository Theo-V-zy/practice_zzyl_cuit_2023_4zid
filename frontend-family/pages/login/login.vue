<template>
  <view class="login-page">
    <view class="brand"><image src="/static/logo.png" mode="aspectFit" /><text class="brand-name">中州养老</text><text class="brand-sub">家属端</text></view>
    <view class="login-form">
      <view class="input-row"><text>账号</text><input v-model.trim="form.account" placeholder="请输入账号" /></view>
      <view class="input-row"><text>密码</text><input v-model="form.password" type="password" placeholder="请输入密码" confirm-type="done" @confirm="login" /></view>
      <button class="login-btn" :loading="loading" @tap="login">登录</button>
      <text class="demo">测试账号 family001 / 123456</text>
    </view>
  </view>
</template>

<script>
import { familyLogin,setSession,getToken } from '../../api/request'
export default{
  data(){return{form:{account:'family001',password:'123456'},loading:false}},
  onLoad(){if(getToken())uni.switchTab({url:'/pages/home/home'})},
  methods:{async login(){if(!this.form.account||!this.form.password)return uni.showToast({title:'请输入账号和密码',icon:'none'});this.loading=true;try{const r=await familyLogin(this.form);setSession(r.data);uni.showToast({title:'登录成功',icon:'success'});setTimeout(()=>uni.switchTab({url:'/pages/home/home'}),400)}catch(e){}finally{this.loading=false}}}
}
</script>

<style scoped>
.login-page{min-height:100vh;padding:180rpx 56rpx 60rpx;background:#f5f5f5}.brand{display:flex;flex-direction:column;align-items:center}.brand image{width:150rpx;height:120rpx}.brand-name{margin-top:12rpx;font-size:44rpx;font-weight:600}.brand-sub{margin-top:8rpx;color:#8f959e}.login-form{margin-top:80rpx}.input-row{height:96rpx;display:flex;align-items:center;border-bottom:1px solid #d8dbe2}.input-row text{width:100rpx;color:#4e5969}.input-row input{flex:1;height:96rpx}.login-btn{margin-top:56rpx;height:88rpx;line-height:88rpx;background:#0052d9;color:#fff;border-radius:8rpx}.demo{display:block;margin-top:30rpx;text-align:center;color:#a1a6ad;font-size:24rpx}
</style>
