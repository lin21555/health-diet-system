import request from './request'

export function getMessages() {
  return request.get('/message/list')
}

export function addMessage(data) {
  return request.post('/message', data)
}
