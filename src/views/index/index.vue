<script setup>

</script>

<template>
  <div class="container">
    <div class="content">
      <h1>欢迎来到校园管理系统</h1>
    </div>
  </div>
</template>

<style scoped>
/* 🔥 高透背景容器（和主页面100%统一） */
.container {
  width: 100vw;
  height: 100vh;
  background-image: url("src/asster/屏幕截图 2026-05-23 162739.png");
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  background-repeat: no-repeat;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  z-index: 0;
}

/* ❌ 去掉白色遮罩，保持背景100%清晰 */
.container::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: transparent !important;
  z-index: 1;
}

/* 🔥 高透玻璃内容框（漫画割裂风格） */
.content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 2.5rem 5rem;
  
  /* 极致高透玻璃 */
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  
  /* 漫画黑白割裂边框 */
  border: 4px solid #000;
  box-shadow: 3px 3px 0 #fff, 5px 5px 10px rgba(0, 0, 0, 0.15);
  border-radius: 0 !important; /* 漫画直角 */
}

/* 🔥 漫画风格标题 */
h1 {
  color: #000 !important;
  font-size: 2.8rem;
  font-weight: 900;
  margin: 0;
  /* 文字描边，保证高透背景下可阅读 */
  text-shadow: 1px 1px 0 #fff, 2px 2px 0 #000;
}

/* 响应式 */
@media (max-width: 768px) {
  .content {
    padding: 1.5rem 2rem;
  }
  
  h1 {
    font-size: 2rem;
  }
}
</style>