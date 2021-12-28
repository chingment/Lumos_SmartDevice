import request from '@/utils/request'

export function loginByAccount(data) {
  return request({
    url: '/own/loginByAccount',
    method: 'post',
    data
  })
}

export function getInfo(params) {
  return request({
    url: '/own/getInfo',
    method: 'get',
    params: params
  })
}

export function logout() {
  return request({
    url: '/own/logout',
    method: 'post'
  })
}

export function checkPermission(params) {
  return request({
    url: '/own/checkPermission',
    method: 'get',
    params: params
  })
}
