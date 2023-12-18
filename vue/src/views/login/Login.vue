<template>

<!--  overflow: hidden; 可以修改上下滑动，将屏幕固定-->
  <div style="height: 100vh;overflow: hidden; position: relative" >
    <!--
    滑块验证  :accuracy="5"是容错率
     @success  @fail @refresh 三个回调函数
    -->
    <el-card class="cover" v-if="loginAdmin.id">
      <slide-verify :l="42"
                    :r="10"
                    :w="310"
                    :h="155"
                    :accuracy="5"
                    :imgs="['https://cdn.pixabay.com/photo/2022/11/09/12/23/lotus-7580478_960_720.jpg',
                    'https://cdn.pixabay.com/photo/2017/08/29/12/07/adult-2693054_960_720.jpg',
                    'https://cdn.pixabay.com/photo/2022/11/16/15/52/mushrooms-7596258_960_720.jpg']"
                    slider-text="向右滑动"
                    @success="onSuccess"
                    @fail="onFail"
                    @refresh="onRefresh"
      ></slide-verify>
    </el-card>

    <div style="width: 500px;height: 400px;background-color: white;border-radius: 10px;
      margin: 150px auto;padding: 50px">
        <div style="margin: 30px;text-align: center;font-size: 30px;font-weight: bold;color: dodgerblue">登录</div>
        <el-form :model="admin" :rules="rules" ref="loginForm">
          <el-form-item prop="username">
          <!-- prefix-icon 指的是前面的图标-->
            <el-input placeholder="请输入账号" prefix-icon="el-icon-user" size="medium" v-model="admin.username"></el-input>
          </el-form-item>

          <el-form-item prop="password">
            <!-- show-password 隐藏和展示密码-->
            <el-input placeholder="请输入密码" show-password prefix-icon="el-icon-lock" size="medium" v-model="admin.password"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button style="width: 100%" size="medium" type="primary" @click="login">登录</el-button>
          </el-form-item>
        </el-form>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";
//需要加入命令 npm i js-cookie -S 安装js-cookie
import Cookies from 'js-cookie'

export default {
  name:'LOGIN',
  data(){
    return{
      loginAdmin:{},
      admin:{username: 'admin',password: '123'},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
        ],
        password:[
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
        ]
      }
    }
    },
  methods:{
    login() {
      this.$refs['loginForm'].validate((valid) => {
        if (valid) {
          request.post('/admin/login', this.admin).then(res => {
            if (res.code === '200') {
              //登录成功滑块组件就出现了
              this.loginAdmin = res.data
              //this.$notify.success("登录成功");
              //如果登录失败 则data值是为null 所以不会存储
              // if (res.data != 'null'){
              //   //把后端传过来的json对象 转换为字符串 传入到Cookie里
              //   Cookies.set('admin',JSON.stringify(res.data))
              // this.$router.push('/')
                /*解析出字符串*/
               // const user = Cookies.get('user')
               //  console.log(JSON.parse(user))
            } else {
              this.$notify.error(res.msg);
            }
          })
        }
      })
    },
    //滑块验证通过之后触发的
    onSuccess() {
      //前面因为把res.data赋值给了this.loginAdmin，所以括号中填入这个
      Cookies.set('admin', JSON.stringify(this.loginAdmin))
      this.$notify.success("登录成功")
      this.$router.push('/')
    },
    //滑块失败时
    onFail() {
      console.log('onFail')
    },
    //滑块刷新时
    onRefresh() {
      console.log('refresh')
    }
  }
}
</script>

<style>
/* 页面居中 并且置顶 */
.cover {
  width: fit-content;
  background-color: white;
  position: absolute;
  top:50%;
  left:50%;
  transform: translate(-50%, -50%);
  z-index: 1000;
}
</style>
