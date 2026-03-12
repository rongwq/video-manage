<template>
  <div class="login-page">
    <div class="login-header">
      <h1 class="title">RuoYi H5</h1>
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
    </van-form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data() {
    return {
      form: {
        username: 'admin',
        password: 'admin123'
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
      this.$store
        .dispatch('Login', this.form)
        .then(() => {
          this.$toast.success('登录成功')
          const redirect = this.$route.query.redirect || '/'
          this.$router.push(redirect)
        })
        .catch(() => {
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
</style>
