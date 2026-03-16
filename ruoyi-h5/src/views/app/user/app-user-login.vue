<template>
  <div class="login-page">
    <div class="login-header">
      <h1 class="title">APP用户登录</h1>
      <p class="subtitle">视频管理系统</p>
    </div>

    <van-form @submit="onSubmit" class="login-form">
      <van-cell-group inset>
        <van-field
          v-model="form.username"
          name="username"
          label="用户名"
          placeholder="请输入用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
          left-icon="user-o"
        />
        <van-field
          v-model="form.password"
          type="password"
          name="password"
          label="密码"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请填写密码' }]"
          left-icon="lock"
        />
      </van-cell-group>

      <div class="login-btn-wrapper">
        <van-button
          round
          block
          type="primary"
          native-type="submit"
          :loading="loading"
          loading-text="登录中..."
        >
          登录
        </van-button>
      </div>

      <div class="register-link">
        <span>还没有账号？</span>
        <router-link to="/app/user/register">立即注册</router-link>
      </div>
    </van-form>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'AppUserLogin',
  data() {
    return {
      form: {
        username: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    onSubmit() {
      this.loading = true
      request({
        url: '/app/api/user/login',
        method: 'post',
        data: {
          username: this.form.username,
          password: this.form.password
        }
      }).then(res => {
        this.$toast.success('登录成功')
        localStorage.setItem('appUserInfo', JSON.stringify(res.data))
        this.$router.push('/')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;

  .title {
    font-size: 36px;
    color: #fff;
    margin: 0 0 10px 0;
    font-weight: 600;
  }

  .subtitle {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.8);
    margin: 0;
  }
}

.login-form {
  .login-btn-wrapper {
    margin: 24px 16px 0;
  }
}

.register-link {
  text-align: center;
  margin-top: 20px;
  color: #fff;
  
  a {
    color: #ffd700;
    text-decoration: none;
  }
}
</style>
