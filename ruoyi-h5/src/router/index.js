import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/video/list'
  },
  {
    path: '/video/list',
    name: 'VideoList',
    component: () => import('@/views/video/VideoList.vue'),
    meta: { title: '视频列表' }
  },
  {
    path: '/video/play',
    name: 'VideoPlay',
    component: () => import('@/views/video/VideoPlay.vue'),
    meta: { title: '视频播放' }
  },
  {
    path: '/video/play/:id',
    name: 'VideoPlayById',
    component: () => import('@/views/video/VideoPlay.vue'),
    meta: { title: '视频播放' }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login.vue'),
    meta: { title: '登录' }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router
