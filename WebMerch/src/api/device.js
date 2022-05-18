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

export function init_booker_manage(params) {
  return request({
    url: '/device/init_booker_manage',
    method: 'get',
    params
  })
}

export function init_booker_baseinfo(params) {
  return request({
    url: '/device/init_booker_baseinfo',
    method: 'get',
    params
  })
}

export function init_booker_stock(params) {
  return request({
    url: '/device/init_booker_stock',
    method: 'get',
    params
  })
}

export function booker_stock(data) {
  return request({
    url: '/device/booker_stock',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/device/edit',
    method: 'post',
    data
  })
}

export default {
  init_bookers: init_bookers,
  bookers: bookers,
  init_booker_manage: init_booker_manage,
  init_booker_baseinfo: init_booker_baseinfo,
  init_booker_stock:init_booker_stock,
  booker_stock:booker_stock,
  edit: edit
}
