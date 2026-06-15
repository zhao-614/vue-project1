<template>
  <div class="container">
    <div id="radar-chart" class="radar-chart-wrap"></div>
    <div class="title-box">学生部门人数占比</div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue';
import * as echarts from 'echarts';
import { queryAllStuReportApi } from '@/api/report';

let radarChart = null;

// 查询接口 + 处理数据
const queryAllReport = async () => {
  const res = await queryAllStuReportApi();
  if (res.code === 1) {
    // 最新接口：[{ name: 'xx学院', sum: 数量 }]
    const list = res.data;

    // 提取 部门名称 + 人数
    const deptNames = list.map(item => item.name);
    const deptCounts = list.map(item => item.sum);

    initRadarChart(deptNames, deptCounts);
  }
};

// 初始化雷达图
const initRadarChart = (jobList, dataList) => {
  const dom = document.getElementById('radar-chart');
  if (!dom || dataList.length === 0) return;

  if (radarChart) radarChart.dispose();
  radarChart = echarts.init(dom);

  // 计算总数
  const total = dataList.reduce((sum, val) => sum + val, 0);
  // 计算百分比（0~100）
  const percentData = dataList.map(v => {
    return total === 0 ? 0 : ((v / total) * 100).toFixed(1);
  });

  // ====================== 放大显示用（比例不变，只变大） ======================
  const showData = percentData.map(item => Number(item) * 2);

  const option = {
    backgroundColor: 'transparent',
    title: { show: false },
    radar: {
      radius: '70%',
      center: ['35%', '45%'],
      shape: 'polygon',
      splitNumber: 4,
      nameGap: 50,
      max: 100, 
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
      indicator: jobList.map(name => ({ name, max: 100 })),
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
        formatter: (params) => {
          // 标签显示真实百分比，不放大
          const realPercent = (Number(params.value) / 2).toFixed(1) + '%';
          return realPercent;
        },
        color: '#fff',
        fontSize: 16,
        fontWeight: 900,
        textShadow: '1px 1px 0 #000'
      },
      data: [{ value: showData }] 
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
  window.removeEventListener('resize', () => radarChart?.resize());
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

.title-box {
  position: absolute;
  left: calc(20% + 550px);
  bottom: calc(8% + 550px);
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