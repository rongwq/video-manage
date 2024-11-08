import request from '@/utils/request'

// 查询首页广告列表
export function listHome(query) {
  return request({
    url: '/video/homeAd/list',
    method: 'get',
    params: query
  })
}

// 查询首页广告详细
export function getHome(id) {
  return request({
    url: '/video/homeAd/' + id,
    method: 'get'
  })
}

// 新增首页广告
export function addHome(data) {
  return request({
    url: '/video/homeAd',
    method: 'post',
    data: data
  })
}

// 修改首页广告
export function updateHome(data) {
  return request({
    url: '/video/homeAd',
    method: 'put',
    data: data
  })
}

// 删除首页广告
export function delHome(id) {
  return request({
    url: '/video/homeAd/' + id,
    method: 'delete'
  })
}
