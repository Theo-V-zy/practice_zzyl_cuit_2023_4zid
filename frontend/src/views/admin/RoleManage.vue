<template>
  <section class="page-section">
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="角色名称">
        <el-input v-model="queryForm.roleName" placeholder="请输入" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增角色</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="roleName" label="角色名称" width="150" />
      <el-table-column prop="roleCode" label="角色编码" width="150" />
      <el-table-column prop="dataScope" label="数据权限" width="120">
        <template #default="{ row }">
          <el-tag size="small">{{ scopeMap[row.dataScope] || row.dataScope }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="150" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="warning" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="queryForm.page" v-model:page-size="queryForm.pageSize"
        :page-sizes="[10, 20, 50]" :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleQuery" @current-change="handleQuery"
      />
    </div>

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="480px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="formData.roleCode" :disabled="isEdit" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="数据权限">
          <el-radio-group v-model="formData.dataScope">
            <el-radio label="ALL">全部数据</el-radio>
            <el-radio label="DEPT">本部门</el-radio>
            <el-radio label="DEPT_AND_CHILD">本部门及下级</el-radio>
            <el-radio label="CUSTOM">自定义</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="formData.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="formData.remark" type="textarea" placeholder="请输入" />
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
import { getRolePage, addRole, updateRole, deleteRole } from '@/api/admin'

const loading = ref(false), submitLoading = ref(false)
const tableData = ref([]), total = ref(0)
const dialogVisible = ref(false), dialogTitle = ref(''), isEdit = ref(false)
const formRef = ref(null)

const scopeMap = { ALL: '全部数据', DEPT: '本部门', DEPT_AND_CHILD: '本部门及下级', CUSTOM: '自定义' }

const queryForm = reactive({ page: 1, pageSize: 10, roleName: '', roleCode: '' })
const formData = reactive({ id: null, roleName: '', roleCode: '', dataScope: 'ALL', status: 1, remark: '' })
const formRules = {
  roleName: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  roleCode: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const res = await getRolePage({ ...queryForm })
    if (res) { tableData.value = res.data || []; total.value = res.total || 0 }
  } finally { loading.value = false }
}

function handleQuery() { queryForm.page = 1; loadData() }
function handleReset() { Object.assign(queryForm, { page: 1, pageSize: 10, roleName: '', roleCode: '' }); loadData() }

function handleAdd() {
  isEdit.value = false; dialogTitle.value = '新增角色'
  Object.assign(formData, { id: null, roleName: '', roleCode: '', dataScope: 'ALL', status: 1, remark: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; dialogTitle.value = '编辑角色'
  Object.assign(formData, { id: row.id, roleName: row.roleName, roleCode: row.roleCode, dataScope: row.dataScope || 'ALL', status: row.status, remark: row.remark || '' })
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updateRole : addRole
    await api({ ...formData })
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}

function handleDelete(row) {
  ElMessageBox.confirm(`确定删除角色 ${row.roleName} 吗？`, '提示', { type: 'warning' })
    .then(async () => { await deleteRole(row.id); ElMessage.success('删除成功'); loadData() })
    .catch(() => {})
}

onMounted(() => loadData())
</script>

<style scoped>
.query-form { margin-bottom: 6px; }
.toolbar { margin-bottom: 12px; }
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
