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

export function edit(data) {
  return request({
    url: '/product/edit',
    method: 'post',
    data
  })
}

export function del(data) {
  return request({
    url: '/product/delete',
    method: 'post',
    data
  })
}

export function init_edit(params) {
  return request({
    url: '/product/init_edit',
    method: 'get',
    params
  })
}

export default {
  list: list,
  add: add,
  init_edit: init_edit,
  edit: edit,
  del: del
}
