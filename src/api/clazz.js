import request from '@/utils/request';
//查询所有班级
export const queryAllClazzApi = () => {
  return request.get('/clazzs/list');
}
//查询班级列表
export const queryClazzListApi = (name,begin,end,page,pageSize) => {
  return request.get(`/clazzs?name=${name}&begin=${begin}&end=${end}&page=${page}&pageSize=${pageSize}`)
}
//查询班主任
export const queryMasterListApi = () => {
  return request.get('/clazzs/masters');
}
//新增班级
export const addClazzApi = (data) => {
  return request.post('/clazzs', data);
}
//根据班级id查询班级详情
export const queryClazzDetailApi = (id) => request.get(`/clazzs/${id}`);
//修改班级
export const updateClazzApi = (data) => request.put('/clazzs', data);
//删除班级
export const deleteClazzApi = (id) => request.delete(`/clazzs/${id}`);
