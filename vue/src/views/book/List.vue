<template>
  <div>
    <!--搜索表单  type="primary"修改了按钮的背景颜色 -->
    <div style="margin-bottom: 10px">
      <el-input style="width: 240px" placeholder="请输入图书名称" v-model="params.name"></el-input>
      <el-input style="width: 240px;margin-left: 5px" placeholder="请输入图书标准码" v-model="params.bookNo"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load"><i class="el-icon-search"></i>搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="rest"><i class="el-icon-refresh"></i>重置</el-button>
    </div>

    <div class="t1">
      <!--   展示数据 -->
      <el-table :data="tableData" stripe row-key="id" default-expand-all>
        <el-table-column prop="id" label="编号" width="80"></el-table-column>
        <el-table-column prop="name" label="图书名称"></el-table-column>
        <el-table-column prop="description" width="300px" label="图书描述"></el-table-column>
        <el-table-column prop="publishDate" label="出版日期"></el-table-column>
        <el-table-column prop="author" label="作者"></el-table-column>
        <el-table-column prop="publisher" label="出版社"></el-table-column>
        <el-table-column prop="category" label="分类"></el-table-column>
        <el-table-column prop="bookNo" label="标准码"></el-table-column>
        <el-table-column prop="score" label="借书积分"></el-table-column>
        <el-table-column prop="nums" label="数量"></el-table-column>

        <!-- :preview-src-list 设置预览效果-->
        <el-table-column prop="cover" label="封面">
          <template v-slot="scope">
            <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
          </template>
        </el-table-column>

        <el-table-column prop="createtime" label="创建时间" width="100"></el-table-column>
        <el-table-column prop="updatetime" label="更新时间" width="100"></el-table-column>
        <!-- 编辑-->
        <el-table-column label="操作" width="140px">
          <!--      scope.row 就是当前行数据-->
          <template v-slot="scope">

            <!--  scope.row.id 这个行 id的数据 -->
            <el-button type="primary" @click="$router.push('/editBook?id='+scope.row.id)">编辑</el-button>

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
  name: 'BookList',
  data() {
    return {
      tableData: [],
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {},
      total: 0,
      params: {
        pageNum: 1,
        pageSize: 5,
        name: '',
        bookNo: ''
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
      request.get('/book/page', {
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
        bookNo:''
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
      request.delete("/book/delete/" + id).then(res => {
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