<template>
  <div class="login-container">
    <div class="login-header">
      <h2 class="title">RuoYi H5</h2>
    </div>
    
    <van-form @submit="onSubmit" class="login-form">
      <van-field
        v-model="form.username"
        name="username"
        label="用户名"
        placeholder="请输入用户名"
        :rules="[{ required: true, message: '请填写用户名' }]"
      />
      <van-field
        v-model="form.password"
        type="password"
        name="password"
        label="密码"
        placeholder="请输入密码"
        :rules="[{ required: true, message: '请填写密码' }]"
      />
      <div class="login-btn">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<script>
import { login } from '@/api/login'
import { Toast } from 'vant'

export default {
  name: 'Login',
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
    async onSubmit() {
      this.loading = true
      try {
        const res = await login(this.form)
        if (res.code === 200) {
          this.$store.dispatch('Login', {
            token: res.token,
            userId: res.user ? res.user.userId : null,
            userInfo: res.user
          })
          Toast.success('登录成功')
          this.$router.push('/')
        } else {
          Toast.fail(res.msg || '登录失败')
        }
      } catch (error) {
        console.error('登录失败:', error)
        Toast.fail('登录失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
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
  
  .title {
    color: #fff;
    font-size: 24px;
    margin: 0;
  }
}

.login-form {
  background: #fff;
  border-radius: 12px;
  padding: 20px;
}

.login-btn {
  margin-top: 30px;
}
</style>
