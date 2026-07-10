<template>
  <section class="page-section">
    <el-form :inline="true" :model="queryForm">
      <el-form-item label="房间号">
        <el-input v-model="queryForm.roomNo" placeholder="房间号" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <el-table :data="bedList" style="width: 100%" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="bedCode" label="床位编码" width="140" />
      <el-table-column prop="building" label="楼栋" width="70" />
      <el-table-column prop="roomNo" label="房间号" width="90" />
      <el-table-column prop="bedNo" label="床位号" width="80" />
      <el-table-column prop="roomType" label="房型" width="90" />
      <el-table-column prop="deviceNo" label="绑定设备" width="130">
        <template #default="{ row }">
          <el-tag v-if="row.deviceNo" type="success" size="small">{{ row.deviceNo }}</el-tag>
          <span v-else style="color: #909399">未绑定</span>
        </template>
      </el-table-column>
      <el-table-column label="设备状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.deviceNo" type="success" size="small">在线</el-tag>
          <el-tag v-else type="info" size="small">未接入</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="elderName" label="入住老人" width="100" />
      <el-table-column label="操作" min-width="160" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="bindDevice(row)">绑定设备</el-button>
          <el-button type="warning" size="small" @click="unbindDevice(row)"
            :disabled="!row.deviceNo">解绑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="queryForm.pageNum" v-model:page-size="queryForm.pageSize"
      :page-sizes="[10, 20, 50]" :total="total"
      layout="total, sizes, prev, pager, next"
      @size-change="handleQuery" @current-change="handleQuery" />

    <el-dialog v-model="deviceDialogVisible" title="绑定设备" width="420px">
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
import { ElMessage } from 'element-plus'
import { getBedPage, updateBed } from '@/api/admin'

const loading = ref(false)
const bedList = ref([])
const total = ref(0)
const deviceDialogVisible = ref(false)
const deviceNo = ref('')
const currentBed = ref(null)

const queryForm = reactive({ roomNo: '', pageNum: 1, pageSize: 10 })

async function loadList() {
  loading.value = true
  try {
    const res = await getBedPage({ ...queryForm })
    bedList.value = res.data || []
    total.value = res.total || 0
  } catch (e) { } finally { loading.value = false }
}

function handleQuery() {
  queryForm.pageNum = 1
  loadList()
}

function bindDevice(row) {
  currentBed.value = row
  deviceNo.value = row.deviceNo || ''
  deviceDialogVisible.value = true
}

async function unbindDevice(row) {
  try {
    await updateBed({ id: row.id, deviceNo: '' })
    ElMessage.success('解绑成功')
    loadList()
  } catch (e) { }
}

async function doBindDevice() {
  try {
    await updateBed({ id: currentBed.value.id, deviceNo: deviceNo.value })
    ElMessage.success('绑定成功')
    deviceDialogVisible.value = false
    loadList()
  } catch (e) { }
}

onMounted(() => { loadList() })
</script>
