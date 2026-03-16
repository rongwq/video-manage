import request from '@/utils/request'

/**
 * APP用户登录
 * @param {string} username 用户名
 * @param {string} password 密码
 * @returns {Promise}
 */
export function appUserLogin(username, password) {
  const data = {
    username,
    password
  }
  return request({
    url: '/app/api/user/login',
    method: 'post',
    data: data
  })
}

/**
 * APP用户注册
 * @param {Object} data 注册数据
 * @returns {Promise}
 */
export function appUserRegister(data) {
  return request({
    url: '/app/api/user/register',
    method: 'post',
    data: data
  })
}

/**
 * 获取当前登录APP用户信息
 * @returns {Promise}
 */
export function getAppUserInfo() {
  return request({
    url: '/app/api/user/info',
    method: 'get'
  })
}

/**
 * 根据用户ID获取APP用户信息
 * @param {number} userId 用户ID
 * @returns {Promise}
 */
export function getAppUserById(userId) {
  return request({
    url: '/app/api/user/' + userId,
    method: 'get'
  })
}

/**
 * 根据用户名获取APP用户信息
 * @param {string} userName 用户名
 * @returns {Promise}
 */
export function getAppUserByName(userName) {
  return request({
    url: '/app/api/user/username/' + userName,
    method: 'get'
  })
}

/**
 * 修改APP用户信息
 * @param {Object} data 用户数据
 * @returns {Promise}
 */
export function updateAppUser(data) {
  return request({
    url: '/app/api/user/update',
    method: 'put',
    data: data
  })
}

/**
 * 修改APP用户密码
 * @param {string} oldPassword 旧密码
 * @param {string} newPassword 新密码
 * @returns {Promise}
 */
export function updateAppUserPwd(oldPassword, newPassword) {
  return request({
    url: '/app/api/user/updatePwd',
    method: 'put',
    params: {
      oldPassword,
      newPassword
    }
  })
}

/**
 * 获取APP用户列表
 * @param {Object} params 查询参数
 * @returns {Promise}
 */
export function listAppUser(params) {
  return request({
    url: '/app/api/user/list',
    method: 'get',
    params: params
  })
}
