<template>
  <section class="page-section">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd(null)">新增根部门</el-button>
    </div>

    <el-table :data="deptTree" v-loading="loading" style="width: 100%" border row-key="id" default-expand-all>
      <el-table-column prop="deptName" label="部门名称" min-width="200" />
      <el-table-column prop="leader" label="负责人" width="120" />
      <el-table-column prop="phone" label="电话" width="140" />
      <el-table-column prop="sort" label="排序" width="60" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleAdd(row)">新增子部门</el-button>
          <el-button size="small" type="warning" link @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="480px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="上级部门">
          <el-input :value="parentName" disabled />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="formData.deptName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="负责人">
          <el-input v-model="formData.leader" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="formData.phone" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDepartmentTree, addDepartment, updateDepartment, deleteDepartment } from '@/api/admin'

const loading = ref(false), submitLoading = ref(false)
const dialogVisible = ref(false), dialogTitle = ref(''), isEdit = ref(false)
const formRef = ref(null), parentName = ref('根节点')
const deptTree = ref([])

const formData = reactive({ id: null, parentId: 0, deptName: '', leader: '', phone: '', sort: 1, status: 1 })
const formRules = { deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }] }

async function loadData() {
  loading.value = true
  try {
    const res = await getDepartmentTree()
    if (res && res.data) deptTree.value = res.data
  } finally { loading.value = false }
}

function handleAdd(parent) {
  isEdit.value = false
  dialogTitle.value = parent ? `新增子部门（父：${parent.deptName}）` : '新增根部门'
  parentName.value = parent ? parent.deptName : '根节点'
  Object.assign(formData, { id: null, parentId: parent ? parent.id : 0, deptName: '', leader: '', phone: '', sort: 1, status: 1 })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; dialogTitle.value = '编辑部门'
  parentName.value = row.parentId === 0 ? '根节点' : '父部门ID: ' + row.parentId
  Object.assign(formData, { id: row.id, parentId: row.parentId, deptName: row.deptName, leader: row.leader || '', phone: row.phone || '', sort: row.sort || 1, status: row.status })
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updateDepartment : addDepartment
    await api({ ...formData })
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定删除部门 ${row.deptName} 吗？`, '提示', { type: 'warning' })
    .then(async () => { await deleteDepartment(row.id); ElMessage.success('删除成功'); loadData() })
    .catch(() => {})
}

onMounted(() => loadData())
</script>

<style scoped>
.toolbar { margin-bottom: 12px; }
</style>
