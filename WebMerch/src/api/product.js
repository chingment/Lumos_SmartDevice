import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/product/list',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/product/add',
    method: 'post',
    data
  })
}

export default {
  list: list,
  add: add
}
