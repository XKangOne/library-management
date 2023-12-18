<template>
  <div>
    <!--
    搜索表单  type="primary"修改了按钮的背景颜色
      v-model="params.name" 绑定数据
    -->
    <div style="margin-bottom: 10px">
      <el-input style="width: 240px" placeholder="请输入名称" v-model="params.name" ></el-input>
      <el-input style="width: 240px; margin-left: 5px" placeholder="请输入联系方式" v-model="params.phone"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class=" el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="primary" @click="rest"><i class=" el-icon-refresh"></i>重置</el-button>
    </div>

    <div class="t1">
    <!--   展示数据 -->
    <el-table :data="tableData" stripe >
      <el-table-column prop="id" label="编号" width="80"></el-table-column>
      <el-table-column prop="username" label="会员号" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="age" label="年龄"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column prop="phone" label="联系方式"></el-table-column>
      <el-table-column prop="sex" label="性别"></el-table-column>
      <el-table-column prop="account" label="账号积分"></el-table-column>
      <el-table-column prop="createtime" label="创建时间"></el-table-column>
      <el-table-column prop="updatetime" label="更新时间"></el-table-column>
      <el-table-column label="状态">
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
      <el-table-column label="操作" width="230">
        <!--scope.row 就是当前行数据-->
        <template v-slot="scope">
          <!--  scope.row.id 这个行 id的数据        -->
          <el-button type="warning" @click="handleAccountAdd(scope.row)">充值</el-button>
          <el-button type="primary" @click="$router.push('/editUser?id='+scope.row.id)">编辑</el-button>

          <el-popconfirm
              style="margin-left: 5px"
              title="您确定删除这行数据吗？"
              @confirm="del(scope.row.id)" >

            <el-button type="danger" slot="reference">删除</el-button>
          </el-popconfirm>
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

      <!-- 弹窗-->
    <el-dialog title="充值" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules" style="width: 85%">
        <el-form-item label="当前账号积分" prop="account">
      <!-- show-password 设置隐藏密码 -->
          <el-input disabled v-model="form.account" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="积分" prop="score">
          <el-input v-model="form.score" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addAccount">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: 'User',
  data() {
    const checkNums = (rule, value, callback) => {
      //将值转换为整型
      value = parseInt(value)
      if (value < 10 || value >= 200) {
        callback(new Error('请输入大于等于10或小于等于200的整数'))
      }
      callback()
    }
    return {
      tableData: [
        // {name: '王二', age: 20, address: '北京市', phone: '12343546', sex: '男'},
        // {name: '王二', age: 20, address: '北京市', phone: '12343546', sex: '男'},
        // {name: '王二', age: 20, address: '北京市', phone: '12343546', sex: '女'},
      ],
      total:0,
      form:{},
      params:{
        pageNum:1,
        pageSize:5,
        name:'',
        phone: ''
      },
      dialogFormVisible: false,
      rules: {
        score:[
          {required: true, message: '请输入积分', trigger: 'blur'},
          {validator: checkNums, trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.load();
  },

  // res 指的就是页面方面的展示的json数据
  methods: {
    load() {
      // fetch("http://localhost:8080/user/list").then(res => res.json()).then(res =>{
      //   console.log(res)
      //   this.tableData = res;
      // })
      //通过request调用这个接口
      request.get('/user/page', {
        params: this.params
      }).then(res => {
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
        name: '',
        phone: ''
      },
          //重新加载
          this.load()
    },
    //这里的pageNum是自己指明的，与上面pageNum无关
    handleCurrentChange(pageNum) {
      //点击分页按钮触发分页
      console.log(pageNum)
      this.params.pageNum = pageNum
      this.load()
    },
    del(id) {
      request.delete("/user/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success("删除成功")
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    },
    handleAccountAdd(row) {
      //将这行数据进行解析
      this.form = JSON.parse(JSON.stringify(row))
      //弹窗打开
      this.dialogFormVisible = true
    },
    addAccount() {
      //这个要注意与上面的对应绑定
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          request.post('/user/account', this.form).then(res => {
            if (res.code === '200') {
              this.$notify.success('充值成功')
              //清空表单格
              this.$refs['ruleForm'].resetFields();
              //保存成功弹窗关闭
              this.dialogFormVisible = false
              this.load()
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    },
    changeStatus(row){
      request.put('/user/update',row).then(res=>{
        if (res.code === '200'){
          this.$notify.success('更新成功')
          //注意此处是router.push 来跳转到/admin 界面
          this.$router.push('/userList')
        }else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>
.t1{
  overflow: hidden;
  padding-left: 1px;
  padding-right: 4px;
}
</style>