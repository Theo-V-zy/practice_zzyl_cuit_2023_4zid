<template>
  <div class="family-list">
    <h3 class="list-title">我的预约</h3>
    <div v-if="list.length === 0" class="empty-hint">暂无预约记录</div>
    <div v-for="item in list" :key="item.id" class="list-card">
      <div class="card-row"><span class="label">预约编号</span><span>{{ item.visitNo || item.visit_no }}</span></div>
      <div class="card-row"><span class="label">来访人</span><span>{{ item.visitorName || item.visitor_name }}</span></div>
      <div class="card-row"><span class="label">预约时间</span><span>{{ item.appointmentTime || item.appointment_time }}</span></div>
      <div class="card-row"><span class="label">状态</span><el-tag size="small">{{ item.status === 'PENDING' ? '待确认' : item.status }}</el-tag></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { familyAppointments } from '@/api/family'

const list = ref([])
onMounted(async () => {
  try { const res = await familyAppointments({ page: 1, pageSize: 50 }); if (res) list.value = res.data || [] } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.family-list { min-width: 0; }
.list-title { font-size: 16px; font-weight: 600; margin: 0 0 12px; color: #333; }
.empty-hint { padding: 60px 0; text-align: center; color: rgba(0,0,0,0.35); }
.list-card { padding: 14px; margin-bottom: 10px; background: #fff; border-radius: 8px; border: 1px solid #e7e9ed; }
.card-row { display: flex; justify-content: space-between; padding: 4px 0; font-size: 14px; }
.label { color: rgba(0,0,0,0.5); }
</style>
