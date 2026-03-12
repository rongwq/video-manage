import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'hash',
  routes: [
    {
      path: '/',
      redirect: '/video'
    },
    {
      path: '/video',
      name: 'Video',
      component: () => import('@/views/VideoPlayer.vue')
    },
    {
      path: '/video/:id',
      name: 'VideoPlay',
      component: () => import('@/views/VideoPlayer.vue')
    }
  ]
})
