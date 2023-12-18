<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">编辑用户</h3>
    <el-form :inline="true" :model="form" label-width="100px">
      <el-form-item label="卡号">
        <el-input v-model="form.username" disabled></el-input>
      </el-form-item>

      <el-form-item label="姓名">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>

      <el-form-item label="年龄">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.sex" placeholder="请输入性别"></el-input>
      </el-form-item>
      <el-form-item label="联系方式">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>

      <el-form-item label="地址">
        <el-input v-model="form.address" placeholder="请输入地址"></el-input>
      </el-form-item>
    </el-form>

    <div style="text-align: center;margin-top: 10px"  >
      <el-button type="primary" @click="save" size="medium">提交</el-button>
      <!--      <el-button type="danger">取消</el-button>-->
    </div>
  </div>
</template>
<script>
import request from "@/utils/request";
export default {
  name:'AddUser',
  data(){
    return{
      form:{}
    }
  },
  created() {
    //获取id
    const id = this.$route.query.id
    request.get("/user/"+id).then(res=>{
      this.form = res.data
    })
  },
  methods:{
    save(){
      //this.form 发送一个form表单的数据
      request.put('/user/update',this.form).then(res=>{
        if (res.code === '200'){
          this.$notify.success('更新成功')
          //注意此处是router.push 来跳转到/user界面
          this.$router.push('/userList')
        }else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>

