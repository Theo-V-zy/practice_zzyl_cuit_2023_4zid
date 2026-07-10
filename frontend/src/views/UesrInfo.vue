<template>
  <section class="page-section">
    <h3 class="page-title">个人信息</h3>
    <div class="profile-layout">
      <!-- 左侧表单 -->
      <div class="form-side">
        <el-form ref="formRef" :model="form" label-width="90px" label-position="left" class="profile-form">
          <el-form-item label="*姓名" prop="realname">
            <el-input v-model="form.realname" maxlength="10" show-word-limit />
          </el-form-item>
          <el-form-item label="*邮箱">
            <span class="readonly-text">{{ form.email || '-' }}</span>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="form.sex">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="*所属部门">
            <span class="readonly-text">{{ form.deptName || form.department || '-' }}</span>
          </el-form-item>
          <el-form-item label="*所属职位">
            <span class="readonly-text">{{ form.postName || form.job || '-' }}</span>
          </el-form-item>
          <el-form-item label="*角色">
            <span class="readonly-text">{{ form.roleName || form.role || '-' }}</span>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" maxlength="11" show-word-limit />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSave">保存</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 右侧头像 -->
      <div class="avatar-side">
        <el-form-item label="*头像" label-position="top">
          <el-upload
            class="avatar-uploader"
            :action="uploadUrl"
            :show-file-list="false"
            name="mf"
            :on-success="handleAvatarSuccess"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar-img" />
            <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
          </el-upload>
          <p class="avatar-tip">图片大小不超过2M</p>
          <p class="avatar-tip">仅支持上传PNG JPG JPEG类型图片</p>
        </el-form-item>
      </div>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { showInfo, updateUser } from '@/api/admin'
import request from '@/api/request'

const uploadUrl = (process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080') + '/upload'

const form = reactive({
  id: null,
  realname: '',
  email: '',
  sex: '男',
  phone: '',
  department: '',
  deptName: '',
  job: '',
  postName: '',
  role: '',
  roleName: '',
  image: ''
})

const imageUrl = ref(null)
const formRef = ref(null)

function loadData() {
  showInfo()
    .then(res => {
      if (res) {
        Object.assign(form, {
          id: res.id || res.ID,
          realname: res.realname || '',
          email: res.email || '',
          sex: res.sex || '男',
          phone: res.phone || '',
          department: res.department || '',
          deptName: res.deptName || '',
          job: res.job || '',
          postName: res.postName || '',
          role: res.role || '',
          roleName: res.roleName || '',
          image: res.image || ''
        })
        imageUrl.value = res.image || ''
      }
    })
    .catch(() => {})
}

function handleAvatarSuccess(value) {
  imageUrl.value = value
  form.image = value
}

function handleSave() {
  if (!form.realname.trim()) {
    ElMessage.warning('请输入姓名')
    return
  }
  updateUser({
    id: form.id,
    realname: form.realname,
    sex: form.sex,
    phone: form.phone,
    email: form.email,
    image: form.image
  }).then(res => {
    if (res && res.code === 200) {
      ElMessage.success('个人信息已更新')
      // 更新 localStorage 缓存
      const cached = JSON.parse(localStorage.getItem('adminUser') || '{}')
      cached.realname = form.realname
      cached.image = form.image
      localStorage.setItem('adminUser', JSON.stringify(cached))
    }
  }).catch(() => {})
}

onMounted(() => loadData())
</script>

<style scoped>
.page-title { font-size: 18px; font-weight: 600; color: #333; margin: 0 0 24px; }
.profile-layout { display: flex; gap: 40px; }
.form-side { flex: 1; max-width: 480px; }
.avatar-side { flex-shrink: 0; }
.profile-form :deep(.el-form-item__label) { color: #333; font-size: 14px; }
.readonly-text { color: rgba(0,0,0,0.45); padding: 0 12px; line-height: 32px; display: inline-block; }

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  width: 158px;
  height: 158px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.avatar-uploader :deep(.el-upload:hover) { border-color: #0052d9; }
.avatar-img { width: 158px; height: 158px; object-fit: cover; border-radius: 6px; }
.avatar-uploader-icon { font-size: 28px; color: #8c939d; }
.avatar-tip { font-size: 12px; color: rgba(0,0,0,0.35); margin: 6px 0 0; max-width: 158px; text-align: center; }
</style>
