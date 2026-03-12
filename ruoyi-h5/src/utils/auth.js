import Cookies from 'js-cookie'

const TokenKey = 'Admin-Token'

/**
 * 获取token
 * @returns {string}
 */
export function getToken() {
  return Cookies.get(TokenKey)
}

/**
 * 设置token
 * @param {string} token
 * @returns {string}
 */
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

/**
 * 移除token
 * @returns {void}
 */
export function removeToken() {
  return Cookies.remove(TokenKey)
}
