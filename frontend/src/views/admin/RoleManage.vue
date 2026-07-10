<template>
  <section class="page-section">
    <!-- 查询区 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="角色名称">
        <el-input v-model="queryForm.roleName" placeholder="请输入" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增角色</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" style="width: 100%" border @row-click="selectRole">
      <el-table-column prop="roleName" label="角色名称" width="150" />
      <el-table-column prop="roleCode" label="角色编码" width="150" />
      <el-table-column label="角色状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注" min-width="150" />
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="danger" link @click.stop="handleDelete(row)">删除</el-button>
          <el-button size="small" :type="row.status === 1 ? 'warning' : 'success'" link @click.stop="handleToggle(row)">{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
          <el-button size="small" type="primary" link @click.stop="handleEdit(row)">编辑</el-button>
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

    <!-- 角色编辑弹窗 -->
    <el-dialog :title="dialogTitle" v-model="dialogVisible" width="480px">
      <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="formData.roleName" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="角色编码" prop="roleCode">
          <el-input v-model="formData.roleCode" :disabled="isEdit" placeholder="请输入" />
        </el-form-item>
        <el-form-item label="角色状态">
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

    <!-- 权限配置区（选中角色后显示） -->
    <div v-if="selectedRole" class="perm-section">
      <el-divider />
      <h3 class="perm-title">权限配置 — {{ selectedRole.roleName }}</h3>
      <el-tabs v-model="activeTab">
        <!-- 菜单权限 Tab -->
        <el-tab-pane label="菜单权限" name="menu">
          <el-tree
            ref="menuTreeRef"
            :data="menuTree"
            show-checkbox
            node-key="id"
            :default-checked-keys="checkedMenuIds"
            default-expand-all
            :props="{ label: 'mname', children: 'subItems' }"
          />
          <el-button type="primary" style="margin-top: 16px;" @click="saveMenus">保存菜单权限</el-button>
        </el-tab-pane>

        <!-- 数据权限 Tab -->
        <el-tab-pane label="数据权限" name="data">
          <el-radio-group v-model="dataScope" style="margin-top: 8px;">
            <el-radio label="ALL">全部数据</el-radio>
            <el-radio label="DEPT">本部门数据</el-radio>
            <el-radio label="DEPT_AND_CHILD">本部门及下级数据</el-radio>
            <el-radio label="CUSTOM">自定义部门数据</el-radio>
          </el-radio-group>
          <div style="margin-top: 16px;">
            <el-button type="primary" @click="saveDataScope">保存数据权限</el-button>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div v-else class="empty-hint">点击表格行选择角色，配置菜单权限和数据权限</div>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRolePage, getRoleList, addRole, updateRole, deleteRole, updateRoleMenus, updateRoleDataScope, getMenuTree } from '@/api/admin'

const loading = ref(false), submitLoading = ref(false)
const tableData = ref([]), total = ref(0)
const dialogVisible = ref(false), dialogTitle = ref(''), isEdit = ref(false)
const formRef = ref(null)
const selectedRole = ref(null)
const activeTab = ref('menu')
const menuTree = ref([])
const menuTreeRef = ref(null)
const checkedMenuIds = ref([])
const dataScope = ref('ALL')

const queryForm = reactive({ page: 1, pageSize: 10, roleName: '' })
const formData = reactive({ id: null, roleName: '', roleCode: '', status: 1, remark: '' })
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

async function loadMenus() {
  try {
    const res = await getMenuTree()
    if (res?.data) menuTree.value = res.data
  } catch (e) { /* ignore */ }
}

function selectRole(row) {
  selectedRole.value = row
  checkedMenuIds.value = row.menuIds ? row.menuIds.split(',').map(Number) : []
  dataScope.value = row.dataScope || 'ALL'
  activeTab.value = 'menu'
}

function handleQuery() { queryForm.page = 1; loadData() }
function handleReset() { Object.assign(queryForm, { page: 1, pageSize: 10, roleName: '' }); loadData() }

function handleAdd() {
  isEdit.value = false; dialogTitle.value = '新增角色'
  Object.assign(formData, { id: null, roleName: '', roleCode: '', status: 1, remark: '' })
  dialogVisible.value = true
}

function handleEdit(row) {
  isEdit.value = true; dialogTitle.value = '编辑角色'
  Object.assign(formData, { id: row.id, roleName: row.roleName, roleCode: row.roleCode, status: row.status, remark: row.remark || '' })
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

async function handleToggle(row) {
  const newStatus = row.status === 1 ? 0 : 1
  await updateRole({ id: row.id, status: newStatus })
  ElMessage.success(newStatus === 1 ? '已启用' : '已禁用')
  loadData()
}

async function saveMenus() {
  if (!selectedRole.value) return
  const checked = menuTreeRef.value.getCheckedKeys()
  const half = menuTreeRef.value.getHalfCheckedKeys()
  const allKeys = [...checked, ...half]
  await updateRoleMenus(selectedRole.value.id, allKeys.join(','))
  ElMessage.success('菜单权限保存成功')
  selectedRole.value.menuIds = allKeys.join(',')
}

async function saveDataScope() {
  if (!selectedRole.value) return
  await updateRoleDataScope(selectedRole.value.id, dataScope.value)
  ElMessage.success('数据权限保存成功')
  selectedRole.value.dataScope = dataScope.value
}

onMounted(() => { loadData(); loadMenus() })
</script>

<style scoped>
.query-form { margin-bottom: 6px; }
.toolbar { margin-bottom: 12px; }
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
.perm-section { margin-top: 24px; }
.perm-title { font-size: 15px; color: #333; margin: 0 0 12px; }
.empty-hint { padding: 40px 0; text-align: center; color: rgba(0,0,0,0.35); margin-top: 24px; }
</style>
