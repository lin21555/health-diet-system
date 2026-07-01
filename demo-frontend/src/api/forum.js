import request from './request'

export function getPosts() {
  return request.get('/forum/list')
}
