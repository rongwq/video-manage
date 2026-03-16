<template>
  <div class="register-container">
    <div class="register-header">
      <h2>APP用户注册</h2>
      <p>创建您的账号</p>
    </div>
    <van-form @submit="handleRegister" class="register-form">
      <van-field
        v-model="registerForm.userName"
        name="userName"
        label="用户名"
        placeholder="请输入用户名"
        left-icon="user-o"
        :rules="[{ required: true, message: '请输入用户名' }]"
      />
      <van-field
        v-model="registerForm.password"
        type="password"
        name="password"
        label="密码"
        placeholder="请输入密码"
        left-icon="lock"
        :rules="[{ required: true, message: '请输入密码' }]"
      />
      <van-field
        v-model="registerForm.confirmPassword"
        type="password"
        name="confirmPassword"
        label="确认密码"
        placeholder="请再次输入密码"
        left-icon="lock"
        :rules="[{ required: true, message: '请确认密码' }]"
      />
      <van-field
        v-model="registerForm.phonenumber"
        name="phonenumber"
        label="手机号"
        placeholder="请输入手机号（选填）"
        left-icon="phone-o"
      />
      <van-field
        v-model="registerForm.nickName"
        name="nickName"
        label="昵称"
        placeholder="请输入昵称（选填）"
        left-icon="contact"
      />
      <div class="form-actions">
        <van-button round block type="primary" native-type="submit" :loading="loading">
          注册
        </van-button>
      </div>
    </van-form>
    <div class="login-link">
      <span>已有账号？</span>
      <router-link to="/app/user/login">立即登录</router-link>
    </div>
  </div>
</template>

<script>
import { appRegister } from '@/api/user'
import { setToken } from '@/utils/auth'
import { Toast } from 'vant'

export default {
  name: 'AppUserRegister',
  data() {
    return {
      registerForm: {
        userName: '',
        password: '',
        confirmPassword: '',
        phonenumber: '',
        nickName: ''
      },
      loading: false
    }
  },
  methods: {
    async handleRegister() {
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        Toast.fail('两次输入的密码不一致')
        return
      }
      if (this.registerForm.password.length < 5 || this.registerForm.password.length > 20) {
        Toast.fail('密码长度必须介于5和20之间')
        return
      }
      this.loading = true
      try {
        const res = await appRegister(this.registerForm)
        if (res.code === 200) {
          setToken(res.data.token)
          Toast.success('注册成功')
          this.$router.push('/home')
        } else {
          Toast.fail(res.msg || '注册失败')
        }
      } catch (error) {
        Toast.fail('注册失败')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
  color: #fff;
}

.register-header h2 {
  font-size: 28px;
  margin-bottom: 10px;
}

.register-header p {
  font-size: 14px;
  opacity: 0.8;
}

.register-form {
  background: #fff;
  border-radius: 12px;
  padding: 30px 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.form-actions {
  margin-top: 30px;
}

.login-link {
  text-align: center;
  margin-top: 30px;
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
}

.login-link a {
  color: #fff;
  font-weight: bold;
  text-decoration: underline;
}
</style>
