<template>
  <div class="student-page">
    <!-- 搜索卡片 -->
    <el-card class="query-card" shadow="never">
      <div class="search-bar">
        <el-form :model="searchForm" inline @submit.prevent class="search-form">
          <el-form-item label="学生姓名" class="form-item">
            <el-input
              v-model="searchForm.name"
              placeholder="请输入学生姓名"
              clearable
              class="form-input"
            />
          </el-form-item>

          <el-form-item label="所属部门" class="form-item">
            <el-select
              v-model="searchForm.deptId"
              placeholder="请选择部门"
              style="width:160px"
              class="form-input"
            >
              <el-option
                v-for="item in deptList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="所属班级" class="form-item">
            <el-select
              v-model="searchForm.clazzId"
              placeholder="请选择班级"
              style="width:160px"
              class="form-input"
            >
              <el-option
                v-for="item in clazzList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item class="form-item btn-group-left">
            <el-button type="primary" :icon="Search" @click="handleSearch" class="form-btn">
              搜索
            </el-button>
            <el-button :icon="Refresh" @click="resetSearch" class="form-btn">
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <div class="btn-group-right">
          <el-button type="success" :icon="Plus" @click="handleAdd" class="form-btn add-btn">
            新增学生
          </el-button>
        </div>
      </div>
    </el-card>

    <!-- 表格卡片 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :data="stuList"
        v-loading="loading"
        stripe
        border
        style="width: 100%;"
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
        class="student-table"
      >
        <!-- 核心调整：所有列宽重新分配，地址列放在固定列前 -->
        <el-table-column prop="no" label="学号" width="120" />
        <el-table-column prop="name" label="姓名" width="80" />
        <el-table-column prop="gender" label="性别" width="70">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="idCard" label="身份证号" width="200" show-overflow-tooltip />
        <el-table-column prop="clazzName" label="所属班级" width="160" show-overflow-tooltip />
        <el-table-column prop="graduationDate" label="毕业日期" width="120" />
        <el-table-column prop="violationCount" label="违规次数" width="100" />
        <el-table-column prop="violationScore" label="违规分数" width="100">
          <template #default="{ row }">
            <span :style="{ color: row.violationScore > 0 ? '#ff0000' : '#000', fontWeight: '900', fontSize: '14px' }">
              {{ row.violationScore }}
            </span>
          </template>
        </el-table-column>
        <!-- 家庭地址列：调整宽度+放在固定列前，避免重叠 -->
        <el-table-column prop="address" label="家庭地址" min-width="280" show-overflow-tooltip />
        
        <!-- 操作列：独立固定列，加宽+层级隔离，彻底不重叠 -->
        <el-table-column label="操作" width="200" fixed="right" align="center">
          <template #default="{ row }">
            <div class="operate-btn-group">
              <el-button link type="primary" size="small" @click="handleEdit(row.id)" class="operate-btn">编辑</el-button>
              <el-button link type="danger" size="small" @click="handleDelete(row.id)" class="operate-btn">删除</el-button>
              <el-button link type="warning" size="small" @click="openViolationDialog(row)" class="operate-btn">违纪</el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-box">
        <el-pagination
          v-model:current-page="page"
          :page-sizes="[8, 12, 16]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
          class="pagination"
        />
      </div>
    </el-card>

    <!-- 学生弹窗 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle" 
      width="650px" 
      destroy-on-close 
      @close="handleDialogClose"
      class="glass-dialog"
    >
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px" class="dialog-form">
        <el-form-item label="学号" prop="no" class="dialog-form-item">
          <el-input v-model="formData.no" placeholder="请输入学号" style="width:100%" class="dialog-input" />
        </el-form-item>
        <el-form-item label="姓名" prop="name" class="dialog-form-item">
          <el-input v-model="formData.name" placeholder="请输入姓名" style="width:100%" class="dialog-input" />
        </el-form-item>
        <el-form-item label="性别" prop="gender" class="dialog-form-item">
          <el-select v-model="formData.gender" style="width:100%" class="dialog-input">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="手机号" prop="phone" class="dialog-form-item">
          <el-input v-model="formData.phone" placeholder="请输入手机号" style="width:100%" class="dialog-input" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard" class="dialog-form-item">
          <el-input v-model="formData.idCard" placeholder="请输入身份证号" style="width:100%" class="dialog-input" />
        </el-form-item>
        <el-form-item label="所属部门" prop="deptId" class="dialog-form-item">
          <el-select v-model="formData.deptId" style="width:100%" class="dialog-input">
            <el-option v-for="d in deptList" :key="d.id" :label="d.name" :value="d.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属班级" prop="clazzId" class="dialog-form-item">
          <el-select v-model="formData.clazzId" style="width:100%" class="dialog-input">
            <el-option v-for="c in clazzList" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否大学生" prop="isCollege" class="dialog-form-item">
          <el-select v-model="formData.isCollege" style="width:100%" class="dialog-input">
            <el-option label="是" :value="1" />
            <el-option label="否" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="毕业日期" prop="graduationDate" class="dialog-form-item">
          <el-date-picker v-model="formData.graduationDate" type="date" value-format="YYYY-MM-DD" style="width:100%" class="dialog-input"/>
        </el-form-item>
        <el-form-item label="家庭地址" prop="address" class="dialog-form-item">
          <el-input v-model="formData.address" type="textarea" rows="3" style="width:100%" class="dialog-input" />
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 违纪弹窗 -->
    <el-dialog 
      v-model="violationDialogVisible" 
      title="学生违纪记录" 
      width="500px"
      class="glass-dialog"
    >
      <div class="violation-box">
        <p><b>学生：</b> {{ violationStudent.name }}（{{ violationStudent.no }}）</p>
        <p><b>当前违规次数：</b> {{ violationStudent.violationCount || 0 }}</p>
        <p><b>当前违规分数：</b> <span style="color:red">{{ violationStudent.violationScore || 0 }}</span></p>

        <el-form label-width="120px" style="margin-top:15px" class="dialog-form">
          <el-form-item label="本次扣分数值" class="dialog-form-item">
            <el-input v-model="violationScore" type="number" placeholder="请输入扣分" style="width:100%" class="dialog-input" />
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <el-button @click="violationDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="submitViolation">确认违纪扣分</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, Edit, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { queryAllClazzApi } from '@/api/clazz'
import { queryAllApi } from '@/api/dept'
import { 
  queryStuListApi, 
  addStuApi, 
  updateStuApi, 
  deleteStuApi, 
  queryStuDetailApi,
  updateViolationApi 
} from '@/api/stu'

// 搜索
const searchForm = reactive({
  name: '',
  deptId: '',
  clazzId: ''
})

// 学生列表
const stuList = ref([])
const loading = ref(false)
const clazzList = ref([])
const deptList = ref([])

// 分页
const page = ref(1)
const pageSize = ref(8)
const total = ref(0)

// 学生弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref(null)
const defaultForm = {
  id: null,
  no: '',
  name: '',
  gender: 1,
  phone: '',
  idCard: '',
  deptId: '',
  clazzId: '',
  isCollege: 1,
  graduationDate: '',
  address: ''
}
const formData = reactive({ ...defaultForm })

// 表单验证
const formRules = {
  no: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  clazzId: [{ required: true, message: '请选择班级', trigger: 'change' }]
}

// ====================== 违纪功能 ======================
const violationDialogVisible = ref(false)
const violationStudent = ref({})
const violationScore = ref(0)

// 打开违纪弹窗
const openViolationDialog = (row) => {
  violationStudent.value = row
  violationScore.value = 0
  violationDialogVisible.value = true
}

// 提交违纪扣分
const submitViolation = async () => {
  const score = Number(violationScore.value)
  if (!score || score <= 0) {
    ElMessage.warning('请输入有效的扣分数值')
    return
  }

  try {
    const res = await updateViolationApi(violationStudent.value.id, score)
    if (res.code === 1) {
      ElMessage.success('违纪扣分成功！')
      violationDialogVisible.value = false
      queryStuList() 
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (err) {
    ElMessage.error('接口请求失败')
    console.error(err)
  }
}

// ====================== 学生功能 ======================
// 查询学生
const queryStuList = async () => {
  loading.value = true
  try {
    const res = await queryStuListApi(searchForm, page.value, pageSize.value)
    if (res.code === 1) {
      stuList.value = res.data.rows
      total.value = res.data.total
    }
  } catch (err) {
    console.error(err)
  } finally {
    loading.value = false
  }
}

// 加载班级部门
const queryAll = async () => {
  try {
    const clazzRes = await queryAllClazzApi()
    if (clazzRes.code) clazzList.value = clazzRes.data
    const deptRes = await queryAllApi()
    if (deptRes.code) deptList.value = deptRes.data
  } catch (err) {
    console.error(err)
  }
}

// 搜索
const handleSearch = () => {
  page.value = 1
  queryStuList()
}

// 重置
const resetSearch = () => {
  Object.assign(searchForm, { name: '', deptId: '', clazzId: '' })
  page.value = 1
  queryStuList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增学生'
  dialogVisible.value = true
  Object.assign(formData, defaultForm)
}

// 编辑
const handleEdit = async (id) => {
  try {
    const res = await queryStuDetailApi(id)
    if (res.code === 1) {
      Object.assign(formData, res.data)
      dialogTitle.value = '编辑学生'
      dialogVisible.value = true
    }
  } catch (e) {
    ElMessage.error('获取详情失败')
  }
}

// 删除
const handleDelete = (id) => {
  ElMessageBox.confirm('确定删除该学生吗？', '提示', {
    type: 'warning',
  }).then(async () => {
    try {
      const res = await deleteStuApi(id)
      if (res.code === 1) {
        ElMessage.success('删除成功')
        queryStuList()
      }
    } catch (e) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消')
  })
}

// 提交
const submitForm = () => {
  formRef.value.validate(async valid => {
    if (!valid) return
    try {
      if (formData.id) {
        await updateStuApi(formData)
        ElMessage.success('修改成功')
      } else {
        await addStuApi(formData)
        ElMessage.success('新增成功')
      }
      dialogVisible.value = false
      queryStuList()
    } catch (e) {
      ElMessage.error('操作失败')
    }
  })
}

const handlePageChange = (val) => {
  page.value = val
  
  queryStuList()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  page.value = 1
  queryStuList()
}

const handleDialogClose = () => {
  formRef.value?.resetFields()
}

onMounted(async () => {
  await queryAll()
  queryStuList()
})
</script>

<style scoped>
.student-page {
  width: 100%;
  padding: 8px;
}

/* 搜索栏左右布局 */
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

.form-input {
  width: 160px !important;
}

/* 按钮 */
.btn-group-left {
  display: flex;
  gap: 8px;
  margin-left: 8px !important;
}

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

/* 卡片漫画风格 */
.query-card,
.table-card {
  background: rgba(255, 255, 255, 0.15) !important;
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff, 3px 3px 5px rgba(0, 0, 0, 0.1);
  border-radius: 0 !important;
  margin-bottom: 20px;
}

:deep(.el-card__body) {
  background: transparent !important;
  padding: 20px;
}

/* 表格核心调整 */
.student-table {
  width: 100% !important;
}

:deep(.el-table) {
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(1px);
  border: 2px solid #000 !important;
  --el-table-border-color: #000 !important;
  --el-table-row-hover-bg-color: rgba(255,255,255,0.3) !important;
}

/* 单元格间距和对齐优化 */
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

/* 操作按钮组：紧凑排版+隔离 */
.operate-btn-group {
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
  height: 100%;
  padding: 0 5px;
}

.operate-btn {
  padding: 0 5px !important;
  font-weight: 700 !important;
  font-size: 12px !important;
  white-space: nowrap;
}

/* 关键修复：固定列样式隔离，避免重叠 */
:deep(.el-table__fixed-right) {
  border-left: 3px solid #000 !important;
  background: rgba(255,255,255,0.2) !important;
  z-index: 10 !important; /* 提高层级，盖住地址列 */
  width: 200px !important; /* 强制固定列宽度 */
}

/* 分页右对齐 */
.pagination-box {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 40px;
  padding-right: 10px;
}

:deep(.pagination) {
  display: flex;
  align-items: center;
}

:deep(.el-pagination button),
:deep(.el-pagination .el-select__selected-item),
:deep(.el-pagination .el-input__inner) {
  background: rgba(255, 255, 255, 0.2) !important;
  border-color: #000 !important;
  height: 32px;
}

/* 弹窗标签加粗 */
:deep(.dialog-form-item .el-form-item__label) {
  font-weight: 900 !important;
  font-size: 14px !important;
  color: #000 !important;
}

/* 弹窗割裂风输入框 */
:deep(.dialog-input) {
  border: 4px solid #000 !important;
  border-radius: 0 !important;
  box-shadow: 3px 3px 0 #fff, 5px 5px 0 #000 !important;
  background: rgba(255, 255, 255, 0.2) !important;
  height: 40px !important;
  font-weight: 700 !important;
}

:deep(.dialog-input .el-input__inner) {
  border: none !important;
  background: transparent !important;
}

.violation-box {
  padding: 10px;
  font-weight: 700;
}
</style>

<style>
/* 全局弹窗漫画风格 */
.glass-dialog {
  background: rgba(255, 255, 255, 0.25) !important;
  backdrop-filter: blur(3px) !important;
  border: 3px solid #000 !important;
  box-shadow: 3px 3px 0 #fff, 5px 5px 10px rgba(0, 0, 0, 0.2) !important;
  border-radius: 0 !important;
}

.glass-dialog .el-dialog__header {
  border-bottom: 2px solid #000 !important;
}

.glass-dialog .el-dialog__footer {
  border-top: 2px solid #000 !important;
  justify-content: flex-end;
  gap: 10px;
}
</style>