<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { Input, Button, message } from 'ant-design-vue'
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue'
import { login } from '../api/user'
import type { LoginReq } from '../types'

const router = useRouter()
const loading = ref(false)

const form = reactive<LoginReq>({ username: '', password: '' })

async function handleLogin() {
  if (!form.username || !form.password) {
    message.warning('请输入用户名和密码')
    return
  }
  loading.value = true
  try {
    const res = await login(form)
    const user = res.data
    localStorage.setItem('user', JSON.stringify(user))
    localStorage.setItem('userId', user.userId)
    message.success(`欢迎回来，${user.name}`)
    if (user.role === 1) {
      router.push('/student/dashboard')
    } else {
      router.push('/teacher/dashboard')
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 左侧品牌区 -->
    <div class="login-brand">
      <div class="brand-content">
        <div class="brand-icon">
          <svg width="48" height="48" viewBox="0 0 48 48" fill="none">
            <rect width="48" height="48" rx="14" fill="#1d1d1f"/>
            <path d="M14 32V16l10 8-10 8z" fill="#fff" opacity="0.9"/>
            <path d="M24 16l10 8-10 8V16z" fill="#fff" opacity="0.6"/>
          </svg>
        </div>
        <h1>智能作业批改平台</h1>
        <p>高效 · 精准 · 智能 — 让教学反馈触手可及</p>
      </div>
    </div>

    <!-- 右侧登录区 -->
    <div class="login-form-area">
      <div class="login-form-card">
        <h2>登录</h2>
        <p class="subtitle">欢迎回来，请登录您的账号</p>

        <div class="form-group">
          <label>用户名</label>
          <Input
            v-model:value="form.username"
            size="large"
            placeholder="请输入用户名"
            class="custom-input"
            @keydown.enter="handleLogin"
          >
            <template #prefix><UserOutlined /></template>
          </Input>
        </div>
        <div class="form-group">
          <label>密码</label>
          <Input.Password
            v-model:value="form.password"
            size="large"
            placeholder="请输入密码"
            class="custom-input"
            @keydown.enter="handleLogin"
          >
            <template #prefix><LockOutlined /></template>
          </Input.Password>
        </div>

        <Button
          type="primary"
          size="large"
          block
          :loading="loading"
          class="login-btn"
          @click="handleLogin"
        >
          登 录
        </Button>

        <p class="hint">测试账号：teacher1 / student1 &nbsp; 密码：123456</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
}

/* 左侧品牌区 */
.login-brand {
  flex: 0 0 44%;
  background: #1d1d1f;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}
.login-brand::before {
  content: '';
  position: absolute;
  top: -40%;
  right: -20%;
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(255,255,255,0.03) 0%, transparent 70%);
  border-radius: 50%;
}
.brand-content {
  position: relative;
  text-align: center;
  color: #fff;
  padding: 40px;
}
.brand-icon {
  margin-bottom: 24px;
  display: inline-block;
}
.brand-content h1 {
  font-size: 32px;
  font-weight: 700;
  color: #fff;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
}
.brand-content p {
  font-size: 16px;
  color: rgba(255,255,255,0.55);
  font-weight: 400;
}

/* 右侧表单区 */
.login-form-area {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f7;
}
.login-form-card {
  width: 380px;
}
.login-form-card h2 {
  font-size: 28px;
  font-weight: 700;
  color: #1d1d1f;
  margin-bottom: 6px;
}
.login-form-card .subtitle {
  color: #86868b;
  font-size: 15px;
  margin-bottom: 36px;
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  font-size: 13px;
  font-weight: 600;
  color: #6e6e73;
  margin-bottom: 6px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.custom-input :deep(.ant-input) {
  border-radius: 12px;
  border: 1px solid #e5e5e7;
  background: #fff;
  height: 48px;
  font-size: 15px;
  transition: all 0.2s ease;
}
.custom-input :deep(.ant-input):focus,
.custom-input :deep(.ant-input-affix-wrapper-focused) {
  border-color: #1d1d1f;
  box-shadow: 0 0 0 3px rgba(29,29,31,0.08);
}
.custom-input :deep(.ant-input-affix-wrapper) {
  border-radius: 12px;
  border: 1px solid #e5e5e7;
  height: 48px;
  padding: 0 12px;
}
.login-btn {
  height: 48px;
  border-radius: 12px;
  font-size: 16px;
  font-weight: 600;
  background: #1d1d1f;
  border-color: #1d1d1f;
  margin-top: 8px;
  transition: all 0.2s ease;
}
.login-btn:hover {
  background: #333;
  border-color: #333;
}
.hint {
  text-align: center;
  color: #aeaeb2;
  font-size: 12px;
  margin-top: 20px;
}

@media (max-width: 768px) {
  .login-brand {
    display: none;
  }
  .login-form-area {
    padding: 24px;
  }
}
</style>
