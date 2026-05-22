<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Table, Button, Empty, Tag } from 'ant-design-vue'
import { PlusOutlined, EditOutlined, EyeOutlined } from '@ant-design/icons-vue'
import { getTeacherHomeworkList } from '../../api/teacher'
import type { Homework } from '../../types'

const router = useRouter()
const userId = localStorage.getItem('userId') || ''
const loading = ref(false)
const list = ref<Homework[]>([])

const columns = [
  { title: '作业标题', dataIndex: 'title', key: 'title' },
  { title: '课程', dataIndex: 'courseId', key: 'courseId', width: 90 },
  { title: '总分', dataIndex: 'totalScore', key: 'score', width: 70 },
  { title: '截止时间', key: 'deadline', width: 170 },
  { title: '状态', key: 'status', width: 80 },
  { title: '', key: 'action', width: 140 },
]

function goQuestions(r: Homework) { router.push(`/teacher/homework/${r.homeworkId}/questions`) }
function goCorrections(r: Homework) { router.push(`/teacher/homework/${r.homeworkId}/corrections`) }
function isActive(d: string) { return new Date(d) > new Date() }

onMounted(async () => {
  loading.value = true
  try {
    const res = await getTeacherHomeworkList(userId)
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>

<template>
  <div class="page">
    <div class="page-top">
      <div>
        <h2>作业管理</h2>
        <p>管理已发布的作业</p>
      </div>
      <Button type="primary" @click="router.push('/teacher/homework/publish')">
        <PlusOutlined /> 发布作业
      </Button>
    </div>

    <Table :columns="columns" :data-source="list" :loading="loading" row-key="homeworkId" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'deadline'">
          {{ new Date((record as Homework).deadline).toLocaleString('zh-CN') }}
        </template>
        <template v-if="column.key === 'status'">
          <Tag :color="isActive((record as Homework).deadline) ? 'green' : 'default'">
            {{ isActive((record as Homework).deadline) ? '进行中' : '已截止' }}
          </Tag>
        </template>
        <template v-if="column.key === 'action'">
          <a @click="goQuestions(record as Homework)" style="margin-right:12px"><EditOutlined /> 题目</a>
          <a @click="goCorrections(record as Homework)"><EyeOutlined /> 批改</a>
        </template>
      </template>
    </Table>

    <div v-if="!loading && list.length === 0" class="empty-wrap">
      <Empty description="暂无作业" />
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 1100px; }
.page-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; margin-bottom: 4px; }
.page-top p { font-size: 13px; color: #8e8e93; }
.empty-wrap { text-align: center; padding: 60px 0; }
</style>
