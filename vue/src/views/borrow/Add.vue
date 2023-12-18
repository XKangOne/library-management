<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">新增借书记录</h3>
    <!--   :rules="rules" ref="ruleForm" 这两个是为了校验-->
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">

      <!-- clearable 选择之后可以去掉-->
      <el-form-item label="图书标准码" prop="bookNo">
        <el-select v-model="form.bookNo" clearable filterable placeholder="请选择" @change="selBook">
          <el-option
              v-for="item in books"
              :key="item.id"
              :label="item.bookNo"
              :value="item.bookNo">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="图书名称" prop="bookName">
        <el-input v-model="form.bookName" disabled placeholder="请输入名称"></el-input>
      </el-form-item>

      <el-form-item label="借书积分" prop="score">
        <el-input-number v-model="form.score" disabled @change="handleChange" :min="1" :max="10"
                         label="描述文字"></el-input-number>
      </el-form-item>

      <el-form-item label="图书数量" prop="nums">
        <el-input v-model="form.nums" disabled></el-input>
      </el-form-item>
      <br/>

      <el-form-item label="会员码" prop="userNo">
        <el-select v-model="form.userNo" filterable placeholder="请选择" @change="selUser">
          <el-option
              v-for="item in users"
              :key="item.id"
              :label="item.username"
              :value="item.username">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="用户名称" prop="userName">
        <el-input v-model="form.userName" disabled></el-input>
      </el-form-item>

      <el-form-item label="用户联系方式" prop="userPhone">
        <el-input v-model="form.userPhone" disabled></el-input>
      </el-form-item>

      <el-form-item label="用户账户积分" prop="account">
        <el-input v-model="form.account" disabled></el-input>
      </el-form-item>

      <el-form-item label="借出的天数" prop="days">
        <el-input-number v-model="form.days" :min="1" :max="30"
                         label="借出的天数"></el-input-number>
      </el-form-item>
    </el-form>

    <div style="text-align: center;margin-top: 10px">
      <el-button type="primary" @click="save" size="medium">提交</el-button>
    </div>
  </div>
</template>

<script>
//需要在package.json 引入axios,然后引入工具包
import request from "@/utils/request";

export default {
  name: 'AddBook',
  data() {
    return {
      form: {days: 1},
      books: [],
      users: [],
      //校验绑定
      rules: {
        bookNo: [
          {required: true, message: '请输入图书标准码', trigger: 'blur'},
        ],
        userNo: [
          {required: true, message: '请输入会员码', trigger: 'blur'},
        ],
      }
    }
  },
  //请求book/list 接口 获取book中的值
  created() {
    request.get('/book/list').then(res => {
      //把值赋给books 与上面book[] 数组相对于 就可以调用其中的值
      this.books = res.data
    })
    //请求user/list 接口 获取user中的值
    request.get('/user/list').then(res => {
      //把值赋给users 与上面users[] 数组相对于 就可以调用其中的值,filter过滤出status为true 才允许借书
      this.users = res.data.filter(v => v.status)
    })
  },
  methods: {
    save() {
      //这个要注意与上面的对应绑定
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/borrow/save', this.form).then(res => {
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
    },
    handleChange(val) {
      console.log(val);
    },
    //这两个目的是将产生联动的关系，一个出来 其他都出来
    selBook() {
      //将选择的bookNo 标准码 与表格中的标准码相等就赋值给book
      const book = this.books.find(v => v.bookNo === this.form.bookNo)
      request.get('/book/' + book.id).then(res => {
        //强制设置对象属性
        this.$set(this.form, 'bookName', res.data.name)
        this.form.score = res.data.score
        this.form.nums = res.data.nums
      })
    },
    selUser() {
      const user = this.users.find(v => v.username === this.form.userNo)
      request.get('/user/' + user.id).then(res => {
        //强制设置对象属性
        this.$set(this.form, 'userName', res.data.name)
        // this.form.userName = user.name
        this.form.userPhone = res.data.phone
        this.form.account = res.data.account
      })
    }
  }
}
</script>