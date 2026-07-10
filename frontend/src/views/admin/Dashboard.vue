<template>
  <section class="page-section">
    <div class="dashboard">
      <!-- 顶部统计卡片 -->
      <el-row :gutter="16" class="stats-row">
        <el-col :span="4" v-for="card in statCards" :key="card.label">
          <div class="stat-card" @click="card.link && router.push(card.link)">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="16" style="margin-top: 16px;">
        <!-- 待办事项 -->
        <el-col :span="12">
          <el-card shadow="never">
            <template #header>
              <span class="card-title">待办事项</span>
            </template>
            <el-table :data="todoList" style="width: 100%" size="small" empty-text="暂无待办">
              <el-table-column prop="applyNo" label="编号" width="140" />
              <el-table-column prop="applyType" label="类型" width="100">
                <template #default="{ row }">
                  <el-tag size="small">{{ applyTypeMap[row.applyType] || row.applyType }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createTime" label="时间" width="160" />
              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag size="small" type="warning">待处理</el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>

        <!-- 预约总览 -->
        <el-col :span="12">
          <el-card shadow="never">
            <template #header>
              <span class="card-title">预约总览</span>
            </template>
            <el-table :data="appointmentList" style="width: 100%" size="small" empty-text="暂无预约">
              <el-table-column prop="visitNo" label="编号" width="140" />
              <el-table-column prop="visitorName" label="来访人" width="100" />
              <el-table-column prop="elderName" label="老人" width="80" />
              <el-table-column prop="appointmentTime" label="预约时间" width="160" />
              <el-table-column prop="status" label="状态" width="80">
                <template #default="{ row }">
                  <el-tag size="small" :type="row.status === 'PENDING' ? 'warning' : 'info'">
                    {{ row.status === 'PENDING' ? '待确认' : row.status }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>

      <!-- 老人统计 -->
      <el-row :gutter="16" style="margin-top: 16px;">
        <el-col :span="12">
          <el-card shadow="never">
            <template #header><span class="card-title">老人护理等级统计</span></template>
            <div v-if="levelStats.length === 0" class="empty-hint">暂无数据</div>
            <div v-else class="stat-list">
              <div v-for="item in levelStats" :key="item.name" class="stat-item">
                <span>{{ item.name || '未知' }}</span>
                <span class="stat-count">{{ item.value }}人</span>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never">
            <template #header><span class="card-title">老人年龄分布</span></template>
            <div v-if="ageStats.length === 0" class="empty-hint">暂无数据</div>
            <div v-else class="stat-list">
              <div v-for="item in ageStats" :key="item.name" class="stat-item">
                <span>{{ item.name }}</span>
                <span class="stat-count">{{ item.value }}人</span>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getDashboardSummary, getDashboardTodo, getDashboardAppointments, getDashboardElderStats } from '@/api/admin'

const router = useRouter()

const statCards = ref([
  { label: '在住老人', value: 0, link: '/Resident' },
  { label: '床位总数', value: 0, link: '/Bed' },
  { label: '员工人数', value: 0, link: '/UserManage' },
  { label: '本月收入(元)', value: 0, link: '/Bill' },
  { label: '服务单数', value: 0, link: '/Order' },
  { label: '床位使用', value: '0/0', link: '/Bed' }
])

const todoList = ref([])
const appointmentList = ref([])
const levelStats = ref([])
const ageStats = ref([])

const applyTypeMap = {
  CHECKIN: '入住申请',
  CHECKOUT: '退住申请',
  LEAVE: '请假申请'
}

onMounted(async () => {
  try {
    const summary = await getDashboardSummary()
    if (summary && summary.data) {
      const d = summary.data
      statCards.value[0].value = d.elderCount || 0
      statCards.value[1].value = d.bedTotal || 0
      statCards.value[2].value = d.employeeCount || 0
      statCards.value[3].value = d.monthRevenue || 0
      statCards.value[4].value = d.serviceOrderCount || 0
      statCards.value[5].value = `${d.bedUsed || 0}/${d.bedTotal || 0}`
    }
  } catch (e) { /* ignore */ }

  try {
    const todo = await getDashboardTodo()
    if (todo && todo.data) todoList.value = todo.data
  } catch (e) { /* ignore */ }

  try {
    const appointments = await getDashboardAppointments()
    if (appointments && appointments.data) appointmentList.value = appointments.data
  } catch (e) { /* ignore */ }

  try {
    const stats = await getDashboardElderStats()
    if (stats && stats.data) {
      levelStats.value = stats.data.levelStats || []
      ageStats.value = stats.data.ageStats || []
    }
  } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.dashboard {
  min-width: 0;
}

.stats-row {
  margin: 0 !important;
}

.stat-card {
  padding: 20px 16px;
  text-align: center;
  background: #fff;
  border: 1px solid #e7e9ed;
  border-radius: 6px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}
.stat-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #0052d9;
  line-height: 36px;
}
.stat-label {
  margin-top: 8px;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.6);
}

.card-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
}

.empty-hint {
  padding: 40px 0;
  text-align: center;
  color: rgba(0, 0, 0, 0.35);
  font-size: 14px;
}

.stat-list {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}
.stat-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: calc(50% - 6px);
  padding: 10px 14px;
  background: #f5f7fa;
  border-radius: 4px;
  font-size: 14px;
}
.stat-count {
  font-weight: 600;
  color: #0052d9;
}
</style>
