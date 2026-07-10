import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MainIndex from '../views/MainIndex.vue'
import UserInfo from '../views/UesrInfo'
import ModifyPwd from '../views/ModifyPwd'
import request from '@/api/request'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { title: '登录' }
  },
  {
    path: '/family/login',
    name: 'FamilyLogin',
    component: () => import('@/views/family/FamilyLogin.vue'),
    meta: { title: '家属登录' }
  },
  {
    path: '/family',
    name: 'FamilyIndex',
    component: () => import('@/layouts/FamilyLayout.vue'),
    redirect: '/family/mine',
    meta: { title: '家属端', requiresFamilyAuth: true },
    children: [
      {
        path: 'mine',
        name: 'FamilyMine',
        component: () => import('@/views/family/FamilyMine.vue'),
        meta: { title: '我的' }
      },
      {
        path: 'contracts',
        name: 'FamilyContracts',
        component: () => import('@/views/family/FamilyContracts.vue'),
        meta: { title: '我的合同' }
      },
      {
        path: 'appointments',
        name: 'FamilyAppointments',
        component: () => import('@/views/family/FamilyAppointments.vue'),
        meta: { title: '我的预约' }
      },
      {
        path: 'orders',
        name: 'FamilyOrders',
        component: () => import('@/views/family/FamilyOrders.vue'),
        meta: { title: '我的订单' }
      },
      {
        path: 'orders/:id',
        name: 'FamilyOrderDetail',
        component: () => import('@/views/family/FamilyOrderDetail.vue'),
        meta: { title: '订单详情' }
      },
      {
        path: 'bills',
        name: 'FamilyBills',
        component: () => import('@/views/family/FamilyBills.vue'),
        meta: { title: '我的账单' }
      }
    ]
  },
  {
    path: '/MainIndex',
    name: 'MainIndex',
    component: MainIndex,
    redirect: '/Dashboard',
    meta: { title: '工作台', requiresAuth: true },
    children: [
      {
        path: '/UserInfo',
        name: 'UserInfo',
        component: UserInfo,
        meta: { title: '个人信息' }
      },
      {
        path: '/ModifyPwd',
        name: 'ModifyPwd',
        component: ModifyPwd,
        meta: { title: '修改密码' }
      },
      { path: '/NursingItem', name: 'NursingItem', meta: { title: '护理项目' }, component: () => import('@/views/NursingItem.vue') },
      { path: '/NursingPlain', name: 'NursingPlain', meta: { title: '护理计划' }, component: () => import('@/views/NursingPlain.vue') },
      { path: '/Dashboard', name: 'Dashboard', meta: { title: '工作台' }, component: () => import('@/views/admin/Dashboard.vue') },
      { path: '/Messages', name: 'Messages', meta: { title: '消息中心' }, component: () => import('@/views/admin/Messages.vue') },
      { path: '/Visit', name: 'Visit', meta: { title: '来访管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/VisitRecord', name: 'VisitRecord', meta: { title: '来访登记' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Apply', name: 'Apply', meta: { title: '入退管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Checkout', name: 'Checkout', meta: { title: '退住办理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Resident', name: 'Resident', meta: { title: '在住管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Bed', name: 'Bed', meta: { title: '床位房型' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/SmartBed', name: 'SmartBed', meta: { title: '智能床位' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/RoomType', name: 'RoomType', meta: { title: '房型设置' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Leave', name: 'Leave', meta: { title: '请假管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/NursingLevel', name: 'NursingLevel', meta: { title: '护理等级' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/ElderAssignment', name: 'ElderAssignment', meta: { title: '负责老人' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/NursingTask', name: 'NursingTask', meta: { title: '任务安排' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Order', name: 'Order', meta: { title: '订单管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Refund', name: 'Refund', meta: { title: '退款管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Bill', name: 'Bill', meta: { title: '财务管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Arrears', name: 'Arrears', meta: { title: '欠费老人' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Prepay', name: 'Prepay', meta: { title: '预缴款充值' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Balance', name: 'Balance', meta: { title: '余额查询' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Customer', name: 'Customer', meta: { title: '客户管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Todo', name: 'Todo', meta: { title: '协同工作' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Application', name: 'Application', meta: { title: '我的申请' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Device', name: 'Device', meta: { title: '智能监测' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Alert', name: 'Alert', meta: { title: '报警数据' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/AlertRule', name: 'AlertRule', meta: { title: '报警规则' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/UserManage', name: 'UserManage', meta: { title: '用户管理' }, component: () => import('@/views/admin/UserManage.vue') },
      { path: '/RoleManage', name: 'RoleManage', meta: { title: '角色管理' }, component: () => import('@/views/admin/RoleManage.vue') },
      { path: '/RoleDataScope', name: 'RoleDataScope', meta: { title: '角色数据权限' }, component: () => import('@/views/admin/RoleDataScope.vue') },
      { path: '/MenuManage', name: 'MenuManage', meta: { title: '菜单管理' }, component: () => import('@/views/admin/MenuManage.vue') },
      { path: '/Department', name: 'Department', meta: { title: '部门管理' }, component: () => import('@/views/admin/Department.vue') },
      { path: '/Post', name: 'Post', meta: { title: '职位管理' }, component: () => import('@/views/admin/Post.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

// 路由守卫：管理端登录状态检查和恢复
router.beforeEach(async (to, from, next) => {
  // 管理端认证
  if (to.matched.some(record => record.meta.requiresAuth !== false && (record.meta.requiresAuth || record.path.startsWith('/MainIndex')))) {
    // 先检查是否已有登录态
    if (to.path === '/MainIndex' || to.matched.some(r => r.path && r.path.startsWith('/MainIndex'))) {
      try {
        const res = await request.get('/loadInfo')
        if (res && res.id) {
          // 登录态有效
          next()
          return
        }
      } catch (e) {
        // 登录态失效，跳回登录页
        localStorage.clear()
        sessionStorage.clear()
        next('/')
        return
      }
    }
    next()
    return
  }

  // 家属端认证
  if (to.matched.some(record => record.meta.requiresFamilyAuth)) {
    const familyToken = localStorage.getItem('familyToken')
    if (!familyToken) {
      next('/family/login')
      return
    }
    try {
      const res = await request.get('/family/profile')
      if (res && res.id) {
        next()
        return
      }
    } catch (e) {
      localStorage.removeItem('familyToken')
      localStorage.removeItem('familyUser')
      next('/family/login')
      return
    }
  }

  // 已登录的管理员不能访问登录页
  if (to.path === '/') {
    try {
      const res = await request.get('/loadInfo')
      if (res && res.id) {
        next('/MainIndex')
        return
      }
    } catch (e) {
      // 未登录，正常访问登录页
    }
  }

  // 已登录的家属不能访问家属登录页
  if (to.path === '/family/login') {
    const familyToken = localStorage.getItem('familyToken')
    if (familyToken) {
      try {
        const res = await request.get('/family/profile')
        if (res && res.id) {
          next('/family/mine')
          return
        }
      } catch (e) {
        localStorage.removeItem('familyToken')
      }
    }
  }

  next()
})

export default router
