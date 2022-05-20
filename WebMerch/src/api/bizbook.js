import request from '@/utils/request'

export function borrowList(data) {
  return request({
    url: '/bizbook/borrow/list',
    method: 'post',
    data
  })
}

export function borrowDetails(params) {
  return request({
    url: '/bizbook/borrow/details',
    method: 'get',
    params
  })
}

export function flowList(data) {
  return request({
    url: '/bizbook/flow/list',
    method: 'post',
    data
  })
}

export function flowDetails(params) {
  return request({
    url: '/bizbook/flow/details',
    method: 'get',
    params
  })
}

export default {
  borrowList: borrowList,
  borrowDetails: borrowDetails,
  flowList: flowList,
  flowDetails: flowDetails
}
