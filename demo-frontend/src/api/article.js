import request from './request'

export function getArticles() {
  return request.get('/article/list')
}

export function getArticleDetail(id) {
  // 优先调用更明确的详情接口；如果本地后端还没更新，自动回退到旧接口。
  return request.get(`/article/detail/${id}`).catch(() => request.get(`/article/${id}`))
}
