<template>
  <!-- 审批对话框 -->
  <el-dialog v-model="approveDialogVisible" title="审批申请" width="40%">
    <el-form label-width="auto" style="margin-left:5%;margin-right:5%">
      <el-form-item label="申请类型">
        <el-input :model-value="approveForm.applyType" disabled />
      </el-form-item>
      <el-form-item label="老人姓名">
        <el-input :model-value="approveForm.elderName" disabled />
      </el-form-item>
      <el-form-item label="申请描述">
        <el-input :model-value="approveForm.description" type="textarea" disabled />
      </el-form-item>
      <el-form-item label="审批意见">
        <el-input v-model="approveForm.approveOpinion" type="textarea" :rows="3" placeholder="请输入审批意见" />
      </el-form-item>
      <el-form-item style="margin-left: 30%">
        <el-button type="success" round @click="doApprove('APPROVED')">通过</el-button>
        <el-button type="danger" round @click="doApprove('已拒绝')">拒绝</el-button>
        <el-button type="warning" round @click="approveDialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 搜索栏 -->
  申请类型&nbsp;:&nbsp;
  <el-select style="width:15%;margin-right: 20px" placeholder="请选择" v-model="condForm.applyType">
    <el-option value="" label="全部" />
    <el-option value="入住" label="入住" />
    <el-option value="退住" label="退住" />
    <el-option value="请假" label="请假" />
  </el-select>
  老人姓名&nbsp;:&nbsp;
  <el-input style="width:15%;margin-right: 20px" v-model="condForm.elderName" placeholder="老人姓名"/>
  <el-button type="primary" @click="loadTodoList">搜索</el-button>
  <hr/>

  <el-table :data="todoList" style="width: 100%" :fit="true">
    <el-table-column type="index" width="50" />
    <el-table-column prop="applyType" label="申请类型" width="90">
      <template #default="scope">
        <el-tag v-if="scope.row.applyType==='入住'" type="success">入住</el-tag>
        <el-tag v-else-if="scope.row.applyType==='退住'" type="danger">退住</el-tag>
        <el-tag v-else type="warning">请假</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="elderName" label="老人姓名" width="100"/>
    <el-table-column prop="applyUser" label="申请人" width="90"/>
    <el-table-column prop="description" label="申请描述" min-width="180" show-overflow-tooltip/>
    <el-table-column prop="applyTime" label="申请时间" width="160"/>
    <el-table-column label="状态" width="90">
      <template #default="scope">
        <span v-if="scope.row.status=='PENDING'" style="color:orange;font-weight:bold">待审批</span>
        <span v-else-if="scope.row.status=='APPROVED'" style="color:green">已通过</span>
        <span v-else-if="scope.row.status=='REJECTED'" style="color:crimson">已驳回</span>
        <span v-else-if="scope.row.status=='DONE'" style="color:dodgerblue">已完成</span>
        <span v-else style="color:red">已拒绝</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="140">
      <template #default="scope">
        <el-button v-if="scope.row.status=='PENDING'" type="primary" size="small" @click="openApproveDialog(scope.row)">审批</el-button>
        <span v-else style="color:#999">--</span>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doTodoPage"/>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

const approveDialogVisible = ref(false);
const approveForm = reactive({
  id: '', applyType: '', elderName: '', description: '',
  approveOpinion: '', approveUser: '马云', approveUserId: 1
});

function openApproveDialog(row) {
  approveForm.id = row.id;
  approveForm.applyType = row.applyType;
  approveForm.elderName = row.elderName;
  approveForm.description = row.description;
  approveForm.approveOpinion = '';
  approveDialogVisible.value = true;
}

function doApprove(status) {
  axios.post("/approveApply", {
    id: approveForm.id,
    status: status,
    approveUser: approveForm.approveUser,
    approveUserId: approveForm.approveUserId,
    approveOpinion: approveForm.approveOpinion
  }).then(response => {
    if (response.data.code == 200) {
      approveDialogVisible.value = false;
      doTodoPage(1);
    }
    ElMessage(response.data.msg);
  }).catch(error => console.log(error));
}

const condForm = reactive({
  applyType: '', status: 'PENDING', elderName: '', pageNum: 1, pageSize: 10
});

const todoList = ref([]);
const total = ref(0);

function loadTodoList() {
  axios.post("/applies/page", condForm)
    .then(response => {
      todoList.value = response.data.data;
      total.value = response.data.total;
    }).catch(error => console.log(error));
}

onMounted(() => { loadTodoList(); });

function doTodoPage(pageNum) {
  condForm.pageNum = pageNum;
  loadTodoList();
}
</script>
