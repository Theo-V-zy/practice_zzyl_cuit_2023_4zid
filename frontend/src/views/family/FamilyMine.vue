<template>
  <div class="family-mine">
    <!-- 用户信息 -->
    <div class="user-card">
      <img class="user-avatar" :src="userInfo.avatar || defaultAvatar" alt="头像" />
      <div class="user-info">
        <div class="user-name">{{ userInfo.name || '家属用户' }}</div>
        <div class="user-phone">{{ userInfo.phone || '-' }}</div>
      </div>
    </div>

    <!-- 菜单入口 -->
    <div class="menu-grid">
      <div class="menu-item" @click="router.push('/family/contracts')">
        <el-icon :size="24"><Notebook /></el-icon>
        <span>我的合同</span>
      </div>
      <div class="menu-item" @click="router.push('/family/appointments')">
        <el-icon :size="24"><Calendar /></el-icon>
        <span>我的预约</span>
      </div>
      <div class="menu-item" @click="router.push('/family/orders')">
        <el-icon :size="24"><Service /></el-icon>
        <span>我的订单</span>
      </div>
      <div class="menu-item" @click="router.push('/family/bills')">
        <el-icon :size="24"><CreditCard /></el-icon>
        <span>我的账单</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Notebook, Calendar, Service, CreditCard } from '@element-plus/icons-vue'
import { familyProfile } from '@/api/family'
import defaultAvatar from '@/assets/zhyl-user-avatar.png'

const router = useRouter()
const userInfo = ref({})

onMounted(async () => {
  try {
    const res = await familyProfile()
    if (res && res.name) userInfo.value = res
  } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.family-mine { min-width: 0; }
.user-card {
  display: flex;
  align-items: center;
  padding: 24px 16px;
  background: linear-gradient(135deg, #0052d9, #3370ff);
  border-radius: 10px;
  color: #fff;
}
.user-avatar { width: 56px; height: 56px; border-radius: 50%; border: 2px solid rgba(255,255,255,0.5); object-fit: cover; }
.user-info { margin-left: 14px; }
.user-name { font-size: 18px; font-weight: 600; }
.user-phone { font-size: 13px; margin-top: 4px; opacity: 0.85; }
.menu-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-top: 20px;
}
.menu-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 24px 12px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #e7e9ed;
  gap: 8px;
  color: #333;
  font-size: 14px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.menu-item:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.menu-item .el-icon { color: #0052d9; }
</style>
