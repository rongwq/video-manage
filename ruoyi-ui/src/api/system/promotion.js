import request from '@/utils/request'

// 查询活动管理列表
export function listPromotion(query) {
  return request({
    url: '/system/promotion/list',
    method: 'get',
    params: query
  })
}

// 查询活动管理详细
export function getPromotion(id) {
  return request({
    url: '/system/promotion/' + id,
    method: 'get'
  })
}

// 新增活动管理
export function addPromotion(data) {
  return request({
    url: '/system/promotion',
    method: 'post',
    data: data
  })
}

// 修改活动管理
export function updatePromotion(data) {
  return request({
    url: '/system/promotion',
    method: 'put',
    data: data
  })
}

// 删除活动管理
export function delPromotion(id) {
  return request({
    url: '/system/promotion/' + id,
    method: 'delete'
  })
}
