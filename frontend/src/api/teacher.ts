import api from './index'
import type { ApiResult, Homework, Question } from '../types'

export function publishHomework(data: Homework): Promise<ApiResult<string>> {
  return api.post('/teacher/homework/publish', data)
}

export function addQuestion(data: Question): Promise<ApiResult<string>> {
  return api.post('/teacher/question/add', data)
}

export function getTeacherHomeworkList(teacherId: string): Promise<ApiResult<Homework[]>> {
  return api.get('/teacher/homework/list', { params: { teacherId } })
}

export function getQuestionList(homeworkId: string): Promise<ApiResult<Question[]>> {
  return api.get('/teacher/question/list', { params: { homeworkId } })
}
