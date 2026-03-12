<template>
  <div class="video-player-container">
    <div class="video-play-section">
      <van-loading v-if="loading" type="spinner" color="#1989fa" class="video-loading" />

      <div v-else-if="videoError" class="video-error">
        <van-icon name="warning-o" size="40" color="#ff4d4f" />
        <p>视频加载失败，请重试</p>
        <van-button type="primary" size="small" @click="reload">重新加载</van-button>
      </div>

      <template v-else>
        <div class="video-wrapper" :class="{ 'fullscreen-mode': isFullscreen }" ref="videoWrapper">
          <video
            ref="videoElement"
            class="video-element"
            :src="currentVideo.url"
            :poster="currentVideo.img || ''"
            preload="metadata"
            playsinline
            webkit-playsinline
            x5-playsinline
            x5-video-player-type="h5"
            x5-video-orientation="landscape|portrait"
            @loadedmetadata="handleLoadedMetadata"
            @timeupdate="handleTimeUpdate"
            @ended="handleVideoEnded"
            @error="handleVideoError"
            @play="handlePlay"
            @pause="handlePause"
            @waiting="handleWaiting"
            @canplay="handleCanPlay"
          ></video>

          <div v-if="!isPlaying && showEndedOverlay" class="ended-overlay" @click.stop="replay">
            <van-icon name="replay" size="30" color="#fff" />
            <p>重新播放</p>
          </div>

          <div v-if="!isPlaying && !showEndedOverlay && showPlayBtn" class="play-btn-overlay" @click.stop="togglePlay">
            <van-icon name="play-circle-o" size="60" color="#fff" />
          </div>

          <transition name="fade">
            <div
              v-show="showControls"
              class="video-controls"
              @touchstart.stop
              @click.stop
            >
              <div class="controls-top">
                <span class="current-time">{{ formatTime(currentTime) }}</span>
                <span class="divider">/</span>
                <span class="total-time">{{ formatTime(duration) }}</span>
              </div>

              <div class="progress-bar" ref="progressBar" @touchstart.stop="startSeek" @touchmove.stop="handleSeekMove" @touchend.stop="endSeek">
                <div class="progress-bg">
                  <div class="progress-buffered" :style="{ width: bufferedPercent + '%' }"></div>
                  <div class="progress-played" :style="{ width: progressPercent + '%' }">
                    <div class="progress-dot"></div>
                  </div>
                </div>
              </div>

              <div class="controls-bottom">
                <div class="controls-left">
                  <van-icon
                    :name="isPlaying ? 'pause-circle-o' : 'play-circle-o'"
                    size="24"
                    color="#fff"
                    class="control-btn"
                    @click.stop="togglePlay"
                  />
                </div>

                <div class="controls-center">
                  <div class="speed-control">
                    <span class="speed-text" @click.stop="showSpeedMenu = !showSpeedMenu">{{ playbackRate }}x</span>
                    <van-popup v-model="showSpeedMenu" position="bottom" overlay-style="background: transparent;" get-container="body" :style="{ padding: '0' }">
                      <div class="speed-menu">
                        <div
                          v-for="speed in [0.5, 1.0, 1.5, 2.0]"
                          :key="speed"
                          class="speed-item"
                          :class="{ active: playbackRate === speed }"
                          @click.stop="setPlaybackRate(speed)"
                        >
                          {{ speed }}x
                        </div>
                      </div>
                    </van-popup>
                  </div>
                </div>

                <div class="controls-right">
                  <van-icon
                    :name="isFullscreen ? 'cross' : 'expand-o'"
                    size="22"
                    color="#fff"
                    class="control-btn"
                    @click.stop="toggleFullscreen"
                  />
                </div>
              </div>
            </div>
          </transition>
        </div>
      </template>
    </div>

    <div v-if="!isFullscreen" class="video-info-section">
      <div class="video-title">{{ currentVideo.title || '暂无标题' }}</div>
      <div class="video-stats">
        <span class="stat-item">
          <van-icon name="eye-o" size="14" />
          <span>{{ currentVideo.playNum || 0 }}</span>
        </span>
        <span class="stat-item">
          <van-icon name="like-o" size="14" />
          <span>{{ currentVideo.likeNum || 0 }}</span>
        </span>
      </div>
    </div>

    <div v-if="!isFullscreen" class="video-list-section">
      <div class="section-title">推荐视频</div>
      <van-list
        v-model="listLoading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <div
          v-for="video in videoList"
          :key="video.id"
          class="video-list-item"
          @click="playVideo(video)"
        >
          <div class="video-thumb">
            <img :src="video.img || video.url" alt="" />
            <div class="play-icon">
              <van-icon name="play-circle-o" size="30" color="#fff" />
            </div>
          </div>
          <div class="video-item-info">
            <div class="video-item-title">{{ video.title }}</div>
            <div class="video-item-stats">
              <span><van-icon name="eye-o" size="12" /> {{ video.playNum || 0 }}</span>
              <span><van-icon name="like-o" size="12" /> {{ video.likeNum || 0 }}</span>
            </div>
          </div>
        </div>
      </van-list>
    </div>
  </div>
</template>

<script>
import { getRecommendList } from '@/api/video'
import screenfull from 'screenfull'

export default {
  name: 'VideoPlayer',
  data() {
    return {
      loading: true,
      videoError: false,
      videoList: [],
      currentVideo: {},
      isPlaying: false,
      currentTime: 0,
      duration: 0,
      progressPercent: 0,
      bufferedPercent: 0,
      volume: 80,
      isMuted: false,
      playbackRate: 1.0,
      isFullscreen: false,
      showControls: true,
      controlsTimer: null,
      showSpeedMenu: false,
      isSeeking: false,
      showEndedOverlay: false,
      showPlayBtn: true,
      isIOS: /iPhone|iPad|iPod/i.test(navigator.userAgent),
      listLoading: false,
      finished: false
    }
  },
  created() {
    this.init()
  },
  mounted() {
    this.bindEvents()
    this.hideControlsAfterDelay()
  },
  beforeDestroy() {
    this.unbindEvents()
    if (this.controlsTimer) {
      clearTimeout(this.controlsTimer)
    }
    if (this.isFullscreen) {
      this.exitFullscreen()
    }
  },
  methods: {
    init() {
      const videoId = this.$route.params.id
      if (videoId) {
        this.loadSingleVideo(videoId)
      } else {
        this.loadRecommendList()
      }
    },
    loadSingleVideo(id) {
      this.loading = true
      getRecommendList().then(res => {
        const list = res.rows || []
        this.videoList = list
        const video = list.find(item => item.id == id)
        if (video) {
          this.currentVideo = video
        } else if (list.length > 0) {
          this.currentVideo = list[0]
        }
        this.loading = false
      }).catch(() => {
        this.videoError = true
        this.loading = false
      })
    },
    loadRecommendList() {
      this.loading = true
      getRecommendList().then(res => {
        const list = res.rows || []
        this.videoList = list
        if (list.length > 0) {
          this.currentVideo = list[0]
        }
        this.loading = false
      }).catch(() => {
        this.videoError = true
        this.loading = false
      })
    },
    onLoad() {
      if (this.videoList.length === 0) {
        this.listLoading = false
        return
      }
      setTimeout(() => {
        this.listLoading = false
        this.finished = true
      }, 1000)
    },
    playVideo(video) {
      if (this.$refs.videoElement) {
        this.$refs.videoElement.pause()
      }
      this.currentVideo = video
      this.isPlaying = false
      this.showPlayBtn = true
      this.currentTime = 0
      this.progressPercent = 0
      this.showEndedOverlay = false
      this.$nextTick(() => {
        if (this.$refs.videoElement) {
          this.$refs.videoElement.load()
        }
      })
    },
    captureFirstFrameAsPoster() {
      if (this.currentVideo.img || !this.$refs.videoElement) return
      const video = this.$refs.videoElement
      const tempCanvas = document.createElement('canvas')
      const ctx = tempCanvas.getContext('2d')
      const handleCanPlay = () => {
        tempCanvas.width = video.videoWidth
        tempCanvas.height = video.videoHeight
        ctx.drawImage(video, 0, 0, tempCanvas.width, tempCanvas.height)
        this.currentVideo.img = tempCanvas.toDataURL()
        video.removeEventListener('canplay', handleCanPlay)
      }
      video.addEventListener('canplay', handleCanPlay)
    },
    bindEvents() {
      document.addEventListener('fullscreenchange', this.handleFullscreenChange)
      document.addEventListener('webkitfullscreenchange', this.handleFullscreenChange)
      document.addEventListener('mozfullscreenchange', this.handleFullscreenChange)
      document.addEventListener('MSFullscreenChange', this.handleFullscreenChange)
      window.addEventListener('orientationchange', this.handleOrientationChange)
    },
    unbindEvents() {
      document.removeEventListener('fullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('webkitfullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('mozfullscreenchange', this.handleFullscreenChange)
      document.removeEventListener('MSFullscreenChange', this.handleFullscreenChange)
      window.removeEventListener('orientationchange', this.handleOrientationChange)
    },
    togglePlay() {
      const video = this.$refs.videoElement
      if (!video) return
      if (this.isPlaying) {
        video.pause()
      } else {
        video.play().catch((err) => {
          console.error('播放失败:', err)
          this.$toast.fail('视频播放失败')
        })
      }
    },
    handlePlay() {
      this.isPlaying = true
      this.showPlayBtn = false
      this.showEndedOverlay = false
      this.hideControlsAfterDelay()
    },
    handlePause() {
      this.isPlaying = false
      this.showPlayBtn = true
      this.showControls = true
    },
    handleVideoEnded() {
      this.isPlaying = false
      this.showEndedOverlay = true
      this.showControls = true
    },
    handleVideoError() {
      this.videoError = true
      this.loading = false
    },
    handleLoadedMetadata() {
      const video = this.$refs.videoElement
      this.duration = video.duration
      if (!this.isIOS) {
        video.volume = this.volume / 100
      }
    },
    handleTimeUpdate() {
      const video = this.$refs.videoElement
      this.currentTime = video.currentTime
      this.progressPercent = (this.currentTime / this.duration) * 100
      this.updateBuffered()
    },
    handleWaiting() {
      this.showControls = true
    },
    handleCanPlay() {
      this.loading = false
    },
    updateBuffered() {
      const video = this.$refs.videoElement
      if (video.buffered.length > 0) {
        const bufferedEnd = video.buffered.end(video.buffered.length - 1)
        this.bufferedPercent = (bufferedEnd / this.duration) * 100
      }
    },
    setPlaybackRate(rate) {
      const video = this.$refs.videoElement
      this.playbackRate = rate
      video.playbackRate = rate
      this.showSpeedMenu = false
    },
    toggleFullscreen() {
      const wrapper = this.$refs.videoWrapper
      if (!wrapper) return
      if (screenfull.isEnabled) {
        if (!this.isFullscreen) {
          screenfull.request(wrapper)
        } else {
          screenfull.exit()
        }
      } else {
        this.fallbackFullscreen()
      }
    },
    fallbackFullscreen() {
      this.isFullscreen = !this.isFullscreen
      const wrapper = this.$refs.videoWrapper
      if (this.isFullscreen) {
        wrapper.style.position = 'fixed'
        wrapper.style.top = '0'
        wrapper.style.left = '0'
        wrapper.style.width = '100%'
        wrapper.style.height = '100%'
        wrapper.style.zIndex = '9999'
        document.body.style.overflow = 'hidden'
      } else {
        wrapper.style = ''
        document.body.style.overflow = ''
      }
    },
    handleFullscreenChange() {
      this.isFullscreen = document.fullscreenElement ||
        document.webkitFullscreenElement ||
        document.mozFullScreenElement ||
        document.msFullscreenElement
    },
    handleOrientationChange() {
      if (window.orientation === 90 || window.orientation === -90) {
        if (!this.isFullscreen) {
          this.toggleFullscreen()
        }
      } else {
        if (this.isFullscreen) {
          this.toggleFullscreen()
        }
      }
    },
    exitFullscreen() {
      if (screenfull.isEnabled && screenfull.isFullscreen) {
        screenfull.exit()
      } else if (this.isFullscreen) {
        this.fallbackFullscreen()
      }
    },
    startSeek(e) {
      this.isSeeking = true
      this.handleSeek(e)
    },
    handleSeekMove(e) {
      if (this.isSeeking) {
        this.handleSeek(e)
      }
    },
    endSeek() {
      this.isSeeking = false
    },
    handleSeek(e) {
      const progressBar = this.$refs.progressBar
      if (!progressBar) return
      const rect = progressBar.getBoundingClientRect()
      const touch = e.touches[0] || e.changedTouches[0]
      const pos = (touch.clientX - rect.left) / rect.width
      const seekTime = Math.max(0, Math.min(pos, 1)) * this.duration
      this.$refs.videoElement.currentTime = seekTime
      this.progressPercent = pos * 100
    },
    toggleControls() {
      this.showControls = !this.showControls
      if (this.showControls) {
        this.hideControlsAfterDelay()
      }
    },
    hideControlsAfterDelay() {
      if (this.controlsTimer) {
        clearTimeout(this.controlsTimer)
      }
      if (this.isPlaying) {
        this.controlsTimer = setTimeout(() => {
          this.showControls = false
          this.showSpeedMenu = false
        }, 3000)
      }
    },
    formatTime(seconds) {
      if (isNaN(seconds)) return '00:00'
      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      return `${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`
    },
    reload() {
      if (this.$route.params.id) {
        this.loadSingleVideo(this.$route.params.id)
      } else {
        this.loadRecommendList()
      }
    },
    replay() {
      const video = this.$refs.videoElement
      video.currentTime = 0
      this.showEndedOverlay = false
      video.play().catch((err) => {
        console.error('重新播放失败:', err)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.video-player-container {
  min-height: 100vh;
  background: #f5f5f5;
}

.video-play-section {
  position: relative;
  width: 100%;
  height: 50vh;
  background: #000;

  @media screen and (max-width: 768px) {
    height: 35vh;
  }
}

.video-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #000;

  &.fullscreen-mode {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: 9999;
  }
}

.video-loading {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 10;
}

.video-error {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  text-align: center;
  padding: 20px;
  height: 100%;
  background: #000;

  p {
    margin: 10px 0 20px 0;
    font-size: 14px;
  }
}

.video-element {
  max-width: 100%;
  max-height: 100%;
  width: auto;
  height: auto;
  object-fit: contain;
}

.play-btn-overlay,
.ended-overlay {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 5;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
  background: rgba(0, 0, 0, 0.5);
  width: 80px;
  height: 80px;
  border-radius: 50%;
  cursor: pointer;

  p {
    margin: 5px 0 0 0;
    font-size: 12px;
  }
}

.video-controls {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(to top, rgba(0, 0, 0, 0.8), transparent);
  padding: 15px;
  color: #fff;
  z-index: 10;
}

.controls-top {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin-bottom: 8px;
  font-size: 14px;
}

.divider {
  margin: 0 4px;
  opacity: 0.7;
}

.progress-bar {
  width: 100%;
  height: 20px;
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  cursor: pointer;
}

.progress-bg {
  width: 100%;
  height: 4px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  position: relative;
}

.progress-buffered {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 2px;
}

.progress-played {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: #1989fa;
  border-radius: 2px;
}

.progress-dot {
  position: absolute;
  right: -6px;
  top: 50%;
  transform: translateY(-50%);
  width: 12px;
  height: 12px;
  background: #1989fa;
  border-radius: 50%;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.5);
}

.controls-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.control-btn {
  padding: 5px;
}

.controls-left,
.controls-right {
  flex: 1;
}

.controls-right {
  text-align: right;
}

.controls-center {
  flex: 2;
  display: flex;
  align-items: center;
  justify-content: center;
}

.speed-control {
  position: relative;
}

.speed-text {
  cursor: pointer;
  padding: 3px 8px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  font-size: 14px;
}

.speed-menu {
  background: rgba(0, 0, 0, 0.8);
  border-radius: 4px;
  overflow: hidden;
}

.speed-item {
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;
  color: #fff;

  &:hover,
  &.active {
    background: #1989fa;
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.video-info-section {
  padding: 15px;
  background: #fff;
  margin-bottom: 10px;
}

.video-title {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: 8px;
  line-height: 1.4;
  color: #333;
}

.video-stats {
  display: flex;
  gap: 20px;
  font-size: 14px;
  color: #666;

  .stat-item {
    display: flex;
    align-items: center;
    gap: 4px;
  }
}

.video-list-section {
  background: #fff;
}

.section-title {
  padding: 12px 15px;
  font-size: 16px;
  font-weight: 500;
  border-bottom: 1px solid #f0f0f0;
}

.video-list-item {
  display: flex;
  padding: 12px 15px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
}

.video-thumb {
  position: relative;
  width: 120px;
  height: 70px;
  margin-right: 12px;
  border-radius: 4px;
  overflow: hidden;

  img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .play-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
}

.video-item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.video-item-title {
  font-size: 14px;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.video-item-stats {
  font-size: 12px;
  color: #666;
  display: flex;
  gap: 15px;
}
</style>
