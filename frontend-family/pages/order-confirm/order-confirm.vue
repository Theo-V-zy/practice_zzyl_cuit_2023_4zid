<template>
  <view class="page-shell confirm-page">
    <view class="panel" v-if="service.id"><text class="service-name">{{service.name}}</text><text class="price">¥{{formatPrice(service.price)}} / {{service.unit}}</text></view>
    <view class="panel">
      <view class="form-row"><text class="form-label">服务家人</text><picker :range="elders" range-key="name" @change="elderIndex=$event.detail.value"><view class="form-input picker">{{elders[elderIndex] ? elders[elderIndex].name : '请选择'}}</view></picker></view>
      <view class="form-row"><text class="form-label">服务时间</text><picker mode="date" :value="date" @change="date=$event.detail.value"><view class="form-input picker">{{date}}</view></picker></view>
      <view class="form-row"><text class="form-label">时间段</text><picker :range="times" @change="timeIndex=$event.detail.value"><view class="form-input picker">{{times[timeIndex]}}</view></picker></view>
      <view class="form-row"><text class="form-label">数量</text><view class="stepper"><button @tap="quantity=Math.max(1,quantity-1)">−</button><text>{{quantity}}</text><button @tap="quantity++">+</button></view></view>
      <view class="form-row"><text class="form-label">备注</text><textarea v-model.trim="remark" class="remark" maxlength="200" placeholder="选填，请填写服务注意事项" /></view>
    </view>
    <view class="total"><text>合计</text><text class="price">¥{{total}}</text></view>
    <button class="primary-btn submit" :loading="loading" @tap="submit">提交订单</button>
  </view>
</template>

<script>
import { familyServiceDetail,familyElders,createFamilyOrder } from '../../api/request'
export default{
  data(){const d=new Date(Date.now()+86400000).toISOString().slice(0,10);return{service:{},elders:[],elderIndex:0,date:d,times:['09:00','10:30','14:00','16:00'],timeIndex:0,quantity:1,remark:'',loading:false}},
  computed:{total(){return (Number(this.service.price||0)*this.quantity).toFixed(2)}},
  async onLoad(o){try{const [s,e]=await Promise.all([familyServiceDetail(o.serviceId),familyElders()]);this.service=s.data||{};this.elders=e.data||[]}catch(e){}},
  methods:{formatPrice(v){return Number(v||0).toFixed(2)},async submit(){if(!this.elders.length)return uni.showModal({title:'提示',content:'请先绑定家人',confirmText:'去绑定',success:r=>r.confirm&&uni.navigateTo({url:'/pages/bind-family/bind-family'})});this.loading=true;try{const r=await createFamilyOrder({serviceId:this.service.id,elderId:this.elders[this.elderIndex].id,quantity:this.quantity,serviceTime:`${this.date} ${this.times[this.timeIndex]}:00`,remark:this.remark});uni.showToast({title:'下单成功',icon:'success'});setTimeout(()=>uni.redirectTo({url:`/pages/order-detail/order-detail?id=${r.data.id}`}),500)}catch(e){}finally{this.loading=false}}}
}
</script>

<style scoped>
.confirm-page{padding-bottom:170rpx}.service-name{display:block;font-size:32rpx;font-weight:600;margin-bottom:12rpx}.picker{line-height:84rpx}.stepper{display:flex;align-items:center;gap:28rpx}.stepper button{margin:0;width:64rpx;height:56rpx;line-height:52rpx;padding:0;background:#f3f4f6}.remark{width:100%;height:160rpx;padding:20rpx;background:#f7f8fa;border:1px solid #e5e6eb;border-radius:8rpx}.total{position:fixed;left:0;right:0;bottom:106rpx;height:70rpx;padding:0 30rpx;background:#fff;display:flex;align-items:center;justify-content:space-between}.submit{position:fixed;left:24rpx;right:24rpx;bottom:14rpx;width:calc(100% - 48rpx)}
</style>
