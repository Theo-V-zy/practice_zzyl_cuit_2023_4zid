<template>
  <h3>报警规则配置</h3>
  <p style="color:#999;margin-bottom:20px">配置设备报警触发规则，当设备运行状态异常或超出阈值时自动触发报警。</p>

  <el-table :data="ruleList" style="width: 100%" :fit="true" border>
    <el-table-column type="index" width="50" label="#" />
    <el-table-column prop="ruleName" label="规则名称" width="180"/>
    <el-table-column prop="deviceType" label="适用设备类型" width="140"/>
    <el-table-column prop="metric" label="监测指标" width="140"/>
    <el-table-column prop="condition" label="触发条件" width="140"/>
    <el-table-column prop="threshold" label="阈值" width="100"/>
    <el-table-column prop="alarmLevel" label="报警级别" width="100">
      <template #default="scope">
        <el-tag v-if="scope.row.alarmLevel=='一般'" type="warning">一般</el-tag>
        <el-tag v-else type="danger">紧急</el-tag>
      </template>
    </el-table-column>
    <el-table-column prop="notifyMethod" label="通知方式" width="120"/>
    <el-table-column label="状态" width="80">
      <template #default="scope">
        <el-switch v-model="scope.row.enabled" @change="toggleRule(scope.row)" />
      </template>
    </el-table-column>
    <el-table-column label="操作" width="120">
      <template #default="scope">
        <el-button type="primary" size="small" @click="editRule(scope.row)">编辑</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- 编辑对话框 -->
  <el-dialog v-model="dialogVisible" title="编辑报警规则" width="40%">
    <el-form label-width="auto" style="margin-left:5%;margin-right:5%">
      <el-form-item label="规则名称">
        <el-input v-model="editForm.ruleName" />
      </el-form-item>
      <el-form-item label="适用设备类型">
        <el-select v-model="editForm.deviceType" style="width:100%">
          <el-option value="手环" label="手环" />
          <el-option value="床垫" label="床垫" />
          <el-option value="摄像头" label="摄像头" />
          <el-option value="紧急按钮" label="紧急按钮" />
        </el-select>
      </el-form-item>
      <el-form-item label="监测指标">
        <el-select v-model="editForm.metric" style="width:100%">
          <el-option value="心率" label="心率" />
          <el-option value="离床时长" label="离床时长" />
          <el-option value="活动量" label="活动量" />
          <el-option value="离线时长" label="离线时长" />
        </el-select>
      </el-form-item>
      <el-form-item label="触发条件">
        <el-select v-model="editForm.condition" style="width:100%">
          <el-option value="大于" label="大于" />
          <el-option value="小于" label="小于" />
          <el-option value="等于" label="等于" />
        </el-select>
      </el-form-item>
      <el-form-item label="阈值">
        <el-input-number v-model="editForm.threshold" :min="0" :max="999" />
      </el-form-item>
      <el-form-item label="报警级别">
        <el-select v-model="editForm.alarmLevel" style="width:100%">
          <el-option value="一般" label="一般报警" />
          <el-option value="紧急" label="紧急报警" />
        </el-select>
      </el-form-item>
      <el-form-item label="通知方式">
        <el-select v-model="editForm.notifyMethod" style="width:100%">
          <el-option value="系统通知" label="系统通知" />
          <el-option value="短信" label="短信" />
          <el-option value="系统通知+短信" label="系统通知+短信" />
        </el-select>
      </el-form-item>
      <el-form-item style="margin-left: 40%">
        <el-button type="primary" round @click="saveRule">保存</el-button>
        <el-button type="warning" round @click="dialogVisible = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script setup>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";

const dialogVisible = ref(false);
const editForm = reactive({
  id: '', ruleName: '', deviceType: '', metric: '', condition: '', threshold: 0, alarmLevel: '', notifyMethod: '', enabled: true
});

// Static demo rules since we use t_device fields only
const ruleList = ref([
  { id: 1, ruleName: '心率异常报警', deviceType: '手环', metric: '心率', condition: '大于', threshold: 120, alarmLevel: '紧急', notifyMethod: '系统通知+短信', enabled: true },
  { id: 2, ruleName: '离床超时报警', deviceType: '床垫', metric: '离床时长', condition: '大于', threshold: 120, alarmLevel: '一般', notifyMethod: '系统通知', enabled: true },
  { id: 3, ruleName: '设备离线报警', deviceType: '手环', metric: '离线时长', condition: '大于', threshold: 30, alarmLevel: '紧急', notifyMethod: '系统通知+短信', enabled: true },
  { id: 4, ruleName: '低活动量报警', deviceType: '手环', metric: '活动量', condition: '小于', threshold: 100, alarmLevel: '一般', notifyMethod: '系统通知', enabled: true },
  { id: 5, ruleName: '紧急按钮触发', deviceType: '紧急按钮', metric: '触发次数', condition: '大于', threshold: 0, alarmLevel: '紧急', notifyMethod: '系统通知+短信', enabled: true },
]);

function editRule(row) {
  Object.assign(editForm, row);
  dialogVisible.value = true;
}

function saveRule() {
  const idx = ruleList.value.findIndex(r => r.id === editForm.id);
  if (idx >= 0) {
    ruleList.value[idx] = { ...editForm };
  }
  dialogVisible.value = false;
  ElMessage('规则已更新');
}

function toggleRule(row) {
  ElMessage(row.enabled ? '规则已启用' : '规则已禁用');
}
</script>
