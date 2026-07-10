<template>
  <section class="page-section">
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="职位名称">
        <el-input v-model="queryForm.postName" placeholder="请输入" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="选择" clearable style="width: 120px">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增职位</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="postName" label="职位名称" width="150" />
      <el-table-column prop="postCode" label="职位编码" width="150" />
      <el-table-column prop="sort" label="排序" width="60" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="150" />
      <el-table-column prop="createTime" label="创建时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
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

    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="460px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="职位名称" prop="postName">
          <el-input v-model="formData.postName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="职位编码" prop="postCode">
          <el-input v-model="formData.postCode" placeholder="请输入" />
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
import { getPostPage, addPost, updatePost, deletePost } from '@/api/admin'

const loading = ref(false), submitLoading = ref(false)
const tableData = ref([]), total = ref(0)
const dialogVisible = ref(false), dialogTitle = ref(''), isEdit = ref(false)
const formRef = ref(null)

const queryForm = reactive({ page: 1, pageSize: 10, postName: '', status: null })
const formData = reactive({ id: null, postName: '', postCode: '', sort: 1, status: 1, remark: '' })
const formRules = {
  postName: [{ required: true, message: '请输入职位名称', trigger: 'blur' }],
  postCode: [{ required: true, message: '请输入职位编码', trigger: 'blur' }]
}

async function loadData() {
  loading.value = true
  try {
    const res = await getPostPage({ ...queryForm })
    if (res) { tableData.value = res.data || []; total.value = res.total || 0 }
  } finally { loading.value = false }
}
function handleQuery() { queryForm.page = 1; loadData() }
function handleReset() { Object.assign(queryForm, { page: 1, pageSize: 10, postName: '', status: null }); loadData() }
function handleAdd() {
  isEdit.value = false; dialogTitle.value = '新增职位'
  Object.assign(formData, { id: null, postName: '', postCode: '', sort: 1, status: 1, remark: '' })
  dialogVisible.value = true
}
function handleEdit(row) {
  isEdit.value = true; dialogTitle.value = '编辑职位'
  Object.assign(formData, { id: row.id, postName: row.postName, postCode: row.postCode, sort: row.sort, status: row.status, remark: row.remark || '' })
  dialogVisible.value = true
}
async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updatePost : addPost
    await api({ ...formData })
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false; loadData()
  } finally { submitLoading.value = false }
}
function handleDelete(row) {
  ElMessageBox.confirm(`确定删除职位 ${row.postName} 吗？`, '提示', { type: 'warning' })
    .then(async () => { await deletePost(row.id); ElMessage.success('删除成功'); loadData() })
    .catch(() => {})
}
onMounted(() => loadData())
</script>

<style scoped>
.query-form { margin-bottom: 6px; }
.toolbar { margin-bottom: 12px; }
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
