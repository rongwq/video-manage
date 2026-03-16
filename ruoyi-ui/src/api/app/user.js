import request from '@/utils/request'

// 查询APP用户列表
export function listAppUser(query) {
  return request({
    url: '/app/manage/user/list',
    method: 'get',
    params: query
  })
}

// 查询APP用户详细
export function getAppUser(userId) {
  return request({
    url: '/app/manage/user/' + userId,
    method: 'get'
  })
}

// 新增APP用户
export function addAppUser(data) {
  return request({
    url: '/app/manage/user',
    method: 'post',
    data: data
  })
}

// 修改APP用户
export function updateAppUser(data) {
  return request({
    url: '/app/manage/user',
    method: 'put',
    data: data
  })
}

// 删除APP用户
export function delAppUser(userId) {
  return request({
    url: '/app/manage/user/' + userId,
    method: 'delete'
  })
}

// 导出APP用户
export function exportAppUser(query) {
  return request({
    url: '/app/manage/user/export',
    method: 'post',
    data: query,
    responseType: 'blob'
  })
}

// 重置APP用户密码
export function resetAppUserPwd(data) {
  return request({
    url: '/app/manage/user/resetPwd',
    method: 'put',
    data: data
  })
}

// 修改APP用户状态
export function changeAppUserStatus(data) {
  return request({
    url: '/app/manage/user/changeStatus',
    method: 'put',
    data: data
  })
}

// 校验手机号是否唯一
export function checkPhoneUnique(data) {
  return request({
    url: '/app/manage/user/checkPhoneUnique',
    method: 'get',
    params: data
  })
}

// 校验用户名是否唯一
export function checkUserNameUnique(data) {
  return request({
    url: '/app/manage/user/checkUserNameUnique',
    method: 'get',
    params: data
  })
}
