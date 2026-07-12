<template>
  <h3>报警数据监控</h3>

  设备名称&nbsp;:&nbsp;
  <el-input style="width:15%;margin-right: 20px" v-model="condForm.deviceName" placeholder="设备名称"/>
  报警状态&nbsp;:&nbsp;
  <el-select style="width:15%;margin-right: 20px" placeholder="请选择" v-model="condForm.alertStatus">
    <el-option value="" label="全部" />
    <el-option value="WARNING" label="一般报警" />
    <el-option value="ALERT" label="紧急报警" />
  </el-select>
  <el-button type="primary" @click="loadAlertList">搜索</el-button>
  <el-button type="danger" @click="loadAlertList" plain>刷新报警</el-button>
  <hr/>

  <el-table :data="alertList" style="width: 100%" :fit="true">
    <el-table-column type="index" width="50" />
    <el-table-column prop="deviceName" label="设备名称" width="140"/>
    <el-table-column prop="deviceCode" label="编号" width="110"/>
    <el-table-column prop="deviceType" label="类型" width="90"/>
    <el-table-column prop="elderName" label="绑定老人" width="100"/>
    <el-table-column prop="installLocation" label="位置" width="120"/>
    <el-table-column label="运行状态" width="90">
      <template #default="scope">
        <el-tag v-if="scope.row.runningStatus=='正常'" type="success">正常</el-tag>
        <el-tag v-else-if="scope.row.runningStatus=='异常'" type="warning">异常</el-tag>
        <el-tag v-else type="danger">离线</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="报警状态" width="120">
      <template #default="scope">
        <span v-if="scope.row.alertStatus=='WARNING'" style="color:orange;font-weight:bold">⚠ 一般报警</span>
        <span v-else-if="scope.row.alertStatus=='ALERT'" style="color:red;font-weight:bold">🔴 紧急报警</span>
        <span v-else style="color:green">正常</span>
      </template>
    </el-table-column>
    <el-table-column prop="lastHeartbeat" label="最后心跳" width="160"/>
    <el-table-column label="操作" width="120">
      <template #default="scope">
        <el-button type="primary" size="small" @click="handleAlert(scope.row)">处理</el-button>
        <el-button size="small" @click="viewDetail(scope.row)">详情</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doAlertPage"/>

  <!-- 详情对话框 -->
  <el-dialog v-model="detailVisible" title="设备详情" width="40%">
    <el-descriptions :column="2" border v-if="detailRow">
      <el-descriptions-item label="设备名称">{{ detailRow.deviceName }}</el-descriptions-item>
      <el-descriptions-item label="设备编号">{{ detailRow.deviceCode }}</el-descriptions-item>
      <el-descriptions-item label="设备类型">{{ detailRow.deviceType }}</el-descriptions-item>
      <el-descriptions-item label="绑定老人">{{ detailRow.elderName || '未绑定' }}</el-descriptions-item>
      <el-descriptions-item label="安装位置">{{ detailRow.installLocation }}</el-descriptions-item>
      <el-descriptions-item label="运行状态">
        <el-tag v-if="detailRow.runningStatus=='正常'" type="success">正常</el-tag>
        <el-tag v-else-if="detailRow.runningStatus=='异常'" type="warning">异常</el-tag>
        <el-tag v-else type="danger">离线</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="报警状态" :span="2">
        <span v-if="detailRow.alertStatus=='NORMAL'" style="color:green">正常</span>
        <span v-else-if="detailRow.alertStatus=='WARNING'" style="color:orange">一般报警</span>
        <span v-else style="color:red">紧急报警</span>
      </el-descriptions-item>
      <el-descriptions-item label="最后心跳">{{ detailRow.lastHeartbeat }}</el-descriptions-item>
      <el-descriptions-item label="创建时间">{{ detailRow.createTime }}</el-descriptions-item>
    </el-descriptions>
  </el-dialog>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

const condForm = reactive({
  deviceName: '', alertStatus: '', pageNum: 1, pageSize: 10
});

const alertList = ref([]);
const total = ref(0);

function loadAlertList() {
  axios.post("/devicePage", condForm)
    .then(response => {
      alertList.value = response.data.data || [];
      total.value = response.data.total;
    }).catch(error => {
      ElMessage({message: '加载报警数据失败', type: 'error'});
      console.log(error);
    });
}

onMounted(() => { loadAlertList(); });

function doAlertPage(pageNum) {
  condForm.pageNum = pageNum;
  loadAlertList();
}

function handleAlert(row) {
  axios.post("/updateDevice", {
    id: row.id,
    alertStatus: 'NORMAL'
  }).then(response => {
    if (response.data.code == 200) loadAlertList();
    ElMessage({message: response.data.msg || '报警已处理', type: response.data.code == 200 ? 'success' : 'error'});
  }).catch(error => {
    ElMessage({message: '处理报警失败', type: 'error'});
    console.log(error);
  });
}

const detailVisible = ref(false);
const detailRow = ref(null);

function viewDetail(row) {
  detailRow.value = row;
  detailVisible.value = true;
}
</script>
