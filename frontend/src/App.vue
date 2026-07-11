<template>
  <router-view />
</template>

<style>
:root {
  color: #303133;
  background: #f4f6f8;
  font-family: "Microsoft YaHei", "PingFang SC", Arial, sans-serif;
  font-size: 14px;
}

* {
  box-sizing: border-box;
}

html,
body,
#app {
  width: 100%;
  min-width: 1024px;
  min-height: 100%;
  height: 100%;
  margin: 0;
}

body {
  background: #f4f6f8;
}

button,
input,
select,
textarea {
  font: inherit;
}

.page-section {
  min-width: 0;
  min-height: 100%;
  padding: 20px;
  background: #fff;
  border: 1px solid #e4e7ed;
}

.page-section .el-form--inline .el-form-item {
  margin-right: 18px;
  margin-bottom: 14px;
}

.page-section .el-table {
  --el-table-header-bg-color: #f5f6f8;
  --el-table-header-text-color: #676d76;
  --el-table-row-hover-bg-color: #f7faff;
  color: #555b65;
  font-size: 12px;
}

.page-section .el-table th.el-table__cell,
.page-section .el-table td.el-table__cell {
  padding: 8px 0;
}

.page-section .el-button {
  border-radius: 2px;
}

.page-section .el-input__wrapper,
.page-section .el-select__wrapper,
.page-section .el-textarea__inner {
  border-radius: 2px;
}

.pagination-wrap {
  display: flex;
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
