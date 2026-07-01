import request from './request'

export function askAi(data) {
  return request.post('/ai/ask', data)
}

export function getAiHistory() {
  return request.get('/ai/history')
}
