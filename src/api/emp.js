import request from '@/utils/request'

//查询教师列表
export const queryEmpListApi = (name,gender,begin,end,page,pageSize) => {
  return request.get(`/emps?name=${name}&gender=${gender}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
}
//保存教师
export const addEmpApi = (emp) => {
  return request.post('/emps',emp)
}
//根据id查询教师详情
export const queryEmpDetailApi = (id) => {
  return request.get(`/emps/${id}`)
}
//根据教师id修改教师
export const updateEmpApi = (emp) => {
  return request.put('/emps',emp)
}
//根据教师id删除教师
export const deleteEmpApi = (ids) => {
  return request.delete(`/emps?id=${ids}`)
}