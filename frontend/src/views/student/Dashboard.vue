<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Row, Col, Card, Statistic, Progress, List, Tag, Spin } from 'ant-design-vue'
import {
  BookOutlined,
  CheckCircleOutlined,
  RiseOutlined,
  ClockCircleOutlined,
} from '@ant-design/icons-vue'
import { getMistakeBook } from '../../api/homework'
import type { Correction } from '../../types'

const router = useRouter()
const loading = ref(false)
const mistakes = ref<Correction[]>([])

const stats = ref({
  totalAnswered: 0,
  correctCount: 0,
  partialCount: 0,
  wrongCount: 0,
  avgScore: 0,
  accuracy: 0,
})

async function fetchData() {
  loading.value = true
  try {
    const userId = localStorage.getItem('userId') || ''
    const res = await getMistakeBook(userId)
    const all = res.data || []
    mistakes.value = all
    const correct = all.filter(c => c.rightFlag === 1).length
    const partial = all.filter(c => c.rightFlag === 2).length
    const wrong = all.filter(c => c.rightFlag === 0).length
    const total = all.length
    stats.value = {
      totalAnswered: total + correct, // 近似
      correctCount: correct,
      partialCount: partial,
      wrongCount: wrong,
      avgScore: total > 0 ? +(all.reduce((s, c) => s + c.score, 0) / total).toFixed(1) : 0,
      accuracy: total > 0 ? +((correct / total) * 100).toFixed(0) : 0,
    }
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)
</script>

<template>
  <div class="dashboard">
    <Spin :spinning="loading">
      <!-- 问候 -->
      <div class="greeting">
        <h2>学习概览</h2>
        <p>追踪你的学习进度，发现改进空间</p>
      </div>

      <!-- 数据卡片 -->
      <Row :gutter="16" class="stats-row">
        <Col :span="6">
          <Card class="stat-card">
            <div class="stat-icon" style="background: #e8f5e9">
              <CheckCircleOutlined style="color: #34c759; font-size: 22px" />
            </div>
            <Statistic title="正确率" :value="stats.accuracy" suffix="%" :value-style="{ fontSize: '28px', fontWeight: 700 }" />
          </Card>
        </Col>
        <Col :span="6">
          <Card class="stat-card">
            <div class="stat-icon" style="background: #f2f2f5">
              <BookOutlined style="color: #1d1d1f; font-size: 22px" />
            </div>
            <Statistic title="答题总数" :value="stats.totalAnswered" :value-style="{ fontSize: '28px', fontWeight: 700 }" />
          </Card>
        </Col>
        <Col :span="6">
          <Card class="stat-card">
            <div class="stat-icon" style="background: #fff3e0">
              <RiseOutlined style="color: #ff9500; font-size: 22px" />
            </div>
            <Statistic title="平均得分" :value="stats.avgScore" :value-style="{ fontSize: '28px', fontWeight: 700 }" />
          </Card>
        </Col>
        <Col :span="6">
          <Card class="stat-card">
            <div class="stat-icon" style="background: #fce4ec">
              <ClockCircleOutlined style="color: #ff3b30; font-size: 22px" />
            </div>
            <Statistic title="待改进题目" :value="stats.wrongCount + stats.partialCount" :value-style="{ fontSize: '28px', fontWeight: 700 }" />
          </Card>
        </Col>
      </Row>

      <!-- 正确率进度环 + 最近错题 -->
      <Row :gutter="16" style="margin-top: 20px">
        <Col :span="10">
          <Card class="progress-card">
            <h3>答题分布</h3>
            <div class="progress-ring">
              <Progress
                type="dashboard"
                :percent="stats.accuracy"
                :stroke-color="{ '0%': '#1d1d1f', '100%': '#34c759' }"
                :stroke-width="8"
                :width="180"
              />
              <div class="ring-labels">
                <div class="ring-label"><span class="dot" style="background: #34c759"></span> 正确 {{ stats.correctCount }}</div>
                <div class="ring-label"><span class="dot" style="background: #ff9500"></span> 部分正确 {{ stats.partialCount }}</div>
                <div class="ring-label"><span class="dot" style="background: #ff3b30"></span> 错误 {{ stats.wrongCount }}</div>
              </div>
            </div>
          </Card>
        </Col>
        <Col :span="14">
          <Card class="recent-card">
            <div class="card-header">
              <h3>最近错题</h3>
              <a @click="router.push('/student/mistakes')">查看全部 →</a>
            </div>
            <List
              :data-source="mistakes.slice(0, 5)"
              :locale="{ emptyText: '暂无错题记录，继续保持！' }"
            >
              <template #renderItem="{ item }">
                <List.Item>
                  <div class="mistake-item">
                    <div class="mistake-info">
                      <Tag :color="item.rightFlag === 2 ? 'orange' : 'red'">
                        {{ item.rightFlag === 2 ? '部分正确' : '错误' }}
                      </Tag>
                      <span class="mistake-answer">{{ item.studentAnswer?.slice(0, 40) }}{{ item.studentAnswer?.length > 40 ? '...' : '' }}</span>
                    </div>
                    <span class="mistake-score">得分 {{ item.score }}</span>
                  </div>
                </List.Item>
              </template>
            </List>
          </Card>
        </Col>
      </Row>
    </Spin>
  </div>
</template>

<style scoped>
.dashboard {
  max-width: 1200px;
}
.greeting {
  margin-bottom: 28px;
}
.greeting h2 {
  font-size: 24px;
  font-weight: 700;
  color: #1d1d1f;
  margin-bottom: 4px;
}
.greeting p {
  color: #8e8e93;
  font-size: 14px;
}

/* 统计卡片 */
.stat-card {
  border-radius: 16px;
}
.stat-card :deep(.ant-card-body) {
  padding: 20px;
}
.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 12px;
}
.stat-card :deep(.ant-statistic-title) {
  font-size: 12px;
  color: #8e8e93;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  margin-bottom: 2px;
}

/* 进度卡片 */
.progress-card h3,
.recent-card h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 20px;
  color: #1d1d1f;
}
.progress-ring {
  display: flex;
  align-items: center;
  gap: 32px;
}
.ring-labels {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.ring-label {
  font-size: 13px;
  color: #6e6e73;
  display: flex;
  align-items: center;
  gap: 6px;
}
.dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  display: inline-block;
}

/* 错题列表 */
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}
.card-header a {
  font-size: 13px;
  color: #1d1d1f;
  cursor: pointer;
}
.mistake-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.mistake-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.mistake-answer {
  font-size: 13px;
  color: #6e6e73;
  max-width: 300px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.mistake-score {
  font-size: 14px;
  font-weight: 600;
  color: #1d1d1f;
}
</style>
