import request from '@/utils/request'

export function spaces(data) {
  return request({
    url: '/ad/spaces',
    method: 'post',
    data
  })
}

export default {
  spaces: spaces
}
