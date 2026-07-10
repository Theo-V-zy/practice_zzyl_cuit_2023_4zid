import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import MainIndex from '../views/MainIndex.vue'
import UserInfo from '../views/UesrInfo'
import ModifyPwd from '../views/ModifyPwd'
/*
import NursingItem from '../views/NursingItem'
*/

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    meta: { title: '登录' }
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/MainIndex',
    name: 'MainIndex',
    component: MainIndex,
    redirect: '/Dashboard',
    meta: { title: '工作台' },
    //配置MainIndex组件的二级组件
    children:[
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
      { path: '/Dashboard', name: 'Dashboard', meta: { title: '工作台' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Messages', name: 'Messages', meta: { title: '消息中心' }, component: () => import('@/views/ModulePlaceholder.vue') },
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
      { path: '/UserManage', name: 'UserManage', meta: { title: '用户管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/RoleManage', name: 'RoleManage', meta: { title: '角色管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/RoleDataScope', name: 'RoleDataScope', meta: { title: '角色数据权限' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/MenuManage', name: 'MenuManage', meta: { title: '菜单管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Department', name: 'Department', meta: { title: '部门管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Post', name: 'Post', meta: { title: '职位管理' }, component: () => import('@/views/ModulePlaceholder.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
