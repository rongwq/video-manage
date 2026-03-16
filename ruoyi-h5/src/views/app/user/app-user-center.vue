<template>
  <div class="user-center">
    <van-nav-bar title="个人中心" left-arrow @click-left="goBack" />
    
    <div class="user-info" v-if="userInfo">
      <van-image round width="80" height="80" :src="userInfo.avatar || defaultAvatar" />
      <div class="info">
        <h3>{{ userInfo.nickName || userInfo.userName }}</h3>
        <p class="vip">VIP等级: {{ userInfo.vipLevel || 0 }}</p>
        <p class="integral">积分: {{ userInfo.integral || 0 }}</p>
      </div>
    </div>

    <van-cell-group inset class="menu-group">
      <van-cell title="个人信息" is-link to="/app/user/profile" />
      <van-cell title="修改密码" is-link to="/app/user/password" />
      <van-cell title="我的收藏" is-link to="/app/user/collect" />
      <van-cell title="观看历史" is-link to="/app/user/history" />
    </van-cell-group>

    <div class="logout-btn">
      <van-button round block type="danger" @click="handleLogout">退出登录</van-button>
    </div>
  </div>
</template>

<script>
import { getAppUserInfo, appLogout } from '@/api/user'
import { removeToken } from '@/utils/auth'
import { Toast, Dialog } from 'vant'

export default {
  name: 'AppUserCenter',
  data() {
    return {
      userInfo: null,
      defaultAvatar: 'https://fastly.jsdelivr.net/npm/@vant/assets/cat.jpeg'
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    async loadUserInfo() {
      try {
        const res = await getAppUserInfo()
        if (res.code === 200) {
          this.userInfo = res.data.user
        }
      } catch (error) {
        console.error('获取用户信息失败', error)
      }
    },
    async handleLogout() {
      try {
        await Dialog.confirm({
          title: '提示',
          message: '确定要退出登录吗？'
        })
        await appLogout()
        removeToken()
        this.$router.push('/app/user/login')
        Toast.success('退出成功')
      } catch (error) {
        if (error !== 'cancel') {
          removeToken()
          this.$router.push('/app/user/login')
        }
      }
    }
  }
}
</script>

<style scoped>
.user-center {
  min-height: 100vh;
  background: #f5f5f5;
}

.user-info {
  display: flex;
  align-items: center;
  padding: 30px 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: #fff;
}

.user-info .info {
  margin-left: 20px;
}

.user-info .info h3 {
  margin: 0 0 10px 0;
  font-size: 20px;
}

.user-info .info p {
  margin: 5px 0;
  font-size: 14px;
  opacity: 0.9;
}

.menu-group {
  margin-top: 20px;
}

.logout-btn {
  padding: 30px 20px;
}
</style>
