/*
*  用这个工具库来请求后端接口
* */
import axios from 'axios'
import router from "@/router";
import Cookies from 'js-cookie'

//这个相等于new一个axios对象为request
const request = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 5000
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
//比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';


    //因为没有请求 所以不能达到点击网址直接是/login 页面 需要在HomeView中的Created()来请求数据
    //请求拦截器 使得在没有完成登录成功 无法跳转到别的页面(强制跳转登录页)
    const adminJson = Cookies.get('admin')
    if (adminJson){
        // 设置请求头
        config.headers['token'] = JSON.parse(adminJson).token;

        //跳转到/login
        // router.push('/login')
    }

    // config.headers['token'] = user.token;  // 设置请求头
    return config
}, error => {
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        //res包含data msg code
        let res = response.data;
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            //进行json解析
            res = res ? JSON.parse(res) : res
        }
        if (res.code === '401'){
            router.push('/login')
        }
        return res;
    },
    error => {
        console.log('err' + error) // for debug
        return Promise.reject(error)
    }
)

export default request