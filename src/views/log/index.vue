<template>
  <div>
    <!-- 背景视频 -->
    <WallpaperBg />

    <!-- 日志内容区域 -->
    <main class="log-container">
      <div class="title">系统操作日志</div>

      <!-- 日志表格 -->
      <el-card class="log-card" shadow="never">
        <el-table
          :data="logList"
          border
          stripe
          style="width: 100%"
          :header-cell-style="{ 
            background: 'rgba(255,255,255,0.2)', 
            color: '#000', 
            fontWeight: '800',
            fontSize: '14px',
            textAlign: 'center'
          }"
          :cell-style="{ 
            textAlign: 'center',
            fontSize: '13px',
            color: '#000',
            fontWeight: '600'
          }"
          class="log-table"
        >
          <el-table-column label="操作人" prop="operateEmpName" width="100" align="center" />
          <el-table-column label="操作时间" prop="operateTime" width="180" align="center" />
          <el-table-column label="功能模块" width="100" align="center">
            <template #default="{ row }">
              <el-tag type="primary" size="small" class="comic-tag">{{ row.moduleName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作类型" width="100" align="center">
            <template #default="{ row }">
              <el-tag :type="row.actionType" size="small" class="comic-tag">{{ row.actionName }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="执行结果" width="90" align="center">
            <template #default="{ row }">
              <el-tag :type="row.operateSuccess === '成功' ? 'success' : 'danger'" size="small" class="comic-tag">
                {{ row.operateSuccess }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="耗时(ms)" prop="costTime" width="80" align="center" />
          <el-table-column label="类名" prop="className" min-width="220" show-overflow-tooltip />
          <el-table-column label="方法名" prop="methodName" width="100" align="center" />
        </el-table>

        <!-- 分页 -->
        <div class="pagination-box" style="text-align: right; margin-top: 15px">
<!-- 分页 -->
<div class="pagination-box" style="text-align: right; margin-top: 15px">
  <el-pagination
    v-model:current-page="page"
    v-model:page-size="pageSize"
    :total="total"
    layout="total, sizes, prev, pager, next, jumper"
    :page-sizes="[8, 12, 16]"
    @size-change="fetchLogs"
    @current-change="fetchLogs"
    class="log-pagination"
  />
</div>
        </div>
      </el-card>
    </main>
  </div>
</template>

<script setup>
import WallpaperBg from '@/components/WallpaperBg.vue'
import { logApi } from '@/api/log'
import { queryEmpDetailApi } from '@/api/emp'
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 数据
const logList = ref([])
const empNameMap = ref({})
const page = ref(1)
const pageSize = ref(8)
const total = ref(0)

// 获取员工姓名
const getEmpName = async (empId) => {
  if (!empId) return '系统'
  if (empNameMap.value[empId]) return empNameMap.value[empId]
  try {
    const res = await queryEmpDetailApi(empId)
    const name = res.data?.name || '未知用户'
    empNameMap.value[empId] = name
    return name
  } catch (error) {
    return '未知用户'
  }
}

// 解析模块名称
const parseModule = (className) => {
  if (className.includes('DeptController')) return { name: '部门', type: 'primary' }
  if (className.includes('ClazzController')) return { name: '班级', type: 'primary' }
  if (className.includes('Empcontroller')) return { name: '教师', type: 'primary' }
  if (className.includes('StudentController')) return { name: '学生', type: 'primary' }
  if (className.includes('LoginController')) return { name: '登录', type: 'primary' }
  return { name: '其他', type: 'info' }
}

// 解析操作名称
const parseAction = (methodName) => {
  if (methodName === 'save') return { name: '新增', type: 'success' }
  if (methodName === 'delete') return { name: '删除', type: 'danger' }
  if (methodName === 'update') return { name: '修改', type: 'warning' }
  if (methodName === 'updateViolation') return { name: '修改违规记录', type: 'warning' }
  if (methodName === 'get' || methodName === 'list' || methodName === 'page') return { name: '查询', type: 'primary' }
  return { name: '操作', type: 'info' }
}

// 解析操作结果
const parseResult = (returnValue) => {
  try {
    const res = JSON.parse(returnValue)
    return res.code === 1 ? '成功' : '失败'
  } catch {
    return '未知'
  }
}

// 显示最新操作提示（只弹1次）
const showLatestLog = (log) => {
  const module = parseModule(log.className).name
  const action = parseAction(log.methodName).name
  const result = parseResult(log.returnValue)
  const msg = `【${module}】${action} ${result}`
  ElMessage({
    message: msg,
    type: result === '成功' ? 'success' : 'error',
    duration: 3000
  })
}

// 获取日志列表
const fetchLogs = async () => {
  try {
    const res = await logApi(page.value, pageSize.value)
    if (res.code === 1 && res.data) {
      const rows = res.data.rows || []
      total.value = res.data.total || 0

      // 遍历解析所有字段
      for (let log of rows) {
        log.operateEmpName = await getEmpName(log.operateEmpId)
        log.moduleName = parseModule(log.className).name
        log.actionName = parseAction(log.methodName).name
        log.actionType = parseAction(log.methodName).type
        log.operateSuccess = parseResult(log.returnValue)
      }

      logList.value = rows

      // 只提示最新一条
      if (rows.length > 0) {
        showLatestLog(rows[0])
      }
    }
  } catch (error) {
    console.error('获取日志失败：', error)
    ElMessage.error('获取日志失败')
  }
}

// 初始化
onMounted(() => {
  fetchLogs()
})
</script>

<style scoped>
.log-container {
  position: relative;
  z-index: 1;
  padding: 30px;
  max-width: 1400px;
  margin: 0 auto;
}

.title {
  font-size: 26px;
  font-weight: 900;
  color: #fff;
  margin-bottom: 20px;
  text-shadow: 2px 2px 0 #000;
}

/* 漫画割裂风卡片 */
.log-card {
  background: rgba(255, 255, 255, 0.15) !important;
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff, 3px 3px 5px rgba(0, 0, 0, 0.1);
  border-radius: 0 !important;
  padding: 20px;
}

:deep(.el-card__body) {
  background: transparent !important;
  padding: 0;
}

/* 漫画风表格 */
:deep(.log-table) {
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(1px);
  border: 2px solid #000 !important;
  --el-table-border-color: #000 !important;
  --el-table-row-hover-bg-color: rgba(255,255,255,0.3) !important;
}

:deep(.el-table td),
:deep(.el-table th) {
  text-align: center !important;
  border-color: #000 !important;
  background: rgba(255, 255, 255, 0.1) !important;
  padding: 10px 5px !important;
  white-space: nowrap;
}

:deep(.el-table--striped .el-table__row--striped td) {
  background: rgba(255, 255, 255, 0.15) !important;
}

/* 标签漫画风 */
.comic-tag {
  font-weight: 700 !important;
  border: 1px solid #000 !important;
}

/* 分页漫画风 */
:deep(.log-pagination) {
  display: flex;
  align-items: center;
}

:deep(.log-pagination button),
:deep(.log-pagination .el-select__selected-item),
:deep(.log-pagination .el-input__inner) {
  background: rgba(255, 255, 255, 0.2) !important;
  border-color: #000 !important;
  height: 32px;
  color: #000 !important;
  font-weight: 700 !important;
}
</style>