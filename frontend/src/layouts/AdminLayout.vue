<template>
  <div class="admin-shell">
    <header class="admin-header">
      <button class="brand-block" type="button" @click="router.push('/Dashboard')">
        <img class="brand-logo" :src="logoMark" alt="中州养老" />
        <span class="brand-copy">
          <strong>中州养老</strong>
          <small>ZHONG ZHOU YANG LAO</small>
        </span>
      </button>

      <div class="topbar">
        <nav class="top-navigation" aria-label="功能模块">
          <button
            v-for="module in topModules"
            :key="module.id"
            class="top-module"
            :class="{ 'is-active': activeModule?.id === module.id }"
            type="button"
            @click="selectTopModule(module)"
          >
            {{ module.mname }}
          </button>
        </nav>

        <button class="bell-btn" type="button" @click="router.push('/Messages')" title="消息通知">
            <el-icon :size="20"><Bell /></el-icon>
            <span v-if="unreadCount > 0" class="bell-badge">{{ unreadCount > 99 ? '99+' : unreadCount }}</span>
          </button>

        <el-dropdown trigger="click" @command="handleUserMenu">
          <button class="user-trigger" type="button">
            <img class="user-avatar" :src="userImage || defaultAvatar" alt="管理员头像" @error="handleUserAvatarError" />
            <span>{{ realName || '管理员' }}</span>
            <el-icon :size="13"><ArrowDown /></el-icon>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="password">修改密码</el-dropdown-item>
              <el-dropdown-item command="messages">消息通知</el-dropdown-item>
              <el-dropdown-item divided command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </header>

    <div class="admin-workspace">
      <aside class="admin-sidebar">
        <el-scrollbar class="menu-scrollbar">
          <el-menu
            :default-active="route.path"
            :default-openeds="defaultOpeneds"
            class="admin-menu"
            router
          >
            <template v-for="menu in sideMenuItems" :key="menu.id">
              <el-menu-item v-if="!hasChildren(menu) && menu.path" :index="menu.path">
                <el-icon><component :is="iconFor(menu.mname)" /></el-icon>
                <span>{{ menu.mname }}</span>
              </el-menu-item>

              <el-sub-menu v-else-if="hasChildren(menu)" :index="String(menu.id)">
                <template #title>
                  <el-icon><component :is="iconFor(menu.mname)" /></el-icon>
                  <span>{{ menu.mname }}</span>
                </template>
                <template v-for="subMenu in childrenOf(menu)" :key="subMenu.id">
                  <el-menu-item v-if="!hasChildren(subMenu) && subMenu.path" :index="subMenu.path">
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
      </aside>

      <main class="admin-content">
        <div class="content-inner">
          <slot />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
import { computed, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getMenus, loadInfo, logout, getMessagePage } from '@/api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  ArrowDown,
  Bell,
  Calendar,
  Checked,
  CreditCard,
  DataAnalysis,
  House,
  Menu as MenuIcon,
  ChatDotRound,
  Monitor,
  OfficeBuilding,
  Service,
  Setting,
  User,
  UserFilled
} from '@element-plus/icons-vue'
import { prototypeNavigation } from '@/config/adminNavigation'
import logoMark from '@/assets/zhyl-logo-mark.png'
import defaultAvatar from '@/assets/zhyl-user-avatar.png'

const route = useRoute()
const router = useRouter()
const realName = ref('')
const userImage = ref('')
const unreadCount = ref(0)
const menuList = ref(prototypeNavigation)
const activeModuleId = ref(1)
let unreadTimer

const iconMap = {
  工作台: House,
  预约来访: Calendar,
  入住管理: OfficeBuilding,
  退住管理: OfficeBuilding,
  合同管理: CreditCard,
  床位管理: OfficeBuilding,
  请假管理: Calendar,
  护理计划: Service,
  护理任务: Checked,
  订单管理: CreditCard,
  账单管理: CreditCard,
  预存管理: CreditCard,
  客户信息: UserFilled,
  用户管理: User,
  权限配置: Setting,
  我的待办: Checked,
  我的申请: DataAnalysis,
  设备管理: Monitor,
  报警管理: Bell,
  AI助手: ChatDotRound,
  个人中心: User,
  消息中心: Bell
}

const topModules = computed(() => menuList.value.filter((item) => item.topVisible !== false))
const activeModule = computed(() => menuList.value.find((item) => item.id === activeModuleId.value) || topModules.value[0])
const sideMenuItems = computed(() => {
  const module = activeModule.value
  return module ? (hasChildren(module) ? childrenOf(module) : [module]) : []
})
const defaultOpeneds = computed(() => collectParentIds(sideMenuItems.value))

function childrenOf(menu) {
  return Array.isArray(menu?.subItems) ? menu.subItems : []
}

function hasChildren(menu) {
  return childrenOf(menu).length > 0
}

function iconFor(name) {
  return iconMap[name] || MenuIcon
}

function collectParentIds(items) {
  return items.flatMap((item) => hasChildren(item) ? [String(item.id), ...collectParentIds(childrenOf(item))] : [])
}

function containsPath(item, path) {
  return item.path === path || childrenOf(item).some((child) => containsPath(child, path))
}

function firstPath(item) {
  if (item.path) return item.path
  for (const child of childrenOf(item)) {
    const p = firstPath(child)
    if (p) return p
  }
  return '/Dashboard'
}

function syncModuleWithRoute(path) {
  const matched = menuList.value.find((module) => containsPath(module, path))
  if (matched) {
    activeModuleId.value = matched.id
    nextTick(() => document.querySelector('.top-module.is-active')?.scrollIntoView({ behavior: 'smooth', block: 'nearest', inline: 'center' }))
  }
}

function selectTopModule(module) {
  activeModuleId.value = module.id
  router.push(firstPath(module))
}

function useServerMenus(data) {
  // 只要服务端返回数组，就直接使用（服务端已按角色过滤）
  return Array.isArray(data) && data.length > 0
}

function loadMenus() {
  getMenus()
    .then((data) => {
      if (useServerMenus(data)) {
        // 个人中心和消息中心不走侧栏，仅通过头像下拉和铃铛访问
        const hideFromSidebar = ['个人中心', '消息中心']
        menuList.value = data
          .filter((item) => !hideFromSidebar.includes(item.mname))
          .sort((a, b) => (a.sort || 0) - (b.sort || 0))
          .map((item) => ({
            ...item,
            subItems: (item.subItems || []).sort((a, b) => (a.sort || 0) - (b.sort || 0))
          }))
      }
      syncModuleWithRoute(route.path)
    })
    .catch(() => {})
}

function loadUserInfo() {
  // 先从缓存恢复，秒开头像
  const cached = localStorage.getItem('adminUser')
  if (cached) {
    try {
      const u = JSON.parse(cached)
      realName.value = u.realname || ''
      userImage.value = u.image || ''
    } catch (e) { /* ignore */ }
  }
  // 再从接口刷新
  loadInfo()
    .then((data) => {
      if (data && data.realname) {
        realName.value = data.realname
        userImage.value = data.image || ''
        localStorage.setItem('adminUser', JSON.stringify(data))
      }
    })
    .catch(() => {})
}

function handleUserAvatarError() {
  userImage.value = ''
}

function handleUserMenu(command) {
  if (command === 'profile') router.push('/UserInfo')
  if (command === 'password') router.push('/ModifyPwd')
  if (command === 'messages') router.push('/Messages')
  if (command === 'logout') {
    ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
    }).then(() => {
      logout().finally(() => {
        localStorage.clear(); sessionStorage.clear()
        router.replace('/'); ElMessage.success('已退出登录')
      })
    }).catch(() => {})
  }
}

watch(() => route.path, syncModuleWithRoute, { immediate: true })

function loadUnreadCount() {
  getMessagePage({ page: 1, pageSize: 1, readStatus: 0 })
    .then(res => { if (res) unreadCount.value = res.total || 0 })
    .catch(() => {})
}

onMounted(() => {
  loadMenus()
  loadUserInfo()
  loadUnreadCount()
  unreadTimer = window.setInterval(loadUnreadCount, 30000)
})

onBeforeUnmount(() => {
  window.clearInterval(unreadTimer)
})
</script>

<style scoped>
.admin-shell {
  display: grid;
  grid-template-rows: 64px minmax(0, 1fr);
  width: 100%;
  height: 100%;
  overflow: hidden;
  color: #333;
  background: #f4f5f7;
}

.admin-header {
  display: grid;
  grid-template-columns: 218px minmax(0, 1fr);
  height: 64px;
  background: #f6f7f9;
  border-bottom: 1px solid #e7e9ed;
}

.brand-block {
  display: flex;
  align-items: center;
  height: 64px;
  padding: 4px 18px 4px 20px;
  color: #333;
  background: #fff;
  border: 0;
  border-right: 1px solid #e7e9ed;
  cursor: pointer;
}

.brand-logo {
  flex: 0 0 48px;
  width: 48px;
  height: 48px;
  object-fit: contain;
}

.brand-copy {
  display: flex;
  min-width: 0;
  margin-left: 8px;
  flex-direction: column;
  align-items: flex-start;
  white-space: nowrap;
}

.brand-copy strong {
  font-family: STXingkai, "Xingkai SC", "Kaiti SC", serif;
  font-size: 25px;
  line-height: 30px;
}

.brand-copy small {
  font-family: Arial, sans-serif;
  font-size: 9px;
  line-height: 14px;
  color: rgba(51, 51, 51, 0.8);
}

.topbar {
  display: flex;
  min-width: 0;
  align-items: stretch;
  background: #f6f7f9;
}

.top-navigation {
  display: flex;
  min-width: 0;
  flex: 1;
  align-items: stretch;
  gap: 4px;
  padding: 0 14px;
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: none;
  background: #f6f7f9;
}

.top-navigation::-webkit-scrollbar {
  display: none;
}

.top-module {
  position: relative;
  flex: 0 0 78px;
  height: 64px;
  padding: 0;
  color: rgba(0, 0, 0, 0.6);
  background: transparent;
  border: 0;
  font-size: 14px;
  cursor: pointer;
  white-space: nowrap;
}

.top-module::after {
  position: absolute;
  right: 18px;
  bottom: 0;
  left: 18px;
  height: 2px;
  content: '';
  background: transparent;
}

.top-module:hover,
.top-module.is-active {
  color: #0052d9;
}

.top-module.is-active::after {
  background: #0052d9;
}

.bell-btn {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 64px;
  padding: 0;
  color: rgba(0, 0, 0, 0.5);
  background: transparent;
  border: 0;
  cursor: pointer;
  margin-right: 4px;
}
.bell-btn:hover { color: #0052d9; }
.bell-badge {
  position: absolute;
  top: 14px;
  right: 2px;
  min-width: 18px;
  height: 18px;
  padding: 0 4px;
  background: #e34d59;
  color: #fff;
  font-size: 10px;
  font-weight: 600;
  line-height: 18px;
  text-align: center;
  border-radius: 9px;
  pointer-events: none;
}

.user-trigger {
  display: flex;
  flex: 0 0 auto;
  min-width: 126px;
  height: 64px;
  padding: 0 18px 0 10px;
  align-items: center;
  gap: 8px;
  color: #333;
  background: transparent;
  border: 0;
  cursor: pointer;
  font-size: 14px;
}

.user-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  object-fit: cover;
}

.admin-workspace {
  display: grid;
  grid-template-columns: 218px minmax(0, 1fr);
  min-height: 0;
}

.admin-sidebar {
  display: grid;
  grid-template-rows: minmax(0, 1fr) auto;
  min-height: 0;
  background: #fff;
  border-right: 1px solid #e7e9ed;
}

.menu-scrollbar {
  min-height: 0;
}

.admin-menu {
  padding: 14px 8px;
  border-right: 0;
}

.admin-menu :deep(.el-menu-item),
.admin-menu :deep(.el-sub-menu__title) {
  height: 40px;
  margin: 0;
  color: rgba(0, 0, 0, 0.6);
  border-radius: 0;
  font-size: 14px;
  line-height: 40px;
}

.admin-menu :deep(.el-sub-menu__title:hover),
.admin-menu :deep(.el-menu-item:hover) {
  color: rgba(0, 0, 0, 0.9);
  background: #f5f7fa;
}

.admin-menu :deep(.el-menu-item.is-active) {
  color: #0052d9;
  background: #eef4ff;
}

.admin-menu :deep(.el-icon) {
  font-size: 16px;
}

.copyright {
  margin: 12px 16px 18px;
  color: rgba(0, 0, 0, 0.35);
  font-size: 11px;
  line-height: 18px;
  text-align: center;
}

.admin-content {
  min-width: 0;
  min-height: 0;
  padding: 16px;
  overflow: auto;
  background: #f4f5f7;
}

.content-inner {
  min-width: 0;
  min-height: 100%;
  background: transparent;
}

@media (max-width: 1200px) {
  .admin-header,
  .admin-workspace {
    grid-template-columns: 196px minmax(0, 1fr);
  }
  .brand-block {
    padding-right: 10px;
    padding-left: 14px;
  }
  .brand-logo {
    width: 44px;
    height: 44px;
    flex-basis: 44px;
  }
  .brand-copy strong {
    font-size: 22px;
  }
  .top-navigation {
    gap: 4px;
    padding: 0 10px;
  }
  .top-module {
    flex-basis: 76px;
  }
  .admin-content {
    padding: 12px;
  }
}
</style>
