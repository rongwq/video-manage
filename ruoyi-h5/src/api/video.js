import request from '@/utils/request'

/**
 * 查询视频详细
 * @param {number} id 视频ID
 * @returns {Promise}
 */
export function getVideo(id) {
  return request({
    url: '/system/video/' + id,
    method: 'get'
  })
}

/**
 * 查询视频列表
 * @param {Object} query 查询参数
 * @returns {Promise}
 */
export function listVideo(query) {
  return request({
    url: '/system/video/list',
    method: 'get',
    params: query
  })
}

/**
 * 新增视频
 * @param {Object} data 视频数据
 * @returns {Promise}
 */
export function addVideo(data) {
  return request({
    url: '/system/video',
    method: 'post',
    data: data
  })
}

/**
 * 修改视频
 * @param {Object} data 视频数据
 * @returns {Promise}
 */
export function updateVideo(data) {
  return request({
    url: '/system/video',
    method: 'put',
    data: data
  })
}

/**
 * 删除视频
 * @param {number} id 视频ID
 * @returns {Promise}
 */
export function delVideo(id) {
  return request({
    url: '/system/video/' + id,
    method: 'delete'
  })
}
