<template>
  <div class="login-container">
    <div class="login-header">
      <h2>APP用户登录</h2>
      <p>欢迎回来</p>
    </div>
    <van-form @submit="handleLogin" class="login-form">
      <van-field
        v-model="loginForm.userName"
        name="userName"
        label="用户名"
        placeholder="请输入用户名"
        left-icon="user-o"
        :rules="[{ required: true, message: '请输入用户名' }]"
      />
      <van-field
        v-model="loginForm.password"
        type="password"
        name="password"
        label="密码"
        placeholder="请输入密码"
        left-icon="lock"
        :rules="[{ required: true, message: '请输入密码' }]"
      />
      <div class="form-actions">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          登录
        </van-button>
      </div>
    </van-form>
    <div class="register-link">
      <span>还没有账号？</span>
      <router-link to="/app/user/register">立即注册</router-link>
    </div>
  </div>
</template>

<script>
import { appLogin } from '@/api/user'
import { setToken } from '@/utils/auth'
import { Toast } from 'vant'

export default {
  name: 'AppUserLogin',
  data() {
    return {
      loginForm: {
        userName: '',
        password: ''
      },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      try {
        const res = await appLogin(this.loginForm)
        if (res.code === 200) {
          setToken(res.data.token)
          Toast.success('登录成功')
          const redirect = this.$route.query.redirect || '/home'
          this.$router.push(redirect)
        } else {
          Toast.fail(res.msg || '登录失败')
        }
      } catch (error) {
        Toast.fail('登录失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
  color: #fff;
}

.login-header h2 {
  font-size: 28px;
  margin-bottom: 10px;
}

.login-header p {
  font-size: 14px;
  opacity: 0.8;
}

.login-form {
  background: #fff;
  border-radius: 12px;
  padding: 30px 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.form-actions {
  margin-top: 30px;
}

.register-link {
  text-align: center;
  margin-top: 30px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.register-link a {
  color: #fff;
  font-weight: bold;
  text-decoration: underline;
}
</style>
