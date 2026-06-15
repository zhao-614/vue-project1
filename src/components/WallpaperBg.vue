<template>
  <div class="wallpaper-bg-container">
    <!-- 视频：自动播放、静音、循环、内联播放（iOS兼容） -->
    <video
      class="bg-video"
      autoplay
      muted
      loop
      playsinline
    >
      <source src="D:\Vue\vue-project1\src\asster\绛影灵瞳 3840x2160@25.mp4" type="video/mp4" />
      你的浏览器不支持视频标签。
    </video>
  </div>
</template>

<script setup></script>

<style scoped>
.wallpaper-bg-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: -1; /* 底层 */
  overflow: hidden;
}

.bg-video {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 覆盖全屏，不变形 */
  transform: translate(-50%, -50%);
}
</style>