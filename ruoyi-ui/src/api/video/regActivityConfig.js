import request from '@/utils/request'

// 查询注册活动配置列表
export function listRegActivityConfig(query) {
  return request({
    url: '/video/regActivityConfig/list',
    method: 'get',
    params: query
  })
}

// 查询注册活动配置详细
export function getRegActivityConfig(id) {
  return request({
    url: '/video/regActivityConfig/' + id,
    method: 'get'
  })
}

// 新增注册活动配置
export function addRegActivityConfig(data) {
  return request({
    url: '/video/regActivityConfig',
    method: 'post',
    data: data
  })
}

// 修改注册活动配置
export function updateRegActivityConfig(data) {
  return request({
    url: '/video/regActivityConfig',
    method: 'put',
    data: data
  })
}

// 删除注册活动配置
export function delRegActivityConfig(id) {
  return request({
    url: '/video/regActivityConfig/' + id,
    method: 'delete'
  })
}
