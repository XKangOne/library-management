import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../views/Layout.vue'
import Cookies from "js-cookie";

Vue.use(VueRouter)

const routes = [
    //========登录==========
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/login/Login.vue')
    },
    //========主页=========
    {
        path: '/',
        name: 'Layout',
        component: Layout ,
        //进入网址直接进入/home页面 并嵌套在进入页面里面
        redirect:'/home',
        //子路由
        children: [
            {
                path: 'home',
                name: 'Home',
                component: () => import('@/views/home/HomeView.vue')
            },
            //=========User==========
            {
                path: 'userList',
                name: 'userList',
                component: () => import('@/views/user/User.vue')
            },
            {
                path: 'addUser',
                name: 'AddUser',
                component: () => import('@/views/user/AddUser.vue')
            },
            {
                path: 'editUser',
                name: 'EditUser',
                component: () => import('@/views/user/EditUser.vue')
            },
            //=========Admin==========
            {
                path: 'adminList',
                name: 'AdminList',
                component: () => import('@/views/admin/List.vue')
            },
            {
                path: 'addAdmin',
                name: 'AddAdmin',
                component: () => import('@/views/admin/Add.vue')
            },
            {
                path: 'editAdmin',
                name: 'EditAdmin',
                component: () => import('@/views/admin/Edit.vue')
            },
            //=========Category==========
            {
                path: 'categoryList',
                name: 'CategoryList',
                component: () => import('@/views/category/List.vue')
            },
            {
                path: 'addCategory',
                name: 'AddCategory',
                component: () => import('@/views/category/Add.vue')
            },
            {
                path: 'editCategory',
                name: 'EditCategory',
                component: () => import('@/views/category/Edit.vue')
            },
            //=========Book==========
            {
                path: 'bookList',
                name: 'borrowList',
                component: () => import('@/views/book/List.vue')
            },
            {
                path: 'addBook',
                name: 'AddBook',
                component: () => import('@/views/book/Add.vue')
            },
            {
                path: 'editBook',
                name: 'EditBook',
                component: () => import('@/views/book/Edit.vue')
            },
            //=========Borrow==========
            {
                path: 'borrowList',
                name: 'borrowList',
                component: () => import('@/views/borrow/List.vue')
            },
            {
                path: 'addBorrow',
                name: 'AddBorrow',
                component: () => import('@/views/borrow/Add.vue')
            },
            {
                path: 'editBorrow',
                name: 'EditBorrow',
                component: () => import('@/views/borrow/Edit.vue')
            },
            //=========Return==========
            {
                path: 'returList',
                name: 'returList',
                component: () => import('@/views/retur/List.vue')
            },
        ]
    },
    {
        path:'*',
        name: '404',
        component:() =>import('@/views/user/404.vue')
    }
]
const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

router.beforeEach((to, from, next) => {
    if (to.path === '/login') next()
    //得到cookie
    const admin = Cookies.get("admin")
    if (!admin && to.path !== '/login')
        //强制进行login页面
        return next("/login")
    //访问/home的时候，并且cookie里面存在数据，这个时候我就直接放行
    next()
})

export default router
