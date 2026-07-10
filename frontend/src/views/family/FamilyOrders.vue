<template>
  <div class="family-list">
    <h3 class="list-title">我的订单</h3>
    <div v-if="list.length === 0" class="empty-hint">暂无订单记录</div>
    <div v-for="item in list" :key="item.id" class="list-card" @click="router.push(`/family/orders/${item.id}`)">
      <div class="card-row"><span class="label">订单编号</span><span>{{ item.orderNo || item.order_no }}</span></div>
      <div class="card-row"><span class="label">服务项目</span><span>{{ item.serviceName || item.service_name || '-' }}</span></div>
      <div class="card-row"><span class="label">金额</span><span class="price">¥{{ item.totalAmount || item.total_amount || 0 }}</span></div>
      <div class="card-row"><span class="label">状态</span><el-tag size="small">{{ orderStatusMap[item.orderStatus] || item.orderStatus || item.order_status }}</el-tag></div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { familyOrders } from '@/api/family'

const router = useRouter()
const list = ref([])
const orderStatusMap = { CREATED: '已创建', PAID: '已支付', SERVING: '服务中', FINISHED: '已完成', CANCELED: '已取消', REFUNDED: '已退款' }

onMounted(async () => {
  try { const res = await familyOrders({ page: 1, pageSize: 50 }); if (res) list.value = res.data || [] } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.family-list { min-width: 0; }
.list-title { font-size: 16px; font-weight: 600; margin: 0 0 12px; color: #333; }
.empty-hint { padding: 60px 0; text-align: center; color: rgba(0,0,0,0.35); }
.list-card { padding: 14px; margin-bottom: 10px; background: #fff; border-radius: 8px; border: 1px solid #e7e9ed; cursor: pointer; transition: box-shadow 0.2s; }
.list-card:hover { box-shadow: 0 1px 6px rgba(0,0,0,0.08); }
.card-row { display: flex; justify-content: space-between; padding: 4px 0; font-size: 14px; }
.label { color: rgba(0,0,0,0.5); }
.price { color: #0052d9; font-weight: 600; }
</style>
