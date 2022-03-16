import request from '@/utils/request'

export function borrowList(data) {
  return request({
    url: '/booker/borrow/list',
    method: 'post',
    data
  })
}

export function borrowDetails(params) {
  return request({
    url: '/booker/borrow/details',
    method: 'get',
    params
  })
}

export function deviceFeedback(data) {
  return request({
    url: '/booker/device/feedback',
    method: 'post',
    data
  })
}

export function deviceFeedbackDetails(params) {
  return request({
    url: '/booker/device/feedback/details',
    method: 'get',
    params
  })
}

export default {
  borrowList: borrowList,
  borrowDetails: borrowDetails,
  deviceFeedback: deviceFeedback,
  deviceFeedbackDetails: deviceFeedbackDetails
}
