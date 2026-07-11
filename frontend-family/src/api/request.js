// 家属端小程序 API 请求封装
const BASE_URL = 'http://localhost:8080'

export function request(options) {
  return new Promise((resolve, reject) => {
    const token = getApp().globalData.token
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'X-Family-Token': token || ''
      },
      success(res) {
        const data = res.data
        if (res.statusCode === 200 && data.code === 200) {
          resolve(data)
        } else if (data.code === 401) {
          getApp().globalData.token = ''
          uni.reLaunch({ url: '/pages/login/login' })
          uni.showToast({ title: '登录已过期', icon: 'none' })
          reject(new Error('登录已过期'))
        } else {
          uni.showToast({ title: data.msg || '请求失败', icon: 'none' })
          reject(new Error(data.msg || '请求失败'))
        }
      },
      fail(err) {
        uni.showToast({ title: '网络异常', icon: 'none' })
        reject(err)
      }
    })
  })
}

// 家属登录
export function familyLogin(data) {
  return request({ url: '/family/login', method: 'POST', data })
}

// 家属退出
export function familyLogout() {
  return request({ url: '/family/logout', method: 'POST' })
}

// 家属信息
export function familyProfile() {
  return request({ url: '/family/profile' })
}

// 我的首页
export function familyMine() {
  return request({ url: '/family/mine' })
}

// 我的合同
export function familyContracts(params) {
  return request({ url: '/family/contracts', method: 'POST', data: params })
}

// 我的预约
export function familyAppointments(params) {
  return request({ url: '/family/appointments', method: 'POST', data: params })
}

// 我的订单
export function familyOrders(params) {
  return request({ url: '/family/orders', method: 'POST', data: params })
}

// 订单详情
export function familyOrderDetail(id) {
  return request({ url: `/family/orders/${id}` })
}

// 我的账单
export function familyBills(params) {
  return request({ url: '/family/bills', method: 'POST', data: params })
}
