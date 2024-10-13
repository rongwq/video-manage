import request from '@/utils/request'

// 查询视频广告关联列表
export function listAd(query) {
  return request({
    url: '/system/video/ad/list',
    method: 'get',
    params: query
  })
}

// 查询视频广告关联详细
export function getAd(id) {
  return request({
    url: '/system/video/ad/' + id,
    method: 'get'
  })
}

// 新增视频广告关联
export function addAd(data) {
  return request({
    url: '/system/video/ad',
    method: 'post',
    data: data
  })
}

// 修改视频广告关联
export function updateAd(data) {
  return request({
    url: '/system/video/ad',
    method: 'put',
    data: data
  })
}

// 删除视频广告关联
export function delAd(id) {
  return request({
    url: '/system/video/ad/' + id,
    method: 'delete'
  })
}
