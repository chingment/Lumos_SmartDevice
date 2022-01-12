import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/shop/list',
    method: 'post',
    data
  })
}

export function details(params) {
  return request({
    url: '/shop/details',
    method: 'get',
    params
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

export function devices(data) {
  return request({
    url: '/shop/devices',
    method: 'post',
    data
  })
}

export function unDevices(data) {
  return request({
    url: '/shop/unDevices',
    method: 'post',
    data
  })
}

export function bindDevice(data) {
  return request({
    url: '/shop/bindDevice',
    method: 'post',
    data
  })
}

export function unBindDevice(data) {
  return request({
    url: '/shop/unBindDevice',
    method: 'post',
    data
  })
}

export default {
  list: list,
  add: add,
  details: details,
  init_edit: init_edit,
  edit: edit,
  devices: devices,
  unDevices: unDevices,
  bindDevice: bindDevice,
  unBindDevice: unBindDevice
}
