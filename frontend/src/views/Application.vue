<template>
  <!-- 新增申请对话框 -->
  <el-dialog v-model="dialogVisible" title="提交申请" width="40%">
    <el-form label-width="auto" style="margin-left:5%;margin-right:5%">
      <el-form-item label="申请类型">
        <el-select v-model="form.applyType" placeholder="请选择" style="width:100%">
          <el-option value="入住" label="入住申请" />
          <el-option value="退住" label="退住申请" />
          <el-option value="请假" label="请假申请" />
        </el-select>
      </el-form-item>
      <el-form-item label="老人姓名">
        <el-input v-model="form.elderName" placeholder="请输入老人姓名"/>
      </el-form-item>
      <el-form-item label="申请描述">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请描述申请内容"/>
      </el-form-item>
      <el-form-item style="margin-left: 40%">
        <el-button type="primary" round @click="saveApply">提交</el-button>
        <el-button type="warning" round @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 搜索栏 -->
  申请类型&nbsp;:&nbsp;
  <el-select style="width:15%;margin-right: 20px" placeholder="请选择" v-model="condForm.applyType">
    <el-option value="" label="全部" />
    <el-option value="CHECKIN" label="入住" />
    <el-option value="CHECKOUT" label="退住" />
    <el-option value="LEAVE" label="请假" />
  </el-select>
  状态&nbsp;:&nbsp;
  <el-select style="width:15%;margin-right: 20px" placeholder="请选择" v-model="condForm.status">
    <el-option value="" label="全部" />
    <el-option value="PENDING" label="待审批" />
    <el-option value="APPROVED" label="已通过" />
    <el-option value="REJECTED" label="已拒绝" />
  </el-select>
  老人姓名&nbsp;:&nbsp;
  <el-input style="width:15%;margin-right: 20px" v-model="condForm.elderName" placeholder="老人姓名"/>
  <el-button type="primary" @click="loadApplyList">搜索</el-button>
  <hr/>
  <div style="text-align: left">
    <el-button type="primary" @click="openAddDialog">提交申请</el-button>
  </div>

  <el-table :data="applyList" style="width: 100%" :fit="true">
    <el-table-column type="index" width="50" />
    <el-table-column prop="applyType" label="申请类型" width="90">
      <template #default="scope">
        <el-tag v-if="scope.row.applyType==='入住'" type="success">入住</el-tag>
        <el-tag v-else-if="scope.row.applyType==='退住'" type="danger">退住</el-tag>
        <el-tag v-else type="warning">请假</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="elderName" label="老人姓名" width="100"/>
    <el-table-column prop="reason" label="申请描述" min-width="180" show-overflow-tooltip/>
    <el-table-column prop="createTime" label="申请时间" width="160"/>
    <el-table-column label="状态" width="90">
      <template #default="scope">
        <span v-if="scope.row.status=='待审批'" style="color:orange">待审批</span>
        <span v-else-if="scope.row.status=='已通过'" style="color:green">已通过</span>
        <span v-else style="color:red">已拒绝</span>
      </template>
    </el-table-column>
    <el-table-column prop="approveUser" label="审批人" width="90"/>
    <el-table-column prop="approveOpinion" label="审批意见" min-width="150" show-overflow-tooltip/>
    <el-table-column prop="approveTime" label="审批时间" width="160"/>
  </el-table>

  <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doApplyPage"/>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

const dialogVisible = ref(false);

const form = reactive({
  applyType: '', elderName: '', description: '',
  applyUser: '马云', applyUserId: 1
});

function openAddDialog() {
  form.applyType = ''; form.elderName = ''; form.description = '';
  dialogVisible.value = true;
}

function saveApply() {
  axios.post("/applies", form)
    .then(response => {
      if (response.data.code == 200) {
        dialogVisible.value = false;
        doApplyPage(1);
      }
      ElMessage({message: response.data.msg, type: response.data.code == 200 ? 'success' : 'error'});
    }).catch(error => {
      ElMessage({message: '提交申请失败', type: 'error'});
      console.log(error);
    });
}

const condForm = reactive({
  applyType: '', status: '', elderName: '', applyUserId: 1, pageNum: 1, pageSize: 10
});

const applyList = ref([]);
const total = ref(0);

function loadApplyList() {
  axios.post("/applies/page", condForm)
    .then(response => {
      applyList.value = response.data.data;
      total.value = response.data.total;
    }).catch(error => {
      ElMessage({message: '加载申请列表失败', type: 'error'});
      console.log(error);
    });
}

onMounted(() => { loadApplyList(); });

function doApplyPage(pageNum) {
  condForm.pageNum = pageNum;
  loadApplyList();
}
</script>
