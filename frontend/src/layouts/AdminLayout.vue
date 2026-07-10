<template>
  <el-container class="admin-shell">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="admin-sidebar">
      <div class="brand-block">
        <div class="brand-mark">养</div>
        <span v-show="!isCollapse" class="brand-name">智慧养老服务平台</span>
      </div>

      <el-scrollbar class="menu-scrollbar">
        <el-menu
          :default-active="route.path"
          :collapse="isCollapse"
          :collapse-transition="false"
          :unique-opened="true"
          class="admin-menu"
          router
        >
          <template v-for="menu in menuList" :key="menu.id">
            <el-menu-item v-if="!hasChildren(menu) && menu.path" :index="menu.path">
              <el-icon><component :is="iconFor(menu.mname)" /></el-icon>
              <template #title>{{ menu.mname }}</template>
            </el-menu-item>

            <el-sub-menu v-else-if="hasChildren(menu)" :index="String(menu.id)">
              <template #title>
                <el-icon><component :is="iconFor(menu.mname)" /></el-icon>
                <span>{{ menu.mname }}</span>
              </template>
              <template v-for="subMenu in childrenOf(menu)" :key="subMenu.id">
                <el-menu-item
                  v-if="!hasChildren(subMenu) && subMenu.path"
                  :index="subMenu.path"
                >
                  {{ subMenu.mname }}
                </el-menu-item>
                <el-sub-menu v-else-if="hasChildren(subMenu)" :index="String(subMenu.id)">
                  <template #title>{{ subMenu.mname }}</template>
                  <el-menu-item
                    v-for="leaf in childrenOf(subMenu)"
                    :key="leaf.id"
                    :index="leaf.path"
                  >
                    {{ leaf.mname }}
                  </el-menu-item>
                </el-sub-menu>
              </template>
            </el-sub-menu>
          </template>
        </el-menu>
      </el-scrollbar>
    </el-aside>

    <el-container class="admin-main-container">
      <el-header class="admin-header">
        <div class="header-left">
          <el-button
            class="collapse-button"
            link
            :aria-label="isCollapse ? '展开菜单' : '收起菜单'"
            @click="isCollapse = !isCollapse"
          >
            <el-icon :size="20"><Expand v-if="isCollapse" /><Fold v-else /></el-icon>
          </el-button>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>工作台</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentTitle && currentTitle !== '工作台'">{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-tooltip content="消息中心" placement="bottom">
            <el-badge :hidden="unreadCount === 0" :value="unreadCount" :max="99">
              <el-button link class="header-icon-button" @click="goMessages">
                <el-icon :size="19"><Bell /></el-icon>
              </el-button>
            </el-badge>
          </el-tooltip>

          <el-dropdown trigger="click" @command="handleUserCommand">
            <button class="user-trigger" type="button">
              <el-avatar :size="30" :src="userImage">{{ userInitial }}</el-avatar>
              <span class="user-name">{{ realName || '管理员' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">个人中心</el-dropdown-item>
                <el-dropdown-item command="password">修改密码</el-dropdown-item>
                <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="admin-content">
        <div class="content-inner">
          <slot />
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import {
  ArrowDown,
  Bell,
  Expand,
  Fold,
  Menu as MenuIcon,
  Setting,
  User,
  Odometer,
  Service,
  Checked,
  Monitor,
  CreditCard,
  UserFilled
} from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const isCollapse = ref(false)
const realName = ref('')
const userImage = ref('')
const unreadCount = ref(0)
const menuList = ref([])

const fallbackMenus = [
  { id: 1, mname: '工作台', path: '/Dashboard', subItems: [] },
  { id: 2, mname: '个人中心', path: '', subItems: [
    { id: 21, mname: '个人信息', path: '/UserInfo', subItems: [] },
    { id: 22, mname: '修改密码', path: '/ModifyPwd', subItems: [] }
  ] },
  { id: 3, mname: '服务管理', path: '', subItems: [
    { id: 31, mname: '护理项目', path: '/NursingItem', subItems: [] },
    { id: 32, mname: '护理计划', path: '/NursingPlain', subItems: [] }
  ] }
]

const iconMap = {
  工作台: Odometer,
  个人中心: User,
  服务管理: Service,
  订单管理: CreditCard,
  财务管理: CreditCard,
  权限配置: Setting,
  智能监测: Monitor,
  协同工作: Checked,
  客户管理: UserFilled
}

const currentTitle = computed(() => route.meta?.title || '')
const userInitial = computed(() => (realName.value || '管').slice(0, 1))

function childrenOf(menu) {
  return Array.isArray(menu?.subItems) ? menu.subItems : []
}

function hasChildren(menu) {
  return childrenOf(menu).length > 0
}

function iconFor(name) {
  return iconMap[name] || MenuIcon
}

function loadMenus() {
  axios.get('/sysMenus')
    .then(({ data }) => {
      menuList.value = Array.isArray(data) && data.length ? data : fallbackMenus
    })
    .catch(() => {
      menuList.value = fallbackMenus
    })
}

function loadUserInfo() {
  axios.get('/loadInfo')
    .then(({ data }) => {
      realName.value = data?.uname || ''
      userImage.value = data?.image || ''
    })
    .catch(() => {})
}

function goMessages() {
  router.push('/Messages')
}

function handleUserCommand(command) {
  if (command === 'profile') router.push('/UserInfo')
  if (command === 'password') router.push('/ModifyPwd')
  if (command === 'logout') {
    localStorage.clear()
    sessionStorage.clear()
    router.replace('/')
  }
}

onMounted(() => {
  loadMenus()
  loadUserInfo()
})
</script>

<style scoped>
.admin-shell,
.admin-main-container {
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.admin-sidebar {
  flex: 0 0 auto;
  background: #1f2d3d;
  transition: width 0.2s ease;
}

.brand-block {
  display: flex;
  align-items: center;
  height: 64px;
  padding: 0 16px;
  color: #fff;
  white-space: nowrap;
  overflow: hidden;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

.brand-mark {
  display: grid;
  place-items: center;
  flex: 0 0 32px;
  width: 32px;
  height: 32px;
  border-radius: 4px;
  background: #2f8f83;
  font-size: 18px;
  font-weight: 600;
}

.brand-name {
  margin-left: 10px;
  font-size: 15px;
  font-weight: 600;
}

.menu-scrollbar {
  height: calc(100% - 64px);
}

.admin-menu {
  border-right: 0;
  background: transparent;
}

.admin-menu:not(.el-menu--collapse) {
  width: 220px;
}

.admin-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
  padding: 0 24px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
}

.header-left,
.header-right,
.user-trigger {
  display: flex;
  align-items: center;
}

.header-left {
  gap: 14px;
}

.header-right {
  gap: 20px;
}

.collapse-button,
.header-icon-button {
  color: #606266;
}

.user-trigger {
  gap: 8px;
  padding: 0;
  color: #303133;
  background: transparent;
  border: 0;
  cursor: pointer;
}

.user-name {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.admin-content {
  padding: 20px;
  overflow: auto;
  background: #f4f6f8;
}

.content-inner {
  min-height: 100%;
  padding: 20px;
  background: #fff;
  border: 1px solid #ebeef5;
}
</style>
