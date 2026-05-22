<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Row, Col, Card, Statistic, List, Tag, Spin } from 'ant-design-vue'
import {
  FileTextOutlined,
  StarOutlined,
  PlusOutlined,
} from '@ant-design/icons-vue'
import { getTeacherHomeworkList } from '../../api/teacher'
import type { Homework } from '../../types'

const router = useRouter()
const loading = ref(false)
const homeworkList = ref<Homework[]>([])

const stats = ref({
  totalHomework: 0,
  activeHomework: 0,
})

async function fetchData() {
  loading.value = true
  try {
    const userId = localStorage.getItem('userId') || ''
    const res = await getTeacherHomeworkList(userId)
    homeworkList.value = res.data || []
    const now = new Date()
    stats.value = {
      totalHomework: homeworkList.value.length,
      activeHomework: homeworkList.value.filter(h => new Date(h.deadline) > now).length,
    }
  } finally {
    loading.value = false
  }
}

function getDeadlineStatus(deadline: string) {
  return new Date(deadline) > new Date() ? '进行中' : '已截止'
}

onMounted(fetchData)
</script>

<template>
  <div class="dashboard">
    <Spin :spinning="loading">
      <div class="greeting">
        <h2>教学概览</h2>
        <p>管理你的作业，追踪学生学习情况</p>
      </div>

      <!-- 统计卡片 -->
      <Row :gutter="16" class="stats-row">
        <Col :span="6">
          <Card class="stat-card">
            <div class="stat-icon" style="background: #f2f2f5">
              <FileTextOutlined style="color: #1d1d1f; font-size: 22px" />
            </div>
            <Statistic title="发布作业" :value="stats.totalHomework" :value-style="{ fontSize: '28px', fontWeight: 700 }" />
          </Card>
        </Col>
        <Col :span="6">
          <Card class="stat-card">
            <div class="stat-icon" style="background: #e8f5e9">
              <StarOutlined style="color: #34c759; font-size: 22px" />
            </div>
            <Statistic title="进行中" :value="stats.activeHomework" :value-style="{ fontSize: '28px', fontWeight: 700, color: '#34c759' }" />
          </Card>
        </Col>
        <Col :span="12">
          <Card class="action-card">
            <h3>快捷操作</h3>
            <div class="quick-actions">
              <div class="action-btn" @click="router.push('/teacher/homework/publish')">
                <PlusOutlined />
                <span>发布新作业</span>
              </div>
              <div class="action-btn" @click="router.push('/teacher/homework')">
                <FileTextOutlined />
                <span>管理作业</span>
              </div>
            </div>
          </Card>
        </Col>
      </Row>

      <!-- 最近作业 -->
      <Row :gutter="16" style="margin-top: 20px">
        <Col :span="24">
          <Card class="homework-card">
            <div class="card-header">
              <h3>最近发布的作业</h3>
              <a @click="router.push('/teacher/homework')">查看全部 →</a>
            </div>
            <List
              :data-source="homeworkList.slice(0, 8)"
              :locale="{ emptyText: '暂无作业，点击「发布新作业」开始' }"
            >
              <template #renderItem="{ item }">
                <List.Item>
                  <div class="hw-item" @click="router.push(`/teacher/homework/${item.homeworkId}/questions`)">
                    <div class="hw-info">
                      <span class="hw-title">{{ item.title }}</span>
                      <span class="hw-course">课程 {{ item.courseId }}</span>
                    </div>
                    <div class="hw-meta">
                      <Tag :color="getDeadlineStatus(item.deadline) === '进行中' ? 'green' : 'default'">
                        {{ getDeadlineStatus(item.deadline) }}
                      </Tag>
                      <span class="hw-deadline">
                        截止 {{ new Date(item.deadline).toLocaleDateString('zh-CN') }}
                      </span>
                      <span class="hw-score">总分 {{ item.totalScore }}</span>
                    </div>
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
}

/* 快捷操作 */
.action-card {
  border-radius: 16px;
}
.action-card :deep(.ant-card-body) {
  padding: 20px;
}
.action-card h3 {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}
.quick-actions {
  display: flex;
  gap: 12px;
}
.action-btn {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 16px;
  background: #f5f5f7;
  border-radius: 12px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  color: #1d1d1f;
  transition: all 0.15s ease;
}
.action-btn:hover {
  background: #e8e8ed;
}

/* 作业列表 */
.homework-card {
  border-radius: 16px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}
.card-header h3 {
  font-size: 16px;
  font-weight: 600;
}
.card-header a {
  font-size: 13px;
  color: #1d1d1f;
  cursor: pointer;
}
.hw-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  cursor: pointer;
  padding: 4px 0;
}
.hw-item:hover .hw-title {
  color: #1d1d1f;
  text-decoration: underline;
}
.hw-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}
.hw-title {
  font-size: 14px;
  font-weight: 500;
  color: #1d1d1f;
  transition: color 0.15s;
}
.hw-course {
  font-size: 12px;
  color: #8e8e93;
}
.hw-meta {
  display: flex;
  align-items: center;
  gap: 16px;
}
.hw-deadline {
  font-size: 12px;
  color: #8e8e93;
}
.hw-score {
  font-size: 13px;
  font-weight: 500;
  color: #6e6e73;
}
</style>
