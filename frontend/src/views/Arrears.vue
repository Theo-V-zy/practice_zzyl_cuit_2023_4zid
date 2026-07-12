<template>
  <section class="page-section">
    <el-form :inline="true" :model="query">
      <el-form-item label="老人"><el-input v-model="query.elderName" placeholder="老人姓名"/></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="list" v-loading="loading" empty-text="暂无欠费记录">
      <el-table-column prop="bill_no" label="账单编号" width="170"/>
      <el-table-column prop="elder_name" label="老人" width="100"/>
      <el-table-column prop="bill_type" label="类型" width="90"/>
      <el-table-column prop="total_amount" label="账单金额" width="100"><template #default="{row}">¥{{row.total_amount}}</template></el-table-column>
      <el-table-column prop="paid_amount" label="已付" width="90"><template #default="{row}">¥{{row.paid_amount}}</template></el-table-column>
      <el-table-column prop="arrears" label="欠费" width="100"><template #default="{row}"><span style="color:#F56C6C;font-weight:bold">¥{{row.arrears||(row.total_amount-row.paid_amount)}}</span></template></el-table-column>
      <el-table-column prop="status" label="状态" width="90"><template #default="{row}"><el-tag type="danger">{{row.status}}</el-tag></template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" :page-size="query.pageSize" :page-sizes="[10,20,30,40]" layout="total,sizes,prev,pager,next" :total="total" @size-change="load" @current-change="load"/>
  </section>
</template>
<script>
import {onMounted, reactive, ref} from 'vue'
import axios from 'axios'
export default {
  setup(){
    const query=reactive({page:1,pageSize:10,elderName:''})
    const list=ref([]);const loading=ref(false);const total=ref(0)
    const load=async()=>{loading.value=true;try{const r=await axios.post('/arrearsPage',query);list.value=r.data.data;total.value=r.data.total}catch(e){}finally{loading.value=false}}
    onMounted(load)
    return{query,list,loading,total,load}
  }
}
</script>
