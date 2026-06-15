<!-- views/emp/index.vue -->
<template>
  <!-- 教师管理 -->
  <div class="emp-manage">
    <h1 class="page-title">教师管理</h1>

    <!-- 搜索卡片 -->
    <el-card class="query-card" shadow="never">
      <el-form :model="searchForm" inline @submit.prevent class="search-form">
        <!-- 教师姓名 -->
        <el-form-item label="教师姓名" label-width="80px">
          <el-input v-model="searchForm.name" placeholder="请输入姓名" clearable class="form-control" />
        </el-form-item>

        <!-- 性别选择 -->
        <el-form-item label="性别" label-width="50px">
          <el-select v-model="searchForm.gender" placeholder="请选择性别" clearable class="form-control">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>

        <!-- 入职日期 -->
        <el-form-item label="入职日期" label-width="80px">
          <el-date-picker v-model="searchForm.date" type="daterange" range-separator="至" start-placeholder="开始日期"
            end-placeholder="结束日期" value-format="YYYY-MM-DD" class="form-control datepicker-control" />
        </el-form-item>

        <!-- 按钮组 -->
        <el-form-item>
          <el-button type="primary" @click="search" class="form-btn">查询</el-button>
          <el-button @click="clear" class="form-btn">清空</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 功能按钮 -->
    <div class="func-btn-group">
      <el-button type="primary" @click="addEmp" class="form-btn">新增教师</el-button>
      <el-button type="primary" :disabled="selectedEmpIds.length === 0" @click="handleDelete"
        class="form-btn">批量删除</el-button>
    </div>
    <!-- 教师列表 - 列宽已全部统一 -->
    <el-table :data="empList" border style="width: 100%" class="emp-table" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="name" label="姓名" width="120" align="center" />
      <el-table-column label="性别" width="120" align="center">
        <template #default="scope">
          <!-- 性别标签已改成黑白漫画风格 -->
          <el-tag class="gender-tag">
            {{ scope.row.gender === 1 ? '男' : '女' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="image" label="头像" width="120" align="center">
        <template #default="scope">
          <!-- 头像已加黑白漫画滤镜 -->
          <el-image :src="getImageUrl(scope.row.image)" width="40px" height="40px" fit="cover" :error="() => { }"
            class="avatar-img" />
        </template>
      </el-table-column>
      <el-table-column label="职务" width="120" align="center">
        <template #default="scope">
          <span v-if="scope.row.job === 1">班主任</span>
          <span v-else-if="scope.row.job === 2">教师</span>
          <span v-else-if="scope.row.job === 3">院长</span>
          <span v-else-if="scope.row.job === 4">辅导员</span>
          <span v-else-if="scope.row.job === 5">校长</span>
          <span v-else>其他</span>
        </template>
      </el-table-column>
      <el-table-column prop="salary" label="薪资" width="120" align="center" />
      <el-table-column prop="entryDate" label="入职日期" width="120" align="center" />
      <el-table-column prop="deptName" label="部门" width="120" align="center" />
      <el-table-column prop="updateTime" label="最后操作时间" width="180" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template #default="scope">
          <el-button type="primary" size="mini" class="table-btn" @click="editEmp(scope.row.id)">
            <el-icon>
              <EditPen />
            </el-icon>编辑
          </el-button>
          <el-button type="danger" size="mini" class="table-btn" @click="deleteEmp(scope.row.id)">
            <el-icon>
              <DeleteFilled />
            </el-icon>删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <div class="pagination-wrap">
      <el-pagination v-model:current-page="page" v-model:page-size="pageSize" :page-sizes="[5, 10, 15, 20]" background
        layout="total, ->, sizes, prev, pager, next, jumper, ->, ->" :total="total" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" class="pagination" />
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px" modal>
      <el-form :rules="rules" ref="formRef" :model="employee" label-width="80px" class="dialog-form">
        <!-- 基本信息 -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用户名" prop="username">
              <el-input v-model="employee.username" placeholder="请输入员工用户名，2-20个字"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="employee.name" placeholder="请输入员工姓名，2-10个字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="性别" prop="gender">
              <el-select v-model="employee.gender" placeholder="请选择性别" style="width:100%">
                <el-option v-for="item in genderOptions" :key="item.value" :label="item.label"
                  :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="手机号" prop="phone">
              <el-input v-model="employee.phone" placeholder="请输入员工手机号"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="职位">
              <el-select v-model="employee.job" placeholder="请选择职位" style="width:100%">
                <el-option v-for="item in jobOptions" :key="item.value" :label="item.label"
                  :value="item.value"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="薪资">
              <el-input v-model="employee.salary" placeholder="请输入员工薪资"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属部门">
              <el-select v-model="employee.deptId" placeholder="请选择部门" style="width:100%">
                <el-option v-for="item in deptList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="入职日期">
              <el-date-picker v-model="employee.entryDate" type="date" style="width:100%" placeholder="选择日期"
                format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 头像
        action="/api/upload" 
        意思：上传头像时，会将头像文件上传到服务器
        :show-file-list="false" 表示不显示上传文件的列表
        :on-success="handleAvatarSuccess" 表示上传成功后，调用的函数
        :before-upload="beforeAvatarUpload" 表示上传前，调用的函数
        -->
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="头像">
              <el-upload class="avatar-uploader" :headers="{ 'token': token }" action="/api/upload" :show-file-list="false"
                :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                <img v-if="employee.image" :src="getImageUrl(employee.image)" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon">
                  <Plus />
                </el-icon>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 工作经历 -->
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form-item label="工作经历">
              <el-button type="success" size="small" @click="addWorkExperience">+ 添加工作经历</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="3" v-for="(item, index) in employee.exprList" :key="index">
          <el-col :span="10">
            <el-form-item size="small" label="时间" label-width="80px">
              <el-date-picker type="daterange" v-model="item.experDate" range-separator="至" start-placeholder="开始日期"
                end-placeholder="结束日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item size="small" label="公司" label-width="60px">
              <el-input v-model="item.company" placeholder="请输入公司名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item size="small" label="职位" label-width="60px">
              <el-input v-model="item.job" placeholder="请输入职位"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-form-item size="small" label-width="0px">
              <el-button type="danger" @click="removeWorkExperience(item)">- 删除</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="saveEmployee">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, watch, computed } from 'vue'
import { EditPen, DeleteFilled, Plus } from '@element-plus/icons-vue'
import { queryEmpListApi, addEmpApi, queryEmpDetailApi, updateEmpApi, deleteEmpApi } from '@/api/emp'
import { onMounted } from 'vue'
import { queryAllApi } from '@/api/dept'
import { ElMessage, ElMessageBox } from 'element-plus'

// 搜索表单
const searchForm = reactive({
  name: '',
  gender: '',
  date: [],
  begin: '',
  end: ''
})

const deptList = ref([])
const token = ref('')
onMounted(() => {
  search()
  getToken()
})

watch(() => searchForm.date, (newVal) => {
  if (newVal.length === 2) {
    searchForm.begin = newVal[0]
    searchForm.end = newVal[1]
  } else {
    searchForm.begin = ''
    searchForm.end = ''
  }
})

const empList = ref([])
const page = ref(1)
const pageSize = ref(5)
const total = ref(0)

const handleSizeChange = (val) => {
  pageSize.value = val
  page.value = 1
  search()
}
const handleCurrentChange = (val) => {
  page.value = val
  search()
}

// 查询
const search = async () => {
  queryEmpListApi(searchForm.name, searchForm.gender, searchForm.begin, searchForm.end, page.value, pageSize.value)
    .then(res => {
      empList.value = res.data?.rows || []
      total.value = res.data?.total || 0
    })
    .catch(err => {
      console.error('查询教师列表失败:', err)
      empList.value = []
      total.value = 0
    })
  queryAllApi()
    .then(res => {
      deptList.value = res.data || []
    })
    .catch(err => {
      console.error('查询部门失败:', err)
    })
}

// 清空
const clear = () => {
  searchForm.name = ''
  searchForm.gender = ''
  searchForm.date = []
  search()
}

// 图片路径
const getImageUrl = (imagePath) => {
  if (!imagePath) return ''
  if (imagePath.includes(':')) {
    const parts = imagePath.split(/[\\/]/)
    return `/api/images/${parts[parts.length - 1]}`
  }
  return `/api/images/${imagePath}`
}

// 弹窗
const dialogVisible = ref(false)
const dialogTitle = ref('新增员工')
const employee = ref({
  username: '',
  name: '',
  gender: '',
  phone: '',
  job: '',
  salary: '',
  deptId: '',
  entryDate: '',
  image: '',
  exprList: []
})

const formRef = ref(null)

// 新增
const addEmp = () => {
  dialogVisible.value = true
  dialogTitle.value = '新增员工'
  employee.value = {
    username: '',
    name: '',
    gender: '',
    phone: '',
    job: '',
    salary: '',
    deptId: '',
    entryDate: '',
    image: '',
    exprList: []
  }
  formRef.value?.resetFields()
}

// 编辑
const editEmp = async (id) => {
  const res = await queryEmpDetailApi(id)
  if (res.code) {
    employee.value = res.data
    dialogTitle.value = '修改教师'
    dialogVisible.value = true
    employee.value.exprList?.forEach(item => {
      item.experDate = [item.begin, item.end]
    })
  } else {
    ElMessage.error('查询失败')
  }
}

// 头像上传
const handleAvatarSuccess = (res) => {
  employee.value.image = res.data
}
const beforeAvatarUpload = (rawFile) => {
  if (!rawFile.type.includes('image')) {
    ElMessage.error('只能上传图片')
    return false
  }
  if (rawFile.size > 10 * 1024 * 1024) {
    ElMessage.error('图片不能超过10M')
    return false
  }
  return true
}

// 工作经历
const jobOptions = ref([
  { label: '班主任', value: '1' },
  { label: '教师', value: '2' },
  { label: '院长', value: '3' },
  { label: '辅导员', value: '4' },
  { label: '校长', value: '5' },
  { label: '其他', value: '6' }
])
const genderOptions = ref([
  { label: '男', value: '1' },
  { label: '女', value: '2' }
])
// 新增工作经历
const addWorkExperience = () => {
  employee.value.exprList.push({ company: '', job: '', experDate: [] })
}
// 删除工作经历
const removeWorkExperience = (item) => {
  employee.value.exprList = employee.value.exprList.filter(i => i !== item)
}

// 保存
const saveEmployee = async () => {
  formRef.value.validate(async (valid) => {
    if (!valid) return
    let res = null
    if (employee.value.id) {
      res = await updateEmpApi(employee.value)
    } else {
      res = await addEmpApi(employee.value)
    }
    if (res.code) {
      ElMessage.success('操作成功')
      dialogVisible.value = false
      search()
    } else {
      ElMessage.error('操作失败')
    }
  })
}

// 删除
const deleteEmp = async (id) => {
  ElMessageBox.confirm('确定删除该教师吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await deleteEmpApi(id)
    if (res.code) {
      ElMessage.success('删除成功')
      search()
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消')
  })
}

// 批量删除
const selectedEmpIds = ref([])
const handleSelectionChange = (val) => {
  selectedEmpIds.value = val.map(item => item.id)
}
const handleDelete = async () => {
  if (selectedEmpIds.value.length === 0) {
    ElMessage.warning('请选择要删除的教师')
    return
  }
  ElMessageBox.confirm('确定删除选中的教师吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    const res = await deleteEmpApi(selectedEmpIds.value)
    if (res.code) {
      ElMessage.success('删除成功')
      search()
    } else {
      ElMessage.error('删除失败')
    }
  }).catch(() => {
    ElMessage.info('已取消')
  })
}

// 校验
const rules = ref({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 2, max: 20, message: '长度 2-20 位', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度 2-10 位', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1\d{10}$/, message: '手机号格式错误', trigger: 'blur' }
  ]
})
// 监听工作经历变化，更新 begin 和 end 字段值
watch(() => employee.value.exprList, (newVal) => {
  newVal.forEach(item => {
    if (item.experDate && item.experDate.length === 2) {
      item.begin = item.experDate[0]
      item.end = item.experDate[1]
    }
  })
}, { deep: true })
// 从localStorage中获取token
const getToken = () => {
  const userInfo = JSON.parse(localStorage.getItem('userInfo'))
  if (userInfo && userInfo.token) {
    token.value = userInfo.token
  }
}
</script>

<style scoped>
.emp-manage {
  width: 100%;
  padding: 0 20px 20px;
  box-sizing: border-box;
}

.page-title {
  color: #000;
  font-weight: 900;
  text-shadow: 1px 1px 0 #fff, 2px 2px 0 #000;
  margin-bottom: 20px;
  margin-top: 10px;
  font-size: 24px;
}

.query-card {
  margin-bottom: 20px;
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(8px) !important;
  -webkit-backdrop-filter: blur(8px) !important;
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff;
  border-radius: 0 !important;
}

.search-form {
  padding: 15px 10px;
  display: flex;
  align-items: center;
  gap: 20px;
  flex-wrap: wrap;
}

:deep(.el-form-item__label) {
  color: #000;
  font-weight: 600;
  line-height: 40px;
  font-size: 14px;
}

:deep(.form-control) {
  width: 180px !important;
  height: 40px !important;
}

:deep(.datepicker-control) {
  width: 300px !important;
}

:deep(.form-btn) {
  height: 40px !important;
  padding: 0 20px !important;
  border: 2px solid #000 !important;
  box-shadow: 2px 2px 0 #122dc7 !important;
  border-radius: 0 !important;
  font-weight: 600;
  white-space: nowrap !important;
  background: rgba(8, 187, 62, 0.8) !important;
  backdrop-filter: blur(4px) !important;
}

.func-btn-group {
  margin-bottom: 20px;
  display: flex;
  gap: 15px;
  align-items: center;
}

/* ✅ 新增教师 蓝色 */
:deep(.func-btn-group .el-button--primary:nth-child(1)) {
  background: #409EFF !important;
}

/* ✅ 批量删除 红色 */
:deep(.func-btn-group .el-button--primary:nth-child(2)) {
  background: #F56C6C !important;
}

.emp-table {
  margin-bottom: 20px;
}

:deep(.table-btn) {
  border: 2px solid #000 !important;
  box-shadow: 1px 1px 0 #fff !important;
  border-radius: 0 !important;
  white-space: nowrap !important;
  display: inline-block !important;
  margin: 0 2px !important;
  padding: 4px 10px !important;
  backdrop-filter: blur(4px) !important;
  font-weight: 600 !important;
  color: #ffffff !important;
}

:deep(.table-btn.el-button--primary) {
  background: #409EFF !important;
}

:deep(.table-btn.el-button--danger) {
  background: #F56C6C !important;
}

.pagination-wrap {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 10px 0;
}

:deep(.pagination) {
  --el-pagination-button-border-color: #000 !important;
  --el-pagination-item-border-color: #000 !important;
  font-weight: 900 !important;
  color: #000 !important;
  font-size: 14px;
}

:deep(.el-pagination__sizes) {
  display: flex;
  align-items: center;
}

:deep(.el-pagination button),
:deep(.el-pagination__item) {
  border: 2px solid #000 !important;
  border-radius: 0 !important;
  box-shadow: 1px 1px 0 #fff !important;
  height: 32px !important;
  font-weight: 900 !important;
  background: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(4px) !important;
}

:deep(.dialog-form) {
  background: rgba(255, 255, 255, 0.25) !important;
  backdrop-filter: blur(10px) !important;
  -webkit-backdrop-filter: blur(10px) !important;
  padding: 25px !important;
  border-radius: 0 !important;
}

:deep(.dialog-form .el-form-item) {
  margin-bottom: 20px !important;
}

:deep(.dialog-form .el-form-item__label) {
  font-weight: 900 !important;
  font-size: 14px !important;
  color: #000 !important;
  padding-right: 10px !important;
  text-shadow: 1px 1px 0 rgba(255, 255, 255, 0.8);
}

.dialog-footer {
  text-align: right;
  padding: 15px 25px !important;
}

:deep(.dialog-footer .el-button) {
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff !important;
  font-weight: 900 !important;
  padding: 8px 25px !important;
  margin-left: 15px !important;
  border-radius: 0 !important;
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(6px) !important;
  color: #000 !important;
}

:deep(.dialog-footer .el-button--primary) {
  background: #409EFF !important;
  color: #fff !important;
}

:deep(.dialog-form .el-button--success) {
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff !important;
  font-weight: 900 !important;
  border-radius: 0 !important;
  padding: 6px 15px !important;
  background: #67C23A !important;
  backdrop-filter: blur(6px) !important;
  color: #fff !important;
}

:deep(.dialog-form .el-button--danger) {
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff !important;
  font-weight: 900 !important;
  border-radius: 0 !important;
  padding: 6px 15px !important;
  background: #F56C6C !important;
  backdrop-filter: blur(6px) !important;
  color: #fff !important;
}
</style>

<style>
.emp-manage .el-input__wrapper,
.emp-manage .el-select__wrapper,
.emp-manage .el-date-editor__wrapper,
.emp-manage .el-range-editor__wrapper {
  background: rgba(255, 255, 255, 0.85) !important;
  backdrop-filter: blur(4px) !important;
  -webkit-backdrop-filter: blur(4px) !important;
  border: 4px solid #000 !important;
  border-radius: 0 !important;
  height: 45px !important;
  box-shadow: none !important;
  padding: 0 10px !important;
}

.emp-manage .el-input__inner,
.emp-manage .el-select__selected-item,
.emp-manage .el-date-editor input,
.emp-manage .el-range-editor input {
  color: #000 !important;
  font-weight: 900 !important;
  font-size: 14px !important;
  height: 100% !important;
  line-height: 45px !important;
  background: transparent !important;
}

.emp-manage .el-select-dropdown {
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(8px) !important;
  border: 4px solid #000 !important;
  border-radius: 0 !important;
}

.emp-manage .el-select-dropdown__item {
  font-weight: 900 !important;
  color: #000 !important;
  padding: 10px !important;
  background: transparent !important;
}

.emp-manage .el-select-dropdown__item.selected {
  background: rgba(200, 200, 200, 0.5) !important;
  color: #000 !important;
}

.emp-manage .el-date-picker {
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(8px) !important;
  border: 4px solid #000 !important;
  border-radius: 0 !important;
}

.emp-manage .el-date-table td {
  font-weight: 900 !important;
  background: transparent !important;
}

.emp-manage .el-table {
  background: rgba(255, 255, 255, 0.2) !important;
  backdrop-filter: blur(8px) !important;
  -webkit-backdrop-filter: blur(8px) !important;
  border: 3px solid #000 !important;
  --el-table-border-color: #000 !important;
  width: 100% !important;
  table-layout: fixed !important;
}

.emp-manage .el-table tr {
  height: 80px !important;
}

.emp-manage .el-table td,
.emp-manage .el-table th {
  background: rgba(255, 255, 255, 0.15) !important;
  backdrop-filter: blur(4px) !important;
  color: #000 !important;
  font-weight: 600 !important;
  vertical-align: middle !important;
  text-align: center !important;
  padding: 5px !important;
  white-space: nowrap !important;
}

.emp-manage .el-dialog {
  background: rgba(255, 255, 255, 0.3) !important;
  backdrop-filter: blur(12px) !important;
  -webkit-backdrop-filter: blur(12px) !important;
  border: 5px solid #000 !important;
  border-radius: 0 !important;
  box-shadow: 6px 6px 0 #000 !important;
  width: 800px !important;
  margin: 0 auto !important;
  top: 50px !important;
}

.emp-manage .el-dialog__header {
  background: rgba(255, 255, 255, 0.4) !important;
  backdrop-filter: blur(8px) !important;
  border-bottom: 4px solid #000 !important;
  padding: 20px 25px !important;
}

.emp-manage .el-dialog__footer {
  background: rgba(255, 255, 255, 0.4) !important;
  backdrop-filter: blur(8px) !important;
  border-top: 4px solid #000 !important;
  padding: 20px 25px !important;
}

.emp-manage .el-dialog__title {
  font-weight: 900;
  font-size: 20px;
  color: #000 !important;
  text-shadow: 1px 1px 0 rgba(255, 255, 255, 0.9);
}

.emp-manage .el-dialog__close {
  font-size: 24px !important;
  color: #000 !important;
  font-weight: 900 !important;
  background: rgba(255, 255, 255, 0.5) !important;
  backdrop-filter: blur(4px) !important;
  border-radius: 0 !important;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-uploader .el-upload {
  background: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(4px) !important;
  border: 4px solid #000 !important;
  width: 100px;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 0 !important;
}

.avatar {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border: 4px solid #000 !important;
  border-radius: 0 !important;
}

.avatar-uploader-icon {
  font-size: 32px;
  color: #000;
  font-weight: 900;
}

.gender-tag {
  background: rgba(255, 255, 255, 0.9) !important;
  backdrop-filter: blur(4px) !important;
  border: 3px solid #000 !important;
  border-radius: 0 !important;
  font-weight: 900 !important;
  padding: 6px 12px !important;
  font-size: 14px;
  color: #000 !important;
  box-shadow: 2px 2px 0 #000 !important;
  display: inline-block !important;
}

.avatar-img {
  filter: none !important;
  border: 4px solid #000 !important;
  border-radius: 0 !important;
  width: 70px !important;
  height: 70px !important;
  object-fit: cover !important;
  box-shadow: 3px 3px 0 #fff, 5px 5px 0 #000 !important;
  position: relative;
  left: -2px;
  top: -2px;
  display: inline-block !important;
}

.emp-manage .el-pagination .el-select__wrapper {
  height: 32px !important;
  border: 3px solid #000 !important;
  background: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(4px) !important;
}

.emp-manage .el-pagination .el-input__wrapper {
  height: 32px !important;
  border: 3px solid #000 !important;
  background: rgba(255, 255, 255, 0.8) !important;
  backdrop-filter: blur(4px) !important;
}

/* 新增教师 = 蓝色 */
:deep(.func-btn-group .el-button--primary) {
  background: #409EFF !important;
}

/* 批量删除 = 红色 */
:deep(.func-btn-group .el-button--danger) {
  background: #F56C6C !important;
}
</style>