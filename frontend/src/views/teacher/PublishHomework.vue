<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { Form, Input, InputNumber, DatePicker, Button, message } from 'ant-design-vue'
import { publishHomework } from '../../api/teacher'
import type { Dayjs } from 'dayjs'

const router = useRouter()
const loading = ref(false)
const userId = localStorage.getItem('userId') || ''

const form = reactive({
  courseId: '', title: '', content: '',
  totalScore: 100, deadline: undefined as Dayjs | undefined,
})

async function handleSubmit() {
  if (!form.title || !form.courseId || !form.deadline) {
    message.warning('请填写完整信息'); return
  }
  loading.value = true
  try {
    await publishHomework({
      homeworkId: '', teacherId: userId,
      courseId: form.courseId, title: form.title, content: form.content,
      deadline: form.deadline!.format('YYYY-MM-DD HH:mm:ss'),
      totalScore: form.totalScore, createTime: '', updateTime: '',
    })
    message.success('发布成功')
    router.push('/teacher/homework')
  } finally { loading.value = false }
}
</script>

<template>
  <div class="page">
    <div class="page-top">
      <h2>发布作业</h2>
    </div>

    <div class="form-wrap">
      <Form layout="vertical">
        <Form.Item label="作业标题" required>
          <Input v-model:value="form.title" placeholder="如：第三章课后习题" size="large" />
        </Form.Item>
        <Form.Item label="课程编号" required>
          <Input v-model:value="form.courseId" placeholder="如：CS101" size="large" />
        </Form.Item>
        <Form.Item label="作业描述">
          <Input.TextArea v-model:value="form.content" :rows="3" placeholder="作业说明与要求..." />
        </Form.Item>
        <Form.Item label="总分">
          <InputNumber v-model:value="form.totalScore" :min="1" :max="1000" style="width:200px" size="large" />
        </Form.Item>
        <Form.Item label="截止时间" required>
          <DatePicker v-model:value="form.deadline" show-time
            format="YYYY-MM-DD HH:mm:ss" style="width:100%" size="large"
            placeholder="选择截止日期" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" size="large" :loading="loading" @click="handleSubmit">发布作业</Button>
          <Button size="large" style="margin-left:12px" @click="router.back()">取消</Button>
        </Form.Item>
      </Form>
    </div>
  </div>
</template>

<style scoped>
.page { max-width: 640px; }
.page-top { margin-bottom: 24px; }
.page-top h2 { font-size: 20px; font-weight: 700; color: #1d1d1f; }
.form-wrap { background: #fafafa; border-radius: 16px; padding: 24px; border: 1px solid #f0f0f0; }
</style>
