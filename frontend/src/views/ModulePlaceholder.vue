<template>
  <section class="module-placeholder">
    <div class="placeholder-icon"><el-icon :size="28"><SetUp /></el-icon></div>
    <h2>{{ route.meta.title }}</h2>
    <p>页面骨架已接入公共布局，等待对应负责人开发。</p>
  </section>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { SetUp } from '@element-plus/icons-vue'

const route = useRoute()
</script>

<style scoped>
.module-placeholder {
  display: flex;
  min-height: 360px;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
  text-align: center;
}

.placeholder-icon {
  display: grid;
  place-items: center;
  width: 56px;
  height: 56px;
  color: #2f8f83;
  background: #e8f5f2;
  border-radius: 50%;
}

h2 {
  margin: 18px 0 8px;
  color: #303133;
  font-size: 20px;
}

p {
  margin: 0;
}
</style>
