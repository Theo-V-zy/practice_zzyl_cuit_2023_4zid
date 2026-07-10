import request from './request'

// ========== 家属登录 ==========
export function familyLogin(data) {
  return request.post('/family/login', data)
}

export function familyLogout() {
  return request.post('/family/logout')
}

export function familyProfile() {
  return request.get('/family/profile')
}

// ========== 家属端"我的" ==========
export function familyMine() {
  return request.get('/family/mine')
}

export function familyContracts(params) {
  return request.post('/family/contracts', params)
}

export function familyAppointments(params) {
  return request.post('/family/appointments', params)
}

export function familyOrders(params) {
  return request.post('/family/orders', params)
}

export function familyOrderDetail(id) {
  return request.get(`/family/orders/${id}`)
}

export function familyBills(params) {
  return request.post('/family/bills', params)
}
