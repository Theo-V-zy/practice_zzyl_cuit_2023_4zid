<template>
  <section class="page-section">
    <el-card header="预缴款充值" style="max-width:500px">
      <el-form :model="form">
        <el-form-item label="老人"><el-select v-model="form.elderId" placeholder="选择老人" filterable style="width:100%"><el-option v-for="e in elders" :key="e.id" :label="e.name" :value="e.id"/></el-select></el-form-item>
        <el-form-item label="充值金额"><el-input-number v-model="form.amount" :precision="2" :step="100" :min="0" :max="99999" style="width:100%"/></el-form-item>
        <el-form-item><el-button type="primary" @click="submit" :loading="loading">确认充值</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card header="充值记录" style="max-width:500px;margin-top:20px">
      <el-table :data="records" empty-text="暂无记录">
        <el-table-column prop="bill_no" label="编号" width="170"/>
        <el-table-column prop="total_amount" label="金额" width="100"><template #default="{row}">¥{{row.total_amount}}</template></el-table-column>
        <el-table-column prop="create_time" label="时间" width="170"/>
      </el-table>
    </el-card>
  </section>
</template>
<script>
import {onMounted, reactive, ref} from 'vue'
import axios from 'axios'
import {ElMessage} from 'element-plus'
export default {
  setup(){
    const form=reactive({elderId:'',amount:0})
    const elders=ref([]);const records=ref([]);const loading=ref(false)
    const loadElders=async()=>{try{const r=await axios.get('/elderList');elders.value=r.data.data||[]}catch(e){}}
    const loadRecords=async()=>{try{const r=await axios.post('/billPage',{page:1,pageSize:50,status:'PAID'});records.value=(r.data.data||[]).filter(b=>b.bill_type==='PREPAY')}catch(e){}}
    const submit=async()=>{if(!form.elderId)return ElMessage.warning('请选择老人');loading.value=true;try{await axios.post('/prepay',form);ElMessage.success('充值成功');form.amount=0;loadRecords()}catch(e){ElMessage.error('充值失败')}finally{loading.value=false}}
    onMounted(()=>{loadElders();loadRecords()})
    return{form,elders,records,loading,submit}
  }
}
</script>
