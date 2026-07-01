<template>
  <div class="layout-wrapper">
    <!-- 粒子特效背景 — 浮在最顶层，鼠标穿透 -->
    <ParticleBg id="layout-particle-bg" />
    <el-container class="app-container">
    <!-- 侧边栏：漫画割裂边框 + 极致高透玻璃 -->
    <el-aside :width="isCollapse ? '64px' : '240px'" class="sidebar">
      <!-- Logo区域 -->
      <div class="logo" :class="{ 'logo-collapse': isCollapse }">
        <div class="logo-icon">
          <el-icon>
            <Management />
          </el-icon>
        </div>
        <span v-if="!isCollapse">管理系统</span>
        <span v-else>MS</span>
      </div>

      <el-menu :default-active="activeMenu" :collapse="isCollapse" :collapse-transition="false" :router="true"
        class="main-menu" background-color="rgba(255,255,255,0.2)" text-color="#000000" active-text-color="#ffffff">
        <!-- 首页菜单 -->
        <el-menu-item index="/index">
          <el-icon class="menu-icon">
            <Promotion />
          </el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <!-- 班级学员管理菜单 -->
        <el-sub-menu index="/manage">
          <template #title>
            <el-icon class="menu-icon">
              <Menu />
            </el-icon>
            <span>班级学员管理</span>
          </template>
          <el-menu-item index="/clazz">
            <el-icon class="submenu-icon">
              <HomeFilled />
            </el-icon>
            <span>班级管理</span>
          </el-menu-item>
          <el-menu-item index="/stu">
            <el-icon class="submenu-icon">
              <UserFilled />
            </el-icon>
            <span>学员管理</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- 系统信息管理 -->
        <el-sub-menu index="/system">
          <template #title>
            <el-icon class="menu-icon">
              <Tools />
            </el-icon>
            <span>系统信息管理</span>
          </template>
          <el-menu-item index="/dept">
            <el-icon class="submenu-icon">
              <HelpFilled />
            </el-icon>
            <span>部门管理</span>
          </el-menu-item>
          <el-menu-item index="/emp">
            <el-icon class="submenu-icon">
              <Avatar />
            </el-icon>
            <span>教师管理</span>
          </el-menu-item>
        </el-sub-menu>

        <!-- 数据统计管理 -->
        <el-sub-menu index="/report">
          <template #title>
            <el-icon class="menu-icon">
              <Histogram />
            </el-icon>
            <span>数据统计管理</span>
          </template>
          <el-menu-item index="/empReport">
            <el-icon class="submenu-icon">
              <InfoFilled />
            </el-icon>
            <span>教师信息统计</span>
          </el-menu-item>
          <el-menu-item index="/stuReport">
            <el-icon class="submenu-icon">
              <Share />
            </el-icon>
            <span>学生信息统计</span>
          </el-menu-item>
          <el-menu-item index="/log">
            <el-icon class="submenu-icon">
              <Document />
            </el-icon>
            <span>日志信息统计</span>
          </el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <!-- 右侧区域 -->
    <el-container>
      <!-- 顶部导航栏：漫画割裂边框 + 极致高透玻璃 -->
      <el-header class="header">
        <div class="header-left">
          <el-button :icon="isCollapse ? Expand : Fold" @click="toggleSidebar" text class="collapse-btn" />
          <span class="title">控制台面板</span>
        </div>
        <div class="header-right">
          <el-badge :value="3" class="badge-item">
            <el-button :icon="Bell" text class="notify-btn" />
          </el-badge>
          <el-dropdown @command="handleCommand">
            <div class="user-info">
              <div class="avatar">
                <el-icon>
                  <User />
                </el-icon>
              </div>
              <span class="username">管理员：{{ name }}</span>
              <el-icon class="caret-icon">
                <CaretBottom />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="user-dropdown">
                <el-dropdown-item command="logout">
                  <el-icon>
                    <SwitchButton />
                  </el-icon>
                  <span>退出登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域：漫画割裂边框 + 极致高透玻璃 -->
      <el-main class="content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {
  Bell, CaretBottom, Fold, Expand, Promotion, Menu, HomeFilled, UserFilled,
  Tools, HelpFilled, Avatar, Histogram, InfoFilled, Share, Document,
  Management, User, SwitchButton
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import router from '@/router'
import ParticleBg from '@/components/ParticleBg.vue'

const route = useRoute()
const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
const name = ref('')

const toggleSidebar = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = (command) => {
  if (command === 'logout') {
    logout()
  }
}

onMounted(() => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
  name.value = userInfo?.name || ''
})

// ✅ 正确退出登录 + 漫画风格弹窗
const logout = () => {
  ElMessageBox.confirm('确定退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
    customClass: 'comic-message-box',
    confirmButtonClass: 'comic-confirm',
    cancelButtonClass: 'comic-cancel',
    showClose: false,
    closeOnClickModal: false
  }).then(() => {
    localStorage.removeItem('userInfo')
    name.value = ''
    ElMessage.success('退出登录成功')
    router.push('/login')
  }).catch(() => {
    ElMessage.info('已取消')
  })
}
</script>

<!-- ✅ 全局漫画弹窗样式 -->
<style>
.comic-message-box {
  border: 4px solid #000 !important;
  background: rgba(255, 255, 255, 0.95) !important;
  box-shadow: 4px 4px 0 #fff, 6px 6px 0 #000 !important;
  border-radius: 0 !important;
}
.comic-message-box .el-message-box__header {
  border-bottom: 3px solid #000 !important;
  background: #fff !important;
}
.comic-message-box .el-message-box__title {
  font-weight: 900 !important;
  color: #000 !important;
  font-size: 18px !important;
}
.comic-message-box .el-message-box__content {
  font-weight: 700 !important;
  color: #000 !important;
  font-size: 16px !important;
}
.comic-confirm {
  background: #000 !important;
  border: 3px solid #000 !important;
  color: #fff !important;
  font-weight: 800 !important;
  border-radius: 0 !important;
  box-shadow: 3px 3px 0 #fff !important;
}
.comic-cancel {
  background: #fff !important;
  border: 3px solid #000 !important;
  color: #000 !important;
  font-weight: 800 !important;
  border-radius: 0 !important;
  box-shadow: 3px 3px 0 #fff !important;
}
</style>

<!-- 你的原有样式不动 -->
<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app-container {
  height: 100vh;
  width: 100vw;
  background-image: url("src/asster/屏幕截图 2026-05-23 162739.png");
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  background-attachment: fixed;
  position: relative;
  filter: none;
  z-index: 0;
}

.app-container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent;
  z-index: 1;
}

.sidebar {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  transition: width 0.3s ease;
  overflow: hidden;
  border-right: 4px solid #000000;
  box-shadow:
    inset -3px 0 0 #ffffff,
    5px 0 0 rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
}

.sidebar::after {
  content: "";
  position: absolute;
  top: 0;
  right: -4px;
  width: 8px;
  height: 100%;
  background: linear-gradient(180deg,
      #000000 0px,
      #000000 4px,
      #ffffff 4px,
      #ffffff 8px);
  background-size: 100% 8px;
  z-index: 9;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #000000;
  font-size: 18px;
  font-weight: 900;
  background: rgba(255, 255, 255, 0.3);
  backdrop-filter: blur(1px);
  white-space: nowrap;
  transition: all 0.3s ease;
  border-bottom: 4px solid #000000;
  box-shadow: inset 0 -3px 0 #ffffff;
}

.logo-collapse {
  font-size: 14px;
}

.logo-icon {
  width: 32px;
  height: 32px;
  border-radius: 0;
  background: #000000;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  box-shadow:
    3px 3px 0 #ffffff,
    4px 4px 0 #000000;
}

.main-menu {
  border-right: none;
  padding: 16px 0;
  --el-menu-hover-text-color: #000000;
  --el-menu-active-color: #ffffff;
  --el-menu-active-bg-color: #000000;
  background: transparent !important;
}

.main-menu :deep(.el-menu-item) {
  margin: 4px 8px;
  border-radius: 0;
  height: 44px;
  line-height: 44px;
  transition: all 0.25s ease;
  color: #000000 !important;
  font-weight: 600;
  background: rgba(255, 255, 255, 0.2);
  border: 3px solid transparent;
}

.main-menu :deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.4);
  color: #000000 !important;
  border: 3px solid #000000;
  box-shadow: 3px 3px 0 #ffffff;
}

.main-menu :deep(.el-menu-item.is-active) {
  background: #000000 !important;
  color: #fff !important;
  box-shadow:
    3px 3px 0 #ffffff,
    5px 5px 0 #000000;
  border: 3px solid #ffffff !important;
}

.main-menu :deep(.el-sub-menu) {
  margin: 4px 8px;
  border-radius: 0;
}

.main-menu :deep(.el-sub-menu__title) {
  height: 44px;
  line-height: 44px;
  border-radius: 0;
  transition: all 0.25s ease;
  color: #000000 !important;
  font-weight: 600;
  border: 3px solid transparent;
  background: rgba(255, 255, 255, 0.2);
}

.main-menu :deep(.el-sub-menu__title:hover) {
  background-color: rgba(255, 255, 255, 0.4);
  color: #000000 !important;
  border: 3px solid #000000;
  box-shadow: 3px 3px 0 #ffffff;
}

.main-menu :deep(.el-sub-menu .el-menu) {
  background: rgba(255, 255, 255, 0.3) !important;
  padding: 8px 0;
  border: 4px solid #000000;
  box-shadow: inset 3px 3px 0 #ffffff;
}

.main-menu :deep(.el-sub-menu .el-menu-item) {
  padding-left: 40px !important;
}

.menu-icon {
  font-size: 18px;
  margin-right: 8px;
}

.submenu-icon {
  font-size: 16px;
  margin-right: 8px;
}

.main-menu :deep(.el-menu--collapse .el-menu-item .el-icon),
.main-menu :deep(.el-menu--collapse .el-sub-menu .el-icon) {
  margin: 0 auto;
  width: 20px;
  font-size: 18px;
}

.header {
  background: rgba(255, 255, 255, 0.2);
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  height: 64px;
  border-bottom: 4px solid #000000;
  box-shadow:
    inset 0 -3px 0 #ffffff,
    0 5px 0 rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
}

.header::after {
  content: "";
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 100%;
  height: 8px;
  background: linear-gradient(90deg,
      #000000 0px,
      #000000 4px,
      #ffffff 4px,
      #ffffff 8px);
  background-size: 8px 100%;
  z-index: 9;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-btn {
  width: 36px;
  height: 36px;
  border-radius: 0;
  transition: all 0.25s ease;
  border: 3px solid transparent;
  background: rgba(255, 255, 255, 0.3);
}

.collapse-btn:hover {
  background-color: rgba(255, 255, 255, 0.5);
  color: #000000;
  border: 3px solid #000000;
  box-shadow: 3px 3px 0 #ffffff;
}

.title {
  font-size: 18px;
  font-weight: 900;
  color: #000000;
  text-shadow:
    1px 1px 0 #ffffff,
    2px 2px 0 #000000;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.notify-btn {
  width: 36px;
  height: 36px;
  border-radius: 0;
  transition: all 0.25s ease;
  border: 3px solid transparent;
  background: rgba(255, 255, 255, 0.3);
}

.notify-btn:hover {
  background-color: rgba(255, 255, 255, 0.5);
  color: #000000;
  border: 3px solid #000000;
  box-shadow: 3px 3px 0 #ffffff;
}

.badge-item {
  --el-badge-background-color: #000000;
  --el-badge-text-color: #ffffff;
  --el-badge-border-color: #ffffff;
  --el-badge-border-width: 2px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 0;
  transition: all 0.25s ease;
  border: 3px solid transparent;
  background: rgba(255, 255, 255, 0.3);
}

.user-info:hover {
  background-color: rgba(255, 255, 255, 0.5);
  border: 3px solid #000000;
  box-shadow: 3px 3px 0 #ffffff;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 0;
  background: #000000;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #ffffff;
  box-shadow:
    3px 3px 0 #ffffff,
    4px 4px 0 #000000;
}

.username {
  font-size: 14px;
  color: #000000;
  font-weight: 800;
  text-shadow: 1px 1px 0 #ffffff;
}

.caret-icon {
  font-size: 12px;
  color: #000000;
  font-weight: bold;
}

.user-dropdown :deep(.el-dropdown-menu) {
  background: rgba(255, 255, 255, 0.4) !important;
  backdrop-filter: blur(1px);
  border: 4px solid #000000 !important;
  box-shadow:
    4px 4px 0 #ffffff,
    6px 6px 0 rgba(0, 0, 0, 0.15);
  border-radius: 0 !important;
}

.user-dropdown :deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  border-radius: 0;
  transition: all 0.25s ease;
  color: #000000;
  font-weight: 600;
  border: 3px solid transparent;
}

.user-dropdown :deep(.el-dropdown-menu__item:hover) {
  background-color: rgba(0, 0, 0, 0.1);
  color: #000000;
  border: 3px solid #000000;
  box-shadow: inset 3px 3px 0 #ffffff;
}

.content {
  background: rgba(255, 255, 255, 0.15);
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  padding: 24px;
  overflow-y: auto;
  height: calc(100vh - 64px);
  border-left: 4px solid #000000;
  box-shadow:
    inset 3px 0 0 #ffffff,
    -3px 0 0 rgba(0, 0, 0, 0.1);
  position: relative;
  z-index: 10;
}

.content::before {
  content: "";
  position: absolute;
  top: 0;
  left: -4px;
  width: 8px;
  height: 100%;
  background: linear-gradient(45deg,
      #000000 0px,
      #000000 4px,
      #ffffff 4px,
      #ffffff 8px);
  background-size: 8px 8px;
  z-index: 9;
}

:deep(.el-table) {
  --el-table-header-text-color: #000000;
  --el-table-row-hover-bg-color: rgba(255,255,255,0.6);
  --el-table-border-color: #000000;
  --el-table-text-color: #000000;
  background: rgba(255, 255, 255, 0.3) !important;
  backdrop-filter: blur(1px);
  border: 4px solid #000000 !important;
  border-radius: 0 !important;
  overflow: hidden;
  box-shadow:
    3px 3px 0 #ffffff,
    5px 5px 0 rgba(0, 0, 0, 0.1);
}

:deep(.el-table th) {
  background: rgba(255, 255, 255, 0.4) !important;
  border-bottom: 3px solid #000000 !important;
  font-weight: 800;
}

:deep(.el-table td) {
  border-bottom: 2px solid #000000 !important;
  font-weight: 600;
}

:deep(.el-button) {
  --el-button-primary-bg-color: #000000;
  --el-button-primary-border-color: #000000;
  --el-button-primary-hover-bg-color: #222;
  --el-button-primary-hover-border-color: #000;
  border-radius: 0 !important;
  border-width: 3px !important;
  box-shadow:
    3px 3px 0 #ffffff,
    4px 4px 0 #000;
  font-weight: 700;
}

:deep(.el-button:hover) {
  box-shadow:
    2px 2px 0 #fff,
    3px 3px 0 #000;
  transform: translate(1px, 1px);
}
</style>