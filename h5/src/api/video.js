import request from '@/utils/request'

export function getRecommendList() {
  return request({
    url: '/video/list/recommend',
    method: 'get'
  })
}

export function getVideoInfo(id) {
  return request({
    url: '/video/' + id,
    method: 'get'
  })
}
