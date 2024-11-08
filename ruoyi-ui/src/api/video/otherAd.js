import request from '@/utils/request'

// 查询其它部位广告列表
export function listOther(query) {
  return request({
    url: '/video/otherAd/list',
    method: 'get',
    params: query
  })
}

// 查询其它部位广告详细
export function getOther(id) {
  return request({
    url: '/video/otherAd/' + id,
    method: 'get'
  })
}

// 新增其它部位广告
export function addOther(data) {
  return request({
    url: '/video/otherAd',
    method: 'post',
    data: data
  })
}

// 修改其它部位广告
export function updateOther(data) {
  return request({
    url: '/video/otherAd',
    method: 'put',
    data: data
  })
}

// 删除其它部位广告
export function delOther(id) {
  return request({
    url: '/video/otherAd/' + id,
    method: 'delete'
  })
}
