<template>
    <div>
      <!-- 背景视频组件 -->
      <WallpaperBg />
  
      <!-- 页面内容：必须 z-index > 0，才能在视频上面 -->
      <main class="content">
      </main>
    </div>
  </template>
  
  <script setup>
  import WallpaperBg from '@/components/WallpaperBg.vue'
  </script>
  
  <style scoped>
  .content {
    position: relative;
    z-index: 1; /* 一定要大于视频的 z-index */
    padding: 20px;
    color: #fff;
    text-shadow: 0 0 4px #000;
  }
  </style>