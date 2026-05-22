import api from './index'
import type { ApiResult, User, LoginReq } from '../types'

export function login(data: LoginReq): Promise<ApiResult<User>> {
  return api.post('/user/login', data)
}

export function getUserInfo(userId: string): Promise<ApiResult<User>> {
  return api.get('/user/info', { params: { userId } })
}
