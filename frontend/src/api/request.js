import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

const request = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || 'http://localhost:8080',
  timeout: 15000,
  withCredentials: true
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    // 家属端请求标记
    const familyToken = localStorage.getItem('familyToken')
    if (familyToken) {
      config.headers['X-Family-Token'] = familyToken
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (response) => {
    const data = response.data
    // 如果返回的是 blob 类型（文件下载等），直接返回
    if (response.config.responseType === 'blob') {
      return response
    }
    // 业务状态码判断
    if (data && data.code === 200) {
      return data
    }
    // Session 失效或未登录
    if (data && (data.code === 401 || data.code === 403)) {
      const isFamily = localStorage.getItem('familyToken')
      if (isFamily) {
        localStorage.removeItem('familyToken')
        localStorage.removeItem('familyUser')
        router.replace('/family/login')
      } else {
        localStorage.clear()
        sessionStorage.clear()
        router.replace('/')
      }
      ElMessage.error(data.msg || '登录已过期，请重新登录')
      return Promise.reject(new Error(data.msg || '登录已过期'))
    }
    // 业务错误
    if (data && data.code === 400) {
      ElMessage.warning(data.msg || '操作失败')
      return Promise.reject(new Error(data.msg || '操作失败'))
    }
    return data
  },
  (error) => {
    // 网络错误
    if (error.message && error.message.includes('timeout')) {
      ElMessage.error('请求超时，请检查网络')
    } else if (error.response) {
      const status = error.response.status
      if (status === 401 || status === 403) {
        const isFamily = localStorage.getItem('familyToken')
        if (isFamily) {
          localStorage.removeItem('familyToken')
          localStorage.removeItem('familyUser')
          router.replace('/family/login')
        } else {
          localStorage.clear()
          sessionStorage.clear()
          router.replace('/')
        }
        ElMessage.error('登录已过期，请重新登录')
      } else if (status >= 500) {
        ElMessage.error('服务器异常，请稍后重试')
      }
    } else {
      ElMessage.error('网络连接异常')
    }
    return Promise.reject(error)
  }
)

export default request
