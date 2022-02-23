import request from '@/utils/request'

export function spaces(data) {
  return request({
    url: '/ad/spaces',
    method: 'post',
    data
  })
}

export function init_creative_add(params) {
  return request({
    url: '/ad/init_creative_add',
    method: 'get',
    params
  })
}

export function creativeAdd(data) {
  return request({
    url: '/ad/creativeAdd',
    method: 'post',
    data
  })
}

export function init_creatives(params) {
  return request({
    url: '/ad/init_creatives',
    method: 'get',
    params
  })
}

export function creatives(data) {
  return request({
    url: '/ad/creatives',
    method: 'post',
    data
  })
}

export default {
  spaces: spaces,
  init_creative_add: init_creative_add,
  creativeAdd: creativeAdd,
  init_creatives: init_creatives,
  creatives: creatives
}
