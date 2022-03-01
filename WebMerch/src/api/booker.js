import request from '@/utils/request'

export function borrowList(data) {
  return request({
    url: '/booker/borrow/list',
    method: 'post',
    data
  })
}

export function deviceFeedback(data) {
  return request({
    url: '/booker/device/feedback',
    method: 'post',
    data
  })
}

export default {
  borrowList: borrowList,
  deviceFeedback: deviceFeedback
}
