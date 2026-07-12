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
    component: HomeView
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
    //配置MainIndex组件的二级组件
    children:[
      {
        path: '/UserInfo',
        name: 'UserInfo',
        component: UserInfo
      },
      {
        path: '/ModifyPwd',
        name: 'ModifyPwd',
        component: ModifyPwd
      },
      { path: '/NursingItem', component: () => import('@/views/NursingItem.vue') },
      { path: '/NursingPlain', component: () => import('@/views/NursingPlain.vue') },
      { path: '/Customer', component: () => import('@/views/Customer.vue') },
      { path: '/Todo', component: () => import('@/views/Todo.vue') },
      { path: '/Application', component: () => import('@/views/Application.vue') },
      { path: '/Device', component: () => import('@/views/Device.vue') },
      { path: '/Alert', component: () => import('@/views/Alert.vue') },
      { path: '/AlertRule', component: () => import('@/views/AlertRule.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
