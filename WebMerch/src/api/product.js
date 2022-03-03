import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/product/list',
    method: 'post',
    data
  })
}

export function init_add(params) {
  return request({
    url: '/product/init_add',
    method: 'get',
    params
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

export function getSysKindAttrs(params) {
  return request({
    url: '/product/getSysKindAttrs',
    method: 'get',
    params
  })
}

export function searchSpu(params) {
  return request({
    url: '/product/searchSpu',
    method: 'get',
    params
  })
}

export default {
  list: list,
  init_add: init_add,
  add: add,
  init_edit: init_edit,
  edit: edit,
  del: del,
  getSysKindAttrs: getSysKindAttrs,
  searchSpu: searchSpu
}
