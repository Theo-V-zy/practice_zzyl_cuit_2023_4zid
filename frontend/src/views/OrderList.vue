<template>
  <section class="page-section">
    <el-form :inline="true" :model="query">
      <el-form-item label="订单编号"><el-input v-model="query.orderNo" placeholder="订单编号"/></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.orderStatus" placeholder="全部" clearable><el-option label="已创建" value="CREATED"/><el-option label="已支付" value="PAID"/><el-option label="已退款" value="REFUNDED"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button @click="openAdd">新增订单</el-button></el-form-item>
    </el-form>
    <el-table :data="list" v-loading="loading" empty-text="暂无数据">
      <el-table-column prop="orderNo" label="订单编号" width="170"/>
      <el-table-column prop="serviceName" label="服务项目" width="130"/>
      <el-table-column prop="totalAmount" label="金额" width="100"><template #default="{row}">¥{{row.totalAmount}}</template></el-table-column>
      <el-table-column prop="payStatus" label="支付" width="80"><template #default="{row}"><el-tag :type="row.payStatus==='PAID'?'success':''">{{row.payStatus==='PAID'?'已支付':'未付'}}</el-tag></template></el-table-column>
      <el-table-column prop="orderStatus" label="订单状态" width="90"><template #default="{row}"><el-tag :type="row.orderStatus==='REFUNDED'?'info':row.orderStatus==='PAID'?'success':''">{{row.orderStatus}}</el-tag></template></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="170"/>
      <el-table-column label="操作" width="220"><template #default="{row}">
        <el-button size="small" type="success" v-if="row.payStatus==='UNPAID'" @click="pay(row)">收款</el-button>
        <el-button size="small" type="danger" v-if="row.payStatus==='PAID'" @click="doRefund(row)">退款</el-button>
        <el-button size="small" @click="del(row.id)">删除</el-button>
      </template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.pageNum" :page-size="query.pageSize" :page-sizes="[10,20,30,40]" layout="total,sizes,prev,pager,next" :total="total" @size-change="load" @current-change="load"/>

    <el-dialog v-model="dialog" :title="form.id?'编辑订单':'新增订单'" width="450px">
      <el-form :model="form">
        <el-form-item label="服务项目"><el-input v-model="form.serviceName"/></el-form-item>
        <el-form-item label="金额"><el-input-number v-model="form.totalAmount" :precision="2" :step="0.1" :max="99999" style="width:100%"/></el-form-item>
        <el-form-item label="数量"><el-input-number v-model="form.quantity" :min="1" style="width:100%"/></el-form-item>
      </el-form>
      <template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="submit">保存</el-button></template>
    </el-dialog>
  </section>
</template>
<script>
import {onMounted, reactive, ref} from 'vue'
import axios from 'axios'
import {ElMessage,ElMessageBox} from 'element-plus'
export default {
  setup(){
    const query=reactive({pageNum:1,pageSize:10,orderNo:'',orderStatus:''})
    const form=reactive({id:null,serviceName:'',totalAmount:0,quantity:1})
    const list=ref([]);const loading=ref(false);const total=ref(0);const dialog=ref(false)
    const load=async()=>{loading.value=true;try{const r=await axios.post('/orderPage',query);list.value=r.data.data||[];total.value=r.data.total}catch(e){}finally{loading.value=false}}
    const openAdd=()=>{form.id=null;form.serviceName='';form.totalAmount=0;form.quantity=1;dialog.value=true}
    const submit=async()=>{const url=form.id?'/updateOrder':'/saveOrder';await axios.post(url,form);ElMessage.success('保存成功');dialog.value=false;load()}
    const pay=async(row)=>{await ElMessageBox.confirm('确认收款？','提示');await axios.post('/orderPay',{id:row.id});ElMessage.success('收款成功');load()}
    const doRefund=async(row)=>{try{const{value}=await ElMessageBox.prompt('退款金额','处理退款',{inputValue:row.totalAmount});await axios.post('/orderRefund',{id:row.id,refund_amount:parseFloat(value)});ElMessage.success('退款成功');load()}catch(e){}}
    const del=async(id)=>{await ElMessageBox.confirm('确认删除？','提示',{type:'warning'});await axios.get('/deleteOrder?id='+id);ElMessage.success('已删除');load()}
    onMounted(load)
    return{query,form,list,loading,total,dialog,load,openAdd,submit,pay,doRefund,del}
  }
}
</script>
