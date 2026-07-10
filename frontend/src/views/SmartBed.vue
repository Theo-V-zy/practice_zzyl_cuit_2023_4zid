<template>
  <section class="smartbed-page">
    <div class="search-bar">
      <span>房间号&nbsp;:&nbsp;</span>
      <el-input v-model="condForm.roomNo" style="width: 16%; margin-right: 16px" placeholder="房间号" />
      <el-button type="primary" @click="loadList">搜索</el-button>
    </div>
    <hr />

    <el-table :data="bedList" style="width: 100%" :fit="true" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="bedCode" label="床位编码" width="140" />
      <el-table-column prop="building" label="楼栋" width="70" />
      <el-table-column prop="roomNo" label="房间号" width="90" />
      <el-table-column prop="bedNo" label="床位号" width="80" />
      <el-table-column prop="roomType" label="房型" width="90" />
      <el-table-column prop="deviceNo" label="绑定设备" width="130">
        <template #default="scope">
          <el-tag v-if="scope.row.deviceNo" type="success" size="small">{{ scope.row.deviceNo }}</el-tag>
          <span v-else style="color: #909399">未绑定</span>
        </template>
      </el-table-column>
      <el-table-column label="设备状态" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.deviceNo" type="success" size="small">在线</el-tag>
          <el-tag v-else type="info" size="small">未接入</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="elderName" label="入住老人" width="100" />
      <el-table-column label="操作" min-width="160" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="bindDevice(scope.row)">绑定设备</el-button>
          <el-button type="warning" size="small" @click="unbindDevice(scope.row)"
            :disabled="!scope.row.deviceNo">解绑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doPage" />

    <el-dialog v-model="deviceDialogVisible" title="绑定设备" width="35%">
      <el-form label-width="100px">
        <el-form-item label="床位编码">
          <el-input :value="currentBed?.bedCode" disabled />
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="deviceNo" placeholder="请输入设备编号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="deviceDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doBindDevice">确认绑定</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const bedList = ref([])
const total = ref(0)
const deviceDialogVisible = ref(false)
const deviceNo = ref('')
const currentBed = ref(null)

const condForm = reactive({ roomNo: '', pageNum: 1, pageSize: 10 })

function loadList() {
  loading.value = true
  axios.post('/bedPage', { ...condForm }).then(({ data }) => {
    bedList.value = data.beds || []
    total.value = data.total || 0
  }).catch(() => ElMessage.error('加载失败')).finally(() => { loading.value = false })
}

function bindDevice(row) {
  currentBed.value = row
  deviceNo.value = row.deviceNo || ''
  deviceDialogVisible.value = true
}

function unbindDevice(row) {
  axios.post('/updateBed', { id: row.id, deviceNo: '' }).then(({ data }) => {
    if (data.code === 200) loadList()
    ElMessage(data.msg)
  }).catch(() => ElMessage.error('操作失败'))
}

function doBindDevice() {
  axios.post('/updateBed', { id: currentBed.value.id, deviceNo: deviceNo.value }).then(({ data }) => {
    if (data.code === 200) {
      deviceDialogVisible.value = false
      loadList()
    }
    ElMessage(data.msg)
  }).catch(() => ElMessage.error('绑定失败'))
}

function doPage(pageNum) { condForm.pageNum = pageNum; loadList() }

onMounted(() => { loadList() })
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  margin-bottom: 12px;
}

.search-bar span {
  white-space: nowrap;
}
</style>
