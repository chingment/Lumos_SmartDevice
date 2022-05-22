import request from '@/utils/request'

export function init_list(params) {
  return request({
    url: '/booker/init_list',
    method: 'get',
    params
  })
}

export function list(data) {
  return request({
    url: '/booker/list',
    method: 'post',
    data
  })
}

export function init_manage(params) {
  return request({
    url: '/booker/init_manage',
    method: 'get',
    params
  })
}

export function init_baseinfo(params) {
  return request({
    url: '/booker/init_baseinfo',
    method: 'get',
    params
  })
}

export function init_stock(params) {
  return request({
    url: '/booker/init_stock',
    method: 'get',
    params
  })
}

export function stock(data) {
  return request({
    url: '/booker/stock',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/booker/edit',
    method: 'post',
    data
  })
}

export function updateApp(data) {
  return request({
    url: '/booker/updateApp',
    method: 'post',
    data
  })
}

export function rebootSys(data) {
  return request({
    url: '/booker/rebootSys',
    method: 'post',
    data
  })
}

export function shutdownSys(data) {
  return request({
    url: '/booker/shutdownSys',
    method: 'post',
    data
  })
}

export default {
  init_list: init_list,
  list: list,
  init_manage: init_manage,
  init_baseinfo: init_baseinfo,
  init_stock: init_stock,
  stock: stock,
  edit: edit,
  updateApp: updateApp,
  rebootSys: rebootSys,
  shutdownSys: shutdownSys
}
