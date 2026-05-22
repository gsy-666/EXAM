<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { Table, Tag, Empty } from 'ant-design-vue'
import { getMistakeBook } from '../../api/homework'
import type { Correction } from '../../types'

const userId = localStorage.getItem('userId') || ''
const loading = ref(false)
const list = ref<Correction[]>([])

const columns = [
  { title: '题目', dataIndex: 'questionId', key: 'qid', width: 160 },
  { title: '你的答案', dataIndex: 'studentAnswer', key: 'answer' },
  { title: '得分', dataIndex: 'score', key: 'score', width: 70 },
  { title: '结果', key: 'flag', width: 100 },
  { title: 'AI 评语', dataIndex: 'comment', key: 'comment' },
  { title: '时间', key: 'time', width: 160 },
]

function getFlag(flag: number) {
  if (flag === 1) return { c: '#34c759', t: '正确' }
  if (flag === 2) return { c: '#ff9500', t: '部分正确' }
  return { c: '#ff3b30', t: '错误' }
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getMistakeBook(userId)
    list.value = res.data || []
  } finally { loading.value = false }
})
</script>

<template>
  <div class="page">
    <div class="page-top">
      <h2>错题本</h2>
      <p>查看所有需要改进的题目</p>
    </div>

    <Table :columns="columns" :data-source="list" :loading="loading" row-key="correctionId" :pagination="false">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'flag'">
          <Tag :color="getFlag(record.rightFlag).c">{{ getFlag(record.rightFlag).t }}</Tag>
        </template>
        <template v-if="column.key === 'time'">
          {{ record.correctionTime ? new Date(record.correctionTime).toLocaleString('zh-CN') : '-' }}
        </template>
      </template>
    </Table>

    <div v-if="!loading && list.length === 0" class="empty-wrap">
      <Empty description="暂无错题，继续保持！" />
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 1100px; }
.page-top { margin-bottom: 20px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; margin-bottom: 4px; }
.page-top p { font-size: 13px; color: #8e8e93; }
.empty-wrap { text-align: center; padding: 60px 0; }
</style>
