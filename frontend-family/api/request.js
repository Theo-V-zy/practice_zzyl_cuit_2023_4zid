const DEFAULT_BASE_URL = 'http://127.0.0.1:8080'

export function getBaseUrl() {
  return uni.getStorageSync('familyApiBaseUrl') || DEFAULT_BASE_URL
}

export function getToken() {
  return uni.getStorageSync('familyToken') || ''
}

export function setSession(data) {
  uni.setStorageSync('familyToken', data.token)
  uni.setStorageSync('familyUser', data)
  getApp().globalData.token = data.token
  getApp().globalData.userInfo = data
}

export function clearSession() {
  uni.removeStorageSync('familyToken')
  uni.removeStorageSync('familyUser')
  getApp().globalData.token = ''
  getApp().globalData.userInfo = null
}

export function request(options) {
  return new Promise((resolve, reject) => {
    uni.request({
      url: getBaseUrl() + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      timeout: 15000,
      header: {
        'Content-Type': 'application/json',
        'X-Family-Token': getToken()
      },
      success(res) {
        const data = res.data || {}
        if (res.statusCode === 401 || data.code === 401) {
          clearSession()
          uni.showToast({ title: '登录已过期', icon: 'none' })
          setTimeout(() => uni.reLaunch({ url: '/pages/login/login' }), 300)
          reject(new Error('登录已过期'))
          return
        }
        if (res.statusCode >= 200 && res.statusCode < 300 && data.code === 200) {
          resolve(data)
          return
        }
        const message = data.msg || data.message || '请求失败'
        uni.showToast({ title: message, icon: 'none' })
        reject(new Error(message))
      },
      fail(error) {
        uni.showToast({ title: '无法连接服务器', icon: 'none' })
        reject(error)
      }
    })
  })
}

export const familyLogin = data => request({ url: '/family/login', method: 'POST', data })
export const familyLogout = () => request({ url: '/family/logout', method: 'POST' })
export const familyProfile = () => request({ url: '/family/profile' })
export const updateFamilyProfile = data => request({ url: '/family/profile', method: 'PUT', data })
export const familyHome = () => request({ url: '/family/home' })
export const familyMine = () => request({ url: '/family/mine' })
export const familyElders = () => request({ url: '/family/elders' })
export const familyElderDetail = id => request({ url: `/family/elders/${id}` })
export const bindFamilyElder = data => request({ url: '/family/elders/bind', method: 'POST', data })
export const unbindFamilyElder = id => request({ url: `/family/elders/${id}`, method: 'DELETE' })
export const familyServices = keyword => request({ url: `/family/services?keyword=${encodeURIComponent(keyword || '')}` })
export const familyServiceDetail = id => request({ url: `/family/services/${id}` })
export const createFamilyOrder = data => request({ url: '/family/orders', method: 'POST', data })
export const familyOrders = data => request({ url: '/family/orders/page', method: 'POST', data })
export const familyOrderDetail = id => request({ url: `/family/orders/${id}` })
export const payFamilyOrder = id => request({ url: `/family/orders/${id}/pay`, method: 'POST' })
export const cancelFamilyOrder = (id, reason) => request({ url: `/family/orders/${id}/cancel`, method: 'POST', data: { reason } })
export const refundFamilyOrder = (id, reason) => request({ url: `/family/orders/${id}/refund`, method: 'POST', data: { reason } })
export const deleteFamilyOrder = id => request({ url: `/family/orders/${id}`, method: 'DELETE' })
export const familyAppointments = () => request({ url: '/family/appointments' })
export const createFamilyAppointment = data => request({ url: '/family/appointments', method: 'POST', data })
export const cancelFamilyAppointment = id => request({ url: `/family/appointments/${id}/cancel`, method: 'POST' })
export const familyContracts = () => request({ url: '/family/contracts' })
export const familyBills = () => request({ url: '/family/bills' })
