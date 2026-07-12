<template>
  <section class="page-section">
    <el-dialog v-model="refundDialog" title="处理退款" width="30%">
      <el-form><el-form-item label="退款金额"><el-input-number v-model="refundForm.refundAmount" :precision="2" :step="0.1" :max="99999"/></el-form-item></el-form>
      <template #footer><el-button type="danger" @click="confirmRefund">确认退款</el-button></template>
    </el-dialog>

    <el-row :gutter="20" style="margin-bottom:20px">
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center"><div style="color:#909399;font-size:14px">总收入</div><div style="color:#67C23A;font-size:28px;font-weight:bold">¥{{totalIncome}}</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center"><div style="color:#909399;font-size:14px">总退款</div><div style="color:#F56C6C;font-size:28px;font-weight:bold">¥{{totalRefund}}</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center"><div style="color:#909399;font-size:14px">待支付</div><div style="color:#E6A23C;font-size:28px;font-weight:bold">{{pendingCount}}笔</div></div></el-card></el-col>
      <el-col :span="6"><el-card shadow="hover"><div style="text-align:center"><div style="color:#909399;font-size:14px">总订单</div><div style="color:#409EFF;font-size:28px;font-weight:bold">{{totalCount}}笔</div></div></el-card></el-col>
    </el-row>

    <el-table :data="orders" v-loading="loading" empty-text="暂无数据">
      <el-table-column prop="orderNo" label="订单编号" width="170"/>
      <el-table-column prop="serviceName" label="服务项目" width="130"/>
      <el-table-column prop="totalAmount" label="金额" width="100"><template #default="{row}">¥{{row.totalAmount}}</template></el-table-column>
      <el-table-column prop="payStatus" label="支付状态" width="90"><template #default="{row}"><el-tag :type="row.payStatus==='PAID'?'success':''">{{row.payStatus}}</el-tag></template></el-table-column>
      <el-table-column prop="orderStatus" label="订单状态" width="90"/>
      <el-table-column prop="refundAmount" label="退款金额" width="100"><template #default="{row}">¥{{row.refundAmount||0}}</template></el-table-column>
      <el-table-column prop="createTime" label="时间" width="170"/>
      <el-table-column label="操作" width="120"><template #default="{row}">
        <el-button size="small" type="danger" @click="openRefund(row)" v-if="row.payStatus==='PAID'&&row.orderStatus!=='REFUNDED'">退款</el-button>
      </template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page.pageNum" :page-size="page.pageSize" :page-sizes="[10,20,30,40]" layout="total,sizes,prev,pager,next" :total="total" @size-change="load" @current-change="load"/>
  </section>
</template>
<script>
import {onMounted, reactive, ref} from 'vue'
import axios from 'axios'
import {ElMessage} from 'element-plus'
export default {
  setup(){
    const page=reactive({pageNum:1,pageSize:10})
    const orders=ref([]);const loading=ref(false);const total=ref(0)
    const totalIncome=ref(0);const totalRefund=ref(0);const pendingCount=ref(0);const totalCount=ref(0)
    const refundDialog=ref(false);const refundForm=reactive({id:null,refundAmount:0})
    const load=async()=>{loading.value=true;try{const[s,r]=await Promise.all([axios.post('/financeSummary',page),axios.post('/orderPage',page)]);const data=s.data;totalIncome.value=data.totalIncome||0;totalRefund.value=data.totalRefund||0;pendingCount.value=data.pendingCount||0;totalCount.value=data.totalCount||0;orders.value=r.data.data||[];total.value=r.data.total}catch(e){}finally{loading.value=false}}
    const openRefund=(row)=>{refundForm.id=row.id;refundForm.refundAmount=row.totalAmount||0;refundDialog.value=true}
    const confirmRefund=async()=>{await axios.post('/orderRefund',{id:refundForm.id,refund_amount:refundForm.refundAmount});ElMessage.success('退款成功');refundDialog.value=false;load()}
    onMounted(load)
    return{page,orders,loading,total,totalIncome,totalRefund,pendingCount,totalCount,refundDialog,refundForm,load,openRefund,confirmRefund}
  }
}
</script>
