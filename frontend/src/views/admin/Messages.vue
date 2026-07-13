<template>
  <section class="page-section">
    <!-- 查询区 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="标题">
        <el-input v-model="queryForm.title" placeholder="请输入" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="消息类型">
        <el-select v-model="queryForm.messageType" placeholder="消息类型" clearable style="width: 140px">
          <el-option label="系统通知" value="SYSTEM" />
          <el-option label="业务提醒" value="BUSINESS" />
          <el-option label="报警通知" value="ALERT" />
        </el-select>
      </el-form-item>
      <el-form-item label="时间范围">
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="-"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="YYYY-MM-DD"
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 未读/已读 Tab + 操作按钮 -->
    <div class="toolbar">
      <div class="tab-group">
        <span class="tab-item" :class="{ active: queryForm.readStatus === 0 }" @click="switchTab(0)">
          未读 <em>{{ unreadCount }}</em>
        </span>
        <span class="tab-item" :class="{ active: queryForm.readStatus === 1 }" @click="switchTab(1)">
          已读 <em>{{ readCount }}</em>
        </span>
      </div>
      <div class="toolbar-actions">
        <el-button @click="handleBatchRead">全部已读</el-button>
        <el-button type="danger" plain @click="handleDeleteAll">全部删除</el-button>
      </div>
    </div>

    <!-- 表格 -->
    <el-table :data="tableData" v-loading="loading" style="width: 100%" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="50" />
      <el-table-column prop="title" label="消息标题" min-width="250">
        <template #default="{ row }">
          <div class="msg-title-cell">
            <span v-if="row.readStatus === 0" class="unread-dot"></span>
            <el-link type="primary" :underline="false" @click="handleView(row)">{{ row.title }}</el-link>
            <el-tag v-if="isNew(row)" size="small" type="primary" effect="dark" style="margin-left:6px">NEW</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="messageType" label="消息类型" width="100">
        <template #default="{ row }">
          <el-tag size="small" :type="row.messageType === 'ALERT' ? 'danger' : ''">{{ typeMap[row.messageType] || row.messageType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="接收时间" width="170" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" type="primary" link @click="handleView(row)">查看</el-button>
          <el-button v-if="row.readStatus === 0" size="small" type="warning" link @click="handleMarkRead(row)">标记为已读</el-button>
          <el-button size="small" type="danger" link @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap">
      <el-pagination
        v-model:current-page="queryForm.page" v-model:page-size="queryForm.pageSize"
        :page-sizes="[10, 20, 30, 40]" :total="total"
        layout="total, sizes, prev, pager, next"
        @size-change="handleQuery" @current-change="loadData"
      />
    </div>

    <!-- 详情弹窗 -->
    <el-dialog title="消息详情" v-model="detailVisible" width="500px">
      <div class="msg-detail">
        <h3>{{ currentMsg.title }}</h3>
        <div class="msg-meta">{{ currentMsg.createTime }} · {{ typeMap[currentMsg.messageType] || currentMsg.messageType }}</div>
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
const dateRange = ref(null)
const unreadCount = ref(0)
const readCount = ref(0)

const typeMap = { SYSTEM: '系统通知', BUSINESS: '业务提醒', ALERT: '报警通知' }
const queryForm = reactive({ page: 1, pageSize: 10, title: '', messageType: '', readStatus: 0 })

function switchTab(status) {
  queryForm.readStatus = status
  queryForm.page = 1
  loadData()
}

async function loadData() {
  loading.value = true
  try {
    const params = { ...queryForm }
    if (dateRange.value && dateRange.value.length === 2) {
      params.startTime = dateRange.value[0]
      params.endTime = dateRange.value[1]
    }
    const res = await getMessagePage(params)
    if (res) { tableData.value = res.data || []; total.value = res.total || 0 }
    // 获取未读/已读计数
    if (queryForm.readStatus === 0) {
      unreadCount.value = res.total || 0
      // 同时查已读数
      try {
        const readRes = await getMessagePage({ page: 1, pageSize: 1, readStatus: 1 })
        if (readRes) readCount.value = readRes.total || 0
      } catch (e) { /* ignore */ }
    } else {
      readCount.value = res.total || 0
      try {
        const unreadRes = await getMessagePage({ page: 1, pageSize: 1, readStatus: 0 })
        if (unreadRes) unreadCount.value = unreadRes.total || 0
      } catch (e) { /* ignore */ }
    }
  } finally { loading.value = false }
}

function handleQuery() { queryForm.page = 1; loadData() }
function handleReset() {
  Object.assign(queryForm, { page: 1, pageSize: 10, title: '', messageType: '', readStatus: 0 })
  dateRange.value = null
  loadData()
}
function isNew(row) { return Date.now() - new Date(row.createTime).getTime() < 60000 }
function handleSelectionChange(rows) { selectedIds.value = rows.map(r => r.id) }

async function handleView(row) {
  try {
    const res = await getMessageDetail(row.id)
    if (res?.data) currentMsg.value = res.data
    detailVisible.value = true
    loadData()
  } catch (e) { /* ignore */ }
}

async function handleMarkRead(row) {
  await readMessage(row.id)
  ElMessage.success('已标记为已读')
  loadData()
}

async function handleBatchRead() {
  const ids = selectedIds.value.length > 0
    ? selectedIds.value.filter(id => tableData.value.find(m => m.id === id && m.readStatus === 0))
    : tableData.value.filter(m => m.readStatus === 0).map(m => m.id)
  if (ids.length === 0) { ElMessage.warning('没有需要标记的消息'); return }
  await readMessageBatch(ids)
  ElMessage.success(`已标记 ${ids.length} 条消息为已读`)
  selectedIds.value = []
  loadData()
}

function handleDelete(row) {
  ElMessageBox.confirm('确定删除这条消息吗？', '提示', { type: 'warning' })
    .then(async () => { await deleteMessage(row.id); ElMessage.success('删除成功'); loadData() })
    .catch(() => {})
}

function handleDeleteAll() {
  const ids = selectedIds.value.length > 0 ? selectedIds.value : tableData.value.map(m => m.id)
  const label = selectedIds.value.length > 0 ? `删除选中的 ${ids.length} 条消息` : '删除当前页全部消息'
  ElMessageBox.confirm(`确定${label}吗？此操作不可恢复。`, '警告', { type: 'warning', confirmButtonText: '确认删除' })
    .then(async () => {
      for (const id of ids) { await deleteMessage(id) }
      ElMessage.success(`已删除 ${ids.length} 条消息`)
      selectedIds.value = []
      loadData()
    })
    .catch(() => {})
}

onMounted(() => loadData())
</script>

<style scoped>
.query-form { margin-bottom: 6px; }
.toolbar { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.tab-group { display: flex; gap: 0; }
.tab-item { padding: 6px 20px; font-size: 14px; color: rgba(0,0,0,0.6); cursor: pointer; border-bottom: 2px solid transparent; transition: all 0.2s; }
.tab-item.active { color: #0052d9; border-bottom-color: #0052d9; font-weight: 600; }
.tab-item em { font-style: normal; margin-left: 4px; }
.toolbar-actions { display: flex; gap: 8px; }
.pagination-wrap { margin-top: 16px; display: flex; justify-content: flex-end; }
.msg-title-cell { display: flex; align-items: center; gap: 8px; }
.unread-dot { display: inline-block; width: 8px; height: 8px; min-width: 8px; background: #0052d9; border-radius: 50%; }
.msg-detail h3 { margin: 0 0 8px; font-size: 18px; color: #333; }
.msg-meta { color: rgba(0,0,0,0.4); font-size: 13px; margin-bottom: 16px; }
.msg-content { padding: 16px; background: #f5f7fa; border-radius: 6px; line-height: 1.8; font-size: 14px; color: #333; }
</style>
