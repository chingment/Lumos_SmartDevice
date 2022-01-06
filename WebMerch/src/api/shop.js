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

export function init_edit(params) {
  return request({
    url: '/shop/init_edit',
    method: 'get',
    params
  })
}

export function edit(data) {
  return request({
    url: '/shop/edit',
    method: 'post',
    data
  })
}

export default {
  list: list,
  add: add,
  init_edit: init_edit,
  edit: edit
}
