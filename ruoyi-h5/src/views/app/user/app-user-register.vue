<template>
  <div class="register-page">
    <div class="register-header">
      <h1 class="title">APP用户注册</h1>
      <p class="subtitle">视频管理系统</p>
    </div>

    <van-form @submit="onSubmit" class="register-form">
      <van-cell-group inset>
        <van-field
          v-model="form.userName"
          name="userName"
          label="用户名"
          placeholder="请输入用户名"
          :rules="[{ required: true, message: '请填写用户名' }]"
          left-icon="user-o"
        />
        <van-field
          v-model="form.nickName"
          name="nickName"
          label="昵称"
          placeholder="请输入昵称"
          left-icon="contact"
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
        <van-field
          v-model="form.phonenumber"
          name="phonenumber"
          label="手机号"
          placeholder="请输入手机号"
          left-icon="phone-o"
        />
        <van-field
          v-model="form.email"
          name="email"
          label="邮箱"
          placeholder="请输入邮箱"
          left-icon="envelope-o"
        />
      </van-cell-group>

      <div class="register-btn-wrapper">
        <van-button
          round
          block
          type="primary"
          native-type="submit"
          :loading="loading"
          loading-text="注册中..."
        >
          注册
        </van-button>
      </div>

      <div class="login-link">
        <span>已有账号？</span>
        <router-link to="/app/user/login">立即登录</router-link>
      </div>
    </van-form>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'AppUserRegister',
  data() {
    return {
      form: {
        userName: '',
        nickName: '',
        password: '',
        phonenumber: '',
        email: ''
      },
      loading: false
    }
  },
  methods: {
    onSubmit() {
      this.loading = true
      request({
        url: '/app/api/user/register',
        method: 'post',
        data: this.form
      }).then(() => {
        this.$toast.success('注册成功')
        this.$router.push('/app/user/login')
      }).catch(() => {
        this.loading = false
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
}

.register-header {
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

.register-form {
  .register-btn-wrapper {
    margin: 24px 16px 0;
  }
}

.login-link {
  text-align: center;
  margin-top: 20px;
  color: #fff;
  
  a {
    color: #ffd700;
    text-decoration: none;
  }
}
</style>
