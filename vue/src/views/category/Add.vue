<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">新增分类</h3>
<!--    :rules="rules" ref="ruleForm" 这两个是为了校验-->
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="图书名" prop="name">
        <el-input v-model="form.name" placeholder="请输入图书名"></el-input>
      </el-form-item>

      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" placeholder="请输入备注"></el-input>
      </el-form-item>

    </el-form>
    <div style="text-align: center;margin-top: 10px"  >
      <el-button type="primary" @click="save" size="medium">提交</el-button>
    </div>
  </div>
</template>

<script>
//需要在package.json 引入axios,然后引入工具包
import request from "@/utils/request";
export default {
  name:'AddCategory',
  data(){
    return{
      form:{},
      //校验绑定
      rules: {
       name: [
          {required: true, message: '请输入分类名称', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    save() {
      //这个要注意与上面的对应绑定
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/category/save', this.form).then(res => {
            if (res.code === '200') {
              //弹出提示信息
              this.$notify.success('新增成功')
              //清空表单格
              this.$refs['ruleForm'].resetFields();
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    }
  }
}
</script>