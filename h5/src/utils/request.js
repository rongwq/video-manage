import axios from 'axios'
import { Toast } from 'vant'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
const service = axios.create({
  baseURL: '/api',
  timeout: 10000
})

service.interceptors.request.use(config => {
  return config
}, error => {
  console.log(error)
  Promise.reject(error)
})

service.interceptors.response.use(res => {
  const code = res.data.code || 200
  const msg = res.data.msg || '未知错误'
  if (code === 401) {
    Toast.fail('登录状态已过期')
    return Promise.reject('无效的会话')
  } else if (code === 500) {
    Toast.fail(msg)
    return Promise.reject(new Error(msg))
  } else if (code !== 200) {
    Toast.fail(msg)
    return Promise.reject('error')
  } else {
    return res.data
  }
}, error => {
  console.log('err' + error)
  let { message } = error
  if (message == 'Network Error') {
    message = '后端接口连接异常'
  } else if (message.includes('timeout')) {
    message = '系统接口请求超时'
  } else if (message.includes('Request failed with status code')) {
    message = '系统接口' + message.substr(message.length - 3) + '异常'
  }
  Toast.fail(message)
  return Promise.reject(error)
})

export default service
