<template>
  <section class="page-section">
    <el-form :inline="true" :model="query">
      <el-form-item label="护理员"><el-input v-model="query.nurseName" placeholder="护理员姓名"/></el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>
    <el-table :data="list" v-loading="loading" empty-text="暂无数据">
      <el-table-column prop="nurse_name" label="护理员" width="120"/>
      <el-table-column prop="elder_count" label="负责老人数" width="100"/>
      <el-table-column prop="elder_names" label="负责老人" min-width="300"/>
    </el-table>
    <el-pagination v-model:current-page="query.page" :page-size="query.pageSize" :page-sizes="[10,20,30,40]" layout="total,sizes,prev,pager,next" :total="total" @size-change="load" @current-change="load"/>
  </section>
</template>
<script>
import {onMounted, reactive, ref} from 'vue'
import axios from 'axios'
import {ElMessage} from 'element-plus'
export default {
  setup(){
    const query=reactive({page:1,pageSize:10,nurseName:''})
    const list=ref([]);const loading=ref(false);const total=ref(0)
    const load=async()=>{loading.value=true;try{const r=await axios.post('/elderAssignmentPage',query);list.value=r.data.data;total.value=r.data.total}catch(e){ElMessage.error('加载失败')}finally{loading.value=false}}
    onMounted(load)
    return{query,list,loading,total,load}
  }
}
</script>
