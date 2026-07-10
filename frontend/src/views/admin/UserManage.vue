<template>
  <section class="page-section">
    <!-- 查询区 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="姓名">
        <el-input v-model="queryForm.realname" placeholder="请输入姓名" clearable style="width: 160px" />
      </el-form-item>
      <el-form-item label="手机号">
        <el-input v-model="queryForm.phone" placeholder="请输入手机号" clearable style="width: 160px" />
      </el-form-item>
      <el-form-item label="部门">
        <el-tree-select
          v-model="queryForm.deptId"
          :data="deptTree"
          :props="{ label: 'deptName', value: 'id' }"
          placeholder="选择部门"
          clearable
          check-strictly
          style="width: 180px"
        />
      </el-form-item>
      <el-form-item label="角色">
        <el-select v-model="queryForm.roleId" placeholder="选择角色" clearable style="width: 160px">
          <el-option v-for="r in roleList" :key="r.id" :label="r.roleName" :value="r.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="选择状态" clearable style="width: 120px">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作栏 -->
    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增用户</el-button>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" v-loading="loading" style="width: 100%" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="account" label="账号" width="120" />
      <el-table-column prop="realname" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="deptName" label="部门" width="120" />
      <el-table-column prop="roleName" label="角色" width="120" />
      <el-table-column prop="email" label="邮箱" min-width="160" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="row.islock === 0 ? 'success' : 'danger'" size="small">
            {{ row.islock === 0 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="170">
        <template #default="{ row }">{{ row.createTime || '-' }}</template>
      </el-table-column>
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="warning" link @click="handleResetPwd(row)">重置密码</el-button>
          <el-button size="small" type="info" link @click="handleAssignRole(row)">角色</el-button>
          <el-button
            size="small"
            :type="row.islock === 0 ? 'danger' : 'success'"
            link
            @click="handleToggleStatus(row)"
          >
            {{ row.islock === 0 ? '禁用' : '启用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="queryForm.page"
        v-model:page-size="queryForm.pageSize"
        :page-sizes="[10, 20, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleQuery"
        @current-change="handleQuery"
      />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="520px" @close="resetForm">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="账号" prop="account">
          <el-input v-model="formData.account" :disabled="isEdit" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item label="姓名" prop="realname">
          <el-input v-model="formData.realname" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="formData.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="formData.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="formData.sex">
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="部门">
          <el-tree-select
            v-model="formData.deptId"
            :data="deptTree"
            :props="{ label: 'deptName', value: 'id' }"
            placeholder="选择部门"
            clearable
            check-strictly
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="职位">
          <el-select v-model="formData.postId" placeholder="选择职位" clearable style="width: 100%">
            <el-option v-for="p in postList" :key="p.id" :label="p.postName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="formData.roleId" placeholder="选择角色" clearable style="width: 100%">
            <el-option v-for="r in roleList" :key="r.id" :label="r.roleName" :value="r.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配角色弹窗 -->
    <el-dialog title="分配角色" v-model="roleDialogVisible" width="420px">
      <el-checkbox-group v-model="selectedRoles">
        <el-checkbox v-for="r in roleList" :key="r.id" :label="r.id" :value="r.id">{{ r.roleName }}</el-checkbox>
      </el-checkbox-group>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRole">保存</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserPage, addUser, updateUserInfo, updateUserStatus, resetPassword, assignRoles } from '@/api/admin'
import { getRoleList } from '@/api/admin'
import { getDepartmentTree } from '@/api/admin'
import { getPostList } from '@/api/admin'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const isEdit = ref(false)
const formRef = ref(null)
const roleDialogVisible = ref(false)
const currentUserId = ref(null)
const selectedRoles = ref([])

const deptTree = ref([])
const postList = ref([])
const roleList = ref([])

const queryForm = reactive({
  page: 1,
  pageSize: 10,
  realname: '',
  phone: '',
  deptId: null,
  roleId: null,
  status: null
})

const formData = reactive({
  id: null,
  account: '',
  realname: '',
  phone: '',
  email: '',
  sex: '男',
  deptId: null,
  postId: null,
  roleId: null
})

const formRules = {
  account: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  realname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }]
}

function resetForm() {
  Object.assign(formData, { id: null, account: '', realname: '', phone: '', email: '', sex: '男', deptId: null, postId: null, roleId: null })
  isEdit.value = false
}

async function loadData() {
  loading.value = true
  try {
    const res = await getUserPage({ ...queryForm })
    if (res) {
      tableData.value = res.data || []
      total.value = res.total || 0
    }
  } catch (e) { /* ignore */ }
  finally { loading.value = false }
}

async function loadOptions() {
  try {
    const deptRes = await getDepartmentTree()
    if (deptRes && deptRes.data) deptTree.value = deptRes.data
  } catch (e) { /* ignore */ }
  try {
    const postRes = await getPostList()
    if (postRes && postRes.data) postList.value = postRes.data
  } catch (e) { /* ignore */ }
  try {
    const roleRes = await getRoleList()
    if (roleRes && roleRes.data) roleList.value = roleRes.data
  } catch (e) { /* ignore */ }
}

function handleQuery() { queryForm.page = 1; loadData() }
function handleReset() { Object.assign(queryForm, { page: 1, pageSize: 10, realname: '', phone: '', deptId: null, roleId: null, status: null }); loadData() }

function handleAdd() {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  resetForm()
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  Object.assign(formData, {
    id: row.id,
    account: row.account,
    realname: row.realname,
    phone: row.phone || '',
    email: row.email || '',
    sex: row.sex || '男',
    deptId: row.deptId,
    postId: row.postId,
    roleId: row.roleId
  })
  dialogVisible.value = true
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    const api = isEdit.value ? updateUserInfo : addUser
    await api({ ...formData })
    ElMessage.success(isEdit.value ? '修改成功' : '新增成功')
    dialogVisible.value = false
    loadData()
  } catch (e) { /* ignore */ }
  finally { submitLoading.value = false }
}

function handleResetPwd(row) {
  ElMessageBox.confirm(`确定重置用户 ${row.realname} 的密码为123456吗？`, '重置密码', { type: 'warning' })
    .then(async () => {
      await resetPassword(row.id)
      ElMessage.success('密码已重置')
    })
    .catch(() => {})
}

function handleToggleStatus(row) {
  const action = row.islock === 0 ? '禁用' : '启用'
  ElMessageBox.confirm(`确定${action}用户 ${row.realname} 吗？`, '提示', { type: 'warning' })
    .then(async () => {
      await updateUserStatus(row.id, row.islock === 0 ? 0 : 1)
      ElMessage.success(`${action}成功`)
      loadData()
    })
    .catch(() => {})
}

function handleAssignRole(row) {
  currentUserId.value = row.id
  selectedRoles.value = row.roleId ? [row.roleId] : []
  roleDialogVisible.value = true
}

async function handleSaveRole() {
  await assignRoles(currentUserId.value, selectedRoles.value)
  ElMessage.success('角色分配成功')
  roleDialogVisible.value = false
  loadData()
}

onMounted(() => {
  loadData()
  loadOptions()
})
</script>

<style scoped>
.query-form { margin-bottom: 6px; }
.toolbar { margin-bottom: 12px; }
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
</style>
