import request from '@/utils/request'

// 查询充值卡列表
export function listKey(query) {
  return request({
    url: '/video/rechargeKey/list',
    method: 'get',
    params: query
  })
}

// 查询充值卡详细
export function getKey(id) {
  return request({
    url: '/video/rechargeKey/' + id,
    method: 'get'
  })
}

// 新增充值卡
export function addKey(data) {
  return request({
    url: '/video/rechargeKey/autoCreate?number='+data.createNumber+'&money='+data.money,
    method: 'post',
  })
}

// 修改充值卡
export function updateKey(data) {
  return request({
    url: '/video/rechargeKey',
    method: 'put',
    data: data
  })
}

// 删除充值卡
export function delKey(id) {
  return request({
    url: '/video/rechargeKey/' + id,
    method: 'delete'
  })
}
