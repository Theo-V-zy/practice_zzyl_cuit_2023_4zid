<template>
  <section class="apply-page">
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
      <el-form label-width="100px" style="margin: 0 5%">
        <el-form-item label="申请类型">
          <el-radio-group v-model="form.applyType" :disabled="isEdit">
            <el-radio value="CHECKIN">入住申请</el-radio>
            <el-radio value="CHECKOUT">退住申请</el-radio>
            <el-radio value="LEAVE">请假申请</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="选择老人">
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

    <div class="search-bar">
      <span>类型&nbsp;:&nbsp;</span>
      <el-select v-model="condForm.applyType" style="width: 16%; margin-right: 16px" placeholder="全部" clearable>
        <el-option label="入住" value="CHECKIN" />
        <el-option label="退住" value="CHECKOUT" />
        <el-option label="请假" value="LEAVE" />
      </el-select>
      <span>状态&nbsp;:&nbsp;</span>
      <el-select v-model="condForm.status" style="width: 16%; margin-right: 16px" placeholder="全部" clearable>
        <el-option label="待处理" value="PENDING" />
        <el-option label="已通过" value="APPROVED" />
        <el-option label="已驳回" value="REJECTED" />
        <el-option label="已完成" value="DONE" />
      </el-select>
      <el-button type="primary" @click="loadList">搜索</el-button>
    </div>
    <hr />
    <div style="text-align: left; margin-bottom: 12px">
      <el-button type="primary" @click="openAdd('CHECKIN')">入住办理</el-button>
      <el-button type="warning" @click="openAdd('CHECKOUT')">退住办理</el-button>
      <el-button type="success" @click="openAdd('LEAVE')">请假申请</el-button>
    </div>

    <el-table :data="applyList" style="width: 100%" :fit="true" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="applyNo" label="编号" width="170" />
      <el-table-column label="类型" width="80">
        <template #default="scope">
          <el-tag v-if="scope.row.applyType === 'CHECKIN'" type="primary" size="small">入住</el-tag>
          <el-tag v-else-if="scope.row.applyType === 'CHECKOUT'" type="warning" size="small">退住</el-tag>
          <el-tag v-else type="success" size="small">请假</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="elderName" label="老人" width="100" />
      <el-table-column prop="reason" label="原因" width="160" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="scope">
          <span v-if="scope.row.status === 'PENDING'" style="color: #e6a23c">待处理</span>
          <span v-else-if="scope.row.status === 'APPROVED'" style="color: #409eff">已通过</span>
          <span v-else-if="scope.row.status === 'REJECTED'" style="color: #f56c6c">已驳回</span>
          <span v-else-if="scope.row.status === 'DONE'" style="color: #67c23a">已完成</span>
          <span v-else style="color: #909399">{{ scope.row.status }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" width="160" />
      <el-table-column prop="endTime" label="结束时间" width="160" />
      <el-table-column label="操作" min-width="250" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEdit(scope.row)">编辑</el-button>
          <el-button v-if="scope.row.status === 'PENDING'" type="success" size="small"
            @click="doApprove(scope.row, 'APPROVED')">通过</el-button>
          <el-button v-if="scope.row.status === 'PENDING'" type="danger" size="small"
            @click="doApprove(scope.row, 'REJECTED')">驳回</el-button>
          <el-button type="danger" size="small" @click="delApply(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doPage" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref, computed } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const dialogVisible = ref(false)
const loading = ref(false)
const applyList = ref([])
const elderList = ref([])
const emptyBedList = ref([])
const total = ref(0)
var url = '/saveApply'

const form = reactive({
  id: null, applyType: 'CHECKIN', elderId: null, bedId: null,
  reason: '', status: 'PENDING', approveComment: '', startTime: '', endTime: ''
})

const condForm = reactive({ applyType: '', status: '', pageNum: 1, pageSize: 10 })

const isEdit = computed(() => !!form.id)
const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑申请'
  if (form.applyType === 'CHECKIN') return '入住办理'
  if (form.applyType === 'CHECKOUT') return '退住办理'
  return '请假申请'
})

function loadList() {
  loading.value = true
  axios.post('/applyPage', condForm).then(({ data }) => {
    applyList.value = data.applies || []
    total.value = data.total || 0
  }).catch(() => ElMessage.error('加载失败')).finally(() => { loading.value = false })
}

function loadElders() {
  axios.post('/elderPage', { pageNum: 1, pageSize: 1000 }).then(({ data }) => {
    elderList.value = data.elders || []
  }).catch(() => { })
}

function loadEmptyBeds() {
  axios.post('/bedPage', { status: 'EMPTY', pageNum: 1, pageSize: 1000 }).then(({ data }) => {
    emptyBedList.value = data.beds || []
  }).catch(() => { })
}

function openAdd(type) {
  cleanForm()
  form.applyType = type
  url = '/saveApply'
  if (type === 'CHECKIN') loadEmptyBeds()
  dialogVisible.value = true
}

function openEdit(row) {
  Object.assign(form, {
    id: row.id, applyType: row.applyType, elderId: row.elderId,
    reason: row.reason, status: row.status, approveComment: row.approveComment,
    startTime: row.startTime, endTime: row.endTime
  })
  url = '/updateApply'
  dialogVisible.value = true
}

function cleanForm() {
  form.id = null; form.applyType = 'CHECKIN'; form.elderId = null; form.bedId = null
  form.reason = ''; form.status = 'PENDING'; form.approveComment = ''
  form.startTime = ''; form.endTime = ''
}

function onElderChange(id) {
  const elder = elderList.value.find(e => e.id === id)
  if (elder) form.elderName = elder.name
}

function saveApply() {
  const payload = { ...form }
  const elder = elderList.value.find(e => e.id === payload.elderId)
  if (elder) payload.elderName = elder.name
  axios.post(url, payload).then(({ data }) => {
    if (data.code === 200) {
      dialogVisible.value = false
      cleanForm()
      loadList()
    }
    ElMessage(data.msg)
  }).catch(() => ElMessage.error('操作失败'))
}

function doApprove(row, status) {
  ElMessageBox.confirm('确认执行该审批操作？', '审批', { type: 'info' }).then(() => {
    axios.post('/approveApply', { id: row.id, status, approveComment: '' }).then(({ data }) => {
      if (data.code === 200) {
        if (status === 'APPROVED') handlePostApprove(row)
        loadList()
      }
      ElMessage(data.msg)
    }).catch(() => ElMessage.error('操作失败'))
  }).catch(() => { })
}

function handlePostApprove(row) {
  const elder = elderList.value.find(e => e.id === row.elderId)
  if (!elder) return
  if (row.applyType === 'CHECKIN') {
    axios.post('/updateElder', { id: elder.id, status: 'IN' })
    if (row.bedId) {
      axios.post('/updateBed', { id: row.bedId, status: 'OCCUPIED', elderId: row.elderId })
      axios.post('/saveContract', { elderId: row.elderId, bedId: row.bedId, status: 'ACTIVE' })
    }
  } else if (row.applyType === 'CHECKOUT') {
    axios.post('/updateElder', { id: elder.id, status: 'CHECKED_OUT' })
    axios.post('/bedPage', { pageNum: 1, pageSize: 1 }).then(({ data }) => {
      const beds = data.beds || []
      const occupied = beds.find(b => b.elderId === row.elderId)
      if (occupied) axios.post('/updateBed', { id: occupied.id, status: 'EMPTY', elderId: null })
    })
  } else if (row.applyType === 'LEAVE') {
    axios.post('/updateElder', { id: elder.id, status: 'LEAVE' })
  }
}

function delApply(id) {
  ElMessageBox.confirm('确定删除该申请？', '提示', { type: 'warning' }).then(() => {
    axios.get('/deleteApply?id=' + id).then(({ data }) => {
      if (data.code === 200) doPage(1)
      ElMessage(data.msg)
    }).catch(() => ElMessage.error('删除失败'))
  }).catch(() => { })
}

function doPage(pageNum) { condForm.pageNum = pageNum; loadList() }

onMounted(() => { loadList(); loadElders() })
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
