<template>
  <!-- 新增/编辑对话框 -->
  <el-dialog v-model="dialogVisible" title="客户信息" width="50%">
    <el-form label-width="auto" style="margin-left:5%;margin-right:5%;max-width: 600px">
      <el-form-item label="客户姓名">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="联系电话">
        <el-input v-model="form.phone" />
      </el-form-item>
      <el-form-item label="意向等级">
        <el-select v-model="form.intentionLevel" placeholder="请选择" style="width:100%">
          <el-option value="A" label="A-高意向" />
          <el-option value="B" label="B-中等意向" />
          <el-option value="C" label="C-低意向" />
          <el-option value="D" label="D-无意向" />
        </el-select>
      </el-form-item>
      <el-form-item label="客户来源">
        <el-select v-model="form.source" placeholder="请选择" style="width:100%">
          <el-option value="线上咨询" label="线上咨询" />
          <el-option value="线下活动" label="线下活动" />
          <el-option value="转介绍" label="转介绍" />
          <el-option value="电话营销" label="电话营销" />
          <el-option value="其他" label="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="跟进内容">
        <el-input v-model="form.followContent" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item label="下次跟进时间">
        <el-date-picker v-model="form.nextFollowTime" type="datetime" placeholder="选择时间" style="width:100%" value-format="YYYY-MM-DD HH:mm:ss" />
      </el-form-item>
      <el-form-item label="跟进状态">
        <el-select v-model="form.status" placeholder="请选择" style="width:100%">
          <el-option value="FOLLOWING" label="跟进中" />
          <el-option value="DEAL" label="已成交" />
          <el-option value="LOST" label="已流失" />
        </el-select>
      </el-form-item>
      <el-form-item style="margin-left: 40%">
        <el-button type="primary" round @click="saveCustomer">确认</el-button>
        <el-button type="warning" round @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 搜索栏 -->
  客户姓名&nbsp;:&nbsp;
  <el-input style="width:15%;margin-right: 20px" v-model="condForm.name" placeholder="姓名"/>
  意向等级&nbsp;:&nbsp;
  <el-select style="width:15%;margin-right: 20px" placeholder="请选择" v-model="condForm.intentionLevel">
    <el-option value="" label="全部" />
    <el-option value="A" label="A" />
    <el-option value="B" label="B" />
    <el-option value="C" label="C" />
    <el-option value="D" label="D" />
  </el-select>
  来源&nbsp;:&nbsp;
  <el-select style="width:15%;margin-right: 20px" placeholder="请选择" v-model="condForm.source">
    <el-option value="" label="全部" />
    <el-option value="线上咨询" label="线上咨询" />
    <el-option value="线下活动" label="线下活动" />
    <el-option value="转介绍" label="转介绍" />
    <el-option value="电话营销" label="电话营销" />
    <el-option value="其他" label="其他" />
  </el-select>
  <el-button type="primary" @click="loadCustomerList">搜索</el-button>
  <hr/>
  <div style="text-align: left">
    <el-button type="primary" @click="openAddDialog">添加客户</el-button>
  </div>

  <!-- 表格 -->
  <el-table :data="customerList" style="width: 100%" :fit="true">
    <el-table-column type="index" width="50" />
    <el-table-column prop="name" label="客户姓名" width="100"/>
    <el-table-column prop="phone" label="联系电话" width="130"/>
    <el-table-column label="意向等级" width="90">
      <template #default="scope">
        <el-tag v-if="scope.row.intentionLevel === 'A'" type="danger">A</el-tag>
        <el-tag v-else-if="scope.row.intentionLevel === 'B'" type="warning">B</el-tag>
        <el-tag v-else-if="scope.row.intentionLevel === 'C'" type="info">C</el-tag>
        <el-tag v-else type="info">D</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="source" label="来源" width="100"/>
    <el-table-column prop="followContent" label="跟进内容" min-width="180" show-overflow-tooltip/>
    <el-table-column prop="nextFollowTime" label="下次跟进" width="160"/>
    <el-table-column prop="createTime" label="创建时间" width="160"/>
    <el-table-column label="状态" width="80">
      <template #default="scope">
        <el-tag v-if="scope.row.status==='FOLLOWING'" type="warning">跟进中</el-tag>
        <el-tag v-else-if="scope.row.status==='DEAL'" type="success">已成交</el-tag>
        <el-tag v-else type="info">{{ scope.row.status }}</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="200">
      <template #default="scope">
        <el-button type="primary" size="small" @click="showCustomerInfo(scope.row)">编辑</el-button>
        <el-button type="warning" size="small" @click="delCustomer(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doCustomerPage"/>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

const dialogVisible = ref(false);
var url = null;

const form = reactive({
  id: '', name: '', phone: '', intentionLevel: '', source: '',
  followContent: '', nextFollowTime: '', status: ''
});

function openAddDialog() {
  url = "/saveCustomer";
  cleanForm();
  dialogVisible.value = true;
}

function cleanForm() {
  form.id = ''; form.name = ''; form.phone = ''; form.intentionLevel = '';
  form.source = ''; form.followContent = ''; form.nextFollowTime = '';
  form.status = '';
}

const condForm = reactive({
  name: '', intentionLevel: '', source: '', status: '', pageNum: 1, pageSize: 10
});

const customerList = ref([]);
const total = ref(0);

function loadCustomerList() {
  axios.post("/customerPage", condForm)
    .then(response => {
      customerList.value = response.data.customers;
      total.value = response.data.total;
    }).catch(error => console.log(error));
}

onMounted(() => { loadCustomerList(); });

function doCustomerPage(pageNum) {
  condForm.pageNum = pageNum;
  loadCustomerList();
}

function saveCustomer() {
  axios.post(url, form)
    .then(response => {
      if (response.data.code == 200) {
        dialogVisible.value = false;
        cleanForm();
        doCustomerPage(1);
      }
      ElMessage(response.data.msg);
    }).catch(error => console.log(error));
}

function showCustomerInfo(row) {
  form.id = row.id; form.name = row.name; form.phone = row.phone;
  form.intentionLevel = row.intentionLevel; form.source = row.source;
  form.followContent = row.followContent; form.nextFollowTime = row.nextFollowTime;
  form.status = row.status;
  dialogVisible.value = true;
  url = "/updateCustomer";
}

function delCustomer(id) {
  axios.get("/deleteCustomer?id=" + id)
    .then(response => {
      if (response.data.code == 200) doCustomerPage(1);
      ElMessage(response.data.msg);
    }).catch(error => console.log(error));
}
</script>
