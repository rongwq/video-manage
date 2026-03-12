import request from '@/utils/request'

export function getVideoInfo(id, userId) {
  return request({
    url: '/api/video/' + id,
    method: 'get',
    params: { userId }
  })
}

export function savePlay(videoId) {
  return request({
    url: '/api/video/savePlay',
    method: 'post',
    params: { videoId }
  })
}

export function saveLike(videoId) {
  return request({
    url: '/api/video/saveLike',
    method: 'post',
    params: { videoId }
  })
}

export function cancelLike(videoId) {
  return request({
    url: '/api/video/cancelLike',
    method: 'post',
    params: { videoId }
  })
}

export function saveCollect(videoId) {
  return request({
    url: '/api/video/saveCollect',
    method: 'post',
    params: { videoId }
  })
}

export function cancelCollect(videoId) {
  return request({
    url: '/api/video/cancelCollect',
    method: 'post',
    params: { videoId }
  })
}

export function buyVideo(videoId) {
  return request({
    url: '/api/video/buyVideo',
    method: 'post',
    params: { videoId }
  })
}

export function getVideoList(query) {
  return request({
    url: '/api/video/list',
    method: 'get',
    params: query
  })
}

export function getHomeRecommend() {
  return request({
    url: '/api/video/list/home/recommend',
    method: 'get'
  })
}

export function getCategoryList() {
  return request({
    url: '/api/video/categoryList',
    method: 'get'
  })
}
