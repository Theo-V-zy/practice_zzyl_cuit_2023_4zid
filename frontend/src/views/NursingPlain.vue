<template>
  <!--  新增/编辑护理计划对话框  -->
  <el-dialog v-model="dialogVisible" :title="dialogTitle" width="550px" center>
    <el-form label-width="80px" style="padding:0 10px">
      <el-form-item label="计划名称">
        <el-input v-model="form.plainname" placeholder="请输入计划名称"/>
      </el-form-item>
      <el-form-item label="护理等级">
        <el-select v-model="form.levelId" placeholder="请选择护理等级" style="width:100%">
          <el-option v-for="lv in levelList" :key="lv.id"
                     :value="lv.id" :label="lv.levelName"/>
        </el-select>
      </el-form-item>

      <el-divider content-position="left">
        <span style="font-size:13px;color:#606266">已选项目</span>
      </el-divider>

      <el-table :data="selectedItems" size="small"
                empty-text="请从下方添加护理项目"
                style="margin-bottom:12px">
        <el-table-column prop="itemname" label="项目" min-width="100"/>
        <el-table-column label="单价" width="70" align="center">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column label="频率" width="60" align="center">
          <template #default="scope">{{ scope.row.unit }}</template>
        </el-table-column>
        <el-table-column label="" width="50" align="center">
          <template #default="scope">
            <el-button type="danger" size="small" :icon="Delete" circle
                       @click="removeSelected(scope.$index)"/>
          </template>
        </el-table-column>
      </el-table>

      <!--  当日费用合计  -->
      <div style="text-align:right;padding:8px 0;font-size:14px;color:#F56C6C">
        费用合计：
        <span style="font-size:20px;font-weight:bold">
          {{ dailyTotal }} 元/天
        </span>
      </div>

      <el-divider content-position="left">
        <span style="font-size:13px;color:#606266">可选项目</span>
      </el-divider>

      <el-table :data="availableItems" size="small" max-height="240">
        <el-table-column prop="itemname" label="项目" min-width="100"/>
        <el-table-column label="单价" width="70" align="center">
          <template #default="scope">¥{{ scope.row.price }}</template>
        </el-table-column>
        <el-table-column prop="unit" label="频率" width="55" align="center"/>
        <el-table-column label="" width="55" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="addSingle(scope.row)">添加</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div style="text-align:center;margin-top:20px">
        <el-button type="primary" @click="save">确认保存</el-button>
        <el-button @click="dialogVisible=false">取消</el-button>
      </div>
    </el-form>
  </el-dialog>

  <!--  搜索  -->
  <div style="display:flex;align-items:center;gap:10px;margin-bottom:12px">
    <span style="white-space:nowrap">计划名称</span>
    <el-input v-model="condForm.plainname" style="width:180px" placeholder="输入名称"/>
    <span style="white-space:nowrap">状态</span>
    <el-select v-model="condForm.islock" style="width:100px" placeholder="全部" clearable>
      <el-option value="启动" label="启动"/>
      <el-option value="禁用" label="禁用"/>
    </el-select>
    <el-button type="primary" @click="loadPage(1)">搜索</el-button>
    <el-button type="success" @click="openAdd" style="margin-left:auto">新增护理计划</el-button>
  </div>

  <!--  计划列表  -->
  <el-table :data="list" style="width:100%" size="default">
    <el-table-column type="index" width="45" align="center"/>
    <el-table-column prop="plainname" label="计划名称" min-width="120"/>
    <el-table-column label="护理等级" width="100" align="center">
      <template #default="scope">
        <span v-if="scope.row.levelName" style="color:#409EFF;font-weight:500">
          {{ scope.row.levelName }}
        </span>
        <span v-else style="color:#C0C4CC">—</span>
      </template>
    </el-table-column>
    <el-table-column label="价格" width="120" align="center">
      <template #default="scope">
        <span style="color:#F56C6C;font-weight:bold;font-size:15px">
          {{ scope.row.totalPrice || 0 }} 元/天
        </span>
      </template>
    </el-table-column>
    <el-table-column label="包含项目" min-width="240">
      <template #default="scope">
        <el-tag v-for="(name, i) in (scope.row.itemNames || [])"
                :key="i" size="small" style="margin:2px 4px 2px 0"
                type="info">{{ name }}</el-tag>
        <span v-if="!scope.row.itemNames || scope.row.itemNames.length==0"
              style="color:#C0C4CC">—</span>
      </template>
    </el-table-column>
    <el-table-column prop="createuser" label="创建人" width="70" align="center"/>
    <el-table-column prop="createtime" label="创建时间" width="155" align="center"/>
    <el-table-column label="状态" width="70" align="center">
      <template #default="scope">
        <el-tag :type="scope.row.islock=='启动'?'success':'danger'"
                size="small" effect="plain">
          {{ scope.row.islock }}
        </el-tag>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="200" align="center">
      <template #default="scope">
        <el-button type="primary" size="small" @click="openEdit(scope.row)">编辑</el-button>
        <el-button type="warning" size="small" @click="del(scope.row.id)">删除</el-button>
        <el-button type="success" size="small" @click="toggleLock(scope.row)">
          {{ scope.row.islock=="启动"?"禁用":"启动" }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <div style="display:flex;justify-content:flex-end;margin-top:10px">
    <el-pagination size="small" background layout="prev, pager, next"
                   :total="total" @change="loadPage"/>
  </div>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
import {Delete} from "@element-plus/icons-vue";

const dialogVisible = ref(false);
const dialogTitle = ref("新增护理计划");

const form = reactive({ id: '', plainname: '', levelId: null, levelName: '' });
const levelList = ref([]);
const selectedItems = ref([]);
const availableItems = ref([]);
var url = null;

//折算每日费用
function calcDaily(price, hlpc, unit) {
  const total = (price || 0) * (hlpc || 1);
  if (unit === '周') return (total / 7).toFixed(2);
  if (unit === '月') return (total / 30).toFixed(2);
  return total.toFixed(2); // 天、次
}

//当日费用合计
const dailyTotal = computed(() => {
  if (!selectedItems.value.length) return '0.00';
  return selectedItems.value.reduce((sum, s) =>
    sum + parseFloat(calcDaily(s.price, s.hlpc, s.unit)), 0).toFixed(2);
});

function loadItems() {
  axios.post("/nursingItemPage", { pageNum: 1, pageSize: 100, islock: "启用" })
      .then(r => { availableItems.value = r.data.nursimgItems || []; });
}

function loadLevelList() {
  axios.get("/nursingLevelList").then(r => {
    levelList.value = (r.data || []).filter(l => l.islock === '启用');
  });
}

function addSingle(row) {
  if (selectedItems.value.find(s => s.itemid == row.id)) {
    ElMessage("该项目已添加......"); return;
  }
  selectedItems.value.push({
    itemid: row.id, itemname: row.itemname,
    price: row.price, unit: row.unit,
    hlpc: 1
  });
}

function removeSelected(idx) { selectedItems.value.splice(idx, 1); }

function openAdd() {
  dialogTitle.value = "新增护理计划"; url = "/saveNursingPlain";
  form.id = ''; form.plainname = ''; form.levelId = null; form.levelName = '';
  selectedItems.value = []; loadItems(); loadLevelList();
  dialogVisible.value = true;
}

function openEdit(row) {
  dialogTitle.value = "编辑护理计划";
  url = "/saveNursingPlain";
  form.id = row.id; form.plainname = row.plainname;
  form.levelId = row.levelId || null; form.levelName = row.levelName || '';
  selectedItems.value = [];
  loadItems(); loadLevelList();
  axios.get("/plainItemsByPlainId?plainId=" + row.id).then(r => {
    selectedItems.value = (r.data || []).map(p => ({
      itemid: p.itemId || p.item_id,
      itemname: p.itemname, price: p.price || 0,
      unit: p.unit || '', hlpc: p.hlpc || 1
    }));
  }).catch(() => { selectedItems.value = []; });
  dialogVisible.value = true;
}

function save() {
  if (!form.plainname) { ElMessage("请输入计划名称......"); return; }
  if (!form.levelId) { ElMessage("请选择护理等级......"); return; }
  if (selectedItems.value.length == 0) { ElMessage("请至少添加一个护理项目......"); return; }

  const selectedLevel = levelList.value.find(l => l.id == form.levelId);
  form.levelName = selectedLevel ? selectedLevel.levelName : '';

  const data = {
    id: form.id, plainname: form.plainname,
    levelId: form.levelId, levelName: form.levelName,
    plainItemList: selectedItems.value.map(s => ({
      itemid: s.itemid, hlmc: s.itemname,
      hlzq: s.unit, hlpc: s.hlpc
    }))
  };
  axios.post(url, data).then(r => {
    if (r.data.code == 200) { dialogVisible.value = false; loadPage(1); }
    ElMessage(r.data.msg);
  });
}

const list = ref([]);
const total = ref(0);
const condForm = reactive({ plainname: '', islock: '', pageNum: 1, pageSize: 10 });

function loadPage(pageNum) {
  condForm.pageNum = pageNum;
  axios.post("/pageList", condForm).then(r => {
    const plains = r.data.nursingPlains || [];
    list.value = plains.map(p => ({ ...p, totalPrice: 0, itemNames: [] }));
    total.value = r.data.total;
    plains.forEach((p, i) => {
      axios.get("/plainItemsByPlainId?plainId=" + p.id).then(r2 => {
        const items = r2.data || [];
        const totalPrice = items.reduce((sum, it) =>
          sum + parseFloat(calcDaily(it.price, it.hlpc, it.unit)), 0).toFixed(2);
        list.value[i].totalPrice = totalPrice;
        list.value[i].itemNames = items.map(it => it.itemname);
      }).catch(() => {});
    });
  });
}

onMounted(() => { loadPage(1); loadLevelList(); });

function del(id) {
  axios.get("/deleteNursingPlain?id=" + id).then(r => {
    if (r.data.code == 200) loadPage(1);
    ElMessage(r.data.msg);
  });
}

function toggleLock(row) {
  const f = { id: row.id, islock: row.islock == "禁用" ? "启动" : "禁用" };
  axios.post("/updateNursingPlain", f).then(r => {
    if (r.data.code == 200) loadPage(1);
    ElMessage(r.data.msg);
  });
}
</script>
