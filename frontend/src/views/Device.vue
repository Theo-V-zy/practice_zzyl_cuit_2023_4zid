<template>
  <el-dialog v-model="dialogVisible" title="设备信息" width="40%">
    <el-form label-width="auto" style="margin-left:5%;margin-right:5%">
      <el-form-item label="设备名称"><el-input v-model="form.deviceName"/></el-form-item>
      <el-form-item label="设备编号"><el-input v-model="form.deviceNo"/></el-form-item>
      <el-form-item label="设备类型"><el-select v-model="form.deviceType" style="width:100%"><el-option v-for="t in ['床垫监测','呼叫器','手环','摄像头','紧急按钮','其他']" :key="t" :value="t" :label="t"/></el-select></el-form-item>
      <el-form-item label="在线状态"><el-select v-model="form.onlineStatus" style="width:100%"><el-option value="ONLINE" label="在线"/><el-option value="OFFLINE" label="离线"/></el-select></el-form-item>
      <el-form-item label="告警状态"><el-select v-model="form.alertStatus" style="width:100%"><el-option value="NORMAL" label="正常"/><el-option value="WARNING" label="告警"/><el-option value="ALERT" label="紧急"/></el-select></el-form-item>
      <el-form-item style="margin-left:40%"><el-button type="primary" round @click="saveDevice">确认</el-button><el-button type="warning" round @click="dialogVisible=false">取消</el-button></el-form-item>
    </el-form>
  </el-dialog>

  设备名称: <el-input style="width:15%;margin-right:20px" v-model="condForm.deviceName" placeholder="设备名称"/>
  类型: <el-select style="width:12%;margin-right:20px" v-model="condForm.deviceType" placeholder="全部"><el-option value="" label="全部"/><el-option v-for="t in ['床垫监测','呼叫器','手环','摄像头','紧急按钮','其他']" :key="t" :value="t" :label="t"/></el-select>
  在线状态: <el-select style="width:12%;margin-right:20px" v-model="condForm.onlineStatus" placeholder="全部"><el-option value="" label="全部"/><el-option value="ONLINE" label="在线"/><el-option value="OFFLINE" label="离线"/></el-select>
  <el-button type="primary" @click="loadDeviceList">搜索</el-button>
  <hr/><div style="text-align:left"><el-button type="primary" @click="openAddDialog">添加设备</el-button></div>

  <el-table :data="deviceList" style="width:100%" :fit="true">
    <el-table-column type="index" width="50"/>
    <el-table-column prop="deviceName" label="设备名称" width="140"/>
    <el-table-column prop="deviceNo" label="编号" width="110"/>
    <el-table-column prop="deviceType" label="类型" width="100"/>
    <el-table-column label="在线状态" width="90"><template #default="scope"><el-tag :type="scope.row.onlineStatus==='ONLINE'?'success':'danger'">{{scope.row.onlineStatus==='ONLINE'?'在线':'离线'}}</el-tag></template></el-table-column>
    <el-table-column label="告警" width="90"><template #default="scope"><el-tag v-if="scope.row.alertStatus==='ALERT'" type="danger">紧急</el-tag><el-tag v-else-if="scope.row.alertStatus==='WARNING'" type="warning">告警</el-tag><span v-else style="color:green">正常</span></template></el-table-column>
    <el-table-column prop="lastEventType" label="最近事件" width="110"/>
    <el-table-column prop="lastEventTime" label="事件时间" width="160"/>
    <el-table-column label="操作" width="180"><template #default="scope">
      <el-button type="primary" size="small" @click="showDeviceInfo(scope.row)">编辑</el-button>
      <el-button type="warning" size="small" @click="delDevice(scope.row.id)">删除</el-button>
    </template></el-table-column>
  </el-table>
  <el-pagination size="small" background layout="prev,pager,next" :total="total" @change="doDevicePage"/>
</template>

<script setup>
import {onMounted, reactive, ref} from 'vue';import axios from 'axios';import {ElMessage} from 'element-plus'
const dialogVisible=ref(false);var url=null
const form=reactive({id:'',deviceName:'',deviceNo:'',deviceType:'',onlineStatus:'ONLINE',alertStatus:'NORMAL'})
function openAddDialog(){url="/saveDevice";cleanForm();dialogVisible.value=true}
function cleanForm(){form.id='';form.deviceName='';form.deviceNo='';form.deviceType='';form.onlineStatus='ONLINE';form.alertStatus='NORMAL'}
const condForm=reactive({deviceName:'',deviceType:'',onlineStatus:'',alertStatus:'',pageNum:1,pageSize:10})
const deviceList=ref([]);const total=ref(0)
function loadDeviceList(){axios.post("/devicePage",condForm).then(r=>{deviceList.value=r.data.data||[];total.value=r.data.total}).catch(e=>{ElMessage({message:'加载设备列表失败',type:'error'});console.log(e)})}
onMounted(loadDeviceList)
function doDevicePage(pn){condForm.pageNum=pn;loadDeviceList()}
function saveDevice(){axios.post(url,form).then(r=>{if(r.data.code==200){dialogVisible.value=false;cleanForm();doDevicePage(1)};ElMessage({message:r.data.msg,type:r.data.code==200?'success':'error'})}).catch(e=>{ElMessage({message:'保存设备失败',type:'error'});console.log(e)})}
function showDeviceInfo(row){form.id=row.id;form.deviceName=row.deviceName;form.deviceNo=row.deviceNo;form.deviceType=row.deviceType;form.onlineStatus=row.onlineStatus;form.alertStatus=row.alertStatus;dialogVisible.value=true;url="/updateDevice"}
function delDevice(id){axios.get("/deleteDevice?id="+id).then(r=>{if(r.data.code==200)doDevicePage(1);ElMessage({message:r.data.msg,type:r.data.code==200?'success':'error'})}).catch(e=>{ElMessage({message:'删除设备失败',type:'error'});console.log(e)})}
</script>
