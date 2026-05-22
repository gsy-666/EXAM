<script setup lang="ts">
import { ConfigProvider } from 'ant-design-vue'

const theme = {
  token: {
    colorPrimary: '#1d1d1f',
    colorLink: '#1d1d1f',
    colorLinkHover: '#3a3a3c',
    borderRadius: 10,
  },
}
</script>

<template>
  <ConfigProvider :theme="theme">
    <router-view />
  </ConfigProvider>
</template>
