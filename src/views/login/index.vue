<template>
  <div class="login-page">
    <WallpaperBg />
    
    <div class="bilibili-login">
      <div class="login-left">
        <div class="logo-box">
          <span class="b-logo">School</span>
          <span class="b-text">资源管理系统</span>
        </div>
        <div class="slogan">登录系统</div>
      </div>

      <div class="login-right">
        <div class="login-tabs">
          <div class="tab active">账号密码登录</div>
        </div>
        
        <el-form label-width="0" class="login-form">
          <el-form-item>
            <el-input 
              v-model="loginForm.username" 
              placeholder="请输入用户名"
              prefix-icon="User"
              class="b-input dialog-input"
            />
          </el-form-item>
          
          <el-form-item>
            <el-input 
              type="password" 
              v-model="loginForm.password" 
              placeholder="请输入密码"
              prefix-icon="Lock"
              class="b-input dialog-input"
            />
          </el-form-item>

          <el-form-item class="btn-group">
            <el-button type="primary" @click="login" class="login-btn">登 录</el-button>
          </el-form-item>
          
          <el-form-item class="other-op">
            <el-button type="text" @click="clear" class="reset-btn">清空表单</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import WallpaperBg from '@/components/WallpaperBg.vue'
import { ref } from 'vue'
import { loginApi } from '@/api/login'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()

const loginForm = ref({
  username: '',
  password: ''
})

const login = async () => {
  try {
    const res = await loginApi(loginForm.value)
    if (res.code) {
      ElMessage.success('登录成功')
      localStorage.setItem('userInfo', JSON.stringify(res.data))
      router.push('/')
      clear()
    } else {
      ElMessage.error(res.msg || '登录失败')
    }
  } catch (error) {
    ElMessage.error('登录失败')
  }
}

const clear = () => {
  loginForm.value = { username: '', password: '' }
}
</script>

<style scoped>
/* 核心调整：页面布局改为左对齐，缩小面板尺寸 */
.login-page {
  position: relative;
  width: 100vw;
  height: 100vh;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: flex-end; /* 靠右 */
  padding-right: 80px;
  padding-left: 0;
}

/* 缩小登录面板尺寸（原800x500 → 600x400） */
.bilibili-login {
  width: 600px;
  height: 400px;
  background: rgba(255, 255, 255, 0.25) !important;
  backdrop-filter: blur(3px);
  -webkit-backdrop-filter: blur(3px);
  border: 4px solid #000 !important;
  box-shadow: 3px 3px 0 #fff, 6px 6px 0 #000 !important;
  border-radius: 0 !important;
  display: flex;
  overflow: hidden;
}

/* 左侧漫画风格（同步缩小内边距） */
.login-left {
  width: 40%;
  background: rgba(255, 255, 255, 0.2) !important;
  border-right: 4px solid #000 !important;
  padding: 40px 30px; /* 缩小内边距 */
  color: #000;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-weight: 900;
}

.logo-box {
  margin-bottom: 20px;
}

.b-logo {
  font-size: 42px; /* 缩小字号 */
  font-weight: 900;
  display: block;
  margin-bottom: 8px;
  color: #000;
  text-shadow: 2px 2px 0 #fff;
}

.b-text {
  font-size: 16px; /* 缩小字号 */
  font-weight: 800;
  color: #000;
}

.slogan {
  font-size: 16px; /* 缩小字号 */
  font-weight: 800;
  color: #000;
}

/* 右侧（同步缩小内边距） */
.login-right {
  width: 60%;
  padding: 40px 40px; /* 缩小内边距 */
}

.login-tabs {
  margin-bottom: 30px;
}

.tab {
  font-size: 22px; /* 缩小字号 */
  font-weight: 900;
  color: #000;
}

.tab.active {
  color: #000;
  position: relative;
}

.tab.active::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: -6px;
  width: 35px;
  height: 4px;
  background: #000;
  border-radius: 0;
}

/* 漫画割裂输入框 */
:deep(.dialog-input) {
  border: 4px solid #000 !important;
  border-radius: 0 !important;
  box-shadow: 3px 3px 0 #fff, 5px 5px 0 #000 !important;
  background: rgba(255, 255, 255, 0.2) !important;
  height: 45px !important;
  font-weight: 800 !important;
  margin-bottom: 18px !important;
}

:deep(.dialog-input .el-input__wrapper) {
  height: 45px;
  border-radius: 0 !important;
  border: none !important;
  box-shadow: none !important;
  background: transparent !important;
}

/* 👇 这里就是修复：你输入的文字变成白色加粗 */
:deep(.dialog-input .el-input__inner) {
  color: #ffffff !important;
  font-weight: 900 !important;
  font-size: 16px !important;
}

:deep(.dialog-input .el-input__placeholder) {
  color: #f0f0f0 !important;
  font-weight: 700 !important;
}

:deep(.dialog-input .el-input__prefix) {
  color: #ffffff !important;
  font-weight: 900;
}

/* 按钮漫画风（同步缩小高度） */
.login-btn {
  width: 100%;
  height: 45px; /* 缩小高度 */
  background: #000 !important;
  border: 3px solid #000 !important;
  border-radius: 0 !important;
  font-size: 16px; /* 缩小字号 */
  font-weight: 900;
  color: #fff;
  box-shadow: 2px 2px 0 #fff, 4px 4px 0 #000;
}

.login-btn:hover {
  transform: translate(2px, 2px);
  box-shadow: 0 0 0 #fff, 2px 2px 0 #000;
}

/* 清空按钮 */
.other-op {
  text-align: right;
  margin-top: 12px;
}

.reset-btn {
  color: #000;
  font-size: 13px; /* 缩小字号 */
  font-weight: 800;
}

.reset-btn:hover {
  color: #000;
  text-decoration: underline;
}
</style>