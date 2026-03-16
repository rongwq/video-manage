<template>
  <div class="video-play-page">
    <!-- 导航栏 -->
    <van-nav-bar
      :title="videoInfo.title || '视频播放'"
      left-arrow
      @click-left="onClickLeft"
      fixed
      placeholder
      :border="false"
      class="video-nav-bar"
    />

    <!-- 视频播放器区域 -->
    <div class="video-wrapper" :class="{ 'fullscreen': isFullscreen }" ref="videoWrapper">
      <!-- 视频元素 -->
      <video
        ref="videoPlayer"
        class="video-player"
        :src="videoUrl"
        :poster="videoPoster"
        :preload="preload"
        playsinline
        webkit-playsinline
        x5-playsinline
        x5-video-player-type="h5"
        x5-video-player-fullscreen="false"
        @click="togglePlay"
        @timeupdate="handleTimeUpdate"
        @loadedmetadata="handleLoadedMetadata"
        @waiting="handleWaiting"
        @playing="handlePlaying"
        @ended="handleEnded"
        @error="handleError"
        @canplay="handleCanPlay"
      ></video>

      <!-- 加载中遮罩 -->
      <div v-if="isLoading" class="loading-mask">
        <van-loading type="spinner" color="#fff" size="40px" />
        <span class="loading-text">加载中...</span>
      </div>

      <!-- 视频加载失败提示 -->
      <div v-if="loadError" class="error-mask" @click.stop="retryLoad">
        <van-icon name="warning-o" class="error-icon" />
        <span class="error-text">视频播放失败，请重试</span>
        <span class="retry-text">点击重新加载</span>
      </div>

      <!-- 播放完成遮罩 -->
      <div v-if="isEnded && !isLoading" class="ended-mask" @click.stop="replay">
        <div class="replay-btn">
          <van-icon name="replay" size="40" />
          <span>重新播放</span>
        </div>
      </div>

      <!-- 播放按钮（初始状态/暂停状态显示） -->
      <div v-if="!isPlaying && !isLoading && !loadError && !isEnded" class="play-btn-mask" @click.stop="play">
        <div class="center-play-btn">
          <van-icon name="play-circle-o" size="60" />
        </div>
      </div>

      <!-- 自定义控制栏 -->
      <div v-show="showControls && !loadError" class="video-controls" @click.stop>
        <!-- 进度条 -->
        <div class="progress-bar-wrapper" @click.stop="handleProgressClick" ref="progressBar">
          <div class="progress-bar">
            <div class="progress-buffered" :style="{ width: bufferedPercent + '%' }"></div>
            <div class="progress-played" :style="{ width: playedPercent + '%' }"></div>
            <div class="progress-handle" :style="{ left: playedPercent + '%' }"></div>
          </div>
        </div>

        <!-- 控制按钮区域 -->
        <div class="controls-main">
          <!-- 左侧：播放/暂停和时间 -->
          <div class="controls-left">
            <button class="control-btn play-pause-btn" @click.stop="togglePlay">
              <van-icon :name="isPlaying ? 'pause-circle-o' : 'play-circle-o'" size="28" />
            </button>
            <span class="time-display">{{ formatVideoTime(currentTime) }} / {{ formatVideoTime(duration) }}</span>
          </div>

          <!-- 右侧：音量、倍速、全屏 -->
          <div class="controls-right">
            <!-- 音量控制 -->
            <button class="control-btn" @click.stop="toggleMute">
              <van-icon :name="isMuted ? 'volume-o' : 'volume'" size="22" />
            </button>

            <!-- 倍速选择 -->
            <div class="speed-wrapper" @click.stop>
              <button class="control-btn speed-btn" @click.stop="showSpeedMenu = !showSpeedMenu">
                {{ playbackRate }}x
              </button>
              <div v-show="showSpeedMenu" class="speed-menu">
                <div
                  v-for="rate in playbackRates"
                  :key="rate"
                  class="speed-item"
                  :class="{ active: playbackRate === rate }"
                  @click.stop="changePlaybackRate(rate)"
                >
                  {{ rate }}x
                </div>
              </div>
            </div>

            <!-- 全屏按钮 -->
            <button class="control-btn fullscreen-btn" @click.stop="toggleFullscreen">
              <van-icon :name="isFullscreen ? 'shrink' : 'expand-o'" size="22" />
            </button>
          </div>
        </div>
      </div>

      <!-- 点击区域（用于显示/隐藏控制栏） -->
      <div class="click-overlay" @click="toggleControls"></div>
    </div>

    <!-- 视频信息区域 -->
    <div class="video-info">
      <h2 class="video-title">{{ videoInfo.title }}</h2>
      <p class="video-desc" v-if="videoInfo.remark">{{ videoInfo.remark }}</p>
      <div class="video-meta">
        <span class="meta-item">
          <van-icon name="play-circle-o" />
          {{ videoInfo.playNum || 0 }} 次播放
        </span>
        <span class="meta-item" v-if="videoInfo.likeNum">
          <van-icon name="good-job-o" />
          {{ videoInfo.likeNum }} 点赞
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import { getVideo } from '@/api/video'
import { formatVideoTime } from '@/utils/index'
import { Toast, Loading } from 'vant'

export default {
  name: 'VideoPlay',
  data() {
    return {
      // 视频数据
      videoId: null,
      videoUrl: '',
      videoPoster: '',
      videoInfo: {},

      // 播放器状态
      isPlaying: false,
      isLoading: false,
      isEnded: false,
      loadError: false,
      showControls: true,
      controlsTimer: null,
      preload: 'metadata',

      // 播放进度
      currentTime: 0,
      duration: 0,
      bufferedPercent: 0,
      playedPercent: 0,

      // 音量控制
      volume: 100,
      isMuted: false,
      lastVolume: 100,

      // 倍速控制
      playbackRate: 1.0,
      playbackRates: [0.5, 1.0, 1.5, 2.0],
      showSpeedMenu: false,

      // 全屏状态
      isFullscreen: false,

      // Loading实例
      loadingInstance: null
    }
  },
  mounted() {
    // 获取视频ID
    this.videoId = this.$route.params.id
    if (this.videoId) {
      this.fetchVideoInfo()
    } else {
      Toast.fail('视频ID不能为空')
    }

    // 监听全屏变化事件
    this.bindFullscreenEvents()

    // 监听屏幕方向变化
    this.bindOrientationEvents()

    // 监听点击其他地方关闭倍速菜单
    document.addEventListener('click', this.handleDocumentClick)
  },
  beforeDestroy() {
    // 清理事件监听
    this.unbindFullscreenEvents()
    this.unbindOrientationEvents()
    document.removeEventListener('click', this.handleDocumentClick)

    // 清理定时器
    if (this.controlsTimer) {
      clearTimeout(this.controlsTimer)
    }

    // 关闭loading
    this.closeLoading()
  },
  methods: {
    formatVideoTime,

    /**
     * 返回上一页
     */
    onClickLeft() {
      this.$router.back()
    },

    /**
     * 获取视频信息
     */
    fetchVideoInfo() {
      this.showLoading()
      getVideo(this.videoId)
        .then(response => {
          this.videoInfo = response.data || {}
          this.videoUrl = this.videoInfo.url || ''
          this.videoPoster = this.videoInfo.img || ''
          this.closeLoading()
        })
        .catch(error => {
          this.closeLoading()
          this.loadError = true
          console.error('获取视频信息失败:', error)
        })
    },

    /**
     * 显示loading
     */
    showLoading() {
      this.loadingInstance = Loading.service({
        message: '正在加载视频...',
        forbidClick: true
      })
    },

    /**
     * 关闭loading
     */
    closeLoading() {
      if (this.loadingInstance) {
        this.loadingInstance.close()
        this.loadingInstance = null
      }
    },

    /**
     * 播放视频
     */
    play() {
      const video = this.$refs.videoPlayer
      if (video) {
        video.play().then(() => {
          this.isPlaying = true
          this.isEnded = false
          this.hideControlsDelayed()
        }).catch(error => {
          console.error('播放失败:', error)
          Toast.fail('视频播放失败，请重试')
        })
      }
    },

    /**
     * 暂停视频
     */
    pause() {
      const video = this.$refs.videoPlayer
      if (video) {
        video.pause()
        this.isPlaying = false
      }
    },

    /**
     * 切换播放/暂停
     */
    togglePlay() {
      if (this.isPlaying) {
        this.pause()
      } else {
        this.play()
      }
    },

    /**
     * 重新播放
     */
    replay() {
      const video = this.$refs.videoPlayer
      if (video) {
        video.currentTime = 0
        this.isEnded = false
        this.play()
      }
    },

    /**
     * 重试加载
     */
    retryLoad() {
      this.loadError = false
      this.fetchVideoInfo()
    },

    /**
     * 视频时间更新
     */
    handleTimeUpdate() {
      const video = this.$refs.videoPlayer
      if (video) {
        this.currentTime = video.currentTime
        this.duration = video.duration || 0
        this.playedPercent = this.duration > 0 ? (this.currentTime / this.duration) * 100 : 0

        // 更新缓冲进度
        if (video.buffered.length > 0) {
          const bufferedEnd = video.buffered.end(video.buffered.length - 1)
          this.bufferedPercent = (bufferedEnd / this.duration) * 100
        }
      }
    },

    /**
     * 视频元数据加载完成
     */
    handleLoadedMetadata() {
      const video = this.$refs.videoPlayer
      if (video) {
        this.duration = video.duration || 0
      }
    },

    /**
     * 视频可以播放
     */
    handleCanPlay() {
      this.isLoading = false
    },

    /**
     * 视频等待缓冲
     */
    handleWaiting() {
      this.isLoading = true
    },

    /**
     * 视频开始播放
     */
    handlePlaying() {
      this.isLoading = false
    },

    /**
     * 视频播放结束
     */
    handleEnded() {
      this.isPlaying = false
      this.isEnded = true
      this.showControls = true
    },

    /**
     * 视频加载错误
     */
    handleError() {
      this.isLoading = false
      this.loadError = true
      Toast.fail('视频加载失败，请检查网络连接')
    },

    /**
     * 点击进度条跳转
     */
    handleProgressClick(event) {
      const progressBar = this.$refs.progressBar
      if (progressBar && this.duration > 0) {
        const rect = progressBar.getBoundingClientRect()
        const clickX = event.clientX - rect.left
        const percent = clickX / rect.width
        const newTime = percent * this.duration

        const video = this.$refs.videoPlayer
        if (video) {
          video.currentTime = newTime
          this.currentTime = newTime
          this.playedPercent = percent * 100
        }
      }
    },

    /**
     * 切换静音
     */
    toggleMute() {
      const video = this.$refs.videoPlayer
      if (video) {
        if (this.isMuted) {
          video.muted = false
          this.volume = this.lastVolume || 100
          this.isMuted = false
        } else {
          this.lastVolume = this.volume
          video.muted = true
          this.volume = 0
          this.isMuted = true
        }
      }
    },

    /**
     * 切换倍速
     */
    changePlaybackRate(rate) {
      const video = this.$refs.videoPlayer
      if (video) {
        video.playbackRate = rate
        this.playbackRate = rate
        this.showSpeedMenu = false
      }
    },

    /**
     * 切换全屏
     */
    toggleFullscreen() {
      const wrapper = this.$refs.videoWrapper
      if (!wrapper) return

      if (!this.isFullscreen) {
        // 进入全屏
        if (wrapper.requestFullscreen) {
          wrapper.requestFullscreen()
        } else if (wrapper.webkitRequestFullscreen) {
          wrapper.webkitRequestFullscreen()
        } else if (wrapper.mozRequestFullScreen) {
          wrapper.mozRequestFullScreen()
        } else if (wrapper.msRequestFullscreen) {
          wrapper.msRequestFullscreen()
        }

        // 锁定横屏
        if (window.screen && window.screen.orientation) {
          window.screen.orientation.lock('landscape').catch(() => {})
        }
      } else {
        // 退出全屏
        if (document.exitFullscreen) {
          document.exitFullscreen()
        } else if (document.webkitExitFullscreen) {
          document.webkitExitFullscreen()
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen()
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen()
        }

        // 解除屏幕方向锁定
        if (window.screen && window.screen.orientation) {
          window.screen.orientation.unlock()
        }
      }
    },

    /**
     * 全屏变化事件处理
     */
    handleFullscreenChange() {
      this.isFullscreen = !!(
        document.fullscreenElement ||
        document.webkitFullscreenElement ||
        document.mozFullScreenElement ||
        document.msFullscreenElement
      )
    },

    /**
     * 绑定全屏事件
     */
    bindFullscreenEvents() {
      const events = [
        'fullscreenchange',
        'webkitfullscreenchange',
        'mozfullscreenchange',
        'MSFullscreenChange'
      ]
      events.forEach(event => {
        document.addEventListener(event, this.handleFullscreenChange)
      })
    },

    /**
     * 解绑全屏事件
     */
    unbindFullscreenEvents() {
      const events = [
        'fullscreenchange',
        'webkitfullscreenchange',
        'mozfullscreenchange',
        'MSFullscreenChange'
      ]
      events.forEach(event => {
        document.removeEventListener(event, this.handleFullscreenChange)
      })
    },

    /**
     * 绑定屏幕方向事件
     */
    bindOrientationEvents() {
      window.addEventListener('orientationchange', this.handleOrientationChange)
    },

    /**
     * 解绑屏幕方向事件
     */
    unbindOrientationEvents() {
      window.removeEventListener('orientationchange', this.handleOrientationChange)
    },

    /**
     * 屏幕方向变化处理
     */
    handleOrientationChange() {
      // 可在此处理横竖屏切换逻辑
    },

    /**
     * 切换控制栏显示
     */
    toggleControls() {
      this.showControls = !this.showControls
      if (this.showControls && this.isPlaying) {
        this.hideControlsDelayed()
      }
    },

    /**
     * 延迟隐藏控制栏
     */
    hideControlsDelayed() {
      if (this.controlsTimer) {
        clearTimeout(this.controlsTimer)
      }
      this.controlsTimer = setTimeout(() => {
        if (this.isPlaying) {
          this.showControls = false
          this.showSpeedMenu = false
        }
      }, 3000)
    },

    /**
     * 点击文档其他地方关闭倍速菜单
     */
    handleDocumentClick(event) {
      const speedWrapper = this.$el.querySelector('.speed-wrapper')
      if (speedWrapper && !speedWrapper.contains(event.target)) {
        this.showSpeedMenu = false
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.video-play-page {
  width: 100%;
  min-height: 100vh;
  background-color: #f5f5f5;
}

// 导航栏
.video-nav-bar {
  background-color: #fff;
  
  ::v-deep .van-nav-bar__title {
    font-weight: 600;
    color: #323233;
  }
}

// 视频包装器
.video-wrapper {
  position: relative;
  width: 100%;
  padding-top: 56.25%; // 16:9 比例
  background-color: #000;
  overflow: hidden;

  &.fullscreen {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    padding-top: 0;
    z-index: 9999;

    .video-player {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
  }
}

// 视频元素
.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #000;
}

// 加载中遮罩
.loading-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.7);
  z-index: 10;

  .loading-text {
    margin-top: 12px;
    color: #fff;
    font-size: 14px;
  }
}

// 错误遮罩
.error-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.8);
  z-index: 20;

  .error-icon {
    font-size: 48px;
    color: #ee0a24;
    margin-bottom: 12px;
  }

  .error-text {
    color: #fff;
    font-size: 16px;
    margin-bottom: 8px;
  }

  .retry-text {
    color: #969799;
    font-size: 12px;
  }
}

// 播放结束遮罩
.ended-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 15;

  .replay-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 40px;
    background-color: rgba(255, 255, 255, 0.15);
    border-radius: 8px;
    backdrop-filter: blur(10px);
    color: #fff;

    span {
      margin-top: 8px;
      font-size: 14px;
    }
  }
}

// 播放按钮遮罩
.play-btn-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 5;

  .center-play-btn {
    width: 80px;
    height: 80px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    backdrop-filter: blur(4px);
    color: #fff;
  }
}

// 点击遮罩层
.click-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: calc(100% - 80px);
  z-index: 3;
}

// 控制栏
.video-controls {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  padding: 10px 12px;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  z-index: 10;
  transition: opacity 0.3s ease;
}

// 进度条
.progress-bar-wrapper {
  width: 100%;
  height: 20px;
  display: flex;
  align-items: center;
  margin-bottom: 8px;
}

.progress-bar {
  position: relative;
  width: 100%;
  height: 4px;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  overflow: visible;
}

.progress-buffered {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 2px;
}

.progress-played {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background-color: #1989fa;
  border-radius: 2px;
}

.progress-handle {
  position: absolute;
  top: 50%;
  width: 12px;
  height: 12px;
  background-color: #fff;
  border-radius: 50%;
  transform: translate(-50%, -50%);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

// 控制按钮区域
.controls-main {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.controls-left,
.controls-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.control-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0;
  background: transparent;
  border: none;
  color: #fff;
  cursor: pointer;

  &:active {
    opacity: 0.7;
  }
}

.play-pause-btn {
  margin-right: 4px;
}

.time-display {
  color: #fff;
  font-size: 12px;
  font-family: monospace;
}

// 倍速控制
.speed-wrapper {
  position: relative;
}

.speed-btn {
  padding: 2px 8px;
  font-size: 14px;
  font-weight: 500;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
}

.speed-menu {
  position: absolute;
  bottom: 100%;
  right: 0;
  margin-bottom: 8px;
  background-color: rgba(0, 0, 0, 0.9);
  border-radius: 4px;
  overflow: hidden;
  min-width: 60px;
}

.speed-item {
  padding: 10px 16px;
  color: #fff;
  font-size: 14px;
  text-align: center;

  &.active {
    background-color: #1989fa;
  }
}

// 视频信息区域
.video-info {
  padding: 16px;
  background-color: #fff;
  margin-top: 8px;

  .video-title {
    margin: 0 0 8px 0;
    font-size: 18px;
    font-weight: 600;
    color: #323233;
    line-height: 1.4;
  }

  .video-desc {
    margin: 0 0 12px 0;
    font-size: 14px;
    color: #666;
    line-height: 1.6;
  }

  .video-meta {
    display: flex;
    gap: 16px;

    .meta-item {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 13px;
      color: #969799;

      .van-icon {
        font-size: 14px;
      }
    }
  }
}

// 横屏模式适配
@media screen and (orientation: landscape) and (max-height: 500px) {
  .video-wrapper:not(.fullscreen) {
    padding-top: 0;
    height: 100vh;

    .video-player {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      max-height: 100vh;
    }
  }

  .video-info,
  .video-nav-bar {
    display: none;
  }
}
</style>
