<template>
  <div class="particle-wrapper">
    <VueParticles
      :id="id"
      :options="particleOptions"
    />
  </div>
</template>

<script setup>
const props = defineProps({
  id: {
    type: String,
    default: 'particle-bg'
  }
})

const particleOptions = {
  background: {
    color: {
      value: 'transparent'
    }
  },
  fpsLimit: 60,
  interactivity: {
    events: {
      onClick: {
        enable: true,
        mode: 'push'
      },
      onHover: {
        enable: true,
        mode: 'grab'
      }
    },
    modes: {
      push: {
        quantity: 4
      },
      grab: {
        distance: 180,
        links: {
          opacity: 0.6,
          color: '#ffffff'
        }
      }
    }
  },
  particles: {
    color: {
      value: '#ffffff'
    },
    links: {
      color: '#ffffff',
      distance: 150,
      enable: true,
      opacity: 0.3,
      width: 1
    },
    move: {
      direction: 'none',
      enable: true,
      outModes: {
        default: 'bounce'
      },
      random: true,
      speed: 1.5,
      straight: false
    },
    number: {
      density: {
        enable: true
      },
      value: 80
    },
    opacity: {
      value: 0.5,
      animation: {
        enable: true,
        speed: 0.5,
        minimumValue: 0.1
      }
    },
    shape: {
      type: 'circle'
    },
    size: {
      value: { min: 1, max: 3 },
      animation: {
        enable: true,
        speed: 2,
        minimumValue: 0.5
      }
    }
  },
  detectRetina: true
}
</script>

<style scoped>
.particle-wrapper {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 5;
  pointer-events: none; /* 让鼠标事件穿透粒子层，不影响下方按钮点击 */
}

/* 恢复 canvas 的鼠标事件，保留粒子 hover/click 交互动效 */
.particle-wrapper :deep(canvas) {
  pointer-events: auto;
}
</style>
