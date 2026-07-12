<template>
  <!-- 新增/编辑对话框 -->
  <el-dialog v-model="dialogVisible" title="设备信息" width="45%">
    <el-form label-width="auto" style="margin-left:5%;margin-right:5%;max-width: 600px">
      <el-form-item label="设备名称">
        <el-input v-model="form.deviceName" />
      </el-form-item>
      <el-form-item label="设备编号">
        <el-input v-model="form.deviceCode" />
      </el-form-item>
      <el-form-item label="设备类型">
        <el-select v-model="form.deviceType" placeholder="请选择" style="width:100%">
          <el-option value="手环" label="手环" />
          <el-option value="床垫" label="床垫" />
          <el-option value="摄像头" label="摄像头" />
          <el-option value="紧急按钮" label="紧急按钮" />
          <el-option value="其他" label="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="安装位置">
        <el-input v-model="form.installLocation" placeholder="如: A栋101室"/>
      </el-form-item>
      <el-form-item label="绑定老人">
        <el-input v-model="form.elderName" placeholder="输入老人姓名"/>
      </el-form-item>
      <el-form-item label="运行状态">
        <el-select v-model="form.runningStatus" placeholder="请选择" style="width:100%">
          <el-option value="正常" label="正常" />
          <el-option value="异常" label="异常" />
          <el-option value="离线" label="离线" />
        </el-select>
      </el-form-item>
      <el-form-item label="报警状态">
        <el-select v-model="form.alarmStatus" placeholder="请选择" style="width:100%">
          <el-option value="无报警" label="无报警" />
          <el-option value="一般报警" label="一般报警" />
          <el-option value="紧急报警" label="紧急报警" />
        </el-select>
      </el-form-item>
      <el-form-item label="启用状态">
        <el-radio-group v-model="form.islock">
          <el-radio value="启用">启用</el-radio>
          <el-radio value="禁用">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item style="margin-left: 40%">
        <el-button type="primary" round @click="saveDevice">确认</el-button>
        <el-button type="warning" round @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

  <!-- 搜索栏 -->
  设备名称&nbsp;:&nbsp;
  <el-input style="width:15%;margin-right: 20px" v-model="condForm.deviceName" placeholder="设备名称"/>
  设备类型&nbsp;:&nbsp;
  <el-select style="width:12%;margin-right: 20px" placeholder="全部" v-model="condForm.deviceType">
    <el-option value="" label="全部" />
    <el-option value="手环" label="手环" />
    <el-option value="床垫" label="床垫" />
    <el-option value="摄像头" label="摄像头" />
    <el-option value="紧急按钮" label="紧急按钮" />
  </el-select>
  运行状态&nbsp;:&nbsp;
  <el-select style="width:12%;margin-right: 20px" placeholder="全部" v-model="condForm.runningStatus">
    <el-option value="" label="全部" />
    <el-option value="正常" label="正常" />
    <el-option value="异常" label="异常" />
    <el-option value="离线" label="离线" />
  </el-select>
  <el-button type="primary" @click="loadDeviceList">搜索</el-button>
  <hr/>
  <div style="text-align: left">
    <el-button type="primary" @click="openAddDialog">添加设备</el-button>
  </div>

  <el-table :data="deviceList" style="width: 100%" :fit="true">
    <el-table-column type="index" width="50" />
    <el-table-column prop="deviceName" label="设备名称" width="130"/>
    <el-table-column prop="deviceCode" label="编号" width="110"/>
    <el-table-column prop="deviceType" label="类型" width="90"/>
    <el-table-column prop="elderName" label="绑定老人" width="100">
      <template #default="scope">
        <span v-if="scope.row.elderName">{{ scope.row.elderName }}</span>
        <span v-else style="color:#999">未绑定</span>
      </template>
    </el-table-column>
    <el-table-column prop="installLocation" label="安装位置" width="120"/>
    <el-table-column label="运行状态" width="90">
      <template #default="scope">
        <el-tag v-if="scope.row.runningStatus=='正常'" type="success">正常</el-tag>
        <el-tag v-else-if="scope.row.runningStatus=='异常'" type="warning">异常</el-tag>
        <el-tag v-else type="danger">离线</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="报警状态" width="100">
      <template #default="scope">
        <span v-if="scope.row.alarmStatus=='无报警'" style="color:green">无报警</span>
        <span v-else-if="scope.row.alarmStatus=='一般报警'" style="color:orange">⚠ 一般报警</span>
        <span v-else style="color:red;font-weight:bold">🔴 紧急报警</span>
      </template>
    </el-table-column>
    <el-table-column label="启用" width="80">
      <template #default="scope">
        <span v-if="scope.row.islock=='启用'" style="color:dodgerblue">启用</span>
        <span v-else style="color:crimson">禁用</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="220">
      <template #default="scope">
        <el-button type="primary" size="small" @click="showDeviceInfo(scope.row)">编辑</el-button>
        <el-button type="success" size="small" @click="toggleDeviceLock(scope.row)">
          {{ scope.row.islock=="启用"?"禁用":"启用" }}
        </el-button>
        <el-button type="warning" size="small" @click="delDevice(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doDevicePage"/>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import axios from "axios";
import { ElMessage } from "element-plus";

const dialogVisible = ref(false);
var url = null;

const form = reactive({
  id: '', deviceName: '', deviceCode: '', deviceType: '', elderName: '',
  bedId: '', installLocation: '', runningStatus: '', alarmStatus: '', islock: ''
});

function openAddDialog() {
  url = "/saveDevice";
  cleanForm();
  dialogVisible.value = true;
}

function cleanForm() {
  form.id = ''; form.deviceName = ''; form.deviceCode = ''; form.deviceType = '';
  form.elderName = ''; form.bedId = ''; form.installLocation = '';
  form.runningStatus = ''; form.alarmStatus = ''; form.islock = '';
}

const condForm = reactive({
  deviceName: '', deviceType: '', runningStatus: '', alarmStatus: '', islock: '', pageNum: 1, pageSize: 10
});

const deviceList = ref([]);
const total = ref(0);

function loadDeviceList() {
  axios.post("/devicePage", condForm)
    .then(response => {
      deviceList.value = response.data.devices;
      total.value = response.data.total;
    }).catch(error => console.log(error));
}

onMounted(() => { loadDeviceList(); });

function doDevicePage(pageNum) {
  condForm.pageNum = pageNum;
  loadDeviceList();
}

function saveDevice() {
  axios.post(url, form)
    .then(response => {
      if (response.data.code == 200) {
        dialogVisible.value = false;
        cleanForm();
        doDevicePage(1);
      }
      ElMessage(response.data.msg);
    }).catch(error => console.log(error));
}

function showDeviceInfo(row) {
  form.id = row.id; form.deviceName = row.deviceName; form.deviceCode = row.deviceCode;
  form.deviceType = row.deviceType; form.elderName = row.elderName;
  form.bedId = row.bedId; form.installLocation = row.installLocation;
  form.runningStatus = row.runningStatus; form.alarmStatus = row.alarmStatus;
  form.islock = row.islock;
  dialogVisible.value = true;
  url = "/updateDevice";
}

function toggleDeviceLock(row) {
  var newLock = row.islock == "禁用" ? "启用" : "禁用";
  axios.post("/updateDeviceLock", { id: row.id, islock: newLock })
    .then(response => {
      if (response.data.code == 200) doDevicePage(1);
      ElMessage(response.data.msg);
    }).catch(error => console.log(error));
}

function delDevice(id) {
  axios.get("/deleteDevice?id=" + id)
    .then(response => {
      if (response.data.code == 200) doDevicePage(1);
      ElMessage(response.data.msg);
    }).catch(error => console.log(error));
}
</script>
