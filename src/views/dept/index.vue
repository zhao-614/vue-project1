<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import request from '@/utils/request';
import { ElMessage, ElMessageBox } from 'element-plus';
import { EditPen, DeleteFilled } from '@element-plus/icons-vue';

import { queryAllApi, addDeptApi, queryDeptById, updateDeptApi, deleteDeptApi } from '@/api/dept';

// 表单引用
const deptFormRef = ref(null);


// 表单验证规则
const rules = ref({
  name: [
    { required: true, message: '请输入部门名称', trigger: 'blur' },
    { min: 2, max: 10, message: '部门名称长度必须在2到10个字符之间', trigger: 'blur' },
  ],
});

// 示例数据
const deptList = ref([]);

//钩子函数
onMounted(() => {
  search();
});

//查询所有部门
const search = async () => {
  try {
    const response = await queryAllApi();
    if (response.code) {
      deptList.value = response.data;
    }
  } catch (error) {
    console.error('Error fetching dept list:', error);
  }
}

// 新增部门 - 打开弹窗
const addDept = () => {
  dialogFormVisible.value = true;
  formTitle.value = '新增部门';
  dept.value = { name: '' };
  if (deptFormRef.value) {
    deptFormRef.value.resetFields();
  }
}

// 弹窗相关
const dialogFormVisible = ref(false);
const formTitle = ref('');
const dept = ref({ name: '' });

// 保存部门
const saveDept = async () => {
  try {
    const valid = await deptFormRef.value.validate();
    if (valid) {
      let response;
      if (dept.value.id) {
        response = await updateDeptApi(dept.value);
      } else {
        response = await addDeptApi(dept.value);
      }
      if (response.code) {
        ElMessage.success(dept.value.id ? '修改成功' : '新增成功');
        dialogFormVisible.value = false;
        search();
        dept.value = { name: '' };
        if (deptFormRef.value) {
          deptFormRef.value.resetFields();
        }
      } else {
        ElMessage.error(response.msg || '新增失败');
      }
    }
  } catch (error) {
    ElMessage.error('操作失败：' + (error.message || '表单验证失败'));
  }
}


// 删除部门
// 删除部门（和学员管理页面完全一致的风格）
const handleDelete = (id) => {
  ElMessageBox.confirm(
    '确定删除该部门吗？', 
    '提示', 
    {
      confirmButtonText: '确定',   // 替换OK为“确定”
      cancelButtonText: '取消',    // 替换Cancel为“取消”
      type: 'warning',
      // 可选：添加漫画风格适配（如果需要更贴合的样式）
      customClass: 'glass-confirm-box' 
    }
  )
  .then(async () => {
    try {
      const response = await deleteDeptApi(id);
      if (response.code) {
        ElMessage.success('删除成功');
        search();
      } else {
        ElMessage.error(response.msg || '删除失败');
      }
    } catch (error) {
      ElMessage.error('操作失败：' + (error.message || '删除失败'));
    }
  })
  .catch(() => {
    ElMessage.info('已取消删除');
  })
};
//根据id查询部门详情
const editPen = async (id) => {
  try {
    const response = await queryDeptById(id);
    if (deptFormRef.value) {
      deptFormRef.value.resetFields();
    }
    if (response.code) {
      dialogFormVisible.value = true;
      dept.value = response.data;
      formTitle.value = '修改部门';
    } else {
      ElMessage.error(response.msg || '查询部门详情失败');
    }
  } catch (error) {
    ElMessage.error('操作失败：' + (error.message || '查询部门详情失败'));
  }
}

</script>

<template>
  <div class="dept-manage">
    <h1 class="page-title">部门管理</h1>

    <!-- 按钮 -->
    <el-button type="primary" @click="addDept" class="add-btn">
      + 新增部门
    </el-button>

    <!-- 高透表格 -->
    <el-table :data="deptList" border style="width: 100%" :header-cell-style="{
      background: 'rgba(255,255,255,0.2)',
      color: '#000',
      fontWeight: '600'
    }">
      <el-table-column type="index" label="序号" width="100" align="center" />
      <el-table-column prop="name" label="部门名称" width="300" align="center" />
      <el-table-column prop="updateTime" label="最后修改时间" width="300" align="center" />
      <el-table-column fixed="right" label="操作" align="center">
        <template #default="scope">
          <el-button size="small" @click="editPen(scope.row.id)">
            <el-icon>
              <EditPen />
            </el-icon>修改
          </el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.row.id)">
            <el-icon>
              <DeleteFilled />
            </el-icon>删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 高透弹窗 -->
    <el-dialog v-model="dialogFormVisible" :title="formTitle" width="500" custom-class="glass-dialog">
      <el-form :model="dept" :rules="rules" ref="deptFormRef">
        <el-form-item label="部门名称" :label-width="100" prop="name">
          <el-input v-model="dept.name" placeholder="请输入部门名称" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取消</el-button>
          <el-button type="primary" @click="saveDept">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped>
/* 🔥 全局高透玻璃漫画风格 */
.dept-manage {
  width: 100%;
}

/* 标题 */
.page-title {
  color: #000;
  font-weight: 900;
  text-shadow: 1px 1px 0 #fff, 2px 2px 0 #000;
  margin-bottom: 20px;
}

/* 按钮 */
.add-btn {
  float: right;
  margin-bottom: 20px;
  border: 2px solid #000 !important;
  box-shadow: 2px 2px 0 #fff !important;
}
</style>

<style>
/* 表格高透玻璃 */
.el-table {
  background: rgba(255, 255, 255, 0.15) !important;
  backdrop-filter: blur(1px);
  -webkit-backdrop-filter: blur(1px);
  border: 3px solid #000 !important;
  box-shadow: 2px 2px 0 #fff, 3px 3px 5px rgba(0, 0, 0, 0.1) !important;
  --el-table-border-color: #000 !important;
}

.el-table tr {
  background: transparent !important;
}

.el-table td {
  background: rgba(255, 255, 255, 0.1) !important;
  border-color: #000 !important;
  color: #000 !important;
  font-weight: 600 !important;
}

.el-table--striped .el-table__row--striped td {
  background: rgba(255, 255, 255, 0.2) !important;
}

/* 弹窗高透玻璃 */
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
}

.glass-dialog .el-dialog__footer {
  background: transparent !important;
  border-top: 2px solid #000 !important;
}

/* 输入框高透 */
.glass-dialog .el-input__inner {
  background: rgba(255, 255, 255, 0.2) !important;
  border-color: #000 !important;
  color: #000 !important;
}
/* 其他原有样式不变，新增以下内容 */
/* 删除确认弹窗 - 高透漫画风格 */
.glass-confirm-box {
  background: rgba(255, 255, 255, 0.25) !important;
  backdrop-filter: blur(3px) !important;
  -webkit-backdrop-filter: blur(3px) !important;
  border: 3px solid #000 !important;
  box-shadow: 3px 3px 0 #fff, 5px 5px 10px rgba(0, 0, 0, 0.2) !important;
  border-radius: 0 !important;
}

/* 弹窗按钮匹配风格 */
.glass-confirm-box .el-message-box__btns button {
  border: 2px solid #000 !important;
  box-shadow: 2px 2px 0 #fff !important;
  border-radius: 0 !important;
}

.glass-confirm-box .el-message-box__header {
  border-bottom: 2px solid #000 !important;
  background: transparent !important;
}

.glass-confirm-box .el-message-box__content {
  background: transparent !important;
  color: #000 !important;
  font-weight: 600 !important;
}
</style>