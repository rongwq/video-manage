import Vue from 'vue'
import Router from 'vue-router'
import { getToken } from '@/utils/auth'
import { Toast } from 'vant'

Vue.use(Router)

// 公共路由
export const constantRoutes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index'),
    hidden: true,
    meta: { title: '登录' }
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/home/index'),
    meta: { title: '首页' }
  },
  {
    path: '/video/play/:id',
    name: 'VideoPlay',
    component: () => import('@/views/video/play'),
    meta: { title: '视频播放' }
  },
  // APP用户登录页面
  {
    path: '/app/user/login',
    name: 'AppUserLogin',
    component: () => import('@/views/app/user/app-user-login'),
    hidden: true,
    meta: { title: 'APP用户登录' }
  },
  // APP用户注册页面
  {
    path: '/app/user/register',
    name: 'AppUserRegister',
    component: () => import('@/views/app/user/app-user-register'),
    hidden: true,
    meta: { title: 'APP用户注册' }
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true,
    meta: { title: '页面不存在' }
  },
  // 404页面必须放在最后
  { path: '*', redirect: '/404', hidden: true }
]

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

// 白名单（不需要登录的页面）
const whiteList = ['/login', '/404', '/app/user/login', '/app/user/register']

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  if (to.meta && to.meta.title) {
    document.title = to.meta.title
  }

  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login' || to.path === '/app/user/login') {
      // 已登录，跳转到首页
      next({ path: '/' })
    } else {
      next()
    }
  } else {
    // 未登录
    if (whiteList.indexOf(to.path) !== -1 || whiteList.some(path => to.path.startsWith(path))) {
      // 白名单直接放行
      next()
    } else {
      // 其他页面跳转到登录页
      Toast('请先登录')
      next(`/app/user/login?redirect=${to.path}`)
    }
  }
})

export default router
