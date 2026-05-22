<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Layout, Menu, Avatar } from 'ant-design-vue'
import {
  DashboardOutlined,
  BookOutlined,
  PlusOutlined,
  LogoutOutlined,
  ExperimentOutlined,
  UnorderedListOutlined,
} from '@ant-design/icons-vue'
import type { User } from '../types'

const router = useRouter()
const route = useRoute()
const user = JSON.parse(localStorage.getItem('user') || '{}') as User
const isStudent = user.role === 1

const menuItems = computed(() => {
  if (isStudent) {
    return [
      { key: '/student/dashboard', icon: DashboardOutlined, label: '学习概览' },
      { key: '/student/homework', icon: BookOutlined, label: '我的作业' },
      { key: '/student/mistakes', icon: ExperimentOutlined, label: '错题本' },
    ]
  }
  return [
    { key: '/teacher/dashboard', icon: DashboardOutlined, label: '教学概览' },
    { key: '/teacher/homework', icon: UnorderedListOutlined, label: '作业管理' },
    { key: '/teacher/homework/publish', icon: PlusOutlined, label: '发布作业' },
  ]
})

const selectedKeys = computed(() => {
  const path = route.path
  for (const item of menuItems.value) {
    if (path === item.key || path.startsWith(item.key + '/')) {
      return [item.key]
    }
  }
  return [route.path]
})

function goTo(path: string) {
  router.push(path)
}

function handleLogout() {
  localStorage.clear()
  router.push('/login')
}
</script>

<template>
  <Layout class="app-layout">
    <!-- 侧边栏 -->
    <Layout.Sider width="240" class="app-sider" :trigger="null" collapsible>
      <div class="sider-header" @click="goTo(isStudent ? '/student/dashboard' : '/teacher/dashboard')">
        <div class="sider-logo">
          <svg width="32" height="32" viewBox="0 0 48 48" fill="none">
            <rect width="48" height="48" rx="12" fill="#1d1d1f"/>
            <path d="M14 32V16l10 8-10 8z" fill="#fff" opacity="0.9"/>
            <path d="M24 16l10 8-10 8V16z" fill="#fff" opacity="0.6"/>
          </svg>
        </div>
        <span class="sider-title">智能批改平台</span>
      </div>

      <Menu
        mode="inline"
        :selected-keys="selectedKeys"
        class="app-menu"
        @click="({ key }) => goTo(key as string)"
      >
        <Menu.Item v-for="item in menuItems" :key="item.key">
          <template #icon><component :is="item.icon" /></template>
          <span>{{ item.label }}</span>
        </Menu.Item>
      </Menu>

      <div class="sider-footer">
        <div class="user-info">
          <Avatar :size="36" :style="{ backgroundColor: '#1d1d1f' }">
            {{ user.name?.charAt(0) }}
          </Avatar>
          <div class="user-text">
            <div class="user-name">{{ user.name }}</div>
            <div class="user-role">{{ isStudent ? '学生' : '教师' }}</div>
          </div>
        </div>
        <div class="logout-btn" @click="handleLogout" title="退出登录">
          <LogoutOutlined />
        </div>
      </div>
    </Layout.Sider>

    <!-- 主内容 -->
    <Layout>
      <Layout.Header class="app-header">
        <div class="header-breadcrumb">
          <span class="header-greeting">{{ isStudent ? '学生端' : '教师端' }}</span>
          <span class="header-divider">/</span>
          <span class="header-college">{{ user.college }}</span>
        </div>
      </Layout.Header>
      <Layout.Content class="app-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </Layout.Content>
    </Layout>
  </Layout>
</template>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: #f5f5f7;
}

/* 侧边栏 */
.app-sider {
  background: #fff !important;
  border-right: 1px solid #e5e5e7;
  display: flex;
  flex-direction: column;
}
.sider-header {
  height: 72px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 0 20px;
  cursor: pointer;
  border-bottom: 1px solid #f0f0f0;
}
.sider-logo {
  flex-shrink: 0;
}
.sider-title {
  font-size: 17px;
  font-weight: 700;
  color: #1d1d1f;
  letter-spacing: -0.3px;
}
.app-menu {
  flex: 1;
  border-right: none !important;
  padding: 8px;
  background: transparent !important;
}
.app-menu :deep(.ant-menu-item) {
  border-radius: 10px;
  margin: 2px 0;
  font-size: 14px;
  height: 42px;
  line-height: 42px;
  color: #3a3a3c;
  transition: all 0.15s ease;
}
.app-menu :deep(.ant-menu-item-selected) {
  background: #f2f2f5;
  color: #1d1d1f;
  font-weight: 600;
}
.app-menu :deep(.ant-menu-item:hover) {
  background: #f5f5f7;
  color: #1d1d1f;
}
.app-menu :deep(.ant-menu-item .anticon) {
  font-size: 18px;
}

/* 侧边栏底部用户区 */
.sider-footer {
  padding: 16px 20px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
}
.user-text {
  line-height: 1.3;
}
.user-name {
  font-size: 13px;
  font-weight: 600;
  color: #1d1d1f;
}
.user-role {
  font-size: 11px;
  color: #8e8e93;
}
.logout-btn {
  width: 34px;
  height: 34px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #8e8e93;
  cursor: pointer;
  transition: all 0.15s ease;
}
.logout-btn:hover {
  background: #f5f5f7;
  color: #ff3b30;
}

/* 顶栏 */
.app-header {
  background: #f5f5f7 !important;
  height: 72px;
  line-height: 72px;
  padding: 0 36px;
  border-bottom: none;
}
.header-breadcrumb {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
}
.header-greeting {
  font-weight: 600;
  color: #1d1d1f;
}
.header-divider {
  color: #c6c6c8;
}
.header-college {
  color: #8e8e93;
}

/* 内容区 */
.app-content {
  margin: 0 24px 24px;
  padding: 24px;
  min-height: calc(100vh - 96px);
  background: #fff;
  border-radius: 20px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.04);
}
</style>
