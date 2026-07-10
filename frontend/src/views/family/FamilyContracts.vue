<template>
  <div class="family-list">
    <h3 class="list-title">我的合同</h3>
    <div v-if="list.length === 0" class="empty-hint">暂无合同记录</div>
    <div v-for="item in list" :key="item.id" class="list-card">
      <div class="card-row"><span class="label">合同编号</span><span>{{ item.contractNo }}</span></div>
      <div class="card-row"><span class="label">老人</span><span>{{ item.elderName || '-' }}</span></div>
      <div class="card-row"><span class="label">月费</span><span class="price">¥{{ item.monthlyFee || 0 }}</span></div>
      <div class="card-row"><span class="label">状态</span><el-tag size="small">{{ item.status }}</el-tag></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { familyContracts } from '@/api/family'

const list = ref([])
onMounted(async () => {
  try { const res = await familyContracts({ page: 1, pageSize: 50 }); if (res) list.value = res.data || [] } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.family-list { min-width: 0; }
.list-title { font-size: 16px; font-weight: 600; margin: 0 0 12px; color: #333; }
.empty-hint { padding: 60px 0; text-align: center; color: rgba(0,0,0,0.35); }
.list-card { padding: 14px; margin-bottom: 10px; background: #fff; border-radius: 8px; border: 1px solid #e7e9ed; }
.card-row { display: flex; justify-content: space-between; padding: 4px 0; font-size: 14px; }
.label { color: rgba(0,0,0,0.5); }
.price { color: #0052d9; font-weight: 600; }
</style>
