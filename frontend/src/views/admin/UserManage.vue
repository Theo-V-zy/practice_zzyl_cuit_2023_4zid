<template>
  <section class="page-section">
    <div class="user-layout">
      <!-- 左侧部门树 -->
      <div class="tree-panel">
        <div class="tree-header">部门</div>
        <el-input v-model="deptFilter" placeholder="请输入" clearable size="small" class="tree-search" />
        <el-tree
          ref="treeRef"
          :data="deptTree"
          :props="{ label: 'deptName', children: 'children' }"
          node-key="id"
          :filter-node-method="filterDept"
          highlight-current
          default-expand-all
          @node-click="onDeptClick"
        >
          <template #default="{ data }">
            <span>{{ data.deptName }}</span>
          </template>
        </el-tree>
      </div>

      <!-- 右侧内容 -->
      <div class="content-panel">
        <!-- 查询区 -->
        <el-form :inline="true" :model="queryForm" class="query-form">
          <el-form-item label="姓名">
            <el-input v-model="queryForm.realname" placeholder="请输入" clearable size="small" style="width:150px" />
          </el-form-item>
          <el-form-item label="用户状态">
            <el-select v-model="queryForm.status" placeholder="请选择" clearable size="small" style="width:130px">
              <el-option label="启用" :value="1" />
              <el-option label="禁用" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="queryForm.email" placeholder="请输入" clearable size="small" style="width:180px" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="handleQuery">搜索</el-button>
            <el-button size="small" @click="handleReset">重置</el-button>
          </el-form-item>
        </el-form>

        <div class="toolbar">
          <el-button type="primary" size="small" @click="handleAdd">新增用户</el-button>
        </div>

        <el-table :data="tableData" v-loading="loading" style="width:100%" border size="small">
          <el-table-column prop="account" label="登录账号" width="140" />
          <el-table-column prop="realname" label="姓名" width="90" />
          <el-table-column prop="email" label="邮箱" min-width="170" />
          <el-table-column prop="deptName" label="所在部门" width="110" />
          <el-table-column prop="postName" label="所属职位" width="90" />
          <el-table-column v-if="showWideColumns" prop="roleName" label="绑定角色" width="90" />
          <el-table-column v-if="showWideColumns" label="性别" width="50"><template #default="{row}">{{ row.sex || '-' }}</template></el-table-column>
          <el-table-column v-if="showWideColumns" prop="phone" label="手机" width="120" />
          <el-table-column label="用户状态" width="80">
            <template #default="{row}">
              <el-tag :type="row.islock===0?'success':'danger'" size="small">{{ row.islock===0?'启用':'禁用' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="220" fixed="right">
            <template #default="{row}">
              <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
              <el-button size="small" :type="row.islock===0?'warning':'success'" link @click="handleToggle(row)">
                {{ row.islock===0?'禁用':'启用' }}
              </el-button>
              <el-button size="small" type="primary" link @click="handleEdit(row)">编辑</el-button>
              <el-button size="small" type="info" link @click="handleResetPwd(row)">重置密码</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pagination-wrap">
          <el-pagination
            v-model:current-page="queryForm.page" v-model:page-size="queryForm.pageSize"
            :page-sizes="[10,20,50]" :total="total" layout="total,sizes,prev,pager,next"
            size="small" @size-change="loadData" @current-change="loadData"
          />
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="dlgTitle" v-model="dlgVisible" width="520px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="90px">
        <el-form-item label="姓名" prop="realname"><el-input v-model="form.realname" maxlength="10" show-word-limit /></el-form-item>
        <el-form-item label="登录账号" prop="account">
          <el-input v-model="form.account" :placeholder="isEdit?'':'留空则自动使用邮箱'" maxlength="50" />
        </el-form-item>
        <el-form-item label="所在部门" prop="deptId">
          <el-tree-select v-model="form.deptId" :data="deptTree" :props="{label:'deptName',value:'id'}" placeholder="请选择部门" check-strictly clearable style="width:100%" />
        </el-form-item>
        <el-form-item label="绑定角色" prop="roleId">
          <el-select v-model="form.roleId" placeholder="请选择角色" style="width:100%">
            <el-option v-for="r in roleList" :key="r.id" :label="r.roleName" :value="r.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email"><el-input v-model="form.email" maxlength="30" show-word-limit /></el-form-item>
        <el-form-item label="所属职位" prop="postId">
          <el-select v-model="form.postId" placeholder="请选择职位" style="width:100%">
            <el-option v-for="p in postList" :key="p.id" :label="p.postName" :value="p.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="用户状态"><el-radio-group v-model="form.islock"><el-radio :value="0">启用</el-radio><el-radio :value="1">禁用</el-radio></el-radio-group></el-form-item>
        <el-form-item label="性别"><el-radio-group v-model="form.sex"><el-radio label="男">男</el-radio><el-radio label="女">女</el-radio></el-radio-group></el-form-item>
        <el-form-item label="手机号"><el-input v-model="form.phone" maxlength="11" show-word-limit /></el-form-item>
        <el-form-item v-if="!isEdit" label="初始密码">
          <span class="readonly-text">We123456 </span>
          <el-tooltip content="新增用户后请提醒用户及时修改密码" placement="top"><span style="color:#909399;cursor:help;font-size:13px;">ⓘ</span></el-tooltip>
        </el-form-item>
      </el-form>
      <template #footer><el-button @click="dlgVisible=false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></template>
    </el-dialog>
  </section>
</template>

<script setup>
import { ref, reactive, onBeforeUnmount, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getUserPage, addUser, updateUserInfo, updateUserStatus, resetPassword } from '@/api/admin'
import { getRoleList, getDepartmentTree, getPostList } from '@/api/admin'

const loading=ref(false), submitLoading=ref(false), tableData=ref([]), total=ref(0)
const dlgVisible=ref(false), dlgTitle=ref(''), isEdit=ref(false), formRef=ref(null)
const deptTree=ref([]), postList=ref([]), roleList=ref([])
const currentDeptId=ref(null)
const deptFilter=ref('')
const showWideColumns=ref(window.innerWidth>=1500)
const syncWideColumns=()=>{showWideColumns.value=window.innerWidth>=1500}

const queryForm=reactive({ page:1, pageSize:10, realname:'', status:null, email:'', deptId:null })
const form=reactive({ id:null, account:'', realname:'', email:'', phone:'', sex:'男', deptId:null, postId:null, roleId:null, islock:0 })

const selectedDeptName = computed(() => {
  if (!form.deptId) return '-'
  const find = (nodes) => { for (const n of nodes) { if (n.id===form.deptId) return n.deptName; const r=find(n.children||[]); if(r) return r } return null }
  return find(deptTree.value) || '-'
})

const formRules = {
  realname: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱号码', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z0-9]+/, message: '请输入正确的邮箱号码', trigger: 'blur' }
  ],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  roleId: [{ required: true, message: '请选择角色', trigger: 'change' }],
  postId: [{ required: true, message: '请选择职位', trigger: 'change' }]
}

function filterDept(value, data) { return data.deptName?.includes(value) }

watch(deptFilter, v => { treeRef.value?.filter(v) })

function onDeptClick(data) {
  currentDeptId.value = data.id
  queryForm.deptId = data.id
  queryForm.page = 1
  loadData()
}

async function loadData() {
  loading.value = true
  try { const res = await getUserPage({...queryForm}); if(res){tableData.value=res.data||[]; total.value=res.total||0} } finally { loading.value=false }
}
async function loadOptions() {
  try { const d=await getDepartmentTree(); if(d?.data) deptTree.value=d.data } catch(e){}
  try { const p=await getPostList(); if(p?.data) postList.value=p.data } catch(e){}
  try { const r=await getRoleList(); if(r?.data) roleList.value=r.data } catch(e){}
}
function handleQuery(){queryForm.page=1;loadData()}
function handleReset(){Object.assign(queryForm,{page:1,pageSize:10,realname:'',status:null,email:'',deptId:currentDeptId.value});loadData()}

function handleAdd(){
  isEdit.value=false;dlgTitle.value='新增用户'
  Object.assign(form,{id:null,account:'',realname:'',email:'',phone:'',sex:'男',deptId:currentDeptId.value,postId:null,roleId:null,islock:0})
  dlgVisible.value=true
}
function handleEdit(row){
  isEdit.value=true;dlgTitle.value='编辑用户'
  Object.assign(form,{id:row.id,account:row.account,realname:row.realname,email:row.email||'',phone:row.phone||'',sex:row.sex||'男',deptId:row.deptId,postId:row.postId,roleId:row.roleId,islock:row.islock||0})
  dlgVisible.value=true
}
function resetForm(){
  Object.assign(form,{id:null,account:'',realname:'',email:'',phone:'',sex:'男',deptId:null,postId:null,roleId:null,islock:0})
}
async function handleSubmit(){
  const valid=await formRef.value.validate().catch(()=>false)
  if(!valid) return
  submitLoading.value=true
  try{
    const submitData = { ...form }
    if (!isEdit.value) {
      if (!submitData.account) submitData.account = submitData.email // 新增时没填账号则用邮箱
    }
    await (isEdit.value ? updateUserInfo : addUser)(submitData)
    ElMessage.success(isEdit.value?'修改成功':'新增成功，默认密码为We123456')
    dlgVisible.value=false;loadData()
  }finally{submitLoading.value=false}
}

function handleDelete(row){
  if(row.islock===0){ ElMessage.warning('启用状态下不可删除'); return }
  ElMessageBox.confirm('确定删除该用户吗？删除后不可恢复。','确认删除',{type:'warning'})
    .then(async()=>{ await updateUserStatus(row.id,-1); ElMessage.success('已删除'); loadData() }).catch(()=>{})
}
function handleToggle(row){
  if(row.islock===0){
    // 禁用需要确认弹窗
    ElMessageBox.confirm('此操作将禁用该用户，是否继续？','确认禁用',{type:'warning'})
      .then(async()=>{ await updateUserStatus(row.id,1); ElMessage.success('禁用成功'); loadData() }).catch(()=>{})
  } else {
    // 启用直接操作，不出确认弹窗
    updateUserStatus(row.id,0).then(()=>{ ElMessage.success('启用成功'); loadData() })
  }
}
function handleResetPwd(row){
  ElMessageBox.confirm('此操作将重置该用户密码，是否继续？初始密码为We123456', '确认重置密码', {type:'warning'})
    .then(async()=>{ await resetPassword(row.id); ElMessage.success('密码已重置') }).catch(()=>{})
}

onMounted(()=>{window.addEventListener('resize',syncWideColumns);loadOptions();loadData()})
onBeforeUnmount(()=>window.removeEventListener('resize',syncWideColumns))
</script>

<style scoped>
.user-layout { display: flex; gap: 0; min-height: 400px; }
.tree-panel { width: 200px; flex-shrink: 0; border-right: 1px solid #e7e9ed; padding-right: 16px; }
.tree-header { font-size: 15px; font-weight: 600; color: #333; margin-bottom: 10px; }
.tree-search { margin-bottom: 8px; }
.tree-panel :deep(.el-tree) { border: none; }
.tree-panel :deep(.el-tree-node__content) { height: 32px; }
.content-panel { flex: 1; min-width: 0; padding-left: 16px; }
.query-form { margin-bottom: 4px; }
.toolbar { margin-bottom: 10px; }
.pagination-wrap { margin-top: 12px; display: flex; justify-content: flex-end; }
</style>
