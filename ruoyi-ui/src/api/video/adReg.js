import request from '@/utils/request'

// 查询注册广告列表
export function listReg(query) {
  return request({
    url: '/system/reg/list',
    method: 'get',
    params: query
  })
}

// 查询注册广告详细
export function getReg(id) {
  return request({
    url: '/system/reg/' + id,
    method: 'get'
  })
}

// 新增注册广告
export function addReg(data) {
  return request({
    url: '/system/reg',
    method: 'post',
    data: data
  })
}

// 修改注册广告
export function updateReg(data) {
  return request({
    url: '/system/reg',
    method: 'put',
    data: data
  })
}

// 删除注册广告
export function delReg(id) {
  return request({
    url: '/system/reg/' + id,
    method: 'delete'
  })
}
