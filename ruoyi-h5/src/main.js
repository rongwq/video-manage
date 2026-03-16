import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 引入 Vant 组件
import {
  Button,
  Cell,
  CellGroup,
  Field,
  Form,
  Icon,
  Image,
  List,
  Loading,
  NavBar,
  PullRefresh,
  Toast,
  Dialog
} from 'vant'

// 引入 Vant 样式
import 'vant/lib/index.css'

// 注册 Vant 组件
Vue.use(Button)
Vue.use(Cell)
Vue.use(CellGroup)
Vue.use(Field)
Vue.use(Form)
Vue.use(Icon)
Vue.use(Image)
Vue.use(List)
Vue.use(Loading)
Vue.use(NavBar)
Vue.use(PullRefresh)
Vue.use(Toast)
Vue.use(Dialog)

// 将 Toast 挂载到 Vue 原型
Vue.prototype.$toast = Toast
Vue.prototype.$dialog = Dialog

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
