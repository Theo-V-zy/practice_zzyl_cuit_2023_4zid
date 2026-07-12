<template>
  <section class="page-section">
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="520px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="选择老人" prop="elderId">
          <el-select v-model="form.elderId" placeholder="请选择老人" style="width: 100%" filterable>
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'"
              :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择床位" prop="bedId">
          <el-select v-model="form.bedId" placeholder="请选择床位" style="width: 100%" filterable>
            <el-option v-for="bed in bedList" :key="bed.id"
              :label="bed.building + ' ' + bed.roomNo + ' ' + bed.bedNo" :value="bed.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始日期" prop="startDate">
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
        <el-form-item label="家属ID">
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

    <el-form :inline="true" :model="queryForm">
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="全部" clearable>
          <el-option label="生效中" value="ACTIVE" />
          <el-option label="已失效" value="EXPIRED" />
          <el-option label="已终止" value="TERMINATED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
      </el-form-item>
    </el-form>

    <div style="margin-bottom: 12px">
      <el-button type="primary" @click="openAdd">新增合同</el-button>
    </div>

    <el-table :data="contractList" style="width: 100%" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="contractNo" label="合同编号" width="170" />
      <el-table-column prop="elderName" label="老人" width="100" />
      <el-table-column prop="bedNo" label="床位" width="120" />
      <el-table-column prop="startDate" label="开始日期" width="110" />
      <el-table-column prop="endDate" label="结束日期" width="110" />
      <el-table-column prop="monthlyFee" label="月费" width="90" />
      <el-table-column prop="deposit" label="押金" width="90" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag v-if="row.status === 'ACTIVE'" type="success" size="small">生效中</el-tag>
          <el-tag v-else-if="row.status === 'EXPIRED'" type="warning" size="small">已失效</el-tag>
          <el-tag v-else type="danger" size="small">已终止</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="140" fixed="right">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="openEdit(row)">编辑</el-button>
          <el-button type="danger" size="small" @click="delContract(row.id)">删除</el-button>
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
import { getContractPage, addContract, updateContract, deleteContract, getElderPage, getBedPage } from '@/api/admin'

const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref(null)
const contractList = ref([])
const elderList = ref([])
const bedList = ref([])
const total = ref(0)
const isEdit = ref(false)

const form = reactive({
  id: null, elderId: null, bedId: null, familyId: null,
  startDate: '', endDate: '', monthlyFee: 0, deposit: 0, status: 'ACTIVE'
})

const formRules = {
  elderId: [{ required: true, message: '请选择老人', trigger: 'change' }],
  bedId: [{ required: true, message: '请选择床位', trigger: 'change' }],
  startDate: [{ required: true, message: '请选择开始日期', trigger: 'change' }]
}

const queryForm = reactive({ status: '', pageNum: 1, pageSize: 10 })

const dialogTitle = computed(() => isEdit.value ? '编辑合同' : '新增合同')

async function loadList() {
  loading.value = true
  try {
    const res = await getContractPage(queryForm)
    contractList.value = res.data || []
    total.value = res.total || 0
  } catch (e) { } finally { loading.value = false }
}

async function loadElders() {
  try {
    const res = await getElderPage({ pageNum: 1, pageSize: 1000 })
    elderList.value = res.data || []
  } catch (e) { }
}

async function loadBeds() {
  try {
    const res = await getBedPage({ pageNum: 1, pageSize: 1000 })
    bedList.value = res.data || []
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
    id: row.id, elderId: row.elderId, bedId: row.bedId,
    familyId: row.familyId, startDate: row.startDate, endDate: row.endDate,
    monthlyFee: row.monthlyFee, deposit: row.deposit, status: row.status
  })
  isEdit.value = true
  dialogVisible.value = true
}

function resetForm() {
  form.id = null; form.elderId = null; form.bedId = null; form.familyId = null
  form.startDate = ''; form.endDate = ''; form.monthlyFee = 0; form.deposit = 0; form.status = 'ACTIVE'
  isEdit.value = false
  if (formRef.value) formRef.value.resetFields()
}

async function saveContract() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  const payload = { ...form }
  const elder = elderList.value.find(e => e.id === payload.elderId)
  if (elder) payload.elderName = elder.name
  const bed = bedList.value.find(b => b.id === payload.bedId)
  if (bed) payload.bedNo = bed.bedNo
  try {
    if (isEdit.value) {
      await updateContract(payload)
    } else {
      await addContract(payload)
    }
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    resetForm()
    loadList()
  } catch (e) { }
}

async function delContract(id) {
  try {
    await ElMessageBox.confirm('确定删除该合同？', '提示', { type: 'warning' })
    await deleteContract(id)
    ElMessage.success('删除成功')
    if (contractList.value.length <= 1 && queryForm.pageNum > 1) queryForm.pageNum--
    loadList()
  } catch (e) { }
}

onMounted(() => { loadList(); loadElders(); loadBeds() })
</script>
