<template>
  <div>
    <!--搜索表单  type="primary"修改了按钮的背景颜色 -->
    <div style="margin-bottom: 10px">
      <el-input style="width: 240px" placeholder="请输入图书名称" v-model="params.name"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入图书标准码"
                v-model="params.borrowNo"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入用户名称" v-model="params.userName"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="rest"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <div class="t1">
      <!--   展示数据 -->
      <el-table :data="tableData" stripe row-key="id" default-expand-all>
        <el-table-column prop="id" label="编号" width="80"></el-table-column>
        <el-table-column prop="bookName" label="图书名称"></el-table-column>
        <el-table-column prop="bookNo" label="图书标准码"></el-table-column>
        <el-table-column prop="userNo" label="会员码"></el-table-column>
        <el-table-column prop="userName" label="用户名称"></el-table-column>
        <el-table-column prop="userPhone" label="用户联系方式"></el-table-column>
        <el-table-column prop="score" label="所用积分"></el-table-column>
        <el-table-column prop="createtime" label="借出时间"></el-table-column>
        <el-table-column prop="status" label="借出状态"></el-table-column>
        <el-table-column prop="days" label="借出天数"></el-table-column>
        <el-table-column prop="returnDate" label="归还日期"></el-table-column>
        <el-table-column prop="realDate" label="实际归还日期"></el-table-column>
        <!--<el-table-column prop="updatetime" label="更新时间"></el-table-column>-->
        <!-- 编辑-->
        <el-table-column label="操作">
          <!--      scope.row 就是当前行数据-->
          <template v-slot="scope">

            <!--  scope.row.id 这个行 id的数据 -->
            <!--            <el-button type="primary" @click="$router.push('/editBorrow?id='+scope.row.id)">编辑</el-button>-->

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
  </div>
</template>

<script>
import request from "@/utils/request";
import Cookies from 'js-cookie'

export default {
  name: 'ReturList',
  data() {
    return {
      tableData: [],
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 5,
        bookName: '',
        borrowNo: '',
        userName: ''
      },
    }
  },
  created() {
    this.load();
  },
  // res 指的就是页面方面的展示的json数据
  methods: {
    load() {
      //通过request调用这个接口
      request.get('/borrow/pageRetur', {
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
        borrowNo: ''
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
      request.delete("/borrow/deleteRetur/" + id).then(res => {
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