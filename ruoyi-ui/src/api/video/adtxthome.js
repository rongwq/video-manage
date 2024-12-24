import request from '@/utils/request'

// 查询首页文字广告列表
export function listAdtxthome(query) {
  return request({
    url: '/system/adtxthome/list',
    method: 'get',
    params: query
  })
}

// 查询首页文字广告详细
export function getAdtxthome(id) {
  return request({
    url: '/system/adtxthome/' + id,
    method: 'get'
  })
}

// 新增首页文字广告
export function addAdtxthome(data) {
  return request({
    url: '/system/adtxthome',
    method: 'post',
    data: data
  })
}

// 修改首页文字广告
export function updateAdtxthome(data) {
  return request({
    url: '/system/adtxthome',
    method: 'put',
    data: data
  })
}

// 删除首页文字广告
export function delAdtxthome(id) {
  return request({
    url: '/system/adtxthome/' + id,
    method: 'delete'
  })
}
