import request from '@/utils/request'

export function add(data) {
  return request({
    url: '/product/add',
    method: 'post',
    data
  })
}

export default {
  add: add
}
