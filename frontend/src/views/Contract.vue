<template>
  <section class="contract-page">
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="45%">
      <el-form label-width="100px" style="margin: 0 5%">
        <el-form-item label="选择老人">
          <el-select v-model="form.elderId" placeholder="请选择老人" style="width: 100%" filterable>
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'"
              :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择床位">
          <el-select v-model="form.bedId" placeholder="请选择床位" style="width: 100%" filterable>
            <el-option v-for="bed in bedList" :key="bed.id"
              :label="bed.building + ' ' + bed.roomNo + ' ' + bed.bedNo" :value="bed.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期">
          <el-date-picker v-model="form.startDate" type="date" placeholder="选择日期"
            style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="结束日期">
          <el-date-picker v-model="form.endDate" type="date" placeholder="选择日期"
            style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="月费">
          <el-input-number v-model="form.monthlyFee" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="押金">
          <el-input-number v-model="form.deposit" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="家属性别">
          <el-input v-model="form.familyId" placeholder="家属ID（可选）" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="生效中" value="ACTIVE" />
            <el-option label="已失效" value="EXPIRED" />
            <el-option label="已终止" value="TERMINATED" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveContract">确认</el-button>
      </template>
    </el-dialog>

    <div class="search-bar">
      <span>状态&nbsp;:&nbsp;</span>
      <el-select v-model="condForm.status" style="width: 16%; margin-right: 16px" placeholder="全部" clearable>
        <el-option label="生效中" value="ACTIVE" />
        <el-option label="已失效" value="EXPIRED" />
        <el-option label="已终止" value="TERMINATED" />
      </el-select>
      <el-button type="primary" @click="loadList">搜索</el-button>
    </div>
    <hr />
    <div style="text-align: left; margin-bottom: 12px">
      <el-button type="primary" @click="openAdd">新增合同</el-button>
    </div>

    <el-table :data="contractList" style="width: 100%" :fit="true" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="contractNo" label="合同编号" width="170" />
      <el-table-column prop="elderName" label="老人" width="100" />
      <el-table-column prop="bedNo" label="床位" width="120" />
      <el-table-column prop="startDate" label="开始日期" width="110" />
      <el-table-column prop="endDate" label="结束日期" width="110" />
      <el-table-column prop="monthlyFee" label="月费" width="90" />
      <el-table-column prop="deposit" label="押金" width="90" />
      <el-table-column label="状态" width="90">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 'ACTIVE'" type="success" size="small">生效中</el-tag>
          <el-tag v-else-if="scope.row.status === 'EXPIRED'" type="warning" size="small">已失效</el-tag>
          <el-tag v-else type="danger" size="small">已终止</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="140" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="delContract(scope.row.id)">删除</el-button>
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
const contractList = ref([])
const elderList = ref([])
const bedList = ref([])
const total = ref(0)
var url = '/saveContract'

const form = reactive({
  id: null, elderId: null, bedId: null, familyId: null,
  startDate: '', endDate: '', monthlyFee: 0, deposit: 0, status: 'ACTIVE'
})

const condForm = reactive({ status: '', pageNum: 1, pageSize: 10 })

const dialogTitle = computed(() => form.id ? '编辑合同' : '新增合同')

function loadList() {
  loading.value = true
  axios.post('/contractPage', condForm).then(({ data }) => {
    contractList.value = data.contracts || []
    total.value = data.total || 0
  }).catch(() => ElMessage.error('加载失败')).finally(() => { loading.value = false })
}

function loadElders() {
  axios.post('/elderPage', { pageNum: 1, pageSize: 1000 }).then(({ data }) => {
    elderList.value = data.elders || []
  }).catch(() => { })
}

function loadBeds() {
  axios.post('/bedPage', { pageNum: 1, pageSize: 1000 }).then(({ data }) => {
    bedList.value = data.beds || []
  }).catch(() => { })
}

function openAdd() {
  cleanForm()
  url = '/saveContract'
  dialogVisible.value = true
}

function openEdit(row) {
  Object.assign(form, {
    id: row.id, elderId: row.elderId, bedId: row.bedId,
    familyId: row.familyId, startDate: row.startDate, endDate: row.endDate,
    monthlyFee: row.monthlyFee, deposit: row.deposit, status: row.status
  })
  url = '/updateContract'
  dialogVisible.value = true
}

function cleanForm() {
  form.id = null; form.elderId = null; form.bedId = null; form.familyId = null
  form.startDate = ''; form.endDate = ''; form.monthlyFee = 0; form.deposit = 0; form.status = 'ACTIVE'
}

function saveContract() {
  const payload = { ...form }
  const elder = elderList.value.find(e => e.id === payload.elderId)
  if (elder) payload.elderName = elder.name
  const bed = bedList.value.find(b => b.id === payload.bedId)
  if (bed) payload.bedNo = bed.bedNo
  axios.post(url, payload).then(({ data }) => {
    if (data.code === 200) {
      dialogVisible.value = false
      cleanForm()
      loadList()
    }
    ElMessage(data.msg)
  }).catch(() => ElMessage.error('操作失败'))
}

function delContract(id) {
  ElMessageBox.confirm('确定删除该合同？', '提示', { type: 'warning' }).then(() => {
    axios.get('/deleteContract?id=' + id).then(({ data }) => {
      if (data.code === 200) doPage(1)
      ElMessage(data.msg)
    }).catch(() => ElMessage.error('删除失败'))
  }).catch(() => { })
}

function doPage(pageNum) { condForm.pageNum = pageNum; loadList() }

onMounted(() => { loadList(); loadElders(); loadBeds() })
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
