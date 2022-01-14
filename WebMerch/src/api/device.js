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

export function init_manage(params) {
  return request({
    url: '/device/init_manage',
    method: 'get',
    params
  })
}

export function init_manage_baseinfo(params) {
  return request({
    url: '/device/init_manage_baseinfo',
    method: 'get',
    params
  })
}

export default {
  init_bookers: init_bookers,
  bookers: bookers,
  init_manage: init_manage,
  init_manage_baseinfo: init_manage_baseinfo
}
