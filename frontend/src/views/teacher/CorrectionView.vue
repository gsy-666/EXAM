<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { Table, Tag, Empty, Card, Statistic, Row, Col } from 'ant-design-vue'
import { CheckCircleFilled, ExclamationCircleFilled, CloseCircleFilled } from '@ant-design/icons-vue'
import { getQuestionList } from '../../api/teacher'
import type { Question, Correction } from '../../types'
import api from '../../api'

const route = useRoute()
const homeworkId = route.params.id as string
const loading = ref(false)
const corrections = ref<Correction[]>([])
const questions = ref<Question[]>([])

const columns = [
  { title: '学生', dataIndex: 'studentId', key: 'sid', width: 160 },
  { title: '题目', dataIndex: 'questionId', key: 'qid', width: 160 },
  { title: '学生答案', dataIndex: 'studentAnswer', key: 'answer' },
  { title: '得分', dataIndex: 'score', key: 'score', width: 70 },
  { title: 'AI 评语', dataIndex: 'comment', key: 'comment' },
  { title: '结果', key: 'flag', width: 90 },
  { title: '时间', key: 'time', width: 160 },
]

function getFlag(f: number) {
  if (f === 1) return { icon: CheckCircleFilled, c: '#34c759', t: '正确' }
  if (f === 2) return { icon: ExclamationCircleFilled, c: '#ff9500', t: '部分正确' }
  return { icon: CloseCircleFilled, c: '#ff3b30', t: '错误' }
}

async function fetchData() {
  loading.value = true
  try {
    const qRes = await getQuestionList(homeworkId)
    questions.value = qRes.data || []
    const res = await api.get('/teacher/correction/list', { params: { homeworkId } })
    corrections.value = res.data || []
  } catch { corrections.value = [] }
  finally { loading.value = false }
}

const stats = computed(() => {
  const list = corrections.value
  const correct = list.filter(c => c.rightFlag === 1).length
  const partial = list.filter(c => c.rightFlag === 2).length
  const wrong = list.filter(c => c.rightFlag === 0).length
  return {
    total: list.length, correct, partial, wrong,
    avgScore: list.length > 0 ? +(list.reduce((s, c) => s + c.score, 0) / list.length).toFixed(1) : 0,
  }
})

onMounted(fetchData)
</script>

<template>
  <div class="page">
    <div class="page-top">
      <h2>批改详情</h2>
    </div>

    <Row :gutter="16" style="margin-bottom: 24px">
      <Col :span="6">
        <Card class="stat-card">
          <Statistic title="提交数" :value="stats.total" :value-style="{ fontSize: '28px', fontWeight: 700 }" />
        </Card>
      </Col>
      <Col :span="6">
        <Card class="stat-card">
          <Statistic title="正确" :value="stats.correct">
            <template #prefix><CheckCircleFilled style="color:#34c759" /></template>
          </Statistic>
        </Card>
      </Col>
      <Col :span="6">
        <Card class="stat-card">
          <Statistic title="部分正确" :value="stats.partial">
            <template #prefix><ExclamationCircleFilled style="color:#ff9500" /></template>
          </Statistic>
        </Card>
      </Col>
      <Col :span="6">
        <Card class="stat-card">
          <Statistic title="平均分" :value="stats.avgScore" />
        </Card>
      </Col>
    </Row>

    <Table :columns="columns" :data-source="corrections" :loading="loading" row-key="correctionId" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'flag'">
          <Tag :color="getFlag(record.rightFlag).c">{{ getFlag(record.rightFlag).t }}</Tag>
        </template>
        <template v-if="column.key === 'time'">
          {{ record.correctionTime ? new Date(record.correctionTime).toLocaleString('zh-CN') : '-' }}
        </template>
      </template>
    </Table>

    <div v-if="!loading && corrections.length === 0" class="empty-wrap">
      <Empty description="暂无批改记录" />
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 1200px; }
.page-top { margin-bottom: 20px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; }
.stat-card { border-radius: 16px; }
.stat-card :deep(.ant-statistic-title) { font-size: 12px; color: #8e8e93; text-transform: uppercase; letter-spacing: 0.5px; }
.empty-wrap { text-align: center; padding: 60px 0; }
</style>
