<template>
  <section class="page-section">
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd(null)">新增根菜单</el-button>
    </div>

    <el-table :data="menuTree" v-loading="loading" style="width: 100%" border row-key="id" default-expand-all>
      <el-table-column prop="mname" label="菜单名称" min-width="200" />
      <el-table-column prop="path" label="路由路径" width="180" />
      <el-table-column prop="sort" label="排序" width="60" />
      <el-table-column label="显示" width="70">
        <template #default="{ row }">
          <el-tag :type="row.visible === 1 ? 'success' : 'info'" size="small">{{ row.visible === 1 ? '显示' : '隐藏' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleAdd(row)">新增子菜单</el-button>
          <el-button size="small" type="warning" link @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="480px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="父菜单">
          <el-input :value="parentName" disabled />
        </el-form-item>
        <el-form-item label="菜单名称" prop="mname">
          <el-input v-model="formData.mname" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="路由路径">
          <el-input v-model="formData.path" placeholder="如 /Dashboard" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="formData.sort" :min="0" />
        </el-form-item>
        <el-form-item label="显示状态">
          <el-radio-group v-model="formData.visible">
            <el-radio :value="1">显示</el-radio>
            <el-radio :value="0">隐藏</el-radio>
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
import { getMenuTree, addMenu, updateMenu, deleteMenu } from '@/api/admin'

const loading = ref(false), submitLoading = ref(false)
const dialogVisible = ref(false), dialogTitle = ref(''), isEdit = ref(false)
const formRef = ref(null), parentName = ref('根目录')
const menuTree = ref([])

const formData = reactive({ id: null, pid: 0, mname: '', path: '', sort: 1, visible: 1 })
const formRules = { mname: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }] }

async function loadData() {
  loading.value = true
  try {
    const res = await getMenuTree()
    if (res && res.data) menuTree.value = res.data
  } finally { loading.value = false }
}

function handleAdd(parent) {
  isEdit.value = false; dialogTitle.value = parent ? `新增子菜单（父：${parent.mname}）` : '新增根菜单'
  parentName.value = parent ? parent.mname : '根目录'
  Object.assign(formData, { id: null, pid: parent ? parent.id : 0, mname: '', path: '', sort: 1, visible: 1 })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; dialogTitle.value = '编辑菜单'
  parentName.value = row.pid === 0 ? '根目录' : '父菜单ID: ' + row.pid
  Object.assign(formData, { id: row.id, pid: row.pid, mname: row.mname, path: row.path || '', sort: row.sort || 1, visible: row.visible })
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updateMenu : addMenu
    await api({ ...formData })
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定删除菜单 ${row.mname} 吗？`, '提示', { type: 'warning' })
    .then(async () => { await deleteMenu(row.id); ElMessage.success('删除成功'); loadData() })
    .catch(() => {})
}

onMounted(() => loadData())
</script>

<style scoped>
.toolbar { margin-bottom: 12px; }
</style>
