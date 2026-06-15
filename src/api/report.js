import request from '@/utils/request'
//查询所有教师报告
export const queryAllReportApi = () => {
  return request.get('/report/empJobData');
}   
//查询所有学生报告
export const queryAllStuReportApi = () => {
  return request.get('/report/studentDegreeData');
}   
