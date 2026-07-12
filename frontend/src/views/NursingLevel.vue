<template>

  <!--  添加/编辑护理等级对话框  -->
  <el-dialog
      v-model="levelDialogVisible"
      title="护理等级信息"
      width="40%">

    <el-form label-width="auto"
             style="margin-left:5%;margin-right:5%;max-width: 600px">
      <el-form-item label="等级名称">
        <el-input v-model="levelForm.levelName"/>
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="levelForm.islock">
          <el-radio value="禁用">禁用</el-radio>
          <el-radio value="启用">启用</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="等级描述">
        <el-input v-model="levelForm.description" type="textarea"/>
      </el-form-item>

      <el-form-item style="margin-left: 40%">
        <el-button type="primary" round @click="saveLevel">确认</el-button>
        <el-button type="warning" round @click="levelDialogVisible=false">取消</el-button>
      </el-form-item>
    </el-form>

  </el-dialog>

  <!--  工具栏  -->
  <div style="text-align: left">
    <el-button type="primary" @click="openLevelDialog">添加护理等级</el-button>
  </div>

  <!--  护理等级列表  -->
  <el-table
      :data="levelList"
      style="width: 100%;margin-top:10px" :fit="true">
    <el-table-column prop="sort" label="排序" width="80"/>
    <el-table-column prop="levelName" label="等级名称" width="150"/>
    <el-table-column prop="description" label="等级描述"/>
    <el-table-column label="状态" width="100">
      <template #default="scope">
        <span v-if="scope.row.islock=='启用'" style="color:dodgerblue">启用</span>
        <span v-else style="color:crimson">禁用</span>
      </template>
    </el-table-column>
    <el-table-column label="操作" width="250">
      <template #default="scope">
        <el-button type="warning" size="small"
                   @click="delLevel(scope.row.id)">删除</el-button>
        <el-button type="primary" size="small"
                   @click="showLevelInfo(scope.row)">编辑</el-button>
        <el-button type="success" size="small"
                   @click="toggleStatus(scope.row)">
          {{ scope.row.islock=="启用"?"禁用":"启用" }}
        </el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>

import {onMounted, reactive, ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";

//对话框状态
const levelDialogVisible = ref(false);

//表单
const levelForm = reactive({
  id: '',
  levelName: '',
  description: '',
  sort: '',
  islock: ''
});

var url = null;

//打开新增对话框
function openLevelDialog() {
  url = "/saveNursingLevel";
  cleanForm();
  // 自动排序：排在现有最大排序值之后
  const maxSort = levelList.value.reduce((max, l) => Math.max(max, l.sort || 0), 0);
  levelForm.sort = maxSort + 1;
  levelDialogVisible.value = true;
}

//保存
function saveLevel() {
  axios.post(url, levelForm)
      .then(response => {
        if (response.data.code == 200) {
          levelDialogVisible.value = false;
          cleanForm();
          loadLevelList();
        }
        ElMessage(response.data.msg);
      })
      .catch(error => {
        console.log(error);
      });
}

//清空表单
function cleanForm() {
  levelForm.id = "";
  levelForm.levelName = "";
  levelForm.description = "";
  levelForm.sort = "";
  levelForm.islock = "";
}

//列表
const levelList = ref([]);

function loadLevelList() {
  axios.get("/nursingLevelList")
      .then(response => {
        levelList.value = response.data;
      })
      .catch(error => {
        console.log(error);
      });
}

onMounted(function () {
  loadLevelList();
});

//编辑回显
function showLevelInfo(row) {
  levelForm.id = row.id;
  levelForm.levelName = row.levelName;
  levelForm.description = row.description;
  levelForm.sort = row.sort;
  levelForm.islock = row.islock;
  url = "/updateNursingLevel";
  levelDialogVisible.value = true;
}

//删除
function delLevel(id) {
  axios.get("/deleteNursingLevel?id=" + id)
      .then(response => {
        if (response.data.code == 200) {
          loadLevelList();
        }
        ElMessage(response.data.msg);
      })
      .catch(error => {
        console.log(error);
      });
}

//状态切换
function toggleStatus(row) {
  var form = { id: row.id };
  if (row.islock == "禁用") {
    form.islock = "启用";
  } else {
    form.islock = "禁用";
  }
  axios.post("/updateNursingLevel", form)
      .then(response => {
        if (response.data.code == 200) {
          loadLevelList();
        }
        ElMessage(response.data.msg);
      })
      .catch(error => {
        console.log(error);
      });
}
</script>

<style scoped>

</style>
