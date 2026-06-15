import request from '@/utils/request'
//查询分页学生列表
export const queryStuListApi = ( searchForm,page,pageSize) => {
  return request.get(`/students?name=${searchForm.name}&deptId=${searchForm.deptId}&clazzId=${searchForm.clazzId}&page=${page}&pageSize=${pageSize}`);
}
//添加学生
export const addStuApi = ( stuForm) => {
  return request.post(`/students`, stuForm);
}
//删除学生
export const deleteStuApi = ( id) => {
  return request.delete(`/students/${id}`);
}
//更新学生
export const updateStuApi = ( stuForm) => {
  return request.put(`/students`, stuForm);
}
//查询学生详情
export const queryStuDetailApi = (id) => {
  return request.get(`/students/${id}`);
}
//修改违纪记录
export const updateViolationApi = ( id,score) => {
  return request.put(`/students/violation/${id}/${score}`);
}
