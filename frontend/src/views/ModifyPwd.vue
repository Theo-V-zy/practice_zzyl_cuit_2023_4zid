<template>
  <section class="page-section">
    <h3 class="page-title">修改密码</h3>
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" class="pwd-form" size="large">
      <el-form-item prop="oldpwd">
        <template #label>
          <span style="color:red">*</span> 原密码
        </template>
        <el-input v-model="form.oldpwd" type="password" placeholder="请输入" show-password />
      </el-form-item>
      <el-form-item prop="newpwd">
        <template #label>
          <span style="color:red">*</span> 新密码
        </template>
        <el-input v-model="form.newpwd" type="password" placeholder="请输入" show-password />
        <p class="pwd-hint">密码长度8-20位，必须包含数字、小写字母、大写字母</p>
      </el-form-item>
      <el-form-item prop="newpwd2">
        <template #label>
          <span style="color:red">*</span> 确认新密码
        </template>
        <el-input v-model="form.newpwd2" type="password" placeholder="请输入" show-password />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSubmit">确定</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 确认修改弹窗 -->
    <el-dialog v-model="confirmVisible" title="确认修改" width="420px" :close-on-click-modal="false">
      <p style="text-align:center; font-size:15px;">密码修改成功后，需重新登录，是否继续？</p>
      <template #footer>
        <el-button @click="confirmVisible = false">取消</el-button>
        <el-button type="primary" @click="doUpdatePwd">确定修改</el-button>
      </template>
    </el-dialog>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { updatePwd, logout } from '@/api/admin'

const router = useRouter()
const formRef = ref(null)
const confirmVisible = ref(false)
const loading = ref(false)

const form = reactive({
  oldpwd: '',
  newpwd: '',
  newpwd2: ''
})

const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入密码'))
    return
  }
  if (value.length < 8 || value.length > 20) {
    callback(new Error('密码长度8-20位，请重新输入'))
    return
  }
  if (!/\d/.test(value)) {
    callback(new Error('必须包含数字，请重新输入'))
    return
  }
  if (!/[a-z]/.test(value)) {
    callback(new Error('必须包含小写字母，请重新输入'))
    return
  }
  if (!/[A-Z]/.test(value)) {
    callback(new Error('必须包含大写字母，请重新输入'))
    return
  }
  callback()
}

const rules = {
  oldpwd: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newpwd: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ],
  newpwd2: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== form.newpwd) {
          callback(new Error('新密码与确认新密码不一致，请重新输入'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  // 检查新密码是否与旧密码一致
  if (form.newpwd === form.oldpwd) {
    ElMessage.warning('新密码不能与原密码一致，请重新输入')
    return
  }

  // 弹确认弹窗
  confirmVisible.value = true
}

async function doUpdatePwd() {
  loading.value = true
  try {
    const res = await updatePwd({ oldpwd: form.oldpwd, newpwd: form.newpwd })
    if (res && res.code === 200) {
      confirmVisible.value = false
      ElMessage.success('密码修改成功，请重新登录')
      handleReset()
      // 退出登录
      logout().finally(() => {
        localStorage.clear()
        sessionStorage.clear()
        setTimeout(() => router.replace('/'), 500)
      })
    }
  } catch (e) { /* ignore */ }
  finally { loading.value = false }
}

function handleReset() {
  form.oldpwd = ''
  form.newpwd = ''
  form.newpwd2 = ''
  formRef.value?.clearValidate()
}
</script>

<style scoped>
.page-title { font-size: 18px; font-weight: 600; color: #333; margin: 0 0 24px; }
.pwd-form { max-width: 500px; }
.pwd-hint { font-size: 12px; color: rgba(0,0,0,0.4); margin: 4px 0 0; line-height: 1.5; }
</style>
