<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">编辑管理员</h3>
    <el-form :inline="true" :model="form" label-width="100px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" placeholder="请输入用户名"></el-input>
      </el-form-item>

      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" placeholder="请输入邮箱"></el-input>
      </el-form-item>

      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码"></el-input>
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
  name:'EditAdmin',
  data(){
    return{
      form:{}
    }
  },
  created() {
    //获取id
    const id = this.$route.query.id
    request.get("/admin/"+id).then(res=>{
      //把获取的数据给表单中
      this.form = res.data
    })
  },
  methods:{
    save(){
      request.put('/admin/update',this.form).then(res=>{
        if (res.code === '200'){
          this.$notify.success('更新成功')
          //注意此处是router.push 来跳转到/admin 界面
          this.$router.push('/adminList')
        }else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
