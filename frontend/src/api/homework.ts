import api from './index'
import type { ApiResult, Homework, Correction, SubmitAnswerReq } from '../types'

export function getHomeworkList(courseId: string): Promise<ApiResult<Homework[]>> {
  return api.get('/student/homework/list', { params: { courseId } })
}

export function submitAnswer(data: SubmitAnswerReq): Promise<ApiResult<Correction>> {
  return api.post('/student/homework/submit', data)
}

export function getMistakeBook(studentId: string): Promise<ApiResult<Correction[]>> {
  return api.get('/student/homework/mistakes', { params: { studentId } })
}
