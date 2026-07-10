<template>
  <div class="order-detail">
    <h3 class="list-title">订单详情</h3>
    <div v-if="detail" class="detail-card">
      <div class="card-row"><span class="label">订单编号</span><span>{{ detail.orderNo || detail.order_no }}</span></div>
      <div class="card-row"><span class="label">服务项目</span><span>{{ detail.serviceName || detail.service_name || '-' }}</span></div>
      <div class="card-row"><span class="label">数量</span><span>{{ detail.quantity || 1 }}</span></div>
      <div class="card-row"><span class="label">总金额</span><span class="price">¥{{ detail.totalAmount || detail.total_amount || 0 }}</span></div>
      <div class="card-row"><span class="label">实付金额</span><span class="price">¥{{ detail.payAmount || detail.pay_amount || 0 }}</span></div>
      <div class="card-row"><span class="label">状态</span><el-tag size="small">{{ detail.orderStatus || detail.order_status }}</el-tag></div>
      <div class="card-row"><span class="label">创建时间</span><span>{{ detail.createTime || detail.create_time }}</span></div>
    </div>
    <div v-else class="empty-hint">订单详情由组员B完成后端对接后显示</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { familyOrderDetail } from '@/api/family'

const route = useRoute()
const detail = ref(null)

onMounted(async () => {
  const id = route.params.id
  try { const res = await familyOrderDetail(id); if (res && res.data) detail.value = res.data } catch (e) { /* ignore */ }
})
</script>

<style scoped>
.order-detail { min-width: 0; }
.list-title { font-size: 16px; font-weight: 600; margin: 0 0 12px; color: #333; }
.empty-hint { padding: 60px 0; text-align: center; color: rgba(0,0,0,0.35); }
.detail-card { padding: 16px; background: #fff; border-radius: 8px; border: 1px solid #e7e9ed; }
.card-row { display: flex; justify-content: space-between; padding: 6px 0; font-size: 14px; border-bottom: 1px solid #f5f7fa; }
.card-row:last-child { border-bottom: 0; }
.label { color: rgba(0,0,0,0.5); }
.price { color: #0052d9; font-weight: 600; }
</style>
