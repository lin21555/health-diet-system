import request from './request'

export function getActivities(params) {
  return request.get('/activity/list', { params })
}

export function getActivityDetail(id) {
  return request.get(`/activity/${id}`)
}

export function signupActivity(id, userId) {
  return request.post(`/activity/signup/${id}`, null, { params: { userId } })
}

export function getJoinedActivities(userId) {
  return request.get('/activity/joined', { params: { userId } })
}

export function addActivity(data) {
  return request.post('/activity', data)
}

export function updateActivity(data) {
  return request.put('/activity', data)
}

export function deleteActivity(id) {
  return request.delete(`/activity/${id}`)
}
