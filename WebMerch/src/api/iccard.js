import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/iccard/list',
    method: 'post',
    data
  })
}

export function add(data) {
  return request({
    url: '/iccard/add',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/iccard/edit',
    method: 'post',
    data
  })
}

export function init_add(params) {
  return request({
    url: '/iccard/init_add',
    method: 'get',
    params
  })
}

export function init_edit(params) {
  return request({
    url: '/iccard/init_edit',
    method: 'get',
    params
  })
}

export default {
  list: list,
  init_add: init_add,
  add: add,
  init_edit: init_edit,
  edit: edit
}
