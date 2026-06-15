import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'



//创建axios实例对象
const request = axios.create({
    baseURL: '/api',
    timeout: 600000
})

//axios的响应 response 拦截器
request.interceptors.response.use(
    (response) => { //成功回调
        return response.data
    },
    (error) => { //失败回调
        //判断是否是401错误
        if (error.response.status == 401) {
            // 401错误表示token过期
            ElMessage.error('token过期，请重新登录')
            // 清除localStorage中的userInfo
            localStorage.removeItem('userInfo')
            // 跳转到登录页
            router.push('/login')
        }
        return Promise.reject(error)
    }
)

//axios请求拦截器,request获取token
request.interceptors.request.use(
    (config) => { //成功回调
        // 从localStorage中获取userInfo
        const userInfo = JSON.parse(localStorage.getItem('userInfo'))
        // 如果userInfo存在，就添加到localStorage中
        if (userInfo && userInfo.token) {
            config.headers.token = userInfo.token
        }
        return config
    },
    (error) => { //失败回调
        return Promise.reject(error)
    }
)
export default request