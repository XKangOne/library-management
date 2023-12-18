<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">新增图书</h3>
    <!--    :rules="rules" ref="ruleForm" 这两个是为了校验-->
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">

      <el-form-item label="图书名" prop="name">
        <el-input v-model="form.name" placeholder="请输入图书名"></el-input>
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input type="textarea" style="width: 400px" v-model="form.description"
                  placeholder="请输入图书描述"></el-input>
      </el-form-item>

      <el-form-item label="出版日期" prop="publishDate">
        <el-date-picker
            v-model="form.publishDate"
            type="date"
            placeholder="请选择出版日期">
        </el-date-picker>
      </el-form-item>

      <el-form-item label="作者" prop="author">
        <el-input v-model="form.author" placeholder="请输入作者"></el-input>
      </el-form-item>

      <el-form-item label="出版社" prop="publisher">
        <el-input v-model="form.publisher" placeholder="请输入出版社"></el-input>
      </el-form-item>

      <el-form-item label="分类" prop="category">
        <!--:props="{value:'name',label:'name'}" 如果没有这个 会导致没有任何值的显示-->
        <el-cascader
            style="width: 220px"
            :props="{value:'name',label:'name'}"
            v-model="form.categories"
            :options="categories"
            @change="handleChange"></el-cascader>
      </el-form-item>

      <el-form-item label="标准码" prop="bookNo">
        <el-input v-model="form.bookNo" placeholder="请输入图书标准码"></el-input>
      </el-form-item>

      <el-form-item label="借书积分" prop="score">
        <el-input-number v-model="form.score" @change="handleChange" :min="1" :max="10"
                         label="描述文字"></el-input-number>
      </el-form-item>

      <el-form-item label="数量" prop="nums">
        <el-input v-model="form.nums" placeholder="请输入数量"></el-input>
      </el-form-item>
      <br/>
      <el-form-item label="封面" prop="cover">
        <el-upload
            class="avatar-uploader"
            :action="`http://localhost:8080/api/book/file/upload?token=`+this.admin.token"
            :show-file-list="false"
            :on-success="handleCoverSuccess">
          <img v-if="form.cover" :src="form.cover" class="avatar">
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
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
import Cookies from 'js-cookie'

export default {
  name: 'AddBook',
  data() {
    const checkNums = (rule, value, callback) => {
      //将值转换为整型
      value = parseInt(value)
      if (!Number.isInteger(value) || value < 0 || value >= 1000) {
        callback(new Error('请输入大于等于0小于1000的整数'))
      }
      callback()
    }
    return {
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      form: {score: 10,cover:''},
      categories: [],
      //校验绑定
      rules: {
        name: [
          {required: true, message: '请输入图书名称', trigger: 'blur'},
        ],
        bookNo: [
          {required: true, message: '请输入图书标准码', trigger: 'blur'},
        ],
        score: [
          {validator: checkNums, trigger: 'blur'},
        ],
        nums: [
          {required: true, message: '请输入数量', trigger: 'blur'},
          {validator: checkNums, trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    request.get('/category/tree').then(res => {
      this.categories = res.data
      console.log(this.categories)
    })
  },
  methods: {
    handleCoverSuccess(res) {
      if (res.code === '200') {
        //显示头像方式一：
        // this.form.cover = res.data
        //显示头像方式二：
        //强制显示头像出来，光光使用上面无法显示，需要在data 中 form: {score: 10,cover:''},加入进去
        this.$set(this.form, 'cover', res.data)
        console.log(res.data)
      }
    },
    save() {
      //这个要注意与上面的对应绑定
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/book/save', this.form).then(res => {
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
    }
  }
}
</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>