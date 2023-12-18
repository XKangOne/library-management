<template>
  <div>
    <!--搜索表单  type="primary"修改了按钮的背景颜色 -->
    <div style="margin-bottom: 10px">
      <el-input style="width: 240px" placeholder="请输入用户名" v-model="params.username"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入邮箱" v-model="params.email"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class=" el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="rest"><i class=" el-icon-refresh"></i>重置</el-button>
    </div>

    <div class="t1">
    <!--   展示数据 -->
    <el-table :data="tableData" stripe  >
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名"></el-table-column>
      <el-table-column prop="phone" label="联系方式"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="updatetime" label="更新时间"></el-table-column>
      <el-table-column prop="password" label="密码"></el-table-column>

      <el-table-column label="状态" width="230">
        <!--
            scope.row 就是当前行数据
            更新之后触发一个函数@change changeStatus()
            scope.row.status 当前行状态
        -->
        <template v-slot="scope">
          <el-switch
              v-model="scope.row.status"
              @change="changeStatus(scope.row)"
              active-color="#13ce66"
              inactive-color="#ff4949">
          </el-switch>
        </template>
      </el-table-column>

      <!-- 编辑-->
      <el-table-column label="操作" width="230px">
        <!-- scope.row 就是当前行数据-->
        <template v-slot="scope">
          <!--  scope.row.id 这个行 id的数据        -->
          <el-button type="primary" @click="$router.push('/editAdmin?id='+scope.row.id)">编辑</el-button>

          <el-popconfirm
              style="margin-left: 5px"
              title="您确定删除这行数据吗？"
              @confirm="del(scope.row.id)" >

            <el-button type="danger" slot="reference" class="t3" >删除</el-button>
          </el-popconfirm>
          <el-button style="margin-left: 5px" type="warning" @click="handleChangePass(scope.row)">修改密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    </div>

    <!-- 分页 :page-size="5" 每页5个 :total="10"总数为10 所以显示2页 -->
    <div style="margin-top: 20px">
      <el-pagination
          background
          :current-page="params.pageNum"
          :page-size="params.pageSize"
          layout="prev, pager, next"
          @current-change="handleCurrentChange"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="formRef" :rules="rules">
        <el-form-item label="新密码">
          <el-input v-model="form.newPass" autocomplete="off" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="savePass">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from 'js-cookie'

export default {
  name: 'Admin',
  data() {
    return {
      tableData: [
        // {name: '王二', age: 20, address: '北京市', phone: '12343546', sex: '男'},
        // {name: '王二', age: 20, address: '北京市', phone: '12343546', sex: '男'},
        // {name: '王二', age: 20, address: '北京市', phone: '12343546', sex: '女'},
      ],
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      total:0,
      form:{},
      dialogFormVisible: false,
      params:{
        pageNum:1,
        pageSize:5,
        username:'',
        phone: '',
        email:''
      },
      rules: {
        newPass: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur'}
          ],
        }
    }
  },
  created() {
    this.load();
  },

  // res 指的就是页面方面的展示的json数据
  methods:{
    changeStatus(row){
      if(this.admin.id === row.id && !row.status){
        row.status = true
        this.$notify.warning('您的操作不合法')
        return
      }
      request.put('/admin/update',row).then(res=>{
        if (res.code === '200'){
          this.$notify.success('更新成功')
          //注意此处是router.push 来跳转到/admin 界面
          this.$router.push('/adminList')
        }else {
          this.$notify.error(res.msg)
        }
      })
    },

    //让弹窗出现
    handleChangePass(row){
      //获取这个行的字符串
      this.form = JSON.parse(JSON.stringify(row))
      //把弹窗打开
      this.dialogFormVisible = true
    },
    //保存修改密码的值
    savePass(){
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          request.put('/admin/password',this.form).then(res =>{
            if (res.code === '200'){
              this.$notify.success("修改成功")
              //当前修改的用户id 等于当前登录的管理员id,那么修改成功之后需要重新登录
              if (this.form.id === this.admin.id){
                Cookies.remove('admin')
                this.$router.push('/login')
              }else {
                //重新加载页面
                this.load()
                //关闭弹窗
                this.dialogFormVisible = false
              }
            }else {
              this.$notify.error("修改失败")
            }
          })
        }
      })
    },
    load(){
      //通过request调用这个接口
      request.get('/admin/page',{
        params:this.params
      }).then(res =>{
        if (res.code === '200') {
          this.tableData = res.data.list
          this.total = res.data.total
        }
      })
    },
    //重置
    rest() {
      this.params = {
        pageNum: 1,
        pageSize: 5,
        username: '',
        phone: '',
        email: ''
      },
          //重新加载
          this.load()
    },
    //这里的pageNum是自己指明的，与上面pageNum无关
    handleCurrentChange(pageNum){
      //点击分页按钮触发分页
      console.log(pageNum)
      this.params.pageNum = pageNum
      this.load()
    },
    del(id){
      request.delete("/admin/delete/" + id).then(res =>{
          if (res.code === '200'){
              this.$notify.success("删除成功")
            //重新加载页面
              this.load()
          }else {
            this.$notify.error(res.msg)
          }
      })
    }
  }
}
</script>