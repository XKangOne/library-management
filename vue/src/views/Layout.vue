<!--为了嵌套主页的内容-->
<template>
  <div>
    <!-- 头部区域 display: flex"---flex布局方便下面的操作  -->
    <div style="height: 60px;line-height: 60px;background-color: white;margin-bottom: 2px;display: flex">
      <div style="width: 300px">
        <img src="@/assets/logo.png" alt="" style="width: 40px;position: relative;top: 10px;left: 20px">
        <span style="margin-left: 25px;font-size: 24px">图书管理系统</span>
      </div>

      <!--
        style="flex: 1"  意思就是头部分为两部分,一部分300px 剩下都是第二部分如下
        text-align: right; 往右边对齐
      -->
      <div style="flex: 1;text-align: right;padding-right: 20px" >
          <!-- style="cursor: pointer"--移动名字这里会变成小手形状-->
        <el-dropdown size="medium" style="cursor: pointer">
          <span class="el-dropdown-link">
         {{admin.username}}<i class="el-icon-arrow-down el-icon--right"></i>
          </span>
          <!--style="margin-top: -5px"---将退出 往上移动-->
          <el-dropdown-menu slot="dropdown" style="margin-top: -5px">
            <el-dropdown-item><div style="width: 50px;text-align: center" @click="logout">退出</div></el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>

    <!-- 侧边栏和主体  -->
    <div style="display: flex">
      <!-- 侧边栏导航 计算属性 calc(100vh - 62px)不会有滚动条的拖拽，如果只是100vh会有滚动条 -->
      <div
          style="width: 170px; min-height: calc(100vh - 62px); overflow: hidden;margin-right: 2px;background-color: white">
        <!--
        $route.path 根据路由路径去切换,router:选中路由(需要每一个index后面都要加入/ 不然没有高亮)
         $route.path ==='/' ? $route.path : $route.path.substring(1) 解决高亮问题
       :default-active  指定是当前正在打开菜单的高亮
       :default-openeds="['user','admin']" 指定是当前正在打开的菜单
        -->
        <el-menu :default-active="$route.path"  router class="el-menu-demo">
          <el-menu-item index="/">
            <i class="el-icon-s-home"></i>
            <spand>首页</spand>
          </el-menu-item>

          <el-submenu index="user">
            <template slot="title">
              <li class="el-icon-s-order"></li>
              <spand>会员管理</spand>
            </template>
            <!--加入/ 点击路由会有高亮的效果，不加则不会产生 并且刷新的时候还是会跳转到当前页面-->
            <el-menu-item index="/addUser">会员添加</el-menu-item>
            <el-menu-item index="/userList">会员列表</el-menu-item>
          </el-submenu>

          <el-submenu index="admin">
            <template slot="title">
              <li class="el-icon-user"></li>
              <spand>管理员管理</spand>
            </template>
            <el-menu-item index="/addAdmin">管理员添加</el-menu-item>
            <el-menu-item index="/adminList">管理员列表</el-menu-item>
          </el-submenu>

          <el-submenu index="category">
            <template slot="title">
              <li class="el-icon-s-operation"></li>
              <spand>分类管理</spand>
            </template>
            <el-menu-item index="/addCategory">图书分类添加</el-menu-item>
            <el-menu-item index="/categoryList">图书分类列表</el-menu-item>
          </el-submenu>

          <el-submenu index="book">
            <template slot="title">
              <li class="el-icon-notebook-1"></li>
              <spand>图书管理</spand>
            </template>
            <el-menu-item index="/addBook">图书添加</el-menu-item>
            <el-menu-item index="/bookList">图书列表</el-menu-item>
          </el-submenu>

          <el-submenu index="borrow">
            <template slot="title">
              <li class="el-icon-notebook-2"></li>
              <spand>借书管理</spand>
            </template>
            <el-menu-item index="/addBorrow">借书添加</el-menu-item>
            <el-menu-item index="/borrowList">借书列表</el-menu-item>
          </el-submenu>

          <el-submenu index="retur">
            <template slot="title">
              <li class="el-icon-document"></li>
              <spand>还书管理</spand>
            </template>
            <el-menu-item index="/returList">还书列表</el-menu-item>
          </el-submenu>
        </el-menu>
      </div>

      <!--主体数据-->
      <!-- width: 0;可以限制容器的宽度，不被子元素无限撑开 -->
      <div style="flex:1; width: 0; background-color: white;padding: 10px">
      <!-- 展示子路由页面-->
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import Cookies from 'js-cookie'

export default {
  name:"Layout",
  data(){
    return{
      //获取admin Cookie 如果有并解析成字符串 否则为空
      admin: Cookies.get('admin') ? JSON.parse(Cookies.get('admin')) : {}
    }
  },
  methods:{
    logout(){
      //用户选择退出时，然后清除浏览器 Cookies 用户数据
      Cookies.remove('admin')
      //跳转登录页面
      this.$router.push("/login")
    }
  }
}
</script>
