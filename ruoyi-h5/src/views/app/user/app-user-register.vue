<template>
  <div class="app-user-register-page">
    <div class="register-header">
      <h1 class="title">APP用户注册</h1>
      <p class="subtitle">创建您的账号</p>
    </div>

    <van-form @submit="onSubmit" class="register-form">
      <van-cell-group inset>
        <van-field
          v-model="form.userName"
          name="userName"
          label="用户名"
          placeholder="请输入用户名(2-30字符)"
          :rules="[
            { required: true, message: '请填写用户名' },
            { min: 2, max: 30, message: '用户名长度2-30字符' }
          ]"
          left-icon="user-o"
        />
        <van-field
          v-model="form.nickName"
          name="nickName"
          label="昵称"
          placeholder="请输入昵称"
          left-icon="smile-o"
        />
        <van-field
          v-model="form.password"
          type="password"
          name="password"
          label="密码"
          placeholder="请输入密码(6-20字符)"
          :rules="[
            { required: true, message: '请填写密码' },
            { min: 6, max: 20, message: '密码长度6-20字符' }
          ]"
          left-icon="lock"
        />
        <van-field
          v-model="form.confirmPassword"
          type="password"
          name="confirmPassword"
          label="确认密码"
          placeholder="请再次输入密码"
          :rules="[
            { required: true, message: '请确认密码' },
            { validator: validateConfirmPassword, message: '两次输入的密码不一致' }
          ]"
          left-icon="lock"
        />
        <van-field
          v-model="form.phonenumber"
          name="phonenumber"
          label="手机号"
          placeholder="请输入手机号"
          :rules="[
            { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号' }
          ]"
          left-icon="phone-o"
        />
        <van-field
          v-model="form.email"
          name="email"
          label="邮箱"
          placeholder="请输入邮箱"
          :rules="[
            { type: 'email', message: '请输入正确的邮箱地址' }
          ]"
          left-icon="envelop-o"
        />
        <van-field
          v-model="form.sex"
          name="sex"
          label="性别"
          placeholder="请选择性别"
          readonly
          clickable
          @click="showSexPicker = true"
          left-icon="friends-o"
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
        <span @click="goLogin">已有账号？立即登录</span>
      </div>
    </van-form>

    <!-- 性别选择器 -->
    <van-popup v-model:show="showSexPicker" position="bottom">
      <van-picker
        :columns="sexColumns"
        @confirm="onSexConfirm"
        @cancel="showSexPicker = false"
      />
    </van-popup>
  </div>
</template>

<script>
import { appUserRegister } from '@/api/app-user'

export default {
  name: 'AppUserRegister',
  data() {
    return {
      form: {
        userName: '',
        nickName: '',
        password: '',
        confirmPassword: '',
        phonenumber: '',
        email: '',
        sex: ''
      },
      loading: false,
      showSexPicker: false,
      sexColumns: [
        { text: '男', value: '0' },
        { text: '女', value: '1' },
        { text: '未知', value: '2' }
      ]
    }
  },
  methods: {
    /**
     * 确认密码校验
     */
    validateConfirmPassword(value) {
      return value === this.form.password
    },
    /**
     * 性别选择确认
     */
    onSexConfirm({ selectedOptions }) {
      this.form.sex = selectedOptions[0].value
      this.showSexPicker = false
    },
    /**
     * 提交注册
     */
    onSubmit() {
      this.loading = true
      // 构造注册数据
      const registerData = {
        userName: this.form.userName,
        nickName: this.form.nickName || this.form.userName,
        password: this.form.password,
        phonenumber: this.form.phonenumber,
        email: this.form.email,
        sex: this.form.sex || '2'
      }

      appUserRegister(registerData)
        .then(response => {
          this.loading = false
          if (response.code === 200) {
            this.$toast.success('注册成功，请登录')
            // 跳转到登录页面
            setTimeout(() => {
              this.$router.push('/app/user/login')
            }, 1500)
          } else {
            this.$toast.fail(response.msg || '注册失败')
          }
        })
        .catch(error => {
          this.loading = false
          console.error('注册失败:', error)
        })
    },
    /**
     * 跳转到登录页面
     */
    goLogin() {
      this.$router.push('/app/user/login')
    }
  }
}
</script>

<style lang="scss" scoped>
.app-user-register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 20px;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;

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

.register-form {
  .register-btn-wrapper {
    margin: 24px 16px 0;
  }

  .login-link {
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
