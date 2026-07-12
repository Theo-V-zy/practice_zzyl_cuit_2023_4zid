<template>
  <section class="page-section">
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="来访人姓名" prop="visitorName">
          <el-input v-model="form.visitorName" placeholder="请输入来访人姓名" />
        </el-form-item>
        <el-form-item label="来访人电话" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" placeholder="请输入来访人电话" />
        </el-form-item>
        <el-form-item label="来访类型">
          <el-select v-model="form.visitType" placeholder="请选择" style="width: 100%">
            <el-option label="家属探访" value="FAMILY" />
            <el-option label="朋友探访" value="FRIEND" />
            <el-option label="咨询参观" value="TOUR" />
            <el-option label="其他" value="OTHER" />
          </el-select>
        </el-form-item>
        <el-form-item label="探访老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="请选择老人" style="width: 100%" filterable clearable>
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'" :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="预约时间" v-if="!isArrival">
          <el-date-picker v-model="form.appointmentTime" type="datetime" placeholder="选择预约时间"
            style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="登记类型">
          <el-radio-group v-model="form.visitStage" :disabled="isEdit">
            <el-radio value="RESERVATION">预约</el-radio>
            <el-radio value="ARRIVAL">直接到访</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%" v-if="isEdit">
            <el-option label="待确认" value="PENDING" />
            <el-option label="已确认" value="CONFIRMED" />
            <el-option label="已完成" value="DONE" />
            <el-option label="已取消" value="CANCELED" />
          </el-select>
          <span v-else>待确认</span>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="备注信息" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveVisit">确认</el-button>
      </template>
    </el-dialog>

    <el-form :inline="true" :model="queryForm">
      <el-form-item label="来访人姓名">
        <el-input v-model="queryForm.visitorName" placeholder="姓名" clearable />
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="queryForm.visitorPhone" placeholder="电话" clearable />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="queryForm.visitStage" placeholder="全部" clearable>
          <el-option label="预约" value="RESERVATION" />
          <el-option label="到访" value="ARRIVAL" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="全部" clearable>
          <el-option label="待确认" value="PENDING" />
          <el-option label="已确认" value="CONFIRMED" />
          <el-option label="已完成" value="DONE" />
          <el-option label="已取消" value="CANCELED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom: 12px">
      <el-button type="primary" @click="openAdd('RESERVATION')">预约登记</el-button>
      <el-button type="success" @click="openAdd('ARRIVAL')">来访登记</el-button>
    </div>

    <el-table :data="visitList" style="width: 100%" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="visitNo" label="编号" width="160" />
      <el-table-column prop="visitorName" label="来访人" width="100" />
      <el-table-column prop="visitorPhone" label="电话" width="130" />
      <el-table-column label="类型" width="90">
        <template #default="{ row }">
          <el-tag v-if="row.visitStage === 'RESERVATION'" type="warning" size="small">预约</el-tag>
          <el-tag v-else type="success" size="small">到访</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="visitType" label="来访方式" width="100">
        <template #default="{ row }">
          {{ visitTypeLabel(row.visitType) }}
        </template>
      </el-table-column>
      <el-table-column prop="elderName" label="探访老人" width="100" />
      <el-table-column label="预约时间" width="170">
        <template #default="{ row }">{{ row.appointmentTime || '-' }}</template>
      </el-table-column>
      <el-table-column label="到院时间" width="170">
        <template #default="{ row }">{{ row.arriveTime || '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <span v-if="row.status === 'PENDING'" style="color: #e6a23c">待确认</span>
          <span v-else-if="row.status === 'CONFIRMED'" style="color: #409eff">已确认</span>
          <span v-else-if="row.status === 'DONE'" style="color: #67c23a">已完成</span>
          <span v-else style="color: #909399">已取消</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="140" show-overflow-tooltip />
      <el-table-column label="操作" min-width="240" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button v-if="row.visitStage === 'RESERVATION' && row.status === 'PENDING'" type="success"
            size="small" @click="doConfirmArrive(row)">到院确认</el-button>
          <el-button type="danger" size="small" @click="delVisit(row.id)">删除</el-button>
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
import { onMounted, reactive, ref, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getVisitPage, addVisit, updateVisit, deleteVisit, confirmArrive, getElderPage } from '@/api/admin'

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref(null)
const visitList = ref([])
const elderList = ref([])
const total = ref(0)
const isEdit = ref(false)

const form = reactive({
  id: null,
  visitorName: '',
  visitorPhone: '',
  visitType: '',
  visitStage: 'RESERVATION',
  elderId: null,
  elderName: '',
  appointmentTime: '',
  status: 'PENDING',
  remark: ''
})

const formRules = {
  visitorName: [{ required: true, message: '请输入来访人姓名', trigger: 'blur' }],
  visitorPhone: [{ required: true, message: '请输入来访人电话', trigger: 'blur' }],
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }]
}

const queryForm = reactive({
  visitorName: '',
  visitorPhone: '',
  visitStage: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const isArrival = computed(() => form.visitStage === 'ARRIVAL')
const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑来访信息'
  return form.visitStage === 'RESERVATION' ? '预约登记' : '来访登记'
})

function visitTypeLabel(v) {
  const map = { FAMILY: '家属探访', FRIEND: '朋友探访', TOUR: '咨询参观', OTHER: '其他' }
  return map[v] || v || '-'
}

async function loadList() {
  loading.value = true
  try {
    const res = await getVisitPage(queryForm)
    visitList.value = res.data || []
    total.value = res.total || 0
  } catch (e) {
    // error handled by interceptor
  } finally {
    loading.value = false
  }
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

function openAdd(stage) {
  resetForm()
  form.visitStage = stage
  isEdit.value = false
  dialogVisible.value = true
}

function openEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id,
    visitorName: row.visitorName,
    visitorPhone: row.visitorPhone,
    visitType: row.visitType,
    visitStage: row.visitStage,
    elderId: row.elderId,
    elderName: row.elderName,
    appointmentTime: row.appointmentTime,
    status: row.status,
    remark: row.remark
  })
  isEdit.value = true
  dialogVisible.value = true
}

function resetForm() {
  form.id = null
  form.visitorName = ''
  form.visitorPhone = ''
  form.visitType = ''
  form.visitStage = 'RESERVATION'
  form.elderId = null
  form.elderName = ''
  form.appointmentTime = ''
  form.status = 'PENDING'
  form.remark = ''
  isEdit.value = false
  if (formRef.value) formRef.value.resetFields()
}

async function saveVisit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const payload = { ...form }
  if (payload.elderId) {
    const elder = elderList.value.find(e => e.id === payload.elderId)
    if (elder) payload.elderName = elder.name
  }
  try {
    if (isEdit.value) {
      await updateVisit(payload)
    } else {
      await addVisit(payload)
    }
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    resetForm()
    loadList()
  } catch (e) { }
}

async function delVisit(id) {
  try {
    await ElMessageBox.confirm('确定删除该记录吗？', '提示', { type: 'warning' })
    await deleteVisit(id)
    ElMessage.success('删除成功')
    if (visitList.value.length <= 1 && queryForm.pageNum > 1) {
      queryForm.pageNum--
    }
    loadList()
  } catch (e) { }
}

async function doConfirmArrive(row) {
  try {
    await ElMessageBox.confirm('确认该来访人已到院？', '到院确认', { type: 'info' })
    await confirmArrive({ id: row.id })
    ElMessage.success('到院确认成功')
    loadList()
  } catch (e) { }
}

onMounted(() => {
  loadList()
  loadElders()
})
</script>
