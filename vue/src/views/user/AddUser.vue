<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">新增用户</h3>
<!--    :rules="rules" ref="ruleForm" 这两个是为了校验-->
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" placeholder="请输入姓名"></el-input>
      </el-form-item>

      <el-form-item label="年龄" prop="age">
        <el-input v-model="form.age" placeholder="请输入年龄"></el-input>
      </el-form-item>

      <el-form-item label="性别" prop="sex">
        <el-select v-model="form.sex"  placeholder="请选择性别" clearable="" >
          <el-option label="男" value="male"></el-option>
          <el-option label="女" value="female"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="联系方式" prop="phone">
        <el-input v-model="form.phone" placeholder="请输入联系方式"></el-input>
      </el-form-item>
      <el-form-item label="地址" prop="address">
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
    const checkAge = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('年龄不能为空'));
      }
      //监测出value的值是个字符串
      // console.log(typeof value)
      setTimeout(() => {
        if (!/^[0-9]+$/.test(value)) {
          callback(new Error('请输入数字值'));
        }else if (parseInt(value)>120 || parseInt(value)<=0) {
          callback(new Error('请输入合理的年龄'));
        }else {
          callback();
        }
      }, 500);
    };
    const checkPhone = (rule, value, callback) => {
      setTimeout(() => {
        if (!/^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value)) {
          callback(new Error('请输入合法手机号'));
        }else {
          callback();
        }
      }, 500);
    };
    return{
      form:{},
      rules: {
        name: [
          {required: true, message: '请输入姓名', trigger: 'blur'},
          // {min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        age:[
          { validator: checkAge, trigger: 'blur' }
        ],
        phone:[
          { validator: checkPhone, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    save() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/user/save', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('新增成功')
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

<style scoped>

</style>