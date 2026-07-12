<template>
  <section class="page-section">
    <h3 class="page-title">个人信息</h3>
    <div class="profile-layout">
      <!-- 左侧表单 -->
      <div class="form-side">
        <el-form ref="formRef" :model="form" label-width="90px" label-position="left" class="profile-form" :require-asterisk-position="'left'">
          <el-form-item label="姓名" required>
            <el-input v-model="form.realname" maxlength="10" show-word-limit />
          </el-form-item>
          <el-form-item label="邮箱">
            <span class="readonly-text">{{ form.email || '-' }}</span>
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="form.sex">
              <el-radio label="男">男</el-radio>
              <el-radio label="女">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="所属部门">
            <span class="readonly-text">{{ form.deptName || form.department || '-' }}</span>
          </el-form-item>
          <el-form-item label="所属职位">
            <span class="readonly-text">{{ form.postName || form.job || '-' }}</span>
          </el-form-item>
          <el-form-item label="角色">
            <span class="readonly-text">{{ form.roleName || form.role || '-' }}</span>
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" maxlength="11" show-word-limit />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSave" :disabled="uploading" :loading="saving">
              {{ uploading ? '头像上传中...' : saving ? '保存中...' : '保存' }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 右侧头像 -->
      <div class="avatar-side">
        <div class="avatar-label">*头像</div>
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :http-request="customUpload"
          :before-upload="beforeAvatarUpload"
          accept="image/png,image/jpeg"
        >
          <img v-if="imageUrl" :src="imageUrl" class="avatar-img" @error="handleAvatarError" />
          <div v-else class="avatar-placeholder">
            <el-icon :size="32"><Plus /></el-icon>
            <span>上传文件</span>
          </div>
        </el-upload>
        <p class="avatar-tip">图片大小不超过2M</p>
        <p class="avatar-tip">仅支持上传PNG JPG JPEG类型图片</p>
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

const form = reactive({ id: null, realname: '', email: '', sex: '男', phone: '', department: '', deptName: '', job: '', postName: '', role: '', roleName: '', image: '' })
const imageUrl = ref(null)
const formRef = ref(null)
const uploading = ref(false)  // 上传 OSS 中
const saving = ref(false)     // 保存到数据库

function loadData() {
  showInfo().then(res => {
    if (res) {
      Object.assign(form, {
        id: res.id, realname: res.realname || '', email: res.email || '',
        sex: res.sex || '男', phone: res.phone || '', department: res.department || '',
        deptName: res.deptName || '', job: res.job || '', postName: res.postName || '',
        role: res.role || '', roleName: res.roleName || '', image: res.image || ''
      })
      imageUrl.value = res.image || ''
    }
  }).catch(() => {})
}

// 选完文件立刻上传 OSS，拿到 URL 后才允许保存
async function customUpload(options) {
  uploading.value = true
  try {
    const formData = new FormData()
    formData.append('mf', options.file)
    const res = await request.post('/upload', formData, { timeout: 60000 })
    const url = (typeof res === 'string') ? res : (res && (res.data || res.url || res))
    if (url && typeof url === 'string' && url.startsWith('http')) {
      imageUrl.value = url
      form.image = url  // 暂存 URL，等保存时才入库
      options.onSuccess?.(url)
    } else {
      throw new Error('返回异常: ' + JSON.stringify(res))
    }
  } catch (e) {
    console.error('上传失败详情:', e)
    const msg = e?.response?.data?.msg || e?.message || '未知错误'
    ElMessage.error('头像上传失败: ' + msg)
    options.onError?.(e)
  } finally {
    uploading.value = false
  }
}

function beforeAvatarUpload(file) {
  const typeAllowed = ['image/jpeg', 'image/png'].includes(file.type)
  if (!typeAllowed) { ElMessage.warning('仅支持 PNG、JPG、JPEG 图片'); return false }
  if (file.size > 2 * 1024 * 1024) { ElMessage.warning('图片大小不能超过 2MB'); return false }
  // 立刻本地预览
  imageUrl.value = URL.createObjectURL(file)
  return true
}

// 只存入库，不上传（上传已在选文件时完成）
function handleSave() {
  if (!form.realname.trim()) { ElMessage.warning('请输入姓名'); return }
  saving.value = true
  updateUser({
    id: form.id, realname: form.realname, sex: form.sex, phone: form.phone, email: form.email, image: form.image
  }).then(res => {
    if (res && res.code === 200) {
      ElMessage.success('个人信息已更新')
      const cached = JSON.parse(localStorage.getItem('adminUser') || '{}')
      cached.realname = form.realname; cached.image = form.image
      localStorage.setItem('adminUser', JSON.stringify(cached))
    }
  }).catch(() => {}).finally(() => { saving.value = false })
}

onMounted(() => loadData())
</script>

<style scoped>
.page-title { min-height: 42px; padding: 10px 14px; font-size: 15px; font-weight: 600; color: #333; margin: -4px 0 24px; background:#f5f6f8; }
.profile-layout { display: flex; max-width: 820px; gap: 60px; align-items: flex-start; }
.form-side { flex: 1; max-width: 520px; }
.avatar-side { flex-shrink: 0; }
.avatar-label { font-size: 14px; color: #333; margin-bottom: 8px; }
.avatar-label::before { content: '*'; color: red; margin-right: 2px; }

.avatar-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9; border-radius: 2px; cursor: pointer;
  width: 150px; height: 150px; display: flex; align-items: center; justify-content: center; overflow: hidden;
}
.avatar-uploader :deep(.el-upload):hover { border-color: #0052d9; }
.avatar-img { width: 150px; height: 150px; object-fit: cover; }
.avatar-placeholder { display: flex; flex-direction: column; align-items: center; gap: 8px; color: rgba(0,0,0,0.35); font-size: 13px; }
.avatar-tip { font-size: 12px; color: rgba(0,0,0,0.35); margin: 6px 0 0; max-width: 150px; text-align: left; }

.readonly-text { color: rgba(0,0,0,0.45); line-height: 32px; }
.readonly-field :deep(.el-input.is-disabled .el-input__wrapper) { background: #f5f7fa; box-shadow: none; }
</style>
