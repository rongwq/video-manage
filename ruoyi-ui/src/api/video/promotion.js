import request from '@/utils/request'

// 查询运营活动列表
export function listPromotion(query) {
  return request({
    url: '/video/activity/list',
    method: 'get',
    params: query
  })
}

// 查询运营活动详细
export function getPromotion(id) {
  return request({
    url: '/video/activity/' + id,
    method: 'get'
  })
}

// 新增运营活动
export function addPromotion(data) {
  return request({
    url: '/video/activity',
    method: 'post',
    data: data
  })
}

// 修改运营活动
export function updatePromotion(data) {
  return request({
    url: '/video/activity',
    method: 'put',
    data: data
  })
}

// 删除运营活动
export function delPromotion(id) {
  return request({
    url: '/video/activity/' + id,
    method: 'delete'
  })
}
