import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/shop/list',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/shop/add',
    method: 'post',
    data
  })
}

export default {
  list: list,
  add: add
}
