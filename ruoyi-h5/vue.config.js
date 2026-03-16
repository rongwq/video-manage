const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  
  // 开发服务器配置
  devServer: {
    port: 8081,
    open: true,
    proxy: {
      // 代理RuoYi后端API
      '/dev-api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/dev-api': ''
        }
      }
    }
  },
  
  // 路径别名
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src')
      }
    }
  },
  
  // CSS配置
  css: {
    loaderOptions: {
      scss: {
        additionalData: `
          @use "sass:math";
        `
      }
    }
  },
  
  // 生产环境配置
  productionSourceMap: false,
  
  // 输出目录
  outputDir: 'dist',
  
  // 静态资源目录
  assetsDir: 'static',
  
  // 基础路径
  publicPath: './'
})
