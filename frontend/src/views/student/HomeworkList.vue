<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Table, Input, Empty } from 'ant-design-vue'
import { getHomeworkList } from '../../api/homework'
import type { Homework } from '../../types'

const router = useRouter()
const loading = ref(false)
const list = ref<Homework[]>([])
const courseId = ref('')
const searched = ref(false)

const columns = [
  { title: '作业标题', dataIndex: 'title', key: 'title' },
  { title: '课程', dataIndex: 'courseId', key: 'courseId', width: 100 },
  { title: '总分', dataIndex: 'totalScore', key: 'totalScore', width: 80 },
  { title: '截止时间', key: 'deadline', width: 170 },
  { title: '', key: 'action', width: 100 },
]

async function fetchList() {
  if (!courseId.value) return
  loading.value = true
  searched.value = true
  try {
    const res = await getHomeworkList(courseId.value)
    list.value = res.data || []
  } finally {
    loading.value = false
  }
}

function goDetail(r: Homework) {
  router.push(`/student/homework/${r.homeworkId}`)
}

function isExpired(d: string) { return new Date(d) < new Date() }
</script>

<template>
  <div class="page">
    <div class="page-top">
      <div>
        <h2>我的作业</h2>
        <p>输入课程编号查看作业列表</p>
      </div>
    </div>

    <div class="search-area">
      <Input.Search
        v-model:value="courseId"
        placeholder="课程编号，如 CS101"
        enter-button="查询"
        size="large"
        style="max-width: 360px"
        @search="fetchList"
      />
    </div>

    <Table
      :columns="columns"
      :data-source="list"
      :loading="loading"
      row-key="homeworkId"
      :pagination="false"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'deadline'">
          <span :style="{ color: isExpired((record as Homework).deadline) ? '#ff3b30' : '#6e6e73' }">
            {{ new Date((record as Homework).deadline).toLocaleString('zh-CN') }}
          </span>
        </template>
        <template v-if="column.key === 'action'">
          <a @click="goDetail(record as Homework)">查看</a>
        </template>
      </template>
    </Table>

    <div v-if="searched && !loading && list.length === 0" class="empty-wrap">
      <Empty description="该课程暂无作业" />
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 1000px; }
.page-top { margin-bottom: 20px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; margin-bottom: 4px; }
.page-top p { font-size: 13px; color: #8e8e93; }
.search-area { margin-bottom: 24px; }
.empty-wrap { text-align: center; padding: 60px 0; }
</style>
