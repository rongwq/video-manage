import axios from 'axios'
import { Toast } from 'vant'
import { getToken } from '@/utils/auth'
import store from '@/store'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API,
  timeout: 30000
})

let isRelogin = { show: false }

service.interceptors.request.use(
  config => {
    const isToken = (config.headers || {}).isToken === false
    if (getToken() && !isToken) {
      config.headers['Authorization'] = 'Bearer ' + getToken()
    }
    if (config.method === 'get' && config.params) {
      let url = config.url + '?'
      for (const propName of Object.keys(config.params)) {
        const value = config.params[propName]
        const part = encodeURIComponent(propName) + '='
        if (value !== null && typeof value !== 'undefined') {
          if (typeof value === 'object') {
            for (const key of Object.keys(value)) {
              if (value[key] !== null && typeof value[key] !== 'undefined') {
                let params = propName + '[' + key + ']'
                let subPart = encodeURIComponent(params) + '='
                url += subPart + encodeURIComponent(value[key]) + '&'
              }
            }
          } else {
            url += part + encodeURIComponent(value) + '&'
          }
        }
      }
      url = url.slice(0, -1)
      config.params = {}
      config.url = url
    }
    return config
  },
  error => {
    console.log(error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  res => {
    const code = res.data.code || 200
    const msg = res.data.msg || '系统未知错误，请反馈给管理员'
    if (res.request.responseType === 'blob' || res.request.responseType === 'arraybuffer') {
      return res.data
    }
    if (code === 401) {
      if (!isRelogin.show) {
        isRelogin.show = true
        Toast.fail('登录状态已过期，请重新登录')
        setTimeout(() => {
          isRelogin.show = false
          store.dispatch('LogOut').then(() => {
            location.href = '/login'
          })
        }, 1500)
      }
      return Promise.reject('无效的会话，或者会话已过期，请重新登录。')
    } else if (code === 500) {
      Toast.fail(msg)
      return Promise.reject(new Error(msg))
    } else if (code !== 200) {
      Toast.fail(msg)
      return Promise.reject('error')
    } else {
      return res.data
    }
  },
  error => {
    console.log('err' + error)
    let { message } = error
    if (message === 'Network Error') {
      message = '后端接口连接异常'
    } else if (message.includes('timeout')) {
      message = '系统接口请求超时'
    } else if (message.includes('Request failed with status code')) {
      message = '系统接口' + message.substr(message.length - 3) + '异常'
    }
    Toast.fail(message)
    return Promise.reject(error)
  }
)

export default service
