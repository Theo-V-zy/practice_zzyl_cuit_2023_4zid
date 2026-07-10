<template>
  <section class="page-section">
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="标题">
        <el-input v-model="queryForm.title" placeholder="请输入" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="queryForm.messageType" placeholder="选择" clearable style="width: 140px">
          <el-option label="系统通知" value="SYSTEM" />
          <el-option label="业务提醒" value="BUSINESS" />
          <el-option label="预警消息" value="ALERT" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.readStatus" placeholder="选择" clearable style="width: 120px">
          <el-option label="未读" :value="0" />
          <el-option label="已读" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <div class="toolbar">
      <el-button type="primary" :disabled="selectedIds.length === 0" @click="handleBatchRead">批量已读</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading" style="width: 100%" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column label="状态" width="70">
        <template #default="{ row }">
          <span class="unread-dot" v-if="row.readStatus === 0"></span>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="200">
        <template #default="{ row }">
          <el-link type="primary" :underline="false" @click="handleView(row)">{{ row.title }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="messageType" label="类型" width="100">
        <template #default="{ row }">
          <el-tag size="small">{{ typeMap[row.messageType] || row.messageType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="时间" width="170" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleView(row)">查看</el-button>
          <el-button v-if="row.readStatus === 0" size="small" type="warning" link @click="handleRead(row)">标为已读</el-button>
          <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="queryForm.page" v-model:page-size="queryForm.pageSize"
        :page-sizes="[10, 20, 50]" :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleQuery" @current-change="handleQuery"
      />
    </div>

    <!-- 消息详情弹窗 -->
    <el-dialog title="消息详情" v-model="detailVisible" width="500px">
      <div class="msg-detail">
        <h3>{{ currentMsg.title }}</h3>
        <div style="color: rgba(0,0,0,0.45); font-size: 13px; margin: 8px 0 16px;">
          {{ currentMsg.createTime }} · {{ typeMap[currentMsg.messageType] || currentMsg.messageType }}
        </div>
        <div class="msg-content">{{ currentMsg.content || '暂无详细内容' }}</div>
      </div>
    </el-dialog>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMessagePage, getMessageDetail, readMessage, readMessageBatch, deleteMessage } from '@/api/admin'

const loading = ref(false)
const tableData = ref([]), total = ref(0)
const selectedIds = ref([])
const detailVisible = ref(false)
const currentMsg = ref({})

const typeMap = { SYSTEM: '系统通知', BUSINESS: '业务提醒', ALERT: '预警消息' }

const queryForm = reactive({ page: 1, pageSize: 10, title: '', messageType: '', readStatus: null })

async function loadData() {
  loading.value = true
  try {
    const res = await getMessagePage({ ...queryForm })
    if (res) { tableData.value = res.data || []; total.value = res.total || 0 }
  } finally { loading.value = false }
}
function handleQuery() { queryForm.page = 1; loadData() }
function handleReset() { Object.assign(queryForm, { page: 1, pageSize: 10, title: '', messageType: '', readStatus: null }); loadData() }
function handleSelectionChange(rows) { selectedIds.value = rows.map(r => r.id) }

async function handleView(row) {
  try {
    const res = await getMessageDetail(row.id)
    if (res && res.data) currentMsg.value = res.data
    detailVisible.value = true
    loadData() // 刷新列表更新已读状态
  } catch (e) { /* ignore */ }
}

async function handleRead(row) {
  await readMessage(row.id)
  ElMessage.success('已标记为已读')
  loadData()
}

async function handleBatchRead() {
  if (selectedIds.value.length === 0) { ElMessage.warning('请选择消息'); return }
  await readMessageBatch(selectedIds.value)
  ElMessage.success('批量已读成功')
  loadData()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除这条消息吗？', '提示', { type: 'warning' })
    .then(async () => { await deleteMessage(row.id); ElMessage.success('删除成功'); loadData() })
    .catch(() => {})
}

onMounted(() => loadData())
</script>

<style scoped>
.query-form { margin-bottom: 6px; }
.toolbar { margin-bottom: 12px; }
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
.unread-dot { display: inline-block; width: 8px; height: 8px; background: #0052d9; border-radius: 50%; }
.msg-detail h3 { margin: 0 0 8px; font-size: 18px; color: #333; }
.msg-content { padding: 16px; background: #f5f7fa; border-radius: 6px; line-height: 1.8; font-size: 14px; color: #333; }
</style>
