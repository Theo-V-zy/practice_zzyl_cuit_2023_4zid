<template>
  <section class="page-section">
    <div style="margin-bottom: 12px">
      <el-button type="primary" @click="loadList">刷新</el-button>
    </div>

    <el-table :data="roomTypes" style="width: 100%" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="roomType" label="房型名称" width="140" />
      <el-table-column label="床位数量" width="120">
        <template #default="{ row }">{{ row.count }}</template>
      </el-table-column>
      <el-table-column label="空闲数量" width="120">
        <template #default="{ row }">{{ row.emptyCount }}</template>
      </el-table-column>
      <el-table-column label="已占用" width="90">
        <template #default="{ row }">{{ row.occupiedCount }}</template>
      </el-table-column>
      <el-table-column label="维护中" width="90">
        <template #default="{ row }">{{ row.maintenanceCount }}</template>
      </el-table-column>
      <el-table-column label="费用区间" width="200">
        <template #default="{ row }">{{ row.priceRange }}</template>
      </el-table-column>
      <el-table-column label="包含房间">
        <template #default="{ row }">{{ row.rooms.join('、') }}</template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { getBedPage } from '@/api/admin'

const loading = ref(false)
const roomTypes = ref([])

async function loadList() {
  loading.value = true
  try {
    const res = await getBedPage({ pageNum: 1, pageSize: 9999 })
    const beds = res.data || []
    const grouped = {}
    beds.forEach(bed => {
      const type = bed.roomType || '未分类'
      if (!grouped[type]) {
        grouped[type] = { roomType: type, count: 0, emptyCount: 0, occupiedCount: 0,
          maintenanceCount: 0, rooms: new Set(), prices: [] }
      }
      grouped[type].count++
      if (bed.status === 'EMPTY') grouped[type].emptyCount++
      else if (bed.status === 'OCCUPIED') grouped[type].occupiedCount++
      else grouped[type].maintenanceCount++
      grouped[type].rooms.add(bed.roomNo)
      if (bed.bedPrice) grouped[type].prices.push(bed.bedPrice)
    })
    roomTypes.value = Object.values(grouped).map(g => ({
      ...g,
      rooms: [...g.rooms],
      priceRange: g.prices.length ? Math.min(...g.prices) + ' ~ ' + Math.max(...g.prices) : '-'
    }))
  } catch (e) { } finally { loading.value = false }
}

onMounted(() => { loadList() })
</script>
