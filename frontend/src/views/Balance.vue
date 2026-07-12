<template>
  <section class="page-section">
    <el-form :inline="true"><el-form-item label="老人"><el-select v-model="elderId" placeholder="选择老人" filterable @change="load" style="width:240px"><el-option v-for="e in elders" :key="e.id" :label="e.name" :value="e.id"/></el-select></el-form-item></el-form>
    <el-row :gutter="20" v-if="data">
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center;color:#909399">预缴款</div><div style="text-align:center;font-size:28px;font-weight:bold;color:#409EFF">¥{{data.prepay}}</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center;color:#909399">已消费</div><div style="text-align:center;font-size:28px;font-weight:bold;color:#E6A23C">¥{{data.charged}}</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center;color:#909399">已支付</div><div style="text-align:center;font-size:28px;font-weight:bold;color:#67C23A">¥{{data.paid}}</div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center;color:#909399">当前余额</div><div style="text-align:center;font-size:28px;font-weight:bold" :style="{color:data.balance>=0?'#67C23A':'#F56C6C'}">¥{{data.balance}}</div></el-card></el-col>
    </el-row>
    <el-empty v-else description="请选择老人查看余额" style="margin-top:60px"/>
  </section>
</template>
<script>
import {onMounted, ref} from 'vue'
import axios from 'axios'
export default {
  setup(){
    const elderId=ref('');const elders=ref([]);const data=ref(null)
    const load=async()=>{if(!elderId.value)return;try{const r=await axios.get('/balance/'+elderId.value);data.value=r.data.data}catch(e){}}
    onMounted(async()=>{try{const r=await axios.get('/elderList');elders.value=r.data.data||[]}catch(e){}})
    return{elderId,elders,data,load}
  }
}
</script>
