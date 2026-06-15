<template>
  <div class="student-manage">
    <!-- 搜索卡片 -->
    <el-card class="query-card" shadow="never">
      <div class="search-bar">
        <el-form :model="searchForm" inline @submit.prevent class="search-form">
          <el-form-item label="班级名称" class="form-item">
            <el-input 
              v-model="searchForm.name" 
              placeholder="请输入班级名称" 
              clearable 
              class="form-input"
            />
          </el-form-item>

          <el-form-item label="开课日期" class="form-item">
            <el-date-picker 
              v-model="searchForm.date" 
              type="daterange" 
              range-separator="至" 
              start-placeholder="开始日期"
              end-placeholder="结束日期" 
              value-format="YYYY-MM-DD" 
              class="form-datepicker"
            />
          </el-form-item>

          <el-form-item class="form-item btn-group-left">
            <el-button type="primary" :icon="Search" @click="handleSearch" class="form-btn">搜索</el-button>
            <el-button :icon="Refresh" @click="resetSearch" class="form-btn">重置</el-button>
          </el-form-item>
        </el-form>
        
        <div class="btn-group-right">
          <el-button type="success" :icon="Plus" @click="handleAdd" class="form-btn add-btn">新增班级</el-button>
        </div>
      </div>
    </el-card>

    <!-- 表格卡片 -->
    <el-card class="table-card" shadow="never">
      <el-table 
        :data="tableData" 
        v-loading="loading" 
        stripe 
        border 
        style="width: 100%"
        :header-cell-style="{ background: 'rgba(255,255,255,0.2)', color: '#000', fontWeight: '600' }"
        class="class-table"
      >
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="name" label="班级名称" min-width="180" />
        <el-table-column prop="room" label="教室" min-width="100" />
        <el-table-column prop="subject" label="学科/部门" min-width="140" />
        <el-table-column prop="masterName" label="班主任" min-width="120" />
        <el-table-column prop="beginDate" label="开课日期" width="120" />
        <el-table-column prop="endDate" label="结课日期" width="120" />

        <el-table-column label="操作" width="160" fixed="right" align="center">
          <template #default="{ row }">
            <el-button link type="primary" :icon="Edit" @click="handleEdit(row.id)">编辑</el-button>
            <el-button link type="danger" :icon="Delete" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-wrapper">
        <el-pagination 
          v-model:current-page="pageNo" 
          v-model:page-size="pageSize" 
          :total="total"
          :page-sizes="[8, 12, 16]"  
          layout="total, sizes, prev, pager, next, jumper" 
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange" 
          class="pagination"
        />
      </div>
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="600px" 
      destroy-on-close 
      @close="handleDialogClose"
      class="glass-dialog"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px" class="dialog-form">
        <el-form-item label="班级名称" prop="name" class="dialog-form-item">
          <el-input v-model="formData.name" placeholder="请输入班级名称" class="dialog-input" />
        </el-form-item>
        <el-form-item label="教室" prop="room" class="dialog-form-item">
          <el-input v-model="formData.room" placeholder="请输入教室" class="dialog-input" />
        </el-form-item>
        
        <!-- 部门下拉（编辑自动回显） -->
        <el-form-item label="所属部门" prop="subject" class="dialog-form-item">
          <el-select v-model="formData.subject" placeholder="请选择部门" style="width:100%" class="dialog-input">
            <el-option v-for="item in deptList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        
        <!-- 班主任下拉（编辑自动回显） -->
        <el-form-item label="班主任" prop="masterId" class="dialog-form-item">
          <el-select v-model="formData.masterId" placeholder="请选择班主任" style="width:100%" class="dialog-input">
            <el-option v-for="item in masterList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="开课日期" prop="beginDate" class="dialog-form-item">
          <el-date-picker v-model="formData.beginDate" type="date" value-format="YYYY-MM-DD" style="width:100%" class="dialog-input"/>
        </el-form-item>
        <el-form-item label="结课日期" prop="endDate" class="dialog-form-item">
          <el-date-picker v-model="formData.endDate" type="date" value-format="YYYY-MM-DD" style="width:100%" class="dialog-input"/>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, onMounted } from 'vue'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryClazzListApi, queryClazzDetailApi, updateClazzApi, deleteClazzApi, addClazzApi } from '@/api/clazz'
import { queryAllApi } from '@/api/dept'
import { queryMasterListApi } from '@/api/clazz'

// --- 搜索与表格数据 ---
const searchForm = reactive({
  name: '',
  date: [],
  begin: '',
  end: ''
})

const tableData = ref([])
const loading = ref(false)
const pageNo = ref(1)
const pageSize = ref(8)
const total = ref(0)

// --- 弹窗与表单数据 ---
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const defaultFormData = {
  id: null,
  name: '',
  room: '',
  subject: null,    // 部门ID（数字）
  masterId: null,   // 班主任ID（数字）
  beginDate: '',
  endDate: ''
}
const formData = reactive({ ...defaultFormData })

// --- 下拉选项数据 ---
const deptList = ref([])
const masterList = ref([])

// --- 表单规则 ---
const formRules = {
  name: [{ required: true, message: '请输入班级名称', trigger: 'blur' }],
  room: [{ required: true, message: '请输入教室', trigger: 'blur' }],
  subject: [{ required: true, message: '请选择部门', trigger: 'change' }],
  masterId: [{ required: true, message: '请选择班主任', trigger: 'change' }],
  beginDate: [{ required: true, message: '请选择开课日期', trigger: 'change' }],
  endDate: [{ required: true, message: '请选择结课日期', trigger: 'change' }]
}

// --- 监听搜索日期变化 ---
watch(() => searchForm.date, (val) => {
  if (val && val.length === 2) {
    searchForm.begin = val[0]
    searchForm.end = val[1]
  } else {
    searchForm.begin = ''
    searchForm.end = ''
  }
}, { deep: true })

// --- 接口请求方法 ---
const loadClassList = async () => {
  loading.value = true
  try {
    const res = await queryClazzListApi(
      searchForm.name,
      searchForm.begin,
      searchForm.end,
      pageNo.value,
      pageSize.value
    )
    if (res.code === 1) {
      tableData.value = res.data.rows || []
      total.value = res.data.total || 0
    }
  } finally {
    loading.value = false
  }
}

const initDeptList = async () => {
  const res = await queryAllApi()
  if(res.code) {
    deptList.value = res.data
    // 确保部门列表加载完成后再处理编辑回显
    console.log('部门列表加载完成：', deptList.value)
  }
}

const initMasterList = async () => {
  const res = await queryMasterListApi()
  if(res.code) {
    masterList.value = res.data
    console.log('班主任列表加载完成：', masterList.value)
  }
}

// --- 事件处理方法 ---
const handleSearch = () => {
  pageNo.value = 1
  loadClassList()
}

const resetSearch = () => {
  searchForm.name = ''
  searchForm.date = []
  searchForm.begin = ''
  searchForm.end = ''
  handleSearch()
}

const handleAdd = () => {
  dialogTitle.value = '新增班级'
  // 重置表单数据
  Object.assign(formData, defaultFormData)
  dialogVisible.value = true
}

// ✅ 核心修复：编辑回显（兼容后端返回的字段）
const handleEdit = async (id) => {
  dialogTitle.value = '编辑班级'
  try {
    const res = await queryClazzDetailApi(id)
    if (res.code === 1) {
      const detail = res.data
      // 关键：优先取 ID 字段，兼容后端返回的字段名
      Object.assign(formData, {
        id: detail.id,
        name: detail.name,
        room: detail.room,
        // 兼容后端可能返回的字段名：subjectId / subject（ID）
        subject: detail.subjectId || detail.subject || null,
        // 兼容后端可能返回的字段名：masterId
        masterId: detail.masterId || null,
        beginDate: detail.beginDate,
        endDate: detail.endDate
      })
      // 确保下拉列表加载完成后再显示弹窗（解决回显延迟）
      await Promise.all([initDeptList(), initMasterList()])
      dialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('获取班级详情失败：' + error.message)
  }
}

const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该班级吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  })
  .then(async () => {
    try {
      const response = await deleteClazzApi(id)
      if (response.code === 1) {
        ElMessage.success('删除成功')
        loadClassList()
      } else {
        ElMessage.error(response.msg || '删除失败')
      }
    } catch (error) {
      ElMessage.error('操作失败：' + (error.message || '删除失败'))
    }
  })
  .catch(() => {
    ElMessage.info('已取消删除')
  })
}

// ✅ 提交时确保 ID 是数字类型
const submitForm = () => {
  formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        // 确保提交的 ID 是数字（防止字符串ID）
        const submitData = {
          ...formData,
          subject: formData.subject ? Number(formData.subject) : null,
          masterId: formData.masterId ? Number(formData.masterId) : null
        }
        
        let response
        if (submitData.id) {
          response = await updateClazzApi(submitData)
        } else {
          response = await addClazzApi(submitData)
        }
        
        if (response.code) {
          ElMessage.success(formData.id ? '修改成功' : '新增成功')
          dialogVisible.value = false
          loadClassList()
        } else {
          ElMessage.error(response.msg || '操作失败')
        }
      } catch (error) {
        ElMessage.error('操作失败：' + (error.message || '请求异常'))
      }
    }
  })
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.assign(formData, defaultFormData)
}

const handleSizeChange = () => loadClassList()
const handleCurrentChange = () => loadClassList()

// ✅ 初始化时先加载下拉列表，再加载表格
onMounted(async () => {
  await Promise.all([initDeptList(), initMasterList()])
  loadClassList()
})
</script>
<style scoped>
.student-manage {
  width: 100%;
  padding: 8px;
}

/* 搜索栏整体布局 - 左右对齐 */
.search-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  padding: 8px 0;
}

.search-form {
  display: flex;
  align-items: center;
  margin: 0;
}

.form-item {
  margin: 0 12px 0 0 !important;
  display: flex;
  align-items: center;
}

/* 输入框统一尺寸 */
.form-input {
  width: 200px !important;
}

.form-datepicker {
  width: 350px !important;
}

/* 左侧按钮组 */
.btn-group-left {
  display: flex;
  gap: 8px;
  margin-left: 8px !important;
}

/* 右侧新增按钮 */
.btn-group-right {
  margin-right: 8px;
}

.form-btn {
  width: 90px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.add-btn {
  width: 110px !important;
}

/* 搜索卡片 */
.query-card {
  margin-bottom: 20px;
  background: rgba(255, 255, 255, 0.15) !important;
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff, 3px 3px 5px rgba(0, 0, 0, 0.1);
  border-radius: 0 !important;
}

/* 表格卡片 */
.table-card {
  background: rgba(255, 255, 255, 0.15) !important;
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff, 3px 3px 5px rgba(0, 0, 0, 0.1);
  border-radius: 0 !important;
}

:deep(.el-card__body) {
  background: transparent !important;
  padding: 20px;
  margin: 0;
}

/* 表格样式 - 消除右侧空白 */
.class-table {
  width: 100% !important;
  margin: 0 auto;
  border-collapse: collapse;
}

:deep(.el-table) {
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(1px);
  border: 2px solid #000 !important;
  --el-table-border-color: #000 !important;
  --el-table-header-text-color: #000;
}

:deep(.el-table tr) {
  background: transparent !important;
}

:deep(.el-table td) {
  background: rgba(255, 255, 255, 0.1) !important;
  border-color: #000 !important;
  text-align: center;
  padding: 8px 4px !important;
}

:deep(.el-table th) {
  text-align: center;
  padding: 10px 4px !important;
}

:deep(.el-table--striped .el-table__row--striped td) {
  background: rgba(255, 255, 255, 0.15) !important;
}

/* 操作列贴边无空白 */
:deep(.el-table__fixed-right) {
  right: 0 !important;
  border-left: 2px solid #000 !important;
}

/* 分页样式 */
.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 40px;
  padding-right: 10px;
}

:deep(.pagination) {
  background: transparent !important;
  display: flex;
  align-items: center;
}

:deep(.el-pagination button),
:deep(.el-pagination .el-select__selected-item),
:deep(.el-pagination .el-input__inner) {
  background: rgba(255, 255, 255, 0.2) !important;
  border-color: #000 !important;
  height: 32px;
  line-height: 32px;
}

/* 弹窗表单项 - 标签加粗 */
:deep(.dialog-form-item .el-form-item__label) {
  font-weight: 900 !important;
  font-size: 14px !important;
  color: #000 !important;
}

/* 弹窗输入框 - 割裂风边框 */
:deep(.dialog-input) {
  border: 4px solid #000 !important;
  border-radius: 0 !important;
  /* 割裂风效果：错位阴影+粗边框 */
  box-shadow: 3px 3px 0 #fff, 5px 5px 0 #000 !important;
  background: rgba(255, 255, 255, 0.2) !important;
  height: 40px !important;
  font-weight: 700 !important;
}

/* 日期选择器/下拉框适配割裂边框 */
:deep(.dialog-input .el-input__inner) {
  border: none !important;
  background: transparent !important;
  height: 32px !important;
}
</style>

<style>
/* 弹窗样式 */
.glass-dialog {
  background: rgba(255, 255, 255, 0.25) !important;
  backdrop-filter: blur(3px) !important;
  -webkit-backdrop-filter: blur(3px) !important;
  border: 3px solid #000 !important;
  box-shadow: 3px 3px 0 #fff, 5px 5px 10px rgba(0, 0, 0, 0.2) !important;
  border-radius: 0 !important;
}

.glass-dialog .el-dialog__header {
  background: transparent !important;
  border-bottom: 2px solid #000 !important;
}

.glass-dialog .el-dialog__body {
  background: transparent !important;
  padding: 24px;
}

.glass-dialog .el-dialog__footer {
  background: transparent !important;
  border-top: 2px solid #000 !important;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 弹窗下拉选项适配 */
.glass-dialog .el-select-dropdown__item {
  background: rgba(255, 255, 255, 0.8) !important;
  border-bottom: 1px solid #000 !important;
}

.glass-dialog .el-select-dropdown__item:hover {
  background: rgba(0, 0, 0, 0.1) !important;
}
</style>