import request from '@/utils/request'

// 查询充值活动列表
export function listActivity(query) {
  return request({
    url: '/system/activity/list',
    method: 'get',
    params: query
  })
}

// 查询充值活动详细
export function getActivity(id) {
  return request({
    url: '/system/activity/' + id,
    method: 'get'
  })
}

// 新增充值活动
export function addActivity(data) {
  return request({
    url: '/system/activity',
    method: 'post',
    data: data
  })
}

// 修改充值活动
export function updateActivity(data) {
  return request({
    url: '/system/activity',
    method: 'put',
    data: data
  })
}

// 删除充值活动
export function delActivity(id) {
  return request({
    url: '/system/activity/' + id,
    method: 'delete'
  })
}
