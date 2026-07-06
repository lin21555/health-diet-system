import request from './request'

export function getArticles() {
  return request.get('/article/list')
}
