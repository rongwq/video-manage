import request from '@/utils/request'

export function listAppUser(query) {
  return request({
    url: '/app/user/list',
    method: 'get',
    params: query
  })
}

export function getAppUser(userId) {
  return request({
    url: '/app/user/' + userId,
    method: 'get'
  })
}

export function addAppUser(data) {
  return request({
    url: '/app/user',
    method: 'post',
    data: data
  })
}

export function updateAppUser(data) {
  return request({
    url: '/app/user',
    method: 'put',
    data: data
  })
}

export function delAppUser(userId) {
  return request({
    url: '/app/user/' + userId,
    method: 'delete'
  })
}

export function resetAppUserPwd(userId, password) {
  const data = {
    userId,
    password
  }
  return request({
    url: '/app/user/resetPwd',
    method: 'put',
    data: data
  })
}

export function changeAppUserStatus(userId, status) {
  const data = {
    userId,
    status
  }
  return request({
    url: '/app/user/changeStatus',
    method: 'put',
    data: data
  })
}
