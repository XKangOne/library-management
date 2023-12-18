<template>
  <div>
    <!--搜索表单  type="primary"修改了按钮的背景颜色 -->
    <div style="margin-bottom: 10px">
      <el-input style="width: 240px" placeholder="请输入分类名称" v-model="params.name"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="rest"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <div class="t1">
      <!--   展示数据 -->
      <el-table :data="tableData" stripe row-key="id" default-expand-all>
        <el-table-column prop="id" label="编号" width="80"></el-table-column>
        <el-table-column prop="name" label="用户名"></el-table-column>
        <el-table-column prop="remark" label="备注"></el-table-column>
        <el-table-column prop="createtime" label="创建时间"></el-table-column>
        <el-table-column prop="updatetime" label="更新时间"></el-table-column>

        <!-- 编辑-->
        <el-table-column label="操作" width="280px">
          <!--      scope.row 就是当前行数据-->
          <template v-slot="scope">

            <el-button type="success" v-if="!scope.row.pid" @click="handleleAdd(scope.row)">添加二级分类</el-button>

            <!--  scope.row.id 这个行 id的数据 -->
            <el-button type="primary" @click="$router.push('/editCategory?id='+scope.row.id)">编辑</el-button>

            <el-popconfirm
                style="margin-left: 5px"
                title="您确定删除这行数据吗？"
                @confirm="del(scope.row.id)">

              <el-button type="danger" slot="reference" class="t3">删除</el-button>
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
    <el-dialog title="添加二级分类" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="100px" ref="ruleForm" :rules="rules">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>

        <el-form-item label="分类备注" prop="remark">
          <el-input v-model="form.remark" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from 'js-cookie'

export default {
  name: 'CategoryList',
  data() {
    return {
      tableData: [],
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      total: 0,
      dialogFormVisible: false,
      form: {},
      pid: null,
      params: {
        pageNum: 1,
        pageSize: 5,
        name: '',
      },
      rules: {
        name: [
          {required: true, message: '请输入分类名称', trigger: 'blur'},
        ],
      }
    }
  },
  created() {
    this.load();
  },
  // res 指的就是页面方面的展示的json数据
  methods: {
    //让弹窗出现
    handleleAdd(row){
      //将当前行的id作为二级分类的id
      this.pid = row.id
      //把弹窗打开
      this.dialogFormVisible = true
    },
    save() {
      //这个要注意与上面的对应绑定
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          //给二级分类赋值pid
          this.form.pid = this.pid
          request.post('/category/save', this.form).then(res => {
            if (res.code === '200') {
              //弹出提示信息
              this.$notify.success('新增二级分类成功')
              //清空表单格
              this.$refs['ruleForm'].resetFields();
              //保存成功弹窗关闭
              this.dialogFormVisible = false
              //重新加载页面
              this.load()
            } else {
              this.$notify.error(res.msg)
            }
          })
        }
      })
    },
    load() {
      //通过request调用这个接口
      request.get('/category/page', {
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
      request.delete("/category/delete/" + id).then(res => {
        if (res.code === '200') {
          this.$notify.success("删除成功")
          //重新加载页面
          this.load()
        } else {
          this.$notify.error(res.msg)
        }
      })
    }
  }
}
</script>