import request from '@/utils/request'

export function init_bookers(params) {
  return request({
    url: '/device/init_bookers',
    method: 'get',
    params
  })
}

export function bookers(data) {
  return request({
    url: '/device/bookers',
    method: 'post',
    data
  })
}

export default {
  init_bookers: init_bookers,
  bookers: bookers
}
