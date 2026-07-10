import request from './request'

// ========== 登录相关 ==========
export function login(data) {
  return request.post('/login', data)
}

export function logout() {
  return request.post('/logout')
}

export function loadInfo() {
  return request.get('/loadInfo')
}

export function showInfo() {
  return request.get('/showInfo')
}

export function updateUser(data) {
  return request.post('/updateUser', data)
}

export function updatePwd(data) {
  return request.post('/updatePwd', data)
}

// ========== 菜单 ==========
export function getMenus() {
  return request.get('/sysMenus')
}

// ========== 文件上传 ==========
export function uploadFile(formData) {
  return request.post('/upload', formData, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}

// ========== 用户管理 ==========
export function getUserPage(params) {
  return request.post('/users/page', params)
}

export function addUser(data) {
  return request.post('/users', data)
}

export function updateUserInfo(data) {
  return request.put('/users', data)
}

export function updateUserStatus(id, status) {
  return request.post(`/users/${id}/status`, { status })
}

export function resetPassword(id) {
  return request.post(`/users/${id}/resetPassword`)
}

export function assignRoles(id, roleIds) {
  return request.post(`/users/${id}/roles`, { roleIds })
}

// ========== 角色管理 ==========
export function getRolePage(params) {
  return request.post('/roles/page', params)
}

export function getRoleList() {
  return request.get('/roles/list')
}

export function addRole(data) {
  return request.post('/roles', data)
}

export function updateRole(data) {
  return request.put('/roles', data)
}

export function deleteRole(id) {
  return request.delete(`/roles/${id}`)
}

export function updateRoleMenus(id, menuIds) {
  return request.post(`/roles/${id}/menus`, { menuIds })
}

export function updateRoleDataScope(id, dataScope) {
  return request.post(`/roles/${id}/dataScope`, { dataScope })
}

// ========== 菜单管理 ==========
export function getMenuTree() {
  return request.get('/menus/tree')
}

export function addMenu(data) {
  return request.post('/menus', data)
}

export function updateMenu(data) {
  return request.put('/menus', data)
}

export function deleteMenu(id) {
  return request.delete(`/menus/${id}`)
}

export function updateMenuStatus(id, visible) {
  return request.post(`/menus/${id}/status`, { visible })
}

// ========== 部门管理 ==========
export function getDepartmentTree() {
  return request.get('/departments/tree')
}

export function addDepartment(data) {
  return request.post('/departments', data)
}

export function updateDepartment(data) {
  return request.put('/departments', data)
}

export function deleteDepartment(id) {
  return request.delete(`/departments/${id}`)
}

// ========== 职位管理 ==========
export function getPostPage(params) {
  return request.post('/posts/page', params)
}

export function getPostList() {
  return request.get('/posts/list')
}

export function addPost(data) {
  return request.post('/posts', data)
}

export function updatePost(data) {
  return request.put('/posts', data)
}

export function deletePost(id) {
  return request.delete(`/posts/${id}`)
}

export function updatePostStatus(id, status) {
  return request.post(`/posts/${id}/status`, { status })
}

// ========== 消息管理 ==========
export function getMessagePage(params) {
  return request.post('/messages/page', params)
}

export function getMessageDetail(id) {
  return request.get(`/messages/${id}`)
}

export function readMessage(id) {
  return request.post(`/messages/${id}/read`)
}

export function readMessageBatch(ids) {
  return request.post('/messages/readBatch', { ids })
}

export function deleteMessage(id) {
  return request.delete(`/messages/${id}`)
}

// ========== 工作台 ==========
export function getDashboardSummary() {
  return request.get('/dashboard/summary')
}

export function getDashboardTodo() {
  return request.get('/dashboard/todo')
}

export function getDashboardAppointments() {
  return request.get('/dashboard/appointments')
}

export function getDashboardElderStats() {
  return request.get('/dashboard/elderStats')
}

export function getDashboardRevenueStats(params) {
  return request.get('/dashboard/revenueStats', { params })
}
