<template>
  <div class="app-user-login-page">
    <div class="login-header">
      <h1 class="title">APP用户登录</h1>
      <p class="subtitle">视频管理系统APP端</p>
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
        <span @click="goRegister">还没有账号？立即注册</span>
      </div>
    </van-form>
  </div>
</template>

<script>
import { appUserLogin } from '@/api/app-user'
import { setToken } from '@/utils/auth'

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
    /**
     * 提交登录
     */
    onSubmit() {
      this.loading = true
      appUserLogin(this.form.username, this.form.password)
        .then(response => {
          this.loading = false
          if (response.code === 200) {
            // 保存token
            setToken(response.data.token)
            // 保存用户信息
            localStorage.setItem('appUserInfo', JSON.stringify({
              userId: response.data.userId,
              userName: response.data.userName,
              nickName: response.data.nickName,
              avatar: response.data.avatar
            }))
            this.$toast.success('登录成功')
            // 跳转到首页
            this.$router.push('/')
          } else {
            this.$toast.fail(response.msg || '登录失败')
          }
        })
        .catch(error => {
          this.loading = false
          console.error('登录失败:', error)
        })
    },
    /**
     * 跳转到注册页面
     */
    goRegister() {
      this.$router.push('/app/user/register')
    }
  }
}
</script>

<style lang="scss" scoped>
.app-user-login-page {
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
    font-size: 32px;
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

  .register-link {
    text-align: center;
    margin-top: 20px;
    color: rgba(255, 255, 255, 0.9);
    font-size: 14px;

    span {
      cursor: pointer;
      text-decoration: underline;

      &:hover {
        color: #fff;
      }
    }
  }
}
</style>
