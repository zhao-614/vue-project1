import request from '@/utils/request';
//查询所有部门
 export const queryAllApi = () => {
  return request.get('/depts');
}
//新增部门
export const addDeptApi = (data) => {
  return request.post('/depts', data);
}
//根据id查询部门详情
export const queryDeptById = (id) => request.get(`/depts/${id}`);
//修改部门
export const updateDeptApi = (data) => request.put('/depts', data);
//删除部门
export const deleteDeptApi = (id) => request.delete(`/depts?id=${id}`);
