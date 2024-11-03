import request from '@/utils/request'

// 查询激活码列表
export function listCdkey(query) {
  return request({
    url: '/system/cdkey/list',
    method: 'get',
    params: query
  })
}

// 查询激活码详细
export function getCdkey(cdkeyId) {
  return request({
    url: '/system/cdkey/' + cdkeyId,
    method: 'get'
  })
}

// 新增激活码
export function addCdkey(data) {
  return request({
    url: '/system/cdkey/autoCreate?number='+data.createNumber,
    method: 'post',
  })
}

// 修改激活码
export function updateCdkey(data) {
  return request({
    url: '/system/cdkey',
    method: 'put',
    data: data
  })
}

// 删除激活码
export function delCdkey(cdkeyId) {
  return request({
    url: '/system/cdkey/' + cdkeyId,
    method: 'delete'
  })
}
