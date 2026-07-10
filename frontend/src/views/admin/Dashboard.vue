<template>
  <section class="page-section">
    <div class="dashboard">
      <!-- 我的信息 -->
      <div class="my-info-card">
        <img class="my-avatar" :src="myInfo.image || defaultAvatar" alt="头像" />
        <div class="my-text">
          <div class="my-greeting">您好，{{ myInfo.realname || '管理员' }}，今天也是元气满满的一天！</div>
          <div class="my-meta">
            <span>{{ myInfo.account || '-' }}</span>
            <span class="sep">|</span>
            <span>{{ myInfo.roleName || myInfo.role || '-' }}</span>
            <span class="sep">|</span>
            <span>{{ myInfo.deptName || myInfo.department || '-' }}</span>
            <span class="sep">|</span>
            <span>{{ myInfo.postName || myInfo.job || '-' }}</span>
          </div>
        </div>
      </div>

      <!-- 数据概览 -->
      <div class="section-title-row">
        <span>数据概览</span>
        <span class="update-time">数据更新：{{ today }}</span>
      </div>
      <div class="stat-cards">
        <div class="stat-card" v-for="card in statCards" :key="card.label" @click="card.link && router.push(card.link)">
          <span class="stat-num">{{ card.value }}<small>{{ card.unit }}</small></span>
          <span class="stat-lbl">{{ card.label }}</span>
        </div>
      </div>

      <!-- 数据统计 -->
      <div class="section-title-row" style="margin-top:24px"><span>数据统计</span></div>
      <div class="chart-area">
        <div class="chart-dates">
          <span v-for="d in chartDates" :key="d" class="date-chip">{{ d }}</span>
        </div>
        <div class="chart-bars">
          <div v-for="(item,i) in revenueStats" :key="i" class="bar-col">
            <div class="bar" :style="{ height: barHeight(item.revenue) }"></div>
            <span class="bar-label">{{ item.month ? item.month.substring(5) : '' }}</span>
          </div>
          <div v-if="revenueStats.length===0" class="chart-empty">暂无数据</div>
        </div>
      </div>

      <!-- 下半部分 -->
      <div class="bottom-grid">
        <!-- 快捷方式 -->
        <div class="shortcut-card">
          <div class="section-title-row"><span>快捷方式</span></div>
          <div class="shortcut-list">
            <div class="shortcut" @click="router.push('/Refund')">
              <span class="shortcut-dot"></span><span>退款管理</span>
            </div>
            <div class="shortcut" @click="router.push('/Todo')">
              <span class="shortcut-dot"></span><span>待办事项</span>
            </div>
            <div class="shortcut" @click="router.push('/Bill')">
              <span class="shortcut-dot"></span><span>入账列表</span>
            </div>
          </div>
        </div>

        <!-- 待办事项 -->
        <div class="list-card">
          <div class="section-title-row"><span>待办事项</span></div>
          <div class="simple-list" v-if="todoList.length>0">
            <div v-for="item in todoList" :key="item.id" class="list-row">
              <span class="list-no">{{ item.applyNo }}</span>
              <el-tag size="small" type="warning">{{ applyTypeMap[item.applyType] || item.applyType }}</el-tag>
              <span class="list-time">{{ item.createTime }}</span>
            </div>
          </div>
          <div v-else class="chart-empty">暂无待办</div>
        </div>

        <!-- 预约总览 -->
        <div class="list-card">
          <div class="section-title-row"><span>预约总览</span></div>
          <div class="simple-list" v-if="appointmentList.length>0">
            <div v-for="item in appointmentList" :key="item.id" class="list-row">
              <span class="list-no">{{ item.visitNo }}</span>
              <span class="list-name">{{ item.visitorName }}</span>
              <span class="list-time">{{ item.appointmentTime }}</span>
            </div>
          </div>
          <div v-else class="chart-empty">暂无预约</div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getDashboardSummary, getDashboardTodo, getDashboardAppointments, getDashboardRevenueStats } from '@/api/admin'
import defaultAvatar from '@/assets/zhyl-user-avatar.png'

const router = useRouter()
const myInfo = ref({})
const todoList = ref([])
const appointmentList = ref([])
const revenueStats = ref([])

const today = computed(() => {
  const d = new Date(); return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
})

const statCards = ref([
  { label: '老人数量', value: 0, unit: '人', link: '/Resident' },
  { label: '床位数量', value: 0, unit: '个', link: '/Bed' },
  { label: '员工数量', value: 0, unit: '人', link: '/UserManage' },
  { label: '收入金额', value: 0, unit: 'W元', link: '/Bill' },
  { label: '服务单数量', value: 0, unit: 'w笔', link: '/Order' }
])

const chartDates = computed(() => revenueStats.value.map(r => r.month).reverse())

const applyTypeMap = { CHECKIN: '入住申请', CHECKOUT: '退住申请', LEAVE: '请假申请' }

function barHeight(rev) { const max=Math.max(...revenueStats.value.map(r=>r.revenue||0),1); return Math.max(4, (rev||0)/max*120)+'px' }

onMounted(async () => {
  try { const c=localStorage.getItem('adminUser'); if(c) myInfo.value=JSON.parse(c) } catch(e){}
  try {
    const s=await getDashboardSummary()
    if(s?.data){ const d=s.data; statCards.value[0].value=d.elderCount||0; statCards.value[1].value=d.bedTotal||0; statCards.value[2].value=d.employeeCount||0; statCards.value[3].value=((d.monthRevenue||0)/10000).toFixed(1); statCards.value[4].value=((d.serviceOrderCount||0)/10000).toFixed(1) }
  }catch(e){}
  try{const r=await getDashboardTodo();if(r?.data)todoList.value=r.data}catch(e){}
  try{const r=await getDashboardAppointments();if(r?.data)appointmentList.value=r.data}catch(e){}
  try{const r=await getDashboardRevenueStats();if(r?.data)revenueStats.value=r.data}catch(e){}
})
</script>

<style scoped>
.dashboard { min-width:0; }

/* 我的信息 */
.my-info-card { display:flex; align-items:center; gap:20px; padding:20px 24px; background:#fff; border:1px solid #e7e9ed; border-radius:8px; margin-bottom:20px; }
.my-avatar { width:56px; height:56px; border-radius:50%; object-fit:cover; background:#eef4ff; flex-shrink:0; }
.my-greeting { font-size:16px; color:#333; margin-bottom:6px; }
.my-meta { display:flex; gap:8px; align-items:center; font-size:13px; color:rgba(0,0,0,0.5); }
.sep { color:#e7e9ed; }

/* section标题行 */
.section-title-row { display:flex; justify-content:space-between; align-items:center; margin-bottom:12px; font-size:15px; font-weight:600; color:#333; }
.update-time { font-size:12px; color:rgba(0,0,0,0.4); font-weight:400; }

/* 统计卡片 */
.stat-cards { display:flex; gap:16px; margin-bottom:8px; }
.stat-card { flex:1; padding:24px 20px; text-align:center; background:#fff; border:1px solid #e7e9ed; border-radius:8px; cursor:pointer; transition:box-shadow 0.2s; }
.stat-card:hover { box-shadow:0 2px 12px rgba(0,0,0,0.06); }
.stat-num { font-size:32px; font-weight:600; color:#333; display:block; }
.stat-num small { font-size:14px; font-weight:400; color:rgba(0,0,0,0.5); margin-left:4px; }
.stat-lbl { font-size:13px; color:rgba(0,0,0,0.5); margin-top:8px; display:block; }

/* 图表区 */
.chart-area { padding:20px; background:#fff; border:1px solid #e7e9ed; border-radius:8px; }
.chart-dates { display:flex; gap:8px; margin-bottom:16px; }
.date-chip { padding:4px 12px; background:#f5f7fa; border-radius:4px; font-size:12px; color:rgba(0,0,0,0.6); }
.chart-bars { display:flex; align-items:flex-end; gap:8px; height:140px; }
.bar-col { flex:1; display:flex; flex-direction:column; align-items:center; justify-content:flex-end; }
.bar { width:28px; background:linear-gradient(to top, #0052d9, #7298ff); border-radius:4px 4px 0 0; min-height:4px; transition:height 0.5s; }
.bar-label { font-size:11px; color:rgba(0,0,0,0.4); margin-top:6px; }
.chart-empty { padding:60px 0; text-align:center; color:rgba(0,0,0,0.3); font-size:13px; }

/* 下半部分三栏 */
.bottom-grid { display:grid; grid-template-columns:180px 1fr 1fr; gap:16px; margin-top:20px; }

/* 快捷方式 */
.shortcut-card { background:#fff; border:1px solid #e7e9ed; border-radius:8px; padding:16px 20px; }
.shortcut-list { display:flex; flex-direction:column; gap:12px; }
.shortcut { display:flex; align-items:center; gap:10px; font-size:14px; color:#333; cursor:pointer; padding:8px 12px; border-radius:4px; transition:background 0.2s; }
.shortcut:hover { background:#f5f7fa; }
.shortcut-dot { width:6px; height:6px; background:#0052d9; border-radius:50%; flex-shrink:0; }

/* 列表卡片 */
.list-card { background:#fff; border:1px solid #e7e9ed; border-radius:8px; padding:16px 20px; }
.simple-list { display:flex; flex-direction:column; gap:8px; }
.list-row { display:flex; align-items:center; gap:10px; padding:8px 0; border-bottom:1px solid #f5f7fa; font-size:13px; }
.list-row:last-child { border-bottom:0; }
.list-no { flex:1; color:#333; overflow:hidden; text-overflow:ellipsis; white-space:nowrap; }
.list-name { width:80px; color:#333; }
.list-time { width:140px; color:rgba(0,0,0,0.4); text-align:right; }
</style>
