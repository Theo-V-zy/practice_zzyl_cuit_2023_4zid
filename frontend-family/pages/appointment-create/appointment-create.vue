<template>
  <view class="page-shell appointment-page">
    <view class="type-tabs"><view :class="{active:form.visitType==='VISIT'}" @tap="form.visitType='VISIT'">探访预约</view><view :class="{active:form.visitType==='INTERVIEW'}" @tap="form.visitType='INTERVIEW'">参观预约</view></view>
    <view class="panel">
      <view v-if="form.visitType==='VISIT'" class="form-row"><text class="form-label">探访家人</text><picker :range="elders" range-key="name" @change="elderIndex=$event.detail.value"><view class="form-input picker">{{elders[elderIndex]?elders[elderIndex].name:'请选择家人'}}</view></picker></view>
      <view class="form-row"><text class="form-label">预约日期</text><picker mode="date" :value="date" :start="minDate" @change="date=$event.detail.value"><view class="form-input picker">{{date}}</view></picker></view>
      <view class="form-row"><text class="form-label">预约时间</text><picker :range="times" @change="timeIndex=$event.detail.value"><view class="form-input picker">{{times[timeIndex]}}</view></picker></view>
      <view class="form-row"><text class="form-label">预约人</text><input class="form-input" v-model.trim="form.visitorName" /></view>
      <view class="form-row"><text class="form-label">联系电话</text><input class="form-input" v-model.trim="form.visitorPhone" type="number" maxlength="11" /></view>
      <view class="form-row"><text class="form-label">备注</text><textarea class="remark" v-model.trim="form.remark" maxlength="200" placeholder="选填" /></view>
      <button class="primary-btn" :loading="loading" @tap="submit">提交预约</button>
    </view>
  </view>
</template>

<script>
import { familyElders,familyProfile,createFamilyAppointment } from '../../api/request'
export default{
  data(){const d=new Date(Date.now()+86400000).toISOString().slice(0,10);return{minDate:new Date().toISOString().slice(0,10),date:d,times:['09:00','10:00','14:00','15:30'],timeIndex:0,elders:[],elderIndex:0,loading:false,form:{visitType:'VISIT',visitorName:'',visitorPhone:'',remark:''}}},
  async onLoad(o){this.form.visitType=o.type||'VISIT';try{const [e,p]=await Promise.all([familyElders(),familyProfile()]);this.elders=e.data||[];this.form.visitorName=p.data.name||'';this.form.visitorPhone=p.data.phone||''}catch(e){}},
  methods:{async submit(){if(this.form.visitType==='VISIT'&&!this.elders.length)return uni.showModal({title:'提示',content:'探访预约需要先绑定家人',confirmText:'去绑定',success:r=>r.confirm&&uni.navigateTo({url:'/pages/bind-family/bind-family'})});if(!this.form.visitorName||!/^1\d{10}$/.test(this.form.visitorPhone))return uni.showToast({title:'请填写正确的预约人和手机号',icon:'none'});this.loading=true;try{await createFamilyAppointment({...this.form,elderId:this.form.visitType==='VISIT'?this.elders[this.elderIndex].id:null,appointmentTime:`${this.date} ${this.times[this.timeIndex]}:00`});uni.showToast({title:'预约成功',icon:'success'});setTimeout(()=>uni.redirectTo({url:'/pages/appointments/appointments'}),500)}catch(e){}finally{this.loading=false}}}
}
</script>

<style scoped>
.appointment-page{padding-top:18rpx}.type-tabs{display:flex;margin:0 24rpx;background:#fff;border-radius:10rpx;overflow:hidden}.type-tabs view{flex:1;padding:24rpx;text-align:center}.type-tabs .active{color:#0052d9;border-bottom:4rpx solid #0052d9}.picker{line-height:84rpx}.remark{width:100%;height:150rpx;padding:20rpx;background:#f7f8fa;border:1px solid #e5e6eb;border-radius:8rpx}
</style>
