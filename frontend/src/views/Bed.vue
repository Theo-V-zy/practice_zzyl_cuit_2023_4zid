<template>
  <section class="bed-page">
    <el-dialog v-model="dialogVisible" title="床位信息" width="45%">
      <el-form label-width="100px" style="margin: 0 5%">
        <el-form-item label="楼栋">
          <el-input v-model="form.building" placeholder="如 A栋" />
        </el-form-item>
        <el-form-item label="楼层">
          <el-input v-model="form.floor" placeholder="如 3层" />
        </el-form-item>
        <el-form-item label="房间号">
          <el-input v-model="form.roomNo" placeholder="如 A301" />
        </el-form-item>
        <el-form-item label="房型">
          <el-select v-model="form.roomType" style="width: 100%">
            <el-option label="单人间" value="单人间" />
            <el-option label="双人间" value="双人间" />
            <el-option label="豪华间" value="豪华间" />
            <el-option label="套间" value="套间" />
          </el-select>
        </el-form-item>
        <el-form-item label="床位号">
          <el-input v-model="form.bedNo" placeholder="如 1号床" />
        </el-form-item>
        <el-form-item label="床位编码">
          <el-input v-model="form.bedCode" placeholder="如 BED-A301-1" />
        </el-form-item>
        <el-form-item label="床位费">
          <el-input-number v-model="form.bedPrice" :precision="2" :step="100" style="width: 100%" />
        </el-form-item>
        <el-form-item label="入住老人">
          <el-select v-model="form.elderId" placeholder="选择老人（可选）" style="width: 100%" filterable clearable>
            <el-option v-for="elder in elderList" :key="elder.id" :label="elder.name + ' (' + elder.elderNo + ')'"
              :value="elder.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备编号">
          <el-input v-model="form.deviceNo" placeholder="绑定设备编号（可选）" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" style="width: 100%">
            <el-option label="空闲" value="EMPTY" />
            <el-option label="已占用" value="OCCUPIED" />
            <el-option label="维护中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBed">确认</el-button>
      </template>
    </el-dialog>

    <div class="search-bar">
      <span>房间号&nbsp;:&nbsp;</span>
      <el-input v-model="condForm.roomNo" style="width: 16%; margin-right: 16px" placeholder="房间号" />
      <span>状态&nbsp;:&nbsp;</span>
      <el-select v-model="condForm.status" style="width: 16%; margin-right: 16px" placeholder="全部" clearable>
        <el-option label="空闲" value="EMPTY" />
        <el-option label="已占用" value="OCCUPIED" />
        <el-option label="维护中" value="MAINTENANCE" />
      </el-select>
      <el-button type="primary" @click="loadList">搜索</el-button>
    </div>
    <hr />
    <div style="text-align: left; margin-bottom: 12px">
      <el-button type="primary" @click="openAdd">添加床位</el-button>
    </div>

    <el-table :data="bedList" style="width: 100%" :fit="true" v-loading="loading">
      <el-table-column type="index" width="50" />
      <el-table-column prop="building" label="楼栋" width="70" />
      <el-table-column prop="floor" label="楼层" width="70" />
      <el-table-column prop="roomNo" label="房间号" width="90" />
      <el-table-column prop="roomType" label="房型" width="90" />
      <el-table-column prop="bedNo" label="床位号" width="80" />
      <el-table-column prop="bedCode" label="编码" width="130" />
      <el-table-column prop="bedPrice" label="费用" width="80" />
      <el-table-column prop="elderName" label="入住老人" width="100" />
      <el-table-column prop="deviceNo" label="设备" width="100" show-overflow-tooltip />
      <el-table-column label="状态" width="90">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 'EMPTY'" type="success" size="small">空闲</el-tag>
          <el-tag v-else-if="scope.row.status === 'OCCUPIED'" type="warning" size="small">已占用</el-tag>
          <el-tag v-else type="danger" size="small">维护中</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="140" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" @click="delBed(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination size="small" background layout="prev, pager, next" :total="total" @change="doPage" />
  </section>
</template>

<script setup>
import { onMounted, reactive, ref } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const dialogVisible = ref(false)
const loading = ref(false)
const bedList = ref([])
const elderList = ref([])
const total = ref(0)
var url = '/saveBed'

const form = reactive({
  id: null, building: '', floor: '', roomNo: '', roomType: '', bedNo: '', bedCode: '',
  bedPrice: 0, elderId: null, deviceNo: '', status: 'EMPTY', remark: ''
})

const condForm = reactive({ roomNo: '', status: '', pageNum: 1, pageSize: 10 })

function loadList() {
  loading.value = true
  axios.post('/bedPage', condForm).then(({ data }) => {
    bedList.value = data.beds || []
    total.value = data.total || 0
  }).catch(() => ElMessage.error('加载失败')).finally(() => { loading.value = false })
}

function loadElders() {
  axios.post('/elderPage', { pageNum: 1, pageSize: 1000 }).then(({ data }) => {
    elderList.value = data.elders || []
  }).catch(() => { })
}

function openAdd() { cleanForm(); url = '/saveBed'; dialogVisible.value = true }

function openEdit(row) {
  Object.assign(form, {
    id: row.id, building: row.building, floor: row.floor, roomNo: row.roomNo,
    roomType: row.roomType, bedNo: row.bedNo, bedCode: row.bedCode,
    bedPrice: row.bedPrice, elderId: row.elderId, deviceNo: row.deviceNo,
    status: row.status, remark: row.remark
  })
  url = '/updateBed'
  dialogVisible.value = true
}

function cleanForm() {
  form.id = null; form.building = ''; form.floor = ''; form.roomNo = ''; form.roomType = ''
  form.bedNo = ''; form.bedCode = ''; form.bedPrice = 0; form.elderId = null
  form.deviceNo = ''; form.status = 'EMPTY'; form.remark = ''
}

function saveBed() {
  const payload = { ...form }
  const elder = elderList.value.find(e => e.id === payload.elderId)
  if (elder) payload.elderName = elder.name
  axios.post(url, payload).then(({ data }) => {
    if (data.code === 200) {
      dialogVisible.value = false
      cleanForm()
      loadList()
    }
    ElMessage(data.msg)
  }).catch(() => ElMessage.error('操作失败'))
}

function delBed(id) {
  ElMessageBox.confirm('确定删除该床位？', '提示', { type: 'warning' }).then(() => {
    axios.get('/deleteBed?id=' + id).then(({ data }) => {
      if (data.code === 200) doPage(1)
      ElMessage(data.msg)
    }).catch(() => ElMessage.error('删除失败'))
  }).catch(() => { })
}

function doPage(pageNum) { condForm.pageNum = pageNum; loadList() }

onMounted(() => { loadList(); loadElders() })
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 4px;
  margin-bottom: 12px;
}

.search-bar span {
  white-space: nowrap;
}
</style>
