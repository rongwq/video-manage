import request from '@/utils/request'

// 查询用户收藏记录列表
export function listRecord(query) {
  return request({
    url: '/video/user/collect/record/list',
    method: 'get',
    params: query
  })
}

// 查询用户收藏记录详细
export function getRecord(id) {
  return request({
    url: '/video/user/collect/record/' + id,
    method: 'get'
  })
}

// 新增用户收藏记录
export function addRecord(data) {
  return request({
    url: '/video/user/collect/record',
    method: 'post',
    data: data
  })
}

// 修改用户收藏记录
export function updateRecord(data) {
  return request({
    url: '/video/user/collect/record',
    method: 'put',
    data: data
  })
}

// 删除用户收藏记录
export function delRecord(id) {
  return request({
    url: '/video/user/collect/record/' + id,
    method: 'delete'
  })
}
