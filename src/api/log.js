import request from '@/utils/request'
// 日志接口
 export const logApi =(page,pageSize)=>{
    return request.get(`/log?page=${page}&pageSize=${pageSize}`)
}
