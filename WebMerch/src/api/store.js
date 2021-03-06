import request from '@/utils/request'

export function list(data) {
  return request({
    url: '/store/list',
    method: 'post',
    data
  })
}

export function initManage(params) {
  return request({
    url: '/store/init_manage',
    method: 'get',
    params
  })
}

export function initManageBaseInfo(params) {
  return request({
    url: '/store/init_manage_baseinfo',
    method: 'get',
    params
  })
}

export function shops(data) {
  return request({
    url: '/store/shops',
    method: 'post',
    data
  })
}

export function unShops(data) {
  return request({
    url: '/store/unShops',
    method: 'post',
    data
  })
}

export function bindShop(data) {
  return request({
    url: '/store/bindShop',
    method: 'post',
    data
  })
}

export function unBindShop(data) {
  return request({
    url: '/store/unBindShop',
    method: 'post',
    data
  })
}

export default {
  list: list,
  initManage: initManage,
  initManageBaseInfo: initManageBaseInfo,
  shops: shops,
  unShops: unShops,
  bindShop: bindShop,
  unBindShop: unBindShop
}
