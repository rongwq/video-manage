import request from '@/utils/request'

// 查询广告扫描记录列表
export function listAdUseRecord(query) {
  return request({
    url: '/video/adUseRecord/list',
    method: 'get',
    params: query
  })
}

// 查询广告扫描记录详细
export function getAdUseRecord(id) {
  return request({
    url: '/video/adUseRecord/' + id,
    method: 'get'
  })
}

// 新增广告扫描记录
export function addAdUseRecord(data) {
  return request({
    url: '/video/adUseRecord',
    method: 'post',
    data: data
  })
}

// 修改广告扫描记录
export function updateAdUseRecord(data) {
  return request({
    url: '/video/adUseRecord',
    method: 'put',
    data: data
  })
}

// 删除广告扫描记录
export function delAdUseRecord(id) {
  return request({
    url: '/video/adUseRecord/' + id,
    method: 'delete'
  })
}
