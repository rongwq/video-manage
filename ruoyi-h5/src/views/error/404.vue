<template>
  <div class="error-page">
    <div class="error-content">
      <van-icon name="warn-o" size="80" color="#969799" />
      <h2 class="error-title">页面不存在</h2>
      <p class="error-desc">抱歉，您访问的页面不存在或已被移除</p>
      <van-button type="primary" round @click="goHome">返回首页</van-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Error404',
  methods: {
    goHome() {
      this.$router.push('/')
    }
  }
}
</script>

<style lang="scss" scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f5f5f5;
  padding: 20px;
}

.error-content {
  text-align: center;

  .error-title {
    margin: 20px 0 10px;
    font-size: 20px;
    color: #323233;
  }

  .error-desc {
    margin: 0 0 24px;
    font-size: 14px;
    color: #969799;
  }
}
</style>
