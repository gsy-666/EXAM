<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { Input, Button, Tag, message, Spin, Divider } from 'ant-design-vue'
import { SendOutlined, CheckCircleFilled, CloseCircleFilled, ExclamationCircleFilled } from '@ant-design/icons-vue'
import { getQuestionList } from '../../api/teacher'
import { submitAnswer } from '../../api/homework'
import type { Question, Correction } from '../../types'

const route = useRoute()
const homeworkId = route.params.id as string
const userId = localStorage.getItem('userId') || ''

const questions = ref<Question[]>([])
const corrections = ref<Record<string, Correction>>({})
const answers = ref<Record<string, string>>({})
const loading = ref(false)
const submitting = ref<Record<string, boolean>>({})

async function fetchQuestions() {
  loading.value = true
  try {
    const res = await getQuestionList(homeworkId)
    questions.value = res.data || []
    answers.value = {}
    corrections.value = {}
    questions.value.forEach(q => { answers.value[q.questionId] = '' })
  } finally { loading.value = false }
}

async function handleSubmit(q: Question) {
  const answer = answers.value[q.questionId]
  if (!answer?.trim()) { message.warning('请输入答案'); return }
  submitting.value[q.questionId] = true
  try {
    const res = await submitAnswer({ homeworkId, studentId: userId, questionId: q.questionId, studentAnswer: answer })
    corrections.value[q.questionId] = res.data
    message.success('提交成功')
  } finally { submitting.value[q.questionId] = false }
}

function getFlagIcon(f: number) {
  if (f === 1) return CheckCircleFilled
  if (f === 2) return ExclamationCircleFilled
  return CloseCircleFilled
}
function getFlagColor(f: number) {
  if (f === 1) return '#34c759'
  if (f === 2) return '#ff9500'
  return '#ff3b30'
}
function getFlagText(f: number) {
  if (f === 1) return '正确'
  if (f === 2) return '部分正确'
  return '错误'
}

onMounted(fetchQuestions)
</script>

<template>
  <div class="page">
    <div class="page-top">
      <h2>作业详情</h2>
    </div>

    <Spin :spinning="loading">
      <div v-for="(q, idx) in questions" :key="q.questionId" class="q-card">
        <div class="q-header">
          <span class="q-num">{{ idx + 1 }}</span>
          <div class="q-info">
            <span class="q-content">{{ q.content }}</span>
            <div class="q-tags">
              <Tag>{{ q.questionType <= 4 ? '客观题' : '主观题' }}</Tag>
              <Tag>{{ q.score }} 分</Tag>
              <Tag v-if="q.knowledgeTag">{{ q.knowledgeTag }}</Tag>
            </div>
          </div>
        </div>

        <Divider style="margin: 12px 0" />

        <div class="q-answer">
          <Input.TextArea
            v-model:value="answers[q.questionId]"
            :rows="2"
            placeholder="输入你的答案..."
            :disabled="!!corrections[q.questionId]"
            class="answer-input"
          />
          <Button
            type="primary"
            :loading="submitting[q.questionId]"
            :disabled="!!corrections[q.questionId]"
            @click="handleSubmit(q)"
            class="submit-btn"
          >
            <SendOutlined /> {{ corrections[q.questionId] ? '已批改' : '提交' }}
          </Button>
        </div>

        <!-- 批改结果 -->
        <div v-if="corrections[q.questionId]" class="correction-block">
          <div class="correction-top">
            <component :is="getFlagIcon(corrections[q.questionId].rightFlag)"
              :style="{ fontSize: '20px', color: getFlagColor(corrections[q.questionId].rightFlag) }" />
            <span class="correction-score">
              {{ corrections[q.questionId].score }} / {{ q.score }} 分
            </span>
            <Tag :color="getFlagColor(corrections[q.questionId].rightFlag)">
              {{ getFlagText(corrections[q.questionId].rightFlag) }}
            </Tag>
          </div>
          <div class="correction-comment">{{ corrections[q.questionId].comment }}</div>
        </div>
      </div>

      <div v-if="!loading && questions.length === 0" class="empty-wrap">暂无题目</div>
    </Spin>
  </div>
</template>

<style scoped>
.page { max-width: 800px; }
.page-top { margin-bottom: 20px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; }

.q-card {
  background: #fafafa;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 12px;
  border: 1px solid #f0f0f0;
  transition: border-color 0.2s;
}
.q-card:hover { border-color: #e0e0e0; }

.q-header { display: flex; gap: 14px; }
.q-num {
  width: 28px; height: 28px; border-radius: 8px;
  background: #1d1d1f; color: #fff;
  display: flex; align-items: center; justify-content: center;
  font-size: 13px; font-weight: 600; flex-shrink: 0;
}
.q-content { font-size: 15px; font-weight: 500; color: #1d1d1f; }
.q-tags { margin-top: 6px; display: flex; gap: 6px; }

.q-answer { display: flex; gap: 12px; align-items: flex-start; }
.answer-input :deep(textarea) {
  border-radius: 12px; border: 1px solid #e5e5e7;
  resize: none; font-size: 14px;
}
.submit-btn { border-radius: 10px; height: 40px; flex-shrink: 0; }

.correction-block {
  margin-top: 14px; padding: 14px 16px;
  background: #fff; border-radius: 12px;
  border: 1px solid #f0f0f0;
}
.correction-top { display: flex; align-items: center; gap: 8px; margin-bottom: 8px; }
.correction-score { font-size: 15px; font-weight: 600; color: #1d1d1f; }
.correction-comment { font-size: 13px; color: #6e6e73; line-height: 1.6; }

.empty-wrap { text-align: center; padding: 80px 0; color: #8e8e93; font-size: 14px; }
</style>
