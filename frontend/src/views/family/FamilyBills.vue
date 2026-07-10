<template>
  <div class="family-list">
    <h3 class="list-title">我的账单</h3>
    <div v-if="list.length === 0" class="empty-hint">暂无账单记录</div>
    <div v-for="item in list" :key="item.id" class="list-card">
      <div class="card-row"><span class="label">账单编号</span><span>{{ item.billNo || item.bill_no }}</span></div>
      <div class="card-row"><span class="label">费用名称</span><span>{{ item.feeName || item.fee_name || '-' }}</span></div>
      <div class="card-row"><span class="label">总金额</span><span class="price">¥{{ item.totalAmount || item.total_amount || 0 }}</span></div>
      <div class="card-row"><span class="label">已付</span><span>¥{{ item.paidAmount || item.paid_amount || 0 }}</span></div>
      <div class="card-row">
        <span class="label">状态</span>
        <el-tag size="small" :type="(item.status === 'UNPAID' || item.status === 'unpaid') ? 'danger' : 'success'">
          {{ item.status === 'UNPAID' ? '未支付' : item.status === 'PAID' ? '已支付' : item.status }}
        </el-tag>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { familyBills } from '@/api/family'

const list = ref([])
onMounted(async () => {
  try { const res = await familyBills({ page: 1, pageSize: 50 }); if (res) list.value = res.data || [] } catch (e) { /* ignore */ }
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
