import request from './request'

export function getFoods(params) {
  return request.get('/food/list', { params })
}

export function getFoodDetail(id) {
  return request.get(`/food/${id}`)
}

export function addFood(data) {
  return request.post('/food', data)
}

export function updateFood(data) {
  return request.put('/food', data)
}

export function deleteFood(id) {
  return request.delete(`/food/${id}`)
}
