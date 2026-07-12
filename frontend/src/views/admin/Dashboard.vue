<template>
  <section ref="dashboardRef" class="dashboard-page">
    <div class="dashboard-grid dashboard-grid--top">
      <article class="dashboard-panel overview-panel">
        <header class="panel-heading">
          <h2>数据概览</h2>
          <span>数据更新：{{ today }}</span>
        </header>
        <div class="overview-list">
          <button
            v-for="(card, index) in statCards"
            :key="card.label"
            class="overview-item"
            type="button"
            @click="card.link && router.push(card.link)"
          >
            <span class="overview-chart" :data-index="index"></span>
          </button>
        </div>
      </article>

      <article class="dashboard-panel info-panel">
        <header class="panel-heading"><h2>我的信息</h2></header>
        <div class="profile-summary">
          <img :src="myInfo.image || defaultAvatar" alt="管理员头像" />
          <div>
            <strong>您好，{{ myInfo.realname || '管理员' }}</strong>
            <p>今天也是元气满满的一天！</p>
          </div>
        </div>
        <dl class="profile-meta">
          <div><dt>账号</dt><dd>{{ myInfo.account || '-' }}</dd></div>
          <div><dt>角色</dt><dd>{{ myInfo.roleName || myInfo.role || '-' }}</dd></div>
          <div><dt>部门</dt><dd>{{ myInfo.deptName || myInfo.department || '-' }}</dd></div>
          <div><dt>职位</dt><dd>{{ myInfo.postName || myInfo.job || '-' }}</dd></div>
        </dl>
        <el-button class="profile-link" link type="primary" @click="router.push('/UserInfo')">查看个人信息</el-button>
      </article>
    </div>

    <div class="dashboard-grid dashboard-grid--middle">
      <article class="dashboard-panel statistics-panel">
        <header class="statistics-heading">
          <div>
            <h2>数据统计</h2>
            <div class="plain-tabs" role="tablist">
              <button v-for="item in statisticTabs" :key="item.value" :class="{ active: statisticTab === item.value }" type="button" @click="statisticTab = item.value; renderTrendChart()">{{ item.label }}</button>
            </div>
          </div>
          <div class="time-switch">
            <button v-for="item in timeTabs" :key="item" :class="{ active: timeTab === item }" type="button" @click="timeTab = item">{{ item }}</button>
            <el-date-picker v-model="dateRange" type="daterange" range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期" size="small" />
          </div>
        </header>
        <div ref="trendChartRef" class="trend-chart" aria-label="业务趋势图"></div>
      </article>

      <article class="dashboard-panel shortcuts-panel">
        <header class="panel-heading"><h2>快捷方式</h2></header>
        <div class="shortcut-grid">
          <button v-for="item in shortcuts" :key="item.label" type="button" @click="router.push(item.path)">
            <span class="shortcut-icon"><el-icon><component :is="item.icon" /></el-icon></span>
            <span>{{ item.label }}</span>
          </button>
        </div>
      </article>
    </div>

    <div class="dashboard-grid dashboard-grid--lists">
      <article class="dashboard-panel list-panel">
        <header class="panel-heading"><h2>待办事项</h2><button type="button" @click="router.push('/Todo')">查看更多</button></header>
        <div v-if="todoList.length" class="dashboard-list">
          <button v-for="item in todoList.slice(0, 5)" :key="item.id" type="button" @click="router.push('/Todo')">
            <span class="list-type">{{ applyTypeMap[item.applyType] || item.applyType || '业务申请' }}</span>
            <span class="list-main">{{ item.applyNo || '待办事项' }}</span>
            <time>{{ formatTime(item.createTime) }}</time>
          </button>
        </div>
        <el-empty v-else description="暂无待办" :image-size="52" />
      </article>

      <article class="dashboard-panel list-panel">
        <header class="panel-heading"><h2>预约总览</h2><button type="button" @click="router.push('/Visit')">查看更多</button></header>
        <div v-if="appointmentList.length" class="dashboard-list">
          <button v-for="item in appointmentList.slice(0, 5)" :key="item.id" type="button" @click="router.push('/Visit')">
            <span class="list-type list-type--blue">预约来访</span>
            <span class="list-main">{{ item.visitorName || item.visitNo || '来访预约' }}</span>
            <time>{{ formatTime(item.appointmentTime) }}</time>
          </button>
        </div>
        <el-empty v-else description="暂无预约" :image-size="52" />
      </article>
    </div>

    <article class="dashboard-panel service-panel">
      <header class="panel-heading"><h2>养老院服务统计</h2></header>
      <div class="service-grid">
        <div v-for="item in serviceCharts" :key="item.title" class="service-stat">
          <h3>{{ item.title }}</h3>
          <div class="service-chart" :data-service="item.key"></div>
        </div>
      </div>
    </article>
  </section>
</template>

<script setup>
import { computed, markRaw, nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import * as echarts from 'echarts/core'
import { LineChart, PieChart } from 'echarts/charts'
import { GridComponent, LegendComponent, TooltipComponent } from 'echarts/components'
import { CanvasRenderer } from 'echarts/renderers'
import { Calendar, Checked, CreditCard, Document, Money, User } from '@element-plus/icons-vue'
import { getDashboardSummary, getDashboardTodo, getDashboardAppointments, getDashboardRevenueStats, getDashboardElderStats, loadInfo } from '@/api/admin'
import defaultAvatar from '@/assets/zhyl-user-avatar.png'

echarts.use([LineChart, PieChart, GridComponent, LegendComponent, TooltipComponent, CanvasRenderer])

const router = useRouter()
const dashboardRef = ref(null)
const trendChartRef = ref(null)
const myInfo = ref({})
const todoList = ref([])
const appointmentList = ref([])
const revenueStats = ref([])
const elderStats = ref({})
const summary = ref({})
const statisticTab = ref('revenue')
const timeTab = ref('本周')
const dateRange = ref(null)
const charts = []
let resizeObserver

const today = computed(() => new Intl.DateTimeFormat('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit' }).format(new Date()).replaceAll('/', '-'))
const statisticTabs = [{ label: '收益情况', value: 'revenue' }, { label: '入退情况', value: 'resident' }, { label: '服务情况', value: 'service' }]
const timeTabs = ['今日', '本周', '本月']
const statCards = computed(() => [
  { label: '老人数量', value: Number(summary.value.elderCount || 0), unit: '人', color: '#8bb7f0', max: Math.max(Number(summary.value.bedTotal || 0), 1), link: '/Resident' },
  { label: '床位数量', value: Number(summary.value.bedTotal || 0), unit: '个', color: '#81d39b', max: Math.max(Number(summary.value.bedTotal || 0), 1), link: '/Bed' },
  { label: '服务单数量', value: Number(summary.value.serviceOrderCount || 0), unit: '笔', color: '#8f91df', max: Math.max(Number(summary.value.serviceOrderCount || 0), 10), link: '/Order' },
  { label: '员工数量', value: Number(summary.value.employeeCount || 0), unit: '人', color: '#f0cc76', max: Math.max(Number(summary.value.employeeCount || 0), 10), link: '/UserManage' },
  { label: '收入金额', value: Number(Number(summary.value.monthRevenue || 0).toFixed(2)), unit: '元', color: '#eb8b87', max: Math.max(Number(summary.value.monthRevenue || 0), 1000), link: '/Bill' }
])
const shortcuts = [
  { label: '入住办理', path: '/Apply', icon: markRaw(User) },
  { label: '退住办理', path: '/Checkout', icon: markRaw(Document) },
  { label: '待办事项', path: '/Todo', icon: markRaw(Checked) },
  { label: '预约登记', path: '/Visit', icon: markRaw(Calendar) },
  { label: '入账列表', path: '/Bill', icon: markRaw(Money) },
  { label: '退款管理', path: '/Refund', icon: markRaw(CreditCard) }
]
const serviceCharts = [
  { key: 'level', title: '老人等级分布' },
  { key: 'age', title: '老人年龄分布' },
  { key: 'capacity', title: '服务能力统计' }
]
const applyTypeMap = { CHECKIN: '入住申请', CHECKOUT: '退住申请', LEAVE: '请假申请' }

function formatNumber(value) {
  const v = Number(value) || 0
  if (v >= 10000) return `${(v / 10000).toFixed(1)}w`
  return Number.isInteger(v) ? String(v) : v.toFixed(1)
}

function formatTime(value) {
  return value ? String(value).replace('T', ' ').slice(0, 16) : '-'
}

function registerChart(chart) {
  charts.push(chart)
  return chart
}

function renderOverviewCharts() {
  dashboardRef.value?.querySelectorAll('.overview-chart').forEach((element, index) => {
    const card = statCards.value[index]
    const existing = echarts.getInstanceByDom(element)
    const chart = existing || registerChart(echarts.init(element))
    const progress = Math.min(card.value / card.max, 1)
    chart.setOption({
      animationDuration: 500,
      series: [{
        type: 'pie', radius: ['70%', '84%'], center: ['50%', '50%'], silent: true,
        label: { show: true, position: 'center', formatter: `{label|${card.label}}\n{value|${formatNumber(card.value)}}{unit|${card.unit}}`, rich: {
          label: { color: '#8a8f99', fontSize: 12, lineHeight: 22 },
          value: { color: '#20242b', fontSize: 25, fontWeight: 600, lineHeight: 32 },
          unit: { color: '#6f7680', fontSize: 12, padding: [7, 0, 0, 3] }
        } },
        data: [{ value: progress, itemStyle: { color: card.color } }, { value: 1 - progress, itemStyle: { color: '#eef1f5' } }]
      }]
    })
  })
}

function trendValues() {
  const field = statisticTab.value === 'revenue' ? 'revenue' : statisticTab.value === 'resident' ? 'elderCount' : 'orderCount'
  const apiValues = revenueStats.value.map(item => Number(item[field] || 0))
  if (apiValues.length > 0 && apiValues.some(value => value > 0)) return apiValues
  const base = statisticTab.value === 'revenue' ? Number(summary.value.monthRevenue || 0) : statisticTab.value === 'resident' ? Number(summary.value.elderCount || 0) : Number(summary.value.serviceOrderCount || 0)
  return [base || 0]
}

function renderTrendChart() {
  if (!trendChartRef.value) return
  const chart = echarts.getInstanceByDom(trendChartRef.value) || registerChart(echarts.init(trendChartRef.value))
  const values = trendValues()
  const labels = revenueStats.value.length > 0 ? revenueStats.value.map(item => item.month?.slice(5) || item.month || '') : ['本月']
  const names = { revenue: '收益金额', resident: '入退人数', service: '服务次数' }
  chart.setOption({
    grid: { left: 40, right: 18, top: 24, bottom: 28 },
    tooltip: { trigger: 'axis', backgroundColor: '#fff', borderColor: '#e4e7ed', textStyle: { color: '#303133' } },
    xAxis: { type: 'category', boundaryGap: false, data: labels, axisLine: { lineStyle: { color: '#dfe3e8' } }, axisTick: { show: false }, axisLabel: { color: '#8a8f99' } },
    yAxis: { type: 'value', splitLine: { lineStyle: { color: '#edf0f4' } }, axisLabel: { color: '#8a8f99' } },
    series: [{ name: names[statisticTab.value], type: 'line', smooth: true, symbolSize: 6, data: values, lineStyle: { color: '#3976d8', width: 2 }, itemStyle: { color: '#3976d8' }, areaStyle: { color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{ offset: 0, color: 'rgba(57,118,216,.32)' }, { offset: 1, color: 'rgba(57,118,216,.03)' }]) } }]
  })
}

function renderServiceCharts() {
  // Get real elder stats from API
  const levelData = elderStats.value?.levelStats || []
  const ageData = elderStats.value?.ageStats || []
  const levelNames = levelData.map(i => i.name || i.level || '未知')
  const levelValues = levelData.map(i => i.value || i.count || 1)
  const levelColors = ['#6b98e8', '#7bd3b3', '#f0bf70', '#e59a94', '#c0b0e8']
  const ageNames = ageData.map(i => i.name || i.range || '未知')
  const ageValues = ageData.map(i => i.value || i.count || 1)
  const ageColors = ['#7aa5e8', '#91d6ae', '#e59a94', '#f0bf70']

  const options = {
    level: {
      names: levelNames.length ? levelNames : ['暂无数据'],
      values: levelValues.length ? levelValues : [1],
      colors: levelColors
    },
    age: {
      names: ageNames.length ? ageNames : ['暂无数据'],
      values: ageValues.length ? ageValues : [1],
      colors: ageColors
    },
    capacity: {
      names: ['已服务', '可服务'],
      values: [Number(summary.value.serviceOrderCount || 0), Math.max(Number(summary.value.bedTotal || 0) - Number(summary.value.serviceOrderCount || 0), 1)],
      colors: ['#7797dc', '#dce6f6']
    }
  }
  dashboardRef.value?.querySelectorAll('.service-chart').forEach(element => {
    const item = options[element.dataset.service]
    if (!item) return
    const chart = echarts.getInstanceByDom(element) || registerChart(echarts.init(element))
    const total = item.values.reduce((a, b) => a + b, 0)
    chart.setOption({
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { bottom: 0, icon: 'circle', itemWidth: 8, textStyle: { color: '#7a8088', fontSize: 11 } },
      series: [{
        type: 'pie', radius: ['48%', '68%'], center: ['50%', '43%'],
        label: { show: true, position: 'outside', formatter: '{b} {c}', fontSize: 11, color: '#555b65' },
        emphasis: { label: { fontSize: 14, fontWeight: 'bold' } },
        data: item.names.map((name, index) => ({ name, value: item.values[index], itemStyle: { color: item.colors[index % item.colors.length] } }))
      }]
    })
  })
}

async function renderCharts() {
  await nextTick()
  renderOverviewCharts()
  renderTrendChart()
  renderServiceCharts()
}

watch(summary, renderCharts, { deep: true })
watch(revenueStats, renderTrendChart, { deep: true })
watch([timeTab, dateRange], () => { renderTrendChart() })

onMounted(async () => {
  try { myInfo.value = JSON.parse(localStorage.getItem('adminUser') || '{}') } catch (error) { myInfo.value = {} }
  try { myInfo.value = await loadInfo() || myInfo.value } catch (error) { /* keep cached profile */ }
  const [summaryRes, todoRes, appointmentRes, revenueRes, elderRes] = await Promise.allSettled([
    getDashboardSummary(), getDashboardTodo(), getDashboardAppointments(), getDashboardRevenueStats(), getDashboardElderStats()
  ])
  if (summaryRes.status === 'fulfilled') summary.value = summaryRes.value?.data || {}
  if (todoRes.status === 'fulfilled') todoList.value = todoRes.value?.data || []
  if (appointmentRes.status === 'fulfilled') appointmentList.value = appointmentRes.value?.data || []
  if (revenueRes.status === 'fulfilled') revenueStats.value = revenueRes.value?.data || []
  if (elderRes.status === 'fulfilled') elderStats.value = elderRes.value?.data || {}
  await renderCharts()
  resizeObserver = new ResizeObserver(() => charts.forEach(chart => chart.resize()))
  if (dashboardRef.value) resizeObserver.observe(dashboardRef.value)
})

onBeforeUnmount(() => {
  resizeObserver?.disconnect()
  charts.forEach(chart => chart.dispose())
})
</script>

<style scoped>
.dashboard-page { display: grid; gap: 14px; min-width: 0; }
.dashboard-grid { display: grid; min-width: 0; gap: 14px; }
.dashboard-grid--top { grid-template-columns: minmax(0, 2fr) minmax(280px, .78fr); }
.dashboard-grid--middle { grid-template-columns: minmax(0, 2fr) minmax(280px, .78fr); }
.dashboard-grid--lists { grid-template-columns: repeat(2, minmax(0, 1fr)); }
.dashboard-panel { min-width: 0; background: #fff; border: 1px solid #e4e7ed; }
.panel-heading { display: flex; min-height: 46px; padding: 0 18px; align-items: center; justify-content: space-between; border-bottom: 1px solid #edf0f3; }
.panel-heading h2, .statistics-heading h2 { margin: 0; color: #30343b; font-size: 16px; font-weight: 600; }
.panel-heading > span { color: #8a9099; font-size: 12px; }
.panel-heading > button { color: #3976d8; background: transparent; border: 0; cursor: pointer; font-size: 12px; }
.overview-list { display: grid; grid-template-columns: repeat(5, minmax(110px, 1fr)); min-height: 188px; padding: 14px 10px 18px; }
.overview-item { min-width: 0; padding: 0; background: transparent; border: 0; cursor: pointer; }
.overview-chart { display: block; width: 100%; height: 150px; }
.profile-summary { display: flex; padding: 18px; gap: 12px; align-items: center; }
.profile-summary img { width: 52px; height: 52px; border-radius: 50%; object-fit: cover; }
.profile-summary strong { display: block; margin-bottom: 6px; color: #30343b; font-size: 15px; }
.profile-summary p { margin: 0; color: #8a9099; font-size: 12px; }
.profile-meta { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); margin: 0 18px; border-top: 1px solid #edf0f3; }
.profile-meta div { display: flex; min-width: 0; padding: 10px 0; font-size: 12px; }
.profile-meta dt { width: 42px; color: #9a9fa7; }
.profile-meta dd { min-width: 0; margin: 0; color: #555b65; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.profile-link { margin: 2px 18px 12px; padding: 0; }
.statistics-heading { display: flex; min-height: 82px; padding: 12px 18px 0; align-items: flex-start; justify-content: space-between; border-bottom: 1px solid #edf0f3; gap: 12px; }
.plain-tabs { display: flex; margin-top: 12px; gap: 26px; }
.plain-tabs button, .time-switch button { padding: 0 0 10px; color: #7a8089; background: transparent; border: 0; border-bottom: 2px solid transparent; cursor: pointer; font-size: 13px; }
.plain-tabs button.active { color: #3976d8; border-bottom-color: #3976d8; }
.time-switch { display: flex; align-items: center; gap: 12px; }
.time-switch button { padding: 6px 0; border: 0; }
.time-switch button.active { color: #3976d8; }
.time-switch :deep(.el-date-editor) { width: 220px; }
.trend-chart { width: 100%; height: 290px; }
.shortcut-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); padding: 18px 14px; gap: 20px 8px; }
.shortcut-grid button { display: flex; min-width: 0; padding: 0; flex-direction: column; align-items: center; gap: 8px; color: #4e5560; background: transparent; border: 0; cursor: pointer; font-size: 12px; }
.shortcut-icon { display: grid; width: 40px; height: 40px; place-items: center; color: #3976d8; background: #edf4ff; border-radius: 4px; font-size: 19px; }
.dashboard-list { padding: 5px 18px 10px; }
.dashboard-list button { display: grid; width: 100%; min-height: 40px; padding: 7px 0; grid-template-columns: 74px minmax(0, 1fr) 130px; align-items: center; text-align: left; color: #555b65; background: transparent; border: 0; border-bottom: 1px solid #f0f2f5; cursor: pointer; font-size: 12px; }
.list-type { justify-self: start; padding: 3px 6px; color: #a96c1c; background: #fff4df; }
.list-type--blue { color: #3976d8; background: #edf4ff; }
.list-main { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.dashboard-list time { color: #9a9fa7; text-align: right; }
.list-panel :deep(.el-empty) { padding: 20px 0; }
.service-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); }
.service-stat { min-width: 0; padding-top: 12px; border-right: 1px solid #edf0f3; }
.service-stat:last-child { border-right: 0; }
.service-stat h3 { margin: 0 18px; color: #59606a; font-size: 13px; font-weight: 500; }
.service-chart { height: 210px; overflow: visible; }
@media (max-width: 1180px) {
  .dashboard-grid--top, .dashboard-grid--middle { grid-template-columns: minmax(0, 1fr) 260px; }
  .overview-list { grid-template-columns: repeat(3, minmax(110px, 1fr)); }
  .overview-chart { height: 130px; }
  .statistics-heading { flex-direction: column; min-height: 116px; }
  .time-switch { padding-bottom: 10px; }
}
@media (max-width: 980px) {
  .dashboard-grid--top, .dashboard-grid--middle, .dashboard-grid--lists { grid-template-columns: 1fr; }
  .service-grid { grid-template-columns: 1fr; }
  .service-stat { border-right: 0; border-bottom: 1px solid #edf0f3; }
}
</style>
