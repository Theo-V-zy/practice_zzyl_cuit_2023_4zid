<template>
  <section class="page-section">
    <div class="dashboard">
      <!-- 我的信息 -->
      <el-card shadow="never" class="my-info-card">
        <div class="my-info">
          <img class="my-avatar" :src="myInfo.image || defaultAvatar" alt="头像" />
          <div class="my-detail">
            <div class="my-greeting">您好，{{ myInfo.realname || '管理员' }}，今天也是元气满满的一天！</div>
            <div class="my-meta">
              <span>{{ myInfo.account || '-' }}</span>
              <span class="meta-divider">|</span>
              <span>{{ myInfo.roleName || myInfo.role || '-' }}</span>
              <span class="meta-divider">|</span>
              <span>{{ myInfo.deptName || myInfo.department || '-' }}</span>
              <span class="meta-divider">|</span>
              <span>{{ myInfo.postName || myInfo.job || '-' }}</span>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 数据概览 -->
      <el-card shadow="never" class="section-card">
        <template #header>
          <div class="card-header">
            <span class="card-title">数据概览</span>
            <span class="card-update">数据更新：{{ today }}</span>
          </div>
        </template>
        <el-row :gutter="16" class="stats-row">
          <el-col :span="4" v-for="card in statCards" :key="card.label">
            <div class="stat-card" @click="card.link && router.push(card.link)">
              <div class="stat-value">{{ card.value }}<small>{{ card.unit }}</small></div>
              <div class="stat-label">{{ card.label }}</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <el-row :gutter="16" style="margin-top: 16px;">
        <!-- 快捷方式 -->
        <el-col :span="6">
          <el-card shadow="never">
            <template #header><span class="card-title">快捷方式</span></template>
            <div class="shortcut-list">
              <div class="shortcut-item" @click="router.push('/Refund')">
                <el-icon :size="20"><CreditCard /></el-icon>
                <span>退款管理</span>
              </div>
              <div class="shortcut-item" @click="router.push('/Todo')">
                <el-icon :size="20"><Checked /></el-icon>
                <span>待办事项</span>
              </div>
              <div class="shortcut-item" @click="router.push('/Bill')">
                <el-icon :size="20"><DataAnalysis /></el-icon>
                <span>入账列表</span>
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 待办事项 -->
        <el-col :span="9">
          <el-card shadow="never">
            <template #header><span class="card-title">待办事项</span></template>
            <el-table :data="todoList" style="width: 100%" size="small" empty-text="暂无待办">
              <el-table-column prop="applyNo" label="编号" width="150" />
              <el-table-column prop="applyType" label="类型" width="90">
                <template #default="{ row }">
                  <el-tag size="small">{{ applyTypeMap[row.applyType] || row.applyType }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="时间" min-width="140" />
            </el-table>
          </el-card>
        </el-col>

        <!-- 预约总览 -->
        <el-col :span="9">
          <el-card shadow="never">
            <template #header><span class="card-title">预约总览</span></template>
            <el-table :data="appointmentList" style="width: 100%" size="small" empty-text="暂无预约">
              <el-table-column prop="visitNo" label="编号" width="150" />
              <el-table-column prop="visitorName" label="来访人" width="80" />
              <el-table-column prop="appointmentTime" label="预约时间" min-width="130" />
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <!-- 数据统计 & 老人统计 -->
      <el-row :gutter="16" style="margin-top: 16px;">
        <el-col :span="12">
          <el-card shadow="never">
            <template #header><span class="card-title">数据统计</span></template>
            <div class="stat-list">
              <div class="stat-item" v-for="item in revenueStats" :key="item.month">
                <span>{{ item.month }}</span>
                <span class="stat-count">¥{{ item.revenue || 0 }}</span>
                <span class="stat-sub">{{ item.orderCount || 0 }}笔</span>
              </div>
              <div v-if="revenueStats.length === 0" class="empty-hint">暂无数据</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never">
            <template #header><span class="card-title">老人统计</span></template>
            <div v-if="levelStats.length === 0 && ageStats.length === 0" class="empty-hint">暂无数据</div>
            <div v-else>
              <h4 class="sub-title">护理等级分布</h4>
              <div class="stat-list">
                <div v-for="item in levelStats" :key="item.name" class="stat-item">
                  <span>{{ item.name || '未知' }}</span>
                  <span class="stat-count">{{ item.value }}人</span>
                </div>
              </div>
              <h4 class="sub-title" style="margin-top: 12px;">年龄分布</h4>
              <div class="stat-list">
                <div v-for="item in ageStats" :key="item.name" class="stat-item">
                  <span>{{ item.name }}</span>
                  <span class="stat-count">{{ item.value }}人</span>
                </div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { CreditCard, Checked, DataAnalysis } from '@element-plus/icons-vue'
import { getDashboardSummary, getDashboardTodo, getDashboardAppointments, getDashboardElderStats, getDashboardRevenueStats } from '@/api/admin'
import defaultAvatar from '@/assets/zhyl-user-avatar.png'

const router = useRouter()
const myInfo = ref({})
const todoList = ref([])
const appointmentList = ref([])
const levelStats = ref([])
const ageStats = ref([])
const revenueStats = ref([])

const today = computed(() => {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
})

const statCards = ref([
  { label: '老人数量', value: 0, unit: '人', link: '/Resident' },
  { label: '床位数量', value: 0, unit: '个', link: '/Bed' },
  { label: '员工数量', value: 0, unit: '人', link: '/UserManage' },
  { label: '收入金额', value: 0, unit: 'W元', link: '/Bill' },
  { label: '服务单数量', value: 0, unit: 'w笔', link: '/Order' }
])

const applyTypeMap = { CHECKIN: '入住申请', CHECKOUT: '退住申请', LEAVE: '请假申请' }

onMounted(async () => {
  // 我的信息 - 从 localStorage 或 API 加载
  try {
    const cached = localStorage.getItem('adminUser')
    if (cached) myInfo.value = JSON.parse(cached)
  } catch (e) { /* ignore */ }

  try {
    const summary = await getDashboardSummary()
    if (summary && summary.data) {
      const d = summary.data
      statCards.value[0].value = d.elderCount || 0
      statCards.value[1].value = d.bedTotal || 0
      statCards.value[2].value = d.employeeCount || 0
      const revenue = d.monthRevenue || 0
      statCards.value[3].value = (revenue / 10000).toFixed(1)
      const orders = d.serviceOrderCount || 0
      statCards.value[4].value = (orders / 10000).toFixed(1)
    }
  } catch (e) { /* ignore */ }

  try { const res = await getDashboardTodo(); if (res?.data) todoList.value = res.data } catch (e) { /* ignore */ }
  try { const res = await getDashboardAppointments(); if (res?.data) appointmentList.value = res.data } catch (e) { /* ignore */ }

  try {
    const res = await getDashboardElderStats()
    if (res?.data) { levelStats.value = res.data.levelStats || []; ageStats.value = res.data.ageStats || [] }
  } catch (e) { /* ignore */ }

  try {
    const res = await getDashboardRevenueStats()
    if (res?.data) revenueStats.value = res.data
  } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.dashboard { min-width: 0; }

.my-info-card { margin-bottom: 16px; }
.my-info { display: flex; align-items: center; gap: 16px; }
.my-avatar { width: 56px; height: 56px; border-radius: 50%; object-fit: cover; background: #eef4ff; }
.my-greeting { font-size: 15px; color: #333; margin-bottom: 6px; }
.my-meta { font-size: 13px; color: rgba(0,0,0,0.5); display: flex; gap: 8px; align-items: center; }
.meta-divider { color: #e7e9ed; }

.section-card { margin-bottom: 16px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.card-title { font-size: 15px; font-weight: 600; color: #333; }
.card-update { font-size: 12px; color: rgba(0,0,0,0.4); }

.stats-row { margin: 0 !important; }
.stat-card { padding: 16px 12px; text-align: center; background: #fff; border: 1px solid #e7e9ed; border-radius: 6px; cursor: pointer; transition: box-shadow 0.2s; }
.stat-card:hover { box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
.stat-value { font-size: 24px; font-weight: 600; color: #0052d9; line-height: 32px; }
.stat-value small { font-size: 13px; font-weight: 400; color: rgba(0,0,0,0.6); }
.stat-label { margin-top: 4px; font-size: 13px; color: rgba(0,0,0,0.6); }

.shortcut-list { display: flex; flex-direction: column; gap: 8px; }
.shortcut-item { display: flex; align-items: center; gap: 10px; padding: 10px 12px; background: #f5f7fa; border-radius: 6px; cursor: pointer; font-size: 14px; color: #333; transition: background 0.2s; }
.shortcut-item:hover { background: #eef4ff; color: #0052d9; }

.empty-hint { padding: 40px 0; text-align: center; color: rgba(0,0,0,0.35); font-size: 14px; }
.sub-title { font-size: 13px; font-weight: 600; color: rgba(0,0,0,0.6); margin: 0 0 8px; }

.stat-list { display: flex; flex-wrap: wrap; gap: 8px; }
.stat-item { display: flex; align-items: center; justify-content: space-between; width: calc(50% - 4px); padding: 8px 12px; background: #f5f7fa; border-radius: 4px; font-size: 13px; }
.stat-count { font-weight: 600; color: #0052d9; }
.stat-sub { font-size: 11px; color: rgba(0,0,0,0.4); }
</style>
