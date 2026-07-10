<template>
  <section class="roomtype-page">
    <div style="text-align: left; margin-bottom: 12px">
      <el-button type="primary" @click="loadList">刷新</el-button>
    </div>

    <el-table :data="roomTypes" style="width: 100%" :fit="true" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="roomType" label="房型名称" width="140" />
      <el-table-column label="床位数量" width="120">
        <template #default="scope">{{ scope.row.count }}</template>
      </el-table-column>
      <el-table-column label="空闲数量" width="120">
        <template #default="scope">{{ scope.row.emptyCount }}</template>
      </el-table-column>
      <el-table-column label="已占用" width="90">
        <template #default="scope">{{ scope.row.occupiedCount }}</template>
      </el-table-column>
      <el-table-column label="维护中" width="90">
        <template #default="scope">{{ scope.row.maintenanceCount }}</template>
      </el-table-column>
      <el-table-column label="费用区间" width="200">
        <template #default="scope">{{ scope.row.priceRange }}</template>
      </el-table-column>
      <el-table-column label="包含房间">
        <template #default="scope">{{ scope.row.rooms.join('、') }}</template>
      </el-table-column>
    </el-table>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const roomTypes = ref([])

function loadList() {
  loading.value = true
  axios.post('/bedPage', { pageNum: 1, pageSize: 9999 }).then(({ data }) => {
    const beds = data.beds || []
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
  }).catch(() => ElMessage.error('加载失败')).finally(() => { loading.value = false })
}

onMounted(() => { loadList() })
</script>
