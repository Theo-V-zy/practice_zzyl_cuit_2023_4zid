<template>
  <view class="page-shell bind-page">
    <view class="panel intro"><text class="section-title">绑定入住家人</text><text class="muted">请输入养老院提供的老人编号，并核对姓名。</text></view>
    <view class="panel">
      <view class="form-row"><text class="form-label">老人编号</text><input class="form-input" v-model.trim="form.elderNo" placeholder="例如 ELD202607001" /></view>
      <view class="form-row"><text class="form-label">老人姓名</text><input class="form-input" v-model.trim="form.name" placeholder="请输入真实姓名" /></view>
      <view class="form-row"><text class="form-label">与老人关系</text><picker :range="relations" @change="form.relation=relations[$event.detail.value]"><view class="form-input picker">{{ form.relation }}</view></picker></view>
      <button class="primary-btn" :loading="loading" @tap="submit">确认绑定</button>
    </view>
  </view>
</template>

<script>
import { bindFamilyElder } from '../../api/request'
export default {
  data() { return { relations: ['子女','配偶','孙辈','兄弟姐妹','其他'], form: { elderNo:'',name:'',relation:'子女' }, loading:false } },
  methods: {
    async submit() {
      if (!this.form.elderNo || !this.form.name) return uni.showToast({ title:'请填写老人编号和姓名',icon:'none' })
      this.loading=true
      try { await bindFamilyElder(this.form); uni.showToast({ title:'绑定成功',icon:'success' }); setTimeout(()=>uni.navigateBack(),500) } catch(e) {}
      finally { this.loading=false }
    }
  }
}
</script>

<style scoped>
.bind-page { padding-top: 4rpx; }
.intro { display:flex;flex-direction:column;gap:12rpx; }
.picker { line-height:84rpx;color:#1f2329; }
</style>
