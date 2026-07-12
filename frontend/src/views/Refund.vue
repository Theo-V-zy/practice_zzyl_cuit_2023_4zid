<template>
  <section class="page-section">
    <el-table :data="list" v-loading="loading" empty-text="暂无退款记录">
      <el-table-column prop="order_no" label="订单编号" width="160"/>
      <el-table-column prop="service_name" label="服务项目" width="130"/>
      <el-table-column prop="pay_amount" label="支付金额" width="100"><template #default="{row}">¥{{row.pay_amount}}</template></el-table-column>
      <el-table-column prop="refund_amount" label="退款金额" width="100"><template #default="{row}">¥{{row.refund_amount||0}}</template></el-table-column>
      <el-table-column prop="refund_reason" label="退款原因" min-width="180"/>
      <el-table-column prop="order_status" label="状态" width="90"><template #default="{row}"><el-tag :type="row.order_status==='REFUNDED'?'info':''">{{row.order_status}}</el-tag></template></el-table-column>
      <el-table-column label="操作" width="120"><template #default="{row}">
        <el-button size="small" type="danger" @click="doRefund(row)" v-if="row.order_status==='PAID'||row.order_status==='SERVING'">退款</el-button>
      </template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.page" :page-size="page.pageSize" :page-sizes="[10,20,30,40]" layout="total,sizes,prev,pager,next" :total="total" @size-change="load" @current-change="load"/>
  </section>
</template>
<script>
import {onMounted, reactive, ref} from 'vue'
import axios from 'axios'
import {ElMessage,ElMessageBox} from 'element-plus'
export default {
  setup(){
    const page=reactive({page:1,pageSize:10})
    const list=ref([]);const loading=ref(false);const total=ref(0)
    const load=async()=>{loading.value=true;try{const r=await axios.post('/orderPage',{page:page.page,pageSize:page.pageSize,status:'REFUNDED'});if(!r.data?.data?.length){const r2=await axios.post('/orderPage',{page:page.page,pageSize:page.pageSize});list.value=(r2.data?.data||[]).filter(o=>o.order_status!=='CREATED'&&o.order_status!=='FINISHED');total.value=list.value.length}else{list.value=r.data.data;total.value=r.data.total}loading.value=false}catch(e){loading.value=false}}
    const doRefund=async(row)=>{try{const{value}=await ElMessageBox.prompt('退款金额','处理退款',{inputValue:row.pay_amount});await axios.post('/orderRefund',{id:row.id,refund_amount:parseFloat(value)});ElMessage.success('退款成功');load()}catch(e){}}
    onMounted(load)
    return{page,list,loading,total,load,doRefund}
  }
}
</script>
