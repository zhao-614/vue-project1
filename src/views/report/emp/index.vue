<template>
  <div class="container">
    <div id="radar-chart" class="radar-chart-wrap"></div>
    <div class="title-box">岗位数据占比</div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { queryAllReportApi } from '@/api/report';

const jobData = {
  jobList: [],
  dataList: []
};

let radarChart = null;

const queryAllReport = async () => {
  const res = await queryAllReportApi();
  if (res.code) {
    jobData.jobList = res.data.jobList;
    jobData.dataList = res.data.dataList;
    initRadarChart();
  }
}

const initRadarChart = () => {
  const dom = document.getElementById('radar-chart');
  if (!dom || jobData.dataList.length === 0) return;
  
  if (radarChart) radarChart.dispose();
  radarChart = echarts.init(dom);

  const total = jobData.dataList.reduce((sum, val) => sum + val, 0);
  const percentData = jobData.dataList.map(v => {
    const percent = total === 0 ? 0 : ((v / total) * 100).toFixed(1);
    // ==============================================
    // 核心修改：数据值直接放大2倍（比例不变，图形扩大一倍）
    // ==============================================
    return Number(percent) * 2; 
  });

  const option = {
    backgroundColor: 'transparent',
    title: { show: false },
    radar: {
      radius: '70%',
      center: ['35%', '45%'],
      shape: 'polygon',
      splitNumber: 4,
      nameGap: 50,
      max: 100, // 最大值仍为100，保证雷达框架不变
      name: {
        formatter: '{value}',
        textStyle: {
          color: '#fff',
          fontSize: 20,
          fontWeight: 900,
          fontFamily: "'Microsoft Yahei', '黑体', sans-serif",
          textShadow: '1px 1px 0 #000, 2px 2px 0 #fff'
        }
      },
      indicator: jobData.jobList.map(name => ({ name, max: 100 })),
      splitLine: { lineStyle: { color: 'rgba(255,255,255,0.6)', width: 1.5 } },
      axisLine: { lineStyle: { color: '#fff', width: 2 } },
      splitArea: {
        show: true,
        areaStyle: { color: ['rgba(255,255,255,0.05)', 'rgba(0,0,0,0.05)'] }
      }
    },
    series: [{
      type: 'radar',
      lineStyle: { color: '#8a2be2', width: 6, shadowBlur: 15 },
      itemStyle: { color: '#fff', borderColor: '#8a2be2', borderWidth: 4, symbolSize: 18 },
      areaStyle: { color: 'rgba(138,43,226,0.05)' },
      label: {
        show: true,
        // ==============================================
        // 关键：标签显示原始百分比（不是放大后的值）
        // ==============================================
        formatter: (params) => {
          // 把放大2倍的值还原，显示原始百分比
          const originalPercent = (Number(params.value) / 2).toFixed(1) + '%';
          return originalPercent;
        },
        color: '#fff',
        fontSize: 16,
        fontWeight: 900,
        textShadow: '1px 1px 0 #000'
      },
      // 传入放大2倍后的数据（图形扩大，比例不变）
      data: [{ value: percentData }]
    }]
  };

  radarChart.setOption(option);
};

onMounted(() => {
  queryAllReport();
  window.addEventListener('resize', () => radarChart?.resize());
});

onUnmounted(() => {
  radarChart?.dispose();
});
</script>

<style scoped>
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

.radar-chart-wrap {
  position: absolute;
  left: 0;
  top: 0;
  width: 60%;
  height: 80%;
  background: transparent !important;
  border: none !important;
  box-shadow: none !important;
}

/* 核心修改：向上20px + 向左50px */
.title-box {
  position: absolute;
  left: calc(20% + 550px);  /* 向左50px */
  bottom: calc(8% + 550px);/* 向上20px */
  z-index: 10;
  font-size: 30px;
  font-weight: 900;
  color: #fff;
  border: 3px solid #000;
  background: #fff;
  -webkit-text-stroke: 2px #000;
  text-shadow: 2px 2px 0 #000;
  padding: 6px 20px;
  box-shadow: 4px 4px 0 #000;
  transform: rotate(-1deg);
  font-family: "'Microsoft Yahei', '黑体', sans-serif";
}
</style>