<template>
  <section class="page-section">
    <el-form :inline="true" :model="query">
      <el-form-item label="老人"><el-input v-model="query.elderName" placeholder="老人姓名"/></el-form-item>
      <el-form-item label="状态"><el-select v-model="query.status" placeholder="全部" clearable><el-option label="待执行" value="PENDING"/><el-option label="已完成" value="DONE"/></el-select></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button><el-button @click="openAdd">新增任务</el-button></el-form-item>
    </el-form>
    <el-table :data="list" v-loading="loading" empty-text="暂无数据">
      <el-table-column prop="task_no" label="任务编号" width="160"/>
      <el-table-column prop="elder_name" label="老人" width="100"/>
      <el-table-column prop="item_name" label="护理项目" width="130"/>
      <el-table-column prop="scheduled_time" label="计划时间" width="160"/>
      <el-table-column prop="status" label="状态" width="90"><template #default="{row}"><el-tag :type="row.status==='DONE'?'success':'warning'">{{row.status==='DONE'?'已完成':'待执行'}}</el-tag></template></el-table-column>
      <el-table-column prop="execute_result" label="执行结果" min-width="150"/>
      <el-table-column label="操作" width="180"><template #default="{row}">
        <el-button size="small" v-if="row.status!=='DONE'" type="success" @click="execute(row)">执行</el-button>
        <el-button size="small" type="danger" @click="del(row.id)">删除</el-button>
      </template></el-table-column>
    </el-table>
    <el-pagination v-model:current-page="query.page" :page-size="query.pageSize" :page-sizes="[10,20,30,40]" layout="total,sizes,prev,pager,next" :total="total" @size-change="load" @current-change="load"/>

    <el-dialog v-model="dialog" :title="form.id?'编辑任务':'新增任务'" width="450px">
      <el-form :model="form">
        <el-form-item label="老人"><el-select v-model="form.elderId" placeholder="选择老人" filterable style="width:100%"><el-option v-for="e in elders" :key="e.id" :label="e.name" :value="e.id"/></el-select></el-form-item>
        <el-form-item label="护理项目"><el-select v-model="form.itemId" placeholder="选择项目" style="width:100%"><el-option v-for="i in items" :key="i.id" :label="i.itemname" :value="i.id"/></el-select></el-form-item>
        <el-form-item label="计划时间"><el-date-picker v-model="form.scheduledTime" type="datetime" placeholder="选择时间" style="width:100%"/></el-form-item>
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
    const query=reactive({page:1,pageSize:10,elderName:'',status:''})
    const form=reactive({id:null,elderId:'',itemId:'',scheduledTime:'',nurseId:1})
    const list=ref([]);const loading=ref(false);const total=ref(0);const dialog=ref(false)
    const elders=ref([]);const items=ref([])
    const load=async()=>{loading.value=true;try{const r=await axios.post('/nursingTaskPage',query);list.value=r.data.data;total.value=r.data.total}catch(e){}finally{loading.value=false}}
    const openAdd=async()=>{form.id=null;form.elderId='';form.itemId='';form.scheduledTime='';dialog.value=true;try{const[r1,r2]=await Promise.all([axios.get('/elderList'),axios.get('/nursingLevelList')]);elders.value=r1.data.data||[];items.value=r2.data.data||[]}catch(e){}}
    const submit=async()=>{await axios.post('/saveNursingTask',form);ElMessage.success('保存成功');dialog.value=false;load()}
    const execute=async(row)=>{try{const{value}=await ElMessageBox.prompt('执行结果','执行任务');await axios.post('/executeNursingTask',{id:row.id,result:value});ElMessage.success('执行完成');load()}catch(e){}}
    const del=async(id)=>{await ElMessageBox.confirm('确认删除？','提示',{type:'warning'});await axios.get('/deleteNursingTask?id='+id);ElMessage.success('已删除');load()}
    onMounted(load)
    return{query,form,list,loading,total,dialog,elders,items,load,openAdd,submit,execute,del}
  }
}
</script>
