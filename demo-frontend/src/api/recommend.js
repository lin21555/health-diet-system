import request from './request'

export function generateRecommend(data) {
  return request.post('/recommend/generate', data)
}
