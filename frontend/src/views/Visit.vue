<template>
  <section class="visit-page">
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="45%">
      <el-form label-width="100px" style="margin: 0 5%">
        <el-form-item label="来访人姓名">
          <el-input v-model="form.visitorName" placeholder="请输入来访人姓名" />
        </el-form-item>
        <el-form-item label="来访人电话">
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
        <el-form-item label="探访老人">
          <el-select v-model="form.elderId" placeholder="请选择老人" style="width: 100%" filterable
            clearable>
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'"
              :value="elder.id" />
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

    <div class="search-bar">
      <span>来访人姓名&nbsp;:&nbsp;</span>
      <el-input v-model="condForm.visitorName" style="width: 18%; margin-right: 16px" placeholder="姓名" />
      <span>电话&nbsp;:&nbsp;</span>
      <el-input v-model="condForm.visitorPhone" style="width: 18%; margin-right: 16px" placeholder="电话" />
      <span>类型&nbsp;:&nbsp;</span>
      <el-select v-model="condForm.visitStage" style="width: 14%; margin-right: 16px" placeholder="全部" clearable>
        <el-option label="预约" value="RESERVATION" />
        <el-option label="到访" value="ARRIVAL" />
      </el-select>
      <span>状态&nbsp;:&nbsp;</span>
      <el-select v-model="condForm.status" style="width: 14%; margin-right: 16px" placeholder="全部" clearable>
        <el-option label="待确认" value="PENDING" />
        <el-option label="已确认" value="CONFIRMED" />
        <el-option label="已完成" value="DONE" />
        <el-option label="已取消" value="CANCELED" />
      </el-select>
      <el-button type="primary" @click="loadList">搜索</el-button>
    </div>

    <hr />

    <div style="text-align: left; margin-bottom: 12px">
      <el-button type="primary" @click="openAdd('RESERVATION')">预约登记</el-button>
      <el-button type="success" @click="openAdd('ARRIVAL')">来访登记</el-button>
    </div>

    <el-table :data="visitList" style="width: 100%" :fit="true" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="visitNo" label="编号" width="160" />
      <el-table-column prop="visitorName" label="来访人" width="100" />
      <el-table-column prop="visitorPhone" label="电话" width="130" />
      <el-table-column label="类型" width="90">
        <template #default="scope">
          <el-tag v-if="scope.row.visitStage === 'RESERVATION'" type="warning" size="small">预约</el-tag>
          <el-tag v-else type="success" size="small">到访</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="visitType" label="来访方式" width="100">
        <template #default="scope">
          {{ visitTypeLabel(scope.row.visitType) }}
        </template>
      </el-table-column>
      <el-table-column prop="elderName" label="探访老人" width="100" />
      <el-table-column label="预约时间" width="170">
        <template #default="scope">{{ scope.row.appointmentTime || '-' }}</template>
      </el-table-column>
      <el-table-column label="到院时间" width="170">
        <template #default="scope">{{ scope.row.arriveTime || '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="scope">
          <span v-if="scope.row.status === 'PENDING'" style="color: #e6a23c">待确认</span>
          <span v-else-if="scope.row.status === 'CONFIRMED'" style="color: #409eff">已确认</span>
          <span v-else-if="scope.row.status === 'DONE'" style="color: #67c23a">已完成</span>
          <span v-else style="color: #909399">已取消</span>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" width="140" show-overflow-tooltip />
      <el-table-column label="操作" min-width="240" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEdit(scope.row)">编辑</el-button>
          <el-button v-if="scope.row.visitStage === 'RESERVATION' && scope.row.status === 'PENDING'" type="success"
            size="small" @click="doConfirmArrive(scope.row)">到院确认</el-button>
          <el-button type="danger" size="small" @click="delVisit(scope.row.id)">删除</el-button>
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
const visitList = ref([])
const elderList = ref([])
const total = ref(0)
var url = '/saveVisit'

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

const condForm = reactive({
  visitorName: '',
  visitorPhone: '',
  visitStage: '',
  status: '',
  pageNum: 1,
  pageSize: 10
})

const isEdit = computed(() => !!form.id)
const isArrival = computed(() => form.visitStage === 'ARRIVAL')
const dialogTitle = computed(() => {
  if (isEdit.value) return '编辑来访信息'
  return form.visitStage === 'RESERVATION' ? '预约登记' : '来访登记'
})

function visitTypeLabel(v) {
  const map = { FAMILY: '家属探访', FRIEND: '朋友探访', TOUR: '咨询参观', OTHER: '其他' }
  return map[v] || v || '-'
}

function loadList() {
  loading.value = true
  axios.post('/visitPage', condForm).then(({ data }) => {
    visitList.value = data.visits || []
    total.value = data.total || 0
  }).catch(() => {
    ElMessage.error('加载列表失败')
  }).finally(() => { loading.value = false })
}

function loadElders() {
  axios.post('/elderPage', { pageNum: 1, pageSize: 1000 }).then(({ data }) => {
    elderList.value = data.elders || []
  }).catch(() => { })
}

function openAdd(stage) {
  cleanForm()
  form.visitStage = stage
  url = '/saveVisit'
  dialogVisible.value = true
}

function openEdit(row) {
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
  url = '/updateVisit'
  dialogVisible.value = true
}

function cleanForm() {
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
}

function saveVisit() {
  const payload = { ...form }
  if (payload.elderId) {
    const elder = elderList.value.find(e => e.id === payload.elderId)
    if (elder) payload.elderName = elder.name
  }
  axios.post(url, payload).then(({ data }) => {
    if (data.code === 200) {
      dialogVisible.value = false
      cleanForm()
      loadList()
    }
    ElMessage(data.msg)
  }).catch(() => ElMessage.error('操作失败'))
}

function delVisit(id) {
  ElMessageBox.confirm('确定删除该记录吗？', '提示', { type: 'warning' }).then(() => {
    axios.get('/deleteVisit?id=' + id).then(({ data }) => {
      if (data.code === 200) doPage(1)
      ElMessage(data.msg)
    }).catch(() => ElMessage.error('删除失败'))
  }).catch(() => { })
}

function doConfirmArrive(row) {
  ElMessageBox.confirm('确认该来访人已到院？', '到院确认', { type: 'info' }).then(() => {
    axios.post('/confirmArrive', { id: row.id }).then(({ data }) => {
      if (data.code === 200) loadList()
      ElMessage(data.msg)
    }).catch(() => ElMessage.error('操作失败'))
  }).catch(() => { })
}

function doPage(pageNum) {
  condForm.pageNum = pageNum
  loadList()
}

onMounted(() => {
  loadList()
  loadElders()
})
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
