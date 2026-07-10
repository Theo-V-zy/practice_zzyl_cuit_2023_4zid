<template>
  <section class="page-section">
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="角色">
        <el-select v-model="currentRoleId" placeholder="请选择角色" @change="loadRoleDetail" style="width: 220px">
          <el-option v-for="r in roleList" :key="r.id" :label="r.roleName" :value="r.id" />
        </el-select>
      </el-form-item>
    </el-form>

    <el-card v-if="currentRole" shadow="never" style="margin-top: 16px;">
      <template #header>
        <span>角色：{{ currentRole.roleName }} — 菜单权限 & 数据权限</span>
      </template>

      <!-- 菜单权限 -->
      <h4>菜单权限</h4>
      <el-tree
        ref="menuTreeRef"
        :data="menuTree"
        show-checkbox
        node-key="id"
        :default-checked-keys="checkedMenuIds"
        default-expand-all
        :props="{ label: 'mname', children: 'subItems' }"
      />

      <!-- 数据权限 -->
      <h4 style="margin-top: 24px;">数据权限</h4>
      <el-radio-group v-model="dataScope" style="margin-top: 8px;">
        <el-radio label="ALL">全部数据</el-radio>
        <el-radio label="DEPT">本部门数据</el-radio>
        <el-radio label="DEPT_AND_CHILD">本部门及下级数据</el-radio>
        <el-radio label="CUSTOM">自定义部门数据</el-radio>
      </el-radio-group>

      <div style="margin-top: 24px;">
        <el-button type="primary" @click="handleSave">保存权限</el-button>
      </div>
    </el-card>

    <div v-else class="empty-hint">请先选择一个角色</div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getRoleList, getMenuTree, updateRoleMenus, updateRoleDataScope } from '@/api/admin'

const roleList = ref([])
const currentRoleId = ref(null)
const currentRole = ref(null)
const menuTree = ref([])
const menuTreeRef = ref(null)
const checkedMenuIds = ref([])
const dataScope = ref('ALL')

async function loadData() {
  try {
    const res = await getRoleList()
    if (res && res.data) roleList.value = res.data
  } catch (e) { /* ignore */ }
  try {
    const res = await getMenuTree()
    if (res && res.data) menuTree.value = res.data
  } catch (e) { /* ignore */ }
}

function loadRoleDetail(roleId) {
  const role = roleList.value.find(r => r.id === roleId)
  if (!role) return
  currentRole.value = role
  dataScope.value = role.dataScope || 'ALL'
  checkedMenuIds.value = role.menuIds ? role.menuIds.split(',').map(Number) : []
}

async function handleSave() {
  if (!currentRoleId.value) { ElMessage.warning('请先选择角色'); return }
  const checkedKeys = menuTreeRef.value.getCheckedKeys()
  const halfCheckedKeys = menuTreeRef.value.getHalfCheckedKeys()
  const allKeys = [...checkedKeys, ...halfCheckedKeys]
  try {
    await updateRoleMenus(currentRoleId.value, allKeys.join(','))
    await updateRoleDataScope(currentRoleId.value, dataScope.value)
    ElMessage.success('权限保存成功')
    // 更新本地状态
    currentRole.value.menuIds = allKeys.join(',')
    currentRole.value.dataScope = dataScope.value
  } catch (e) { /* ignore */ }
}

onMounted(() => loadData())
</script>

<style scoped>
.empty-hint { padding: 60px 0; text-align: center; color: rgba(0,0,0,0.35); }
</style>
