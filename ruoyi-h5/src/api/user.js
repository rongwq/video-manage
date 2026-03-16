import request from '@/utils/request'

/**
 * APP用户登录
 * @param {Object} data 登录数据
 * @returns {Promise}
 */
export function appLogin(data) {
  return request({
    url: '/app/api/user/login',
    method: 'post',
    data
  })
}

/**
 * APP用户注册
 * @param {Object} data 注册数据
 * @returns {Promise}
 */
export function appRegister(data) {
  return request({
    url: '/app/api/user/register',
    method: 'post',
    data
  })
}

/**
 * 获取APP用户信息
 * @returns {Promise}
 */
export function getAppUserInfo() {
  return request({
    url: '/app/api/user/info',
    method: 'get'
  })
}

/**
 * 更新APP用户信息
 * @param {Object} data 用户数据
 * @returns {Promise}
 */
export function updateAppUserInfo(data) {
  return request({
    url: '/app/api/user/info',
    method: 'put',
    data
  })
}

/**
 * APP用户退出登录
 * @returns {Promise}
 */
export function appLogout() {
  return request({
    url: '/app/api/user/logout',
    method: 'post'
  })
}
