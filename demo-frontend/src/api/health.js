import request from './request'

export function getHealthProfile(userId) {
  return request.get(`/health/profile/${userId}`)
}

export function saveHealthProfile(data) {
  return request.post('/health/profile', data)
}
