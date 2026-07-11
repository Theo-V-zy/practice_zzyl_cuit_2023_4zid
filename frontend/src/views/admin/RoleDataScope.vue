<template>
  <section class="page-section">
    <div class="scope-layout">
      <aside class="role-panel">
        <div class="panel-toolbar">
          <el-input v-model="roleKeyword" placeholder="请输入" clearable size="small" />
          <span>角色名称</span><span>角色状态</span>
        </div>
        <button
          v-for="role in filteredRoles"
          :key="role.id"
          type="button"
          class="role-row"
          :class="{ active: currentRoleId === role.id }"
          @click="loadRoleDetail(role.id)"
        >
          <span>{{ role.roleName }}</span>
          <el-tag :type="role.status === 1 ? 'success' : 'danger'" size="small">{{ role.status === 1 ? '启用' : '禁用' }}</el-tag>
        </button>
      </aside>

      <div v-if="currentRole" class="scope-panel">
        <header class="scope-header">
          <strong>数据权限</strong>
          <span>{{ currentRole.roleName }}</span>
          <el-button type="primary" size="small" @click="handleSave">保存权限</el-button>
        </header>
        <div class="scope-options">
          <span class="field-label">数据范围：</span>
          <el-radio-group v-model="dataScope">
            <el-radio value="ALL">全部</el-radio>
            <el-radio value="DEPT">本级及子级</el-radio>
            <el-radio value="CUSTOM">自定义</el-radio>
          </el-radio-group>
        </div>
        <div class="tree-section">
          <span class="field-label">菜单范围：</span>
          <el-tree
            ref="menuTreeRef"
            :data="menuTree"
            show-checkbox
            node-key="id"
            :default-checked-keys="checkedMenuIds"
            default-expand-all
            :props="{ label: 'mname', children: 'subItems' }"
          />
        </div>
      </div>
      <div v-else class="scope-panel empty-hint">请先选择一个角色</div>
    </div>
  </section>
</template>

<script setup>
import { computed, ref, onMounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'
import { getRoleList, getMenuTree, updateRoleMenus, updateRoleDataScope } from '@/api/admin'

const roleList = ref([])
const roleKeyword = ref('')
const currentRoleId = ref(null)
const currentRole = ref(null)
const menuTree = ref([])
const menuTreeRef = ref(null)
const checkedMenuIds = ref([])
const dataScope = ref('ALL')
const filteredRoles = computed(() => roleList.value.filter(role => !roleKeyword.value || role.roleName?.includes(roleKeyword.value)))

async function loadData() {
  try {
    const res = await getRoleList()
    roleList.value = res?.data || []
  } catch (error) { roleList.value = [] }
  try {
    const res = await getMenuTree()
    menuTree.value = res?.data || []
  } catch (error) { menuTree.value = [] }
  if (roleList.value.length) loadRoleDetail(roleList.value[0].id)
}

async function loadRoleDetail(roleId) {
  const role = roleList.value.find(item => item.id === roleId)
  if (!role) return
  currentRoleId.value = roleId
  currentRole.value = role
  dataScope.value = role.dataScope || 'ALL'
  checkedMenuIds.value = role.menuIds ? role.menuIds.split(',').map(Number) : []
  await nextTick()
  menuTreeRef.value?.setCheckedKeys(checkedMenuIds.value)
}

async function handleSave() {
  if (!currentRoleId.value) return
  const allKeys = [...menuTreeRef.value.getCheckedKeys(), ...menuTreeRef.value.getHalfCheckedKeys()]
  await updateRoleMenus(currentRoleId.value, allKeys.join(','))
  await updateRoleDataScope(currentRoleId.value, dataScope.value)
  currentRole.value.menuIds = allKeys.join(',')
  currentRole.value.dataScope = dataScope.value
  ElMessage.success('权限保存成功')
}

onMounted(loadData)
</script>

<style scoped>
.scope-layout { display:grid; grid-template-columns:340px minmax(0,1fr); min-height:560px; }
.role-panel { padding-right:16px; border-right:1px solid #e4e7ed; }
.panel-toolbar { display:grid; grid-template-columns:minmax(0,1fr) 80px; align-items:center; min-height:72px; color:#757b84; background:#f5f6f8; border:1px solid #e4e7ed; font-size:12px; }
.panel-toolbar :deep(.el-input) { grid-column:1 / -1; margin:8px 10px 6px; width:auto; }
.panel-toolbar > span { padding:0 10px 8px; }
.role-row { display:grid; width:100%; min-height:42px; padding:0 10px; grid-template-columns:minmax(0,1fr) 80px; align-items:center; text-align:left; color:#555b65; background:#fff; border:1px solid #e4e7ed; border-top:0; cursor:pointer; font-size:12px; }
.role-row.active { background:#eef4ff; box-shadow:inset 3px 0 #3976d8; }
.scope-panel { min-width:0; padding-left:20px; }
.scope-header { display:flex; min-height:42px; align-items:center; gap:16px; border-bottom:1px solid #e4e7ed; }
.scope-header strong { font-size:15px; }
.scope-header span { color:#8a9099; font-size:12px; }
.scope-header .el-button { margin-left:auto; }
.scope-options, .tree-section { display:flex; padding:22px 8px; align-items:flex-start; border-bottom:1px solid #edf0f3; }
.field-label { flex:0 0 86px; color:#60656e; font-size:13px; }
.tree-section :deep(.el-tree) { flex:1; }
.empty-hint { display:grid; place-items:center; color:#9a9fa7; }
@media (max-width:1050px) { .scope-layout { grid-template-columns:300px minmax(0,1fr); } }
</style>
