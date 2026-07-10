<template>
  <div class="family-shell">
    <header class="family-header">
      <button v-if="showBack" class="back-btn" type="button" @click="router.back()">
        <el-icon :size="20"><ArrowLeft /></el-icon>
      </button>
      <span class="family-header-title">{{ title }}</span>
      <button class="logout-btn" type="button" @click="handleLogout">退出</button>
    </header>

    <main class="family-content">
      <router-view />
    </main>

    <nav class="family-tabs">
      <button
        v-for="tab in tabs"
        :key="tab.path"
        class="family-tab"
        :class="{ 'is-active': route.path === tab.path }"
        type="button"
        @click="router.push(tab.path)"
      >
        <el-icon :size="20"><component :is="tab.icon" /></el-icon>
        <span>{{ tab.label }}</span>
      </button>
    </nav>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ArrowLeft, User, House, Service, Notebook } from '@element-plus/icons-vue'
import { familyLogout } from '@/api/family'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

const showBack = computed(() => {
  return route.path !== '/family/mine'
})

const title = computed(() => {
  return route.meta.title || '家属端'
})

const tabs = [
  { path: '/family/mine', label: '我的', icon: User },
  { path: '/family/contracts', label: '合同', icon: Notebook },
  { path: '/family/orders', label: '订单', icon: Service },
  { path: '/family/mine', label: '首页', icon: House }
]

// 简化导航：只保留"我的"作为核心
const actualTabs = [
  { path: '/family/mine', label: '我的', icon: User }
]

function handleLogout() {
  ElMessageBox.confirm('确定要退出吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    familyLogout().finally(() => {
      localStorage.removeItem('familyToken')
      localStorage.removeItem('familyUser')
      router.replace('/family/login')
      ElMessage.success('已退出')
    })
  }).catch(() => {})
}
</script>

<style scoped>
.family-shell {
  display: flex;
  flex-direction: column;
  width: 100%;
  max-width: 480px;
  min-height: 100vh;
  margin: 0 auto;
  background: #f4f5f7;
  font-size: 14px;
}

.family-header {
  display: flex;
  align-items: center;
  height: 48px;
  padding: 0 16px;
  background: #0052d9;
  color: #fff;
  flex-shrink: 0;
}

.back-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  margin-right: 8px;
  color: #fff;
  background: transparent;
  border: 0;
  cursor: pointer;
}

.family-header-title {
  flex: 1;
  font-size: 17px;
  font-weight: 500;
  text-align: center;
}

.logout-btn {
  padding: 4px 12px;
  color: #fff;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.5);
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
}

.family-content {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  -webkit-overflow-scrolling: touch;
}

.family-tabs {
  display: flex;
  height: 56px;
  background: #fff;
  border-top: 1px solid #e7e9ed;
  flex-shrink: 0;
}

.family-tab {
  display: flex;
  flex: 1;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  padding: 4px;
  color: rgba(0, 0, 0, 0.45);
  background: transparent;
  border: 0;
  cursor: pointer;
  font-size: 11px;
  transition: color 0.2s;
}

.family-tab.is-active {
  color: #0052d9;
}
</style>
