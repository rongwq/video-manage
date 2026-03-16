import request from '@/utils/request'
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询APP用户列表
export function listAppUser(query) {
  return request({
    url: '/app/user/list',
    method: 'get',
    params: query
  })
}

// 查询APP用户详细
export function getAppUser(userId) {
  return request({
    url: '/app/user/' + parseStrEmpty(userId),
    method: 'get'
  })
}

// 新增APP用户
export function addAppUser(data) {
  return request({
    url: '/app/user',
    method: 'post',
    data: data
  })
}

// 修改APP用户
export function updateAppUser(data) {
  return request({
    url: '/app/user',
    method: 'put',
    data: data
  })
}

// 删除APP用户
export function delAppUser(userId) {
  return request({
    url: '/app/user/' + userId,
    method: 'delete'
  })
}

// APP用户密码重置
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

// APP用户状态修改
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
