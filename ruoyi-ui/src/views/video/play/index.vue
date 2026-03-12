<template>
  <div class="video-play-container">
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
        <i class="el-icon-loading loading-icon"></i>
        <span class="loading-text">加载中...</span>
      </div>

      <!-- 视频加载失败提示 -->
      <div v-if="loadError" class="error-mask" @click="retryLoad">
        <i class="el-icon-warning-outline error-icon"></i>
        <span class="error-text">视频播放失败，请重试</span>
        <span class="retry-text">点击重新加载</span>
      </div>

      <!-- 播放完成遮罩 -->
      <div v-if="isEnded && !isLoading" class="ended-mask" @click="replay">
        <div class="replay-btn">
          <i class="el-icon-refresh-left"></i>
          <span>重新播放</span>
        </div>
      </div>

      <!-- 播放按钮（初始状态/暂停状态显示） -->
      <div v-if="!isPlaying && !isLoading && !loadError && !isEnded" class="play-btn-mask" @click="play">
        <div class="center-play-btn">
          <i class="el-icon-video-play"></i>
        </div>
      </div>

      <!-- 自定义控制栏 -->
      <div v-show="showControls && !loadError" class="video-controls" @click.stop>
        <!-- 进度条 -->
        <div class="progress-bar-wrapper" @click="handleProgressClick" ref="progressBar">
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
            <button class="control-btn play-pause-btn" @click="togglePlay">
              <i :class="isPlaying ? 'el-icon-video-pause' : 'el-icon-video-play'"></i>
            </button>
            <span class="time-display">{{ formatTime(currentTime) }} / {{ formatTime(duration) }}</span>
          </div>

          <!-- 右侧：音量、倍速、全屏 -->
          <div class="controls-right">
            <!-- 音量控制 -->
            <div class="volume-wrapper" @click.stop>
              <button class="control-btn" @click="toggleMute">
                <i :class="isMuted ? 'el-icon-turn-off-microphone' : 'el-icon-microphone'"></i>
              </button>
              <div class="volume-slider">
                <input
                  type="range"
                  min="0"
                  max="100"
                  v-model="volume"
                  @input="handleVolumeChange"
                  class="volume-range"
                />
              </div>
            </div>

            <!-- 倍速选择 -->
            <div class="speed-wrapper" @click.stop>
              <button class="control-btn speed-btn" @click="showSpeedMenu = !showSpeedMenu">
                {{ playbackRate }}x
              </button>
              <div v-show="showSpeedMenu" class="speed-menu">
                <div
                  v-for="rate in playbackRates"
                  :key="rate"
                  class="speed-item"
                  :class="{ active: playbackRate === rate }"
                  @click="changePlaybackRate(rate)"
                >
                  {{ rate }}x
                </div>
              </div>
            </div>

            <!-- 全屏按钮 -->
            <button class="control-btn fullscreen-btn" @click="toggleFullscreen">
              <i :class="isFullscreen ? 'el-icon-copy-document' : 'el-icon-full-screen'"></i>
            </button>
          </div>
        </div>
      </div>

      <!-- 点击区域（用于显示/隐藏控制栏） -->
      <div class="click-overlay" @click="toggleControls"></div>
    </div>

    <!-- 视频信息区域 -->
    <div class="video-info" v-if="videoInfo.title">
      <h2 class="video-title">{{ videoInfo.title }}</h2>
      <p class="video-desc" v-if="videoInfo.remark">{{ videoInfo.remark }}</p>
      <div class="video-meta">
        <span class="meta-item">
          <i class="el-icon-video-play"></i>
          {{ videoInfo.playNum || 0 }} 次播放
        </span>
        <span class="meta-item" v-if="videoInfo.likeNum">
          <i class="el-icon-thumb"></i>
          {{ videoInfo.likeNum }} 点赞
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import { getVideo } from '@/api/video/video'
import { Loading } from 'element-ui'

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
    this.videoId = this.$route.params.id || this.$route.query.id
    if (this.videoId) {
      this.fetchVideoInfo()
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
     * 显示RuoYi内置loading
     */
    showLoading() {
      this.loadingInstance = Loading.service({
        text: '正在加载视频...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
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
          this.$message.error('视频播放失败，请重试')
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
      this.$message.error('视频加载失败，请检查网络连接')
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
     * 音量变化
     */
    handleVolumeChange() {
      const video = this.$refs.videoPlayer
      if (video) {
        video.volume = this.volume / 100
        this.isMuted = this.volume === 0
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

        // iOS 横屏处理
        if (window.screen && window.screen.orientation) {
          window.screen.orientation.lock('landscape').catch(() => {
            // 锁定方向失败，静默处理
          })
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
      // 横屏时自动进入全屏（可选功能）
      const orientation = window.orientation
      if (orientation === 90 || orientation === -90) {
        // 横屏
        if (!this.isFullscreen) {
          // this.toggleFullscreen()
        }
      }
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
    },

    /**
     * 格式化时间
     */
    formatTime(seconds) {
      if (!seconds || isNaN(seconds)) return '00:00'
      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    }
  }
}
</script>

<style lang="scss" scoped>
.video-play-container {
  width: 100%;
  min-height: 100vh;
  background-color: #000;
  display: flex;
  flex-direction: column;
}

/* 视频包装器 */
.video-wrapper {
  position: relative;
  width: 100%;
  padding-top: 56.25%; /* 16:9 比例 */
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

/* 视频元素 */
.video-player {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: contain;
  background-color: #000;
}

/* 加载中遮罩 */
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

  .loading-icon {
    font-size: 48px;
    color: #fff;
    animation: rotating 2s linear infinite;
  }

  .loading-text {
    margin-top: 16px;
    color: #fff;
    font-size: 14px;
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

/* 错误遮罩 */
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
  cursor: pointer;

  .error-icon {
    font-size: 48px;
    color: #f56c6c;
    margin-bottom: 16px;
  }

  .error-text {
    color: #fff;
    font-size: 16px;
    margin-bottom: 8px;
  }

  .retry-text {
    color: #909399;
    font-size: 12px;
  }
}

/* 播放结束遮罩 */
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
  cursor: pointer;

  .replay-btn {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 20px 40px;
    background-color: rgba(255, 255, 255, 0.15);
    border-radius: 8px;
    backdrop-filter: blur(10px);

    i {
      font-size: 48px;
      color: #fff;
      margin-bottom: 8px;
    }

    span {
      color: #fff;
      font-size: 14px;
    }
  }
}

/* 播放按钮遮罩 */
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
  cursor: pointer;

  .center-play-btn {
    width: 80px;
    height: 80px;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    backdrop-filter: blur(4px);

    i {
      font-size: 40px;
      color: #fff;
      margin-left: 4px;
    }
  }
}

/* 点击遮罩层 */
.click-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: calc(100% - 80px);
  z-index: 3;
}

/* 控制栏 */
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

/* 进度条 */
.progress-bar-wrapper {
  width: 100%;
  height: 20px;
  display: flex;
  align-items: center;
  cursor: pointer;
  margin-bottom: 8px;

  &:hover {
    .progress-handle {
      transform: scale(1.2);
    }
  }
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
  background-color: #409eff;
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
  transition: transform 0.2s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

/* 控制按钮区域 */
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
  width: 36px;
  height: 36px;
  padding: 0;
  background: transparent;
  border: none;
  color: #fff;
  font-size: 20px;
  cursor: pointer;
  transition: opacity 0.2s ease;

  &:active {
    opacity: 0.7;
  }

  i {
    font-size: 20px;
  }
}

.play-pause-btn {
  i {
    font-size: 24px;
  }
}

.time-display {
  color: #fff;
  font-size: 12px;
  font-family: monospace;
}

/* 音量控制 */
.volume-wrapper {
  position: relative;
  display: flex;
  align-items: center;

  &:hover {
    .volume-slider {
      width: 80px;
      opacity: 1;
      margin-left: 8px;
    }
  }
}

.volume-slider {
  width: 0;
  opacity: 0;
  overflow: hidden;
  transition: all 0.3s ease;
}

.volume-range {
  width: 80px;
  height: 4px;
  -webkit-appearance: none;
  appearance: none;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  outline: none;

  &::-webkit-slider-thumb {
    -webkit-appearance: none;
    appearance: none;
    width: 12px;
    height: 12px;
    background-color: #fff;
    border-radius: 50%;
    cursor: pointer;
  }

  &::-moz-range-thumb {
    width: 12px;
    height: 12px;
    background-color: #fff;
    border-radius: 50%;
    cursor: pointer;
    border: none;
  }
}

/* 倍速控制 */
.speed-wrapper {
  position: relative;
}

.speed-btn {
  width: auto;
  padding: 0 8px;
  font-size: 14px;
  font-weight: 500;
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
  cursor: pointer;
  transition: background-color 0.2s ease;

  &:hover,
  &.active {
    background-color: #409eff;
  }
}

/* 视频信息区域 */
.video-info {
  padding: 16px;
  background-color: #fff;

  .video-title {
    margin: 0 0 8px 0;
    font-size: 18px;
    font-weight: 600;
    color: #303133;
    line-height: 1.4;
  }

  .video-desc {
    margin: 0 0 12px 0;
    font-size: 14px;
    color: #606266;
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
      color: #909399;

      i {
        font-size: 14px;
      }
    }
  }
}

/* 移动端适配 */
@media screen and (max-width: 768px) {
  .video-wrapper {
    padding-top: 56.25%;
  }

  .center-play-btn {
    width: 60px !important;
    height: 60px !important;

    i {
      font-size: 30px !important;
    }
  }

  .control-btn {
    width: 32px;
    height: 32px;

    i {
      font-size: 18px;
    }
  }

  .play-pause-btn i {
    font-size: 20px;
  }

  .time-display {
    font-size: 11px;
  }

  .video-info {
    padding: 12px;

    .video-title {
      font-size: 16px;
    }

    .video-desc {
      font-size: 13px;
    }
  }
}

/* iOS 全屏适配 */
@supports (-webkit-touch-callout: none) {
  .video-wrapper.fullscreen {
    .video-player {
      object-fit: contain;
    }
  }
}

/* 横屏模式适配 */
@media screen and (orientation: landscape) and (max-width: 896px) {
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

  .video-info {
    display: none;
  }
}
</style>
