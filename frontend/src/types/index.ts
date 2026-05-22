// 用户
export interface User {
  userId: string
  username: string
  password?: string
  role: number          // 1=学生 2=教师 3=管理员
  name: string
  college: string
  major: string
  className: string
  createTime: string
  updateTime: string
}

// 作业
export interface Homework {
  homeworkId: string
  courseId: string
  teacherId: string
  title: string
  content: string
  deadline: string
  totalScore: number
  createTime: string
  updateTime: string
}

// 题目
export interface Question {
  questionId: string
  homeworkId: string
  questionType: number   // 1-4=客观题 5+=主观题
  content: string
  standardAnswer: string
  score: number
  ruleConfig: string
  knowledgeTag: string
  createTime: string
}

// 批改记录
export interface Correction {
  correctionId: string
  homeworkId: string
  studentId: string
  questionId: string
  studentAnswer: string
  score: number
  comment: string
  rightFlag: number      // 1=正确 2=部分正确 0=错误
  correctionType: number
  correctionTime: string
}

// 学习数据（预留）
export interface LearningData {
  dataId: string
  userId: string
  courseId: string
  knowledgeTag: string
  correctRate: number
  totalCount: number
  updateTime: string
}

// API 统一响应
export interface ApiResult<T> {
  code: number
  message: string
  data: T
}

// 提交答案请求
export interface SubmitAnswerReq {
  homeworkId: string
  studentId: string
  questionId: string
  studentAnswer: string
}

// 登录请求
export interface LoginReq {
  username: string
  password: string
}
