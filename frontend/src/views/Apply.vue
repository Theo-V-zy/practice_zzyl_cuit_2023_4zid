<template>
  <section class="page-section">
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="申请类型">
          <el-radio-group v-model="form.applyType" :disabled="isEdit">
            <el-radio value="CHECKIN">入住申请</el-radio>
            <el-radio value="CHECKOUT">退住申请</el-radio>
            <el-radio value="LEAVE">请假申请</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="请选择老人" style="width: 100%" filterable
            @change="onElderChange">
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'"
              :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择床位" v-if="form.applyType === 'CHECKIN'">
          <el-select v-model="form.bedId" placeholder="请选择床位" style="width: 100%" filterable>
            <el-option v-for="bed in emptyBedList" :key="bed.id"
              :label="bed.building + ' ' + bed.roomNo + ' ' + bed.bedNo + ' (' + bed.roomType + ')'" :value="bed.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" v-if="form.applyType !== 'CHECKOUT'">
          <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择时间"
            style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="结束时间" v-if="form.applyType === 'LEAVE'">
          <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择时间"
            style="width: 100%" value-format="YYYY-MM-DD HH:mm:ss" />
        </el-form-item>
        <el-form-item label="申请原因">
          <el-input v-model="form.reason" type="textarea" placeholder="请输入申请原因" />
        </el-form-item>
        <el-form-item label="状态" v-if="isEdit">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="待处理" value="PENDING" />
            <el-option label="已通过" value="APPROVED" />
            <el-option label="已驳回" value="REJECTED" />
            <el-option label="已完成" value="DONE" />
          </el-select>
        </el-form-item>
        <el-form-item label="审批意见" v-if="isEdit">
          <el-input v-model="form.approveComment" type="textarea" placeholder="审批意见" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveApply">确认</el-button>
      </template>
    </el-dialog>

    <el-form :inline="true" :model="queryForm">
      <el-form-item label="类型">
        <el-select v-model="queryForm.applyType" placeholder="全部" clearable>
          <el-option label="入住" value="CHECKIN" />
          <el-option label="退住" value="CHECKOUT" />
          <el-option label="请假" value="LEAVE" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="全部" clearable>
          <el-option label="待处理" value="PENDING" />
          <el-option label="已通过" value="APPROVED" />
          <el-option label="已驳回" value="REJECTED" />
          <el-option label="已完成" value="DONE" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom: 12px">
      <el-button type="primary" @click="openAdd('CHECKIN')">入住办理</el-button>
      <el-button type="warning" @click="openAdd('CHECKOUT')">退住办理</el-button>
      <el-button type="success" @click="openAdd('LEAVE')">请假申请</el-button>
    </div>

    <el-table :data="applyList" style="width: 100%" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="applyNo" label="编号" width="170" />
      <el-table-column label="类型" width="80">
        <template #default="{ row }">
          <el-tag v-if="row.applyType === 'CHECKIN'" type="primary" size="small">入住</el-tag>
          <el-tag v-else-if="row.applyType === 'CHECKOUT'" type="warning" size="small">退住</el-tag>
          <el-tag v-else type="success" size="small">请假</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="elderName" label="老人" width="100" />
      <el-table-column prop="reason" label="原因" width="160" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <span v-if="row.status === 'PENDING'" style="color: #e6a23c">待处理</span>
          <span v-else-if="row.status === 'APPROVED'" style="color: #409eff">已通过</span>
          <span v-else-if="row.status === 'REJECTED'" style="color: #f56c6c">已驳回</span>
          <span v-else-if="row.status === 'DONE'" style="color: #67c23a">已完成</span>
          <span v-else style="color: #909399">{{ row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="150" />
      <el-table-column prop="endTime" label="结束时间" width="150" />
      <el-table-column label="操作" min-width="255" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button v-if="row.status === 'PENDING'" type="success" size="small"
            @click="doApprove(row, 'APPROVED')">通过</el-button>
          <el-button v-if="row.status === 'PENDING'" type="danger" size="small"
            @click="doApprove(row, 'REJECTED')">驳回</el-button>
          <el-button type="danger" size="small" @click="delApply(row.id)">删除</el-button>
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
import { getApplyPage, addApply, updateApply, deleteApply, approveApply, getElderPage, getBedPage, addContract, getContractPage } from '@/api/admin'
import { updateElder } from '@/api/admin'
import { updateBed } from '@/api/admin'

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref(null)
const applyList = ref([])
const elderList = ref([])
const emptyBedList = ref([])
const total = ref(0)
const isEdit = ref(false)

const form = reactive({
  id: null, applyType: 'CHECKIN', elderId: null, bedId: null,
  reason: '', status: 'PENDING', approveComment: '', startTime: '', endTime: ''
})

const formRules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }]
}

const queryForm = reactive({ applyType: '', status: '', pageNum: 1, pageSize: 10 })

const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑申请'
  if (form.applyType === 'CHECKIN') return '入住办理'
  if (form.applyType === 'CHECKOUT') return '退住办理'
  return '请假申请'
})

async function loadList() {
  loading.value = true
  try {
    const res = await getApplyPage(queryForm)
    applyList.value = res.data || []
    total.value = res.total || 0
  } catch (e) { } finally { loading.value = false }
}

async function loadElders() {
  try {
    const res = await getElderPage({ pageNum: 1, pageSize: 1000 })
    elderList.value = res.data || []
  } catch (e) { }
}

async function loadEmptyBeds() {
  try {
    const res = await getBedPage({ status: 'EMPTY', pageNum: 1, pageSize: 1000 })
    emptyBedList.value = res.data || []
  } catch (e) { }
}

function handleQuery() {
  queryForm.pageNum = 1
  loadList()
}

function openAdd(type) {
  resetForm()
  form.applyType = type
  if (type === 'CHECKIN') loadEmptyBeds()
  dialogVisible.value = true
}

function openEdit(row) {
  resetForm()
  Object.assign(form, {
    id: row.id, applyType: row.applyType, elderId: row.elderId,
    reason: row.reason, status: row.status, approveComment: row.approveComment,
    startTime: row.startTime, endTime: row.endTime
  })
  isEdit.value = true
  dialogVisible.value = true
}

function resetForm() {
  form.id = null; form.applyType = 'CHECKIN'; form.elderId = null; form.bedId = null
  form.reason = ''; form.status = 'PENDING'; form.approveComment = ''
  form.startTime = ''; form.endTime = ''
  isEdit.value = false
  if (formRef.value) formRef.value.resetFields()
}

function onElderChange(id) {
  const elder = elderList.value.find(e => e.id === id)
  if (elder) form.elderName = elder.name
}

async function saveApply() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const payload = { ...form }
  const elder = elderList.value.find(e => e.id === payload.elderId)
  if (elder) payload.elderName = elder.name
  try {
    if (isEdit.value) {
      await updateApply(payload)
    } else {
      await addApply(payload)
    }
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    resetForm()
    loadList()
  } catch (e) { }
}

async function doApprove(row, status) {
  try {
    await ElMessageBox.confirm('确认执行该审批操作？', '审批', { type: 'info' })
    await approveApply({ id: row.id, status, approveComment: '' })
    if (status === 'APPROVED') await handlePostApprove(row)
    ElMessage.success('审批成功')
    loadList()
  } catch (e) { }
}

async function handlePostApprove(row) {
  try {
    const elder = elderList.value.find(e => e.id === row.elderId)
    if (!elder) return
    if (row.applyType === 'CHECKIN') {
      await updateElder({ id: elder.id, status: 'IN' })
      if (row.bedId) {
        await updateBed({ id: row.bedId, status: 'OCCUPIED', elderId: row.elderId })
        await addContract({ elderId: row.elderId, bedId: row.bedId, status: 'ACTIVE' })
      }
    } else if (row.applyType === 'CHECKOUT') {
      await updateElder({ id: elder.id, status: 'CHECKED_OUT' })
      const res = await getBedPage({ pageNum: 1, pageSize: 1 })
      const beds = res.data || []
      const occupied = beds.find(b => b.elderId === row.elderId)
      if (occupied) await updateBed({ id: occupied.id, status: 'EMPTY', elderId: null })
    } else if (row.applyType === 'LEAVE') {
      await updateElder({ id: elder.id, status: 'LEAVE' })
    }
  } catch (e) { }
}

async function delApply(id) {
  try {
    await ElMessageBox.confirm('确定删除该申请？', '提示', { type: 'warning' })
    await deleteApply(id)
    ElMessage.success('删除成功')
    if (applyList.value.length <= 1 && queryForm.pageNum > 1) queryForm.pageNum--
    loadList()
  } catch (e) { }
}

onMounted(() => { loadList(); loadElders() })
</script>
