<template>
  <section class="page-section">
    <div class="role-layout">
      <!-- 左侧角色列表 -->
      <div class="list-panel">
        <div class="panel-header">角色列表</div>
        <div class="panel-toolbar">
          <el-input v-model="queryForm.roleName" placeholder="请输入" clearable size="small" @change="handleQuery" />
          <el-button type="primary" size="small" @click="handleAdd">新增角色</el-button>
        </div>
        <div class="role-columns"><span>角色名称</span><span>角色状态</span><span>操作</span></div>
        <div class="role-items">
          <div
            v-for="r in tableData" :key="r.id"
            class="role-item"
            :class="{ active: selectedRole?.id === r.id }"
            @click="selectRole(r)"
          >
            <div class="role-name">{{ r.roleName }}</div>
            <el-tag :type="r.status===1?'success':'danger'" size="small">{{ r.status===1?'启用':'禁用' }}</el-tag>
            <div class="role-actions" @click.stop>
              <el-button size="small" type="danger" link @click="handleDelete(r)">删除</el-button>
              <el-button size="small" :type="r.status===1?'warning':'success'" link @click="handleToggle(r)">{{ r.status===1?'禁用':'启用' }}</el-button>
              <el-button size="small" type="primary" link @click="handleEdit(r)">编辑</el-button>
            </div>
          </div>
          <div v-if="tableData.length===0" class="no-data">暂无角色</div>
        </div>
      </div>

      <!-- 右侧权限配置 -->
      <div class="perm-panel" v-if="selectedRole">
        <div class="panel-header">权限配置 — {{ selectedRole.roleName }}</div>
        <el-tabs v-model="activeTab">
          <el-tab-pane label="菜单权限" name="menu">
            <div class="perm-toolbar">
              <el-button size="small" @click="editMode=!editMode">{{ editMode?'取消':'编辑' }}</el-button>
              <el-button v-if="editMode" size="small" type="primary" @click="saveMenus">保存</el-button>
            </div>
            <div class="menu-tree-wrap">
              <el-tree
                ref="menuTreeRef"
                :key="selectedRole?.id"
                :data="menuTree"
                show-checkbox
                node-key="id"
                default-expand-all
                :props="{ label:'mname', children:'subItems', disabled:()=>!editMode }"
              />
            </div>
          </el-tab-pane>
          <el-tab-pane label="数据权限" name="data">
            <el-radio-group v-model="dataScope" class="scope-group">
              <el-radio label="ALL">全部数据</el-radio>
              <el-radio label="DEPT">本部门数据</el-radio>
              <el-radio label="DEPT_AND_CHILD">本部门及下级数据</el-radio>
              <el-radio label="CUSTOM">自定义部门数据</el-radio>
            </el-radio-group>
            <el-button type="primary" size="small" style="margin-top:16px" @click="saveDataScope">保存</el-button>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div v-else class="perm-panel empty-perm">点击左侧角色查看菜单权限和数据权限</div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dlgTitle" v-model="dlgVisible" width="440px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="90px">
        <el-form-item label="*角色名称" prop="roleName"><el-input v-model="form.roleName" maxlength="10" show-word-limit /></el-form-item>
        <el-form-item label="*角色状态">
          <el-radio-group v-model="form.status"><el-radio :value="1">启用</el-radio><el-radio :value="0">禁用</el-radio></el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dlgVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRolePage, addRole, updateRole, deleteRole, updateRoleMenus, updateRoleDataScope, getMenuTree } from '@/api/admin'

const loading=ref(false), submitLoading=ref(false)
const tableData=ref([]), total=ref(0)
const dlgVisible=ref(false), dlgTitle=ref(''), isEdit=ref(false), formRef=ref(null)
const selectedRole=ref(null), activeTab=ref('menu'), editMode=ref(false)
const menuTree=ref([]), menuTreeRef=ref(null)
const checkedMenuIds=ref([]), dataScope=ref('ALL')

const queryForm=reactive({ page:1, pageSize:50, roleName:'' })
const form=reactive({ id:null, roleName:'', roleCode:'', status:1 })
const formRules={ roleName:[{ required:true, message:'请输入角色名称', trigger:'blur' }] }

async function loadData() {
  loading.value=true
  try {
    const res=await getRolePage({...queryForm})
    if(res){
      tableData.value=res.data||[]
      total.value=res.total||0
      if(!selectedRole.value && tableData.value.length) selectRole(tableData.value[0])
    }
  } finally { loading.value=false }
}
async function loadMenus() {
  try { const res=await getMenuTree(); if(res?.data) menuTree.value=res.data } catch(e){}
}
function handleQuery(){loadData()}

function selectRole(r){
  selectedRole.value=r
  const ids=r.menuIds?r.menuIds.split(',').map(Number).filter(id=>id):[]
  checkedMenuIds.value=ids
  dataScope.value=r.dataScope||'ALL'
  activeTab.value='menu'
  editMode.value=false
  // 用 setCheckedKeys 强制更新勾选，default-checked-keys 不可靠
  if(menuTreeRef.value){
    menuTreeRef.value.setCheckedKeys([])
    setTimeout(()=>menuTreeRef.value.setCheckedKeys(ids), 50)
  }
}

function handleAdd(){
  isEdit.value=false;dlgTitle.value='新增角色'
  Object.assign(form,{id:null,roleName:'',roleCode:'',status:1})
  dlgVisible.value=true
}
function handleEdit(r){
  isEdit.value=true;dlgTitle.value='编辑角色'
  Object.assign(form,{id:r.id,roleName:r.roleName,roleCode:r.roleCode,status:r.status})
  dlgVisible.value=true
}
async function handleSubmit(){
  const valid=await formRef.value.validate().catch(()=>false); if(!valid) return
  submitLoading.value=true
  try{
    await (isEdit.value?updateRole:addRole)({...form})
    ElMessage.success(isEdit.value?'修改成功':'新增成功')
    dlgVisible.value=false;loadData()
  }finally{submitLoading.value=false}
}
function handleDelete(r){
  if(r.status===1){ElMessage.warning('启用状态下不可删除');return}
  ElMessageBox.confirm('确定删除该角色吗？','确认删除',{type:'warning'})
    .then(async()=>{await deleteRole(r.id);ElMessage.success('已删除');if(selectedRole.value?.id===r.id)selectedRole.value=null;loadData()}).catch(()=>{})
}
function handleToggle(r){
  if(r.status===1){
    ElMessageBox.confirm('禁用后将无法展示，是否继续？','确认禁用',{type:'warning'})
      .then(async()=>{await updateRole({id:r.id,status:0});ElMessage.success('禁用成功');loadData()}).catch(()=>{})
  } else {
    updateRole({id:r.id,status:1}).then(()=>{ElMessage.success('启用成功');loadData()})
  }
}
// 递归收集团队某节点及其所有子孙的ID
function collectAllIds(node) {
  const ids = [node.id]
  ;(node.subItems || []).forEach(c => { ids.push(...collectAllIds(c)) })
  return ids
}
// 在树中按ID查找节点
function findInTree(id, nodes) {
  for (const n of nodes) {
    if (n.id === id) return n
    const r = findInTree(id, n.subItems || [])
    if (r) return r
  }
  return null
}
async function saveMenus(){
  if(!selectedRole.value)return
  const checked = menuTreeRef.value.getCheckedKeys()
  const half = menuTreeRef.value.getHalfCheckedKeys()
  const allIds = new Set([...checked, ...half])
  // 双向联动：每个勾选节点的所有子孙全带上
  for (const id of [...allIds]) {
    const node = findInTree(id, menuTree.value)
    if (node) collectAllIds(node).forEach(i => allIds.add(i))
  }
  await updateRoleMenus(selectedRole.value.id, [...allIds].join(','))
  ElMessage.success('菜单权限保存成功')
  selectedRole.value.menuIds = [...allIds].join(',')
  editMode.value = false
}
async function saveDataScope(){
  if(!selectedRole.value)return
  await updateRoleDataScope(selectedRole.value.id,dataScope.value)
  ElMessage.success('数据权限保存成功')
  selectedRole.value.dataScope=dataScope.value
}

onMounted(()=>{loadData();loadMenus()})
</script>

<style scoped>
.role-layout { display:flex; gap:0; min-height:560px; }
.list-panel { width:410px; flex-shrink:0; border-right:1px solid #e7e9ed; padding-right:16px; }
.panel-header { font-size:15px; font-weight:600; color:#333; margin-bottom:12px; }
.panel-toolbar { display:grid; grid-template-columns:minmax(0, 1fr) 92px; gap:8px; margin-bottom:12px; }
.role-columns { display:grid; grid-template-columns:minmax(110px,1fr) 72px 138px; align-items:center; height:34px; padding:0 10px; color:#777d86; background:#f5f6f8; border:1px solid #e7e9ed; border-bottom:0; font-size:12px; }
.role-items { max-height:500px; overflow-y:auto; }
.role-item { display:grid; grid-template-columns:minmax(110px,1fr) 72px 138px; min-height:42px; padding:0 10px; align-items:center; border:1px solid #e7e9ed; border-bottom:0; cursor:pointer; transition:all 0.2s; }
.role-item:last-child { border-bottom:1px solid #e7e9ed; }
.role-item:hover { background:#f7faff; }
.role-item.active { background:#eef4ff; box-shadow:inset 3px 0 #3976d8; }
.role-name { min-width:0; color:#4d535c; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; font-size:12px; }
.role-actions { display:flex; gap:0; }
.role-actions :deep(.el-button + .el-button) { margin-left:6px; }
.no-data { text-align:center; padding:40px 0; color:rgba(0,0,0,0.35); }

.perm-panel { flex:1; min-width:0; padding-left:20px; }
.empty-perm { display:flex; align-items:center; justify-content:center; color:rgba(0,0,0,0.35); font-size:14px; }
.perm-toolbar { display:flex; gap:8px; margin-bottom:12px; }
.menu-tree-wrap { max-height:450px; overflow-y:auto; }
.scope-group { display:flex; flex-direction:column; gap:12px; }
@media (max-width: 1100px) {
  .list-panel { width: 360px; }
  .role-columns, .role-item { grid-template-columns:minmax(90px,1fr) 62px 132px; }
}
.scope-group :deep(.el-radio) { margin-right:0; }
</style>
