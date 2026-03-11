import request from '@/utils/request'

// 查询注册送积分活动配置列表
export function listRegisterActivity(query) {
  return request({
    url: '/video/registerActivity/list',
    method: 'get',
    params: query
  })
}

// 查询注册送积分活动配置详细
export function getRegisterActivity(id) {
  return request({
    url: '/video/registerActivity/' + id,
    method: 'get'
  })
}

// 新增注册送积分活动配置
export function addRegisterActivity(data) {
  return request({
    url: '/video/registerActivity',
    method: 'post',
    data: data
  })
}

// 修改注册送积分活动配置
export function updateRegisterActivity(data) {
  return request({
    url: '/video/registerActivity',
    method: 'put',
    data: data
  })
}

// 删除注册送积分活动配置
export function delRegisterActivity(ids) {
  return request({
    url: '/video/registerActivity/' + ids,
    method: 'delete'
  })
}

// 查询当前有效的活动配置
export function getCurrentActivity() {
  return request({
    url: '/video/registerActivity/current',
    method: 'get'
  })
}
