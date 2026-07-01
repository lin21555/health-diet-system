import request from './request'

export function generateRecommend(data) {
  return request.post('/recommend/generate', data)
}

export function getRecommendHistory(userId) {
  return request.get(`/recommend/history/${userId}`)
}
