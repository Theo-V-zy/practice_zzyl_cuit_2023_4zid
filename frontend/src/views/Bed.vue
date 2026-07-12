<template>
  <section class="page-section">
    <el-dialog v-model="dialogVisible" title="床位信息" width="520px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="楼栋" prop="building">
          <el-input v-model="form.building" placeholder="如 A栋" />
        </el-form-item>
        <el-form-item label="楼层" prop="floor">
          <el-input v-model="form.floor" placeholder="如 3层" />
        </el-form-item>
        <el-form-item label="房间号" prop="roomNo">
          <el-input v-model="form.roomNo" placeholder="如 A301" />
        </el-form-item>
        <el-form-item label="房型">
          <el-select v-model="form.roomType" style="width: 100%">
            <el-option label="单人间" value="单人间" />
            <el-option label="双人间" value="双人间" />
            <el-option label="豪华间" value="豪华间" />
            <el-option label="套间" value="套间" />
          </el-select>
        </el-form-item>
        <el-form-item label="床位号">
          <el-input v-model="form.bedNo" placeholder="如 1号床" />
        </el-form-item>
        <el-form-item label="床位编码" prop="bedCode">
          <el-input v-model="form.bedCode" placeholder="如 BED-A301-1" />
        </el-form-item>
        <el-form-item label="床位费">
          <el-input-number v-model="form.bedPrice" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="入住老人">
          <el-select v-model="form.elderId" placeholder="选择老人（可选）" style="width: 100%" filterable clearable>
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'"
              :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="form.deviceNo" placeholder="绑定设备编号（可选）" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="空闲" value="EMPTY" />
            <el-option label="已占用" value="OCCUPIED" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBed">确认</el-button>
      </template>
    </el-dialog>

    <el-form :inline="true" :model="queryForm">
      <el-form-item label="房间号">
        <el-input v-model="queryForm.roomNo" placeholder="房间号" clearable />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="全部" clearable>
          <el-option label="空闲" value="EMPTY" />
          <el-option label="已占用" value="OCCUPIED" />
          <el-option label="维护中" value="MAINTENANCE" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom: 12px">
      <el-button type="primary" @click="openAdd">添加床位</el-button>
    </div>

    <el-table :data="bedList" style="width: 100%" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="building" label="楼栋" width="70" />
      <el-table-column prop="floor" label="楼层" width="70" />
      <el-table-column prop="roomNo" label="房间号" width="90" />
      <el-table-column prop="roomType" label="房型" width="90" />
      <el-table-column prop="bedNo" label="床位号" width="80" />
      <el-table-column prop="bedCode" label="编码" width="130" />
      <el-table-column prop="bedPrice" label="费用" width="80" />
      <el-table-column prop="elderName" label="入住老人" width="100" />
      <el-table-column prop="deviceNo" label="设备" width="100" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'EMPTY'" type="success" size="small">空闲</el-tag>
          <el-tag v-else-if="row.status === 'OCCUPIED'" type="warning" size="small">已占用</el-tag>
          <el-tag v-else type="danger" size="small">维护中</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="140" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="delBed(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination v-model:current-page="queryForm.pageNum" v-model:page-size="queryForm.pageSize"
      :page-sizes="[10, 20, 50]" :total="total"
      layout="total, sizes, prev, pager, next"
      @size-change="handleQuery" @current-change="handleQuery" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getBedPage, addBed, updateBed, deleteBed, getElderPage } from '@/api/admin'

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref(null)
const bedList = ref([])
const elderList = ref([])
const total = ref(0)
const isEdit = ref(false)

const form = reactive({
  id: null, building: '', floor: '', roomNo: '', roomType: '', bedNo: '', bedCode: '',
  bedPrice: 0, elderId: null, deviceNo: '', status: 'EMPTY', remark: ''
})

const formRules = {
  building: [{ required: true, message: '请输入楼栋', trigger: 'blur' }],
  floor: [{ required: true, message: '请输入楼层', trigger: 'blur' }],
  roomNo: [{ required: true, message: '请输入房间号', trigger: 'blur' }],
  bedCode: [{ required: true, message: '请输入床位编码', trigger: 'blur' }]
}

const queryForm = reactive({ roomNo: '', status: '', pageNum: 1, pageSize: 10 })

async function loadList() {
  loading.value = true
  try {
    const res = await getBedPage(queryForm)
    bedList.value = res.data || []
    total.value = res.total || 0
  } catch (e) { } finally { loading.value = false }
}

async function loadElders() {
  try {
    const res = await getElderPage({ pageNum: 1, pageSize: 1000 })
    elderList.value = res.data || []
  } catch (e) { }
}

function handleQuery() {
  queryForm.pageNum = 1
  loadList()
}

function openAdd() {
  resetForm()
  dialogVisible.value = true
}

function openEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id, building: row.building, floor: row.floor, roomNo: row.roomNo,
    roomType: row.roomType, bedNo: row.bedNo, bedCode: row.bedCode,
    bedPrice: row.bedPrice, elderId: row.elderId, deviceNo: row.deviceNo,
    status: row.status, remark: row.remark
  })
  isEdit.value = true
  dialogVisible.value = true
}

function resetForm() {
  form.id = null; form.building = ''; form.floor = ''; form.roomNo = ''; form.roomType = ''
  form.bedNo = ''; form.bedCode = ''; form.bedPrice = 0; form.elderId = null
  form.deviceNo = ''; form.status = 'EMPTY'; form.remark = ''
  isEdit.value = false
  if (formRef.value) formRef.value.resetFields()
}

async function saveBed() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const payload = { ...form }
  const elder = elderList.value.find(e => e.id === payload.elderId)
  if (elder) payload.elderName = elder.name
  try {
    if (isEdit.value) {
      await updateBed(payload)
    } else {
      await addBed(payload)
    }
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    resetForm()
    loadList()
  } catch (e) { }
}

async function delBed(id) {
  try {
    await ElMessageBox.confirm('确定删除该床位？', '提示', { type: 'warning' })
    await deleteBed(id)
    ElMessage.success('删除成功')
    if (bedList.value.length <= 1 && queryForm.pageNum > 1) queryForm.pageNum--
    loadList()
  } catch (e) { }
}

onMounted(() => { loadList(); loadElders() })
</script>
