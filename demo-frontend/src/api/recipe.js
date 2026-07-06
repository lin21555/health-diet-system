import request from './request'

export function getRecipes(params) {
  return request.get('/recipe/list', { params })
}

export function getRecipeDetail(id) {
  return request.get(`/recipe/${id}`)
}

export function addRecipe(data) {
  return request.post('/recipe', data)
}

export function updateRecipe(data) {
  return request.put('/recipe', data)
}

export function deleteRecipe(id) {
  return request.delete(`/recipe/${id}`)
}

export function favoriteRecipe(id) {
  return request.post(`/recipe/favorite/${id}`)
}
