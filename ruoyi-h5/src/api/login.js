import request from '@/utils/request'

/**
 * 用户登录
 * @param {string} username 用户名
 * @param {string} password 密码
 * @returns {Promise}
 */
export function login(username, password) {
  const data = {
    username,
    password
  }
  return request({
    url: '/login',
    method: 'post',
    data: data
  })
}

/**
 * 获取用户信息
 * @returns {Promise}
 */
export function getInfo() {
  return request({
    url: '/getInfo',
    method: 'get'
  })
}

/**
 * 退出登录
 * @returns {Promise}
 */
export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

/**
 * 获取验证码
 * @returns {Promise}
 */
export function getCodeImg() {
  return request({
    url: '/captchaImage',
    method: 'get',
    timeout: 20000
  })
}
