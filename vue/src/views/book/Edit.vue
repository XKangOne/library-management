<template>
  <div style="width: 90%">
    <h3 style="margin-bottom: 20px">编辑管理员</h3>
    <el-form :inline="true" :model="form" :rules="rules" ref="ruleForm" label-width="100px">

      <el-form-item label="图书名" prop="name">
        <el-input v-model="form.name" placeholder="请输入图书名"></el-input>
      </el-form-item>

      <el-form-item label="描述" prop="description">
        <el-input type="textarea" style="width: 400px" v-model="form.description" placeholder="请输入图书描述"></el-input>
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
            :options="categories"></el-cascader>
      </el-form-item>

      <el-form-item label="标准码" prop="bookNo">
        <el-input v-model="form.bookNo" placeholder="请输入图书标准码"></el-input>
      </el-form-item>

      <el-form-item label="借书积分" prop="score">
        <el-input-number v-model="form.score"  :min="1" :max="10" label="描述文字"></el-input-number>
      </el-form-item>

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

      <el-form-item label="封面" prop="cover">
        <el-input v-model="form.cover" placeholder="请输入图书封面"></el-input>
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
import Cookies from "js-cookie";
export default {
  name:'EditBook',
  data(){
    return{
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      form:{ score:10 },
      categories:[],
      rules: {
        name: [
          {required: true, message: '请输入图书名称', trigger: 'blur'},
        ],
        bookNo: [
          {required: true, message: '请输入图书标准码', trigger: 'blur'},
        ],
        score: [
          {required: true, message: '请输入借书积分', trigger: 'blur'}
        ],
      }

    }
  },
  //向后端请求接口
  created() {
    request.get('/category/tree').then(res =>{
      this.categories = res.data
    })

    //获取id
    const id = this.$route.query.id
    request.get("/book/"+id).then(res=>{
      //把获取的数据给表单中
      this.form = res.data
      //如果有值
      if (this.form.category) {
        //将分割的 > 展示出来
        this.form.categories = this.form.category.split('>')
        console.log(this.form.categories)
      }
    })
  },
  methods:{
    handleCoverSuccess(res) {
      if (res.code === '200') {
        // this.form.cover = res.data
        this.$set(this.form, 'cover', res.data)
      }
    },
    save(){
      request.put('/book/update',this.form).then(res=>{
        if (res.code === '200'){
          this.$notify.success('更新成功')
          //注意此处是router.push 来跳转到/admin 界面
          this.$router.push('/bookList')
        }else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>
