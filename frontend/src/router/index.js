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
      { path: '/Apply', name: 'Apply', meta: { title: '入退管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Resident', name: 'Resident', meta: { title: '在住管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Order', name: 'Order', meta: { title: '订单管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Bill', name: 'Bill', meta: { title: '财务管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Customer', name: 'Customer', meta: { title: '客户管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Todo', name: 'Todo', meta: { title: '协同工作' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/Device', name: 'Device', meta: { title: '智能监测' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/UserManage', name: 'UserManage', meta: { title: '用户管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
      { path: '/RoleManage', name: 'RoleManage', meta: { title: '角色管理' }, component: () => import('@/views/ModulePlaceholder.vue') },
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
