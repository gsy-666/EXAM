<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { useRoute } from 'vue-router'
import { Table, Button, Modal, Form, Input, InputNumber, Select, Empty, message } from 'ant-design-vue'
import { PlusOutlined } from '@ant-design/icons-vue'
import { getQuestionList, addQuestion } from '../../api/teacher'
import type { Question } from '../../types'

const route = useRoute()
const homeworkId = route.params.id as string
const loading = ref(false)
const list = ref<Question[]>([])
const modalVisible = ref(false)
const submitting = ref(false)

const form = reactive({
  questionType: 1, content: '', standardAnswer: '',
  score: 10, knowledgeTag: '', ruleConfig: '',
})

const columns = [
  { title: '#', key: 'idx', width: 50 },
  { title: '题目内容', dataIndex: 'content', key: 'content' },
  { title: '题型', key: 'type', width: 100 },
  { title: '分值', dataIndex: 'score', key: 'score', width: 70 },
  { title: '知识点', dataIndex: 'knowledgeTag', key: 'tag', width: 120 },
]

async function fetchList() {
  loading.value = true
  try {
    const res = await getQuestionList(homeworkId)
    list.value = res.data || []
  } finally { loading.value = false }
}

async function handleAdd() {
  if (!form.content) { message.warning('请输入题目内容'); return }
  submitting.value = true
  try {
    await addQuestion({
      questionId: '', homeworkId,
      ...form, createTime: '',
    } as Question)
    message.success('添加成功')
    modalVisible.value = false
    form.content = ''; form.standardAnswer = ''; form.ruleConfig = ''; form.knowledgeTag = ''
    fetchList()
  } finally { submitting.value = false }
}

onMounted(fetchList)
</script>

<template>
  <div class="page">
    <div class="page-top">
      <div>
        <h2>题目管理</h2>
        <p>作业 ID：{{ homeworkId }}</p>
      </div>
      <Button type="primary" @click="modalVisible = true">
        <PlusOutlined /> 添加题目
      </Button>
    </div>

    <Table :columns="columns" :data-source="list" :loading="loading" row-key="questionId" :pagination="false">
      <template #bodyCell="{ column, record, index }">
        <template v-if="column.key === 'idx'">{{ index + 1 }}</template>
        <template v-if="column.key === 'type'">{{ record.questionType <= 4 ? '客观题' : '主观题' }}</template>
      </template>
    </Table>

    <div v-if="!loading && list.length === 0" class="empty-wrap">
      <Empty description="暂无题目" />
    </div>

    <Modal v-model:open="modalVisible" title="添加题目" @ok="handleAdd" :confirm-loading="submitting" width="600px">
      <Form layout="vertical">
        <Form.Item label="题目内容" required>
          <Input.TextArea v-model:value="form.content" :rows="3" placeholder="请输入题目内容" />
        </Form.Item>
        <Form.Item label="题型" required>
          <Select v-model:value="form.questionType" style="width:200px">
            <Select.Option :value="1">客观题</Select.Option>
            <Select.Option :value="5">主观题（AI 批改）</Select.Option>
          </Select>
        </Form.Item>
        <Form.Item label="标准答案" required>
          <Input v-model:value="form.standardAnswer" placeholder="请输入标准答案" />
        </Form.Item>
        <Form.Item label="分值">
          <InputNumber v-model:value="form.score" :min="1" :max="100" style="width:200px" />
        </Form.Item>
        <Form.Item label="知识点标签">
          <Input v-model:value="form.knowledgeTag" placeholder="如：线性代数" />
        </Form.Item>
        <Form.Item label="AI 批改规则（JSON，主观题用）">
          <Input.TextArea v-model:value="form.ruleConfig" :rows="2" placeholder='{"criteria": ["逻辑清晰", "答案完整"]}' />
        </Form.Item>
      </Form>
    </Modal>
  </div>
</template>

<style scoped>
.page { max-width: 1000px; }
.page-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; margin-bottom: 4px; }
.page-top p { font-size: 13px; color: #8e8e93; word-break: break-all; }
.empty-wrap { text-align: center; padding: 60px 0; }
</style>
