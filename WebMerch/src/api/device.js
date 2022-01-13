import request from '@/utils/request'

export function bookers(data) {
  return request({
    url: '/device/bookers',
    method: 'post',
    data
  })
}

export default {
  bookers: bookers
}
