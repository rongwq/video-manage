import request from '@/utils/request'

// 查询用户注册广告请求记录列表
export function listAdRegRecord(query) {
  return request({
    url: '/video/adRegRecord/list',
    method: 'get',
    params: query
  })
}

// 查询用户注册广告请求记录详细
export function getAdRegRecord(id) {
  return request({
    url: '/video/adRegRecord/' + id,
    method: 'get'
  })
}

// 新增用户注册广告请求记录
export function addAdRegRecord(data) {
  return request({
    url: '/video/adRegRecord',
    method: 'post',
    data: data
  })
}

// 修改用户注册广告请求记录
export function updateAdRegRecord(data) {
  return request({
    url: '/video/adRegRecord',
    method: 'put',
    data: data
  })
}

// 删除用户注册广告请求记录
export function delAdRegRecord(id) {
  return request({
    url: '/video/adRegRecord/' + id,
    method: 'delete'
  })
}
