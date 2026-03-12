<template>
  <div class="video-play-container">
    <van-nav-bar
      :title="videoInfo.title || '视频播放'"
      left-arrow
      fixed
      placeholder
      @click-left="onClickLeft"
    />
    
    <div class="video-wrapper" :class="{ 'is-fullscreen': isFullscreen }">
      <video
        ref="videoPlayer"
        class="video-player"
        :src="videoInfo.url"
        :poster="videoInfo.img || videoPoster"
        webkit-playsinline
        playsinline
        :x5-video-player-type="isFullscreen ? 'h5' : ''"
        :x5-video-player-fullscreen="isFullscreen"
        preload="metadata"
        @loadedmetadata="onLoadedMetadata"
        @timeupdate="onTimeUpdate"
        @ended="onEnded"
        @error="onVideoError"
        @waiting="onWaiting"
        @canplay="onCanPlay"
        @play="onPlay"
        @pause="onPause"
      ></video>
      
      <div v-if="showControls" class="video-controls" @click="toggleControls">
        <div v-if="loading" class="loading-overlay">
          <van-loading type="spinner" color="#ffffff" size="36px" />
        </div>
        
        <div v-if="videoError" class="error-overlay">
          <van-icon name="warning-o" size="48" color="#ffffff" />
          <p>视频播放失败，请重试</p>
          <van-button type="primary" size="small" @click.stop="retryPlay">重新加载</van-button>
        </div>
        
        <div v-if="playEnded" class="ended-overlay" @click.stop="replayVideo">
          <van-icon name="replay" size="48" color="#ffffff" />
          <p>重新播放</p>
        </div>
        
        <div v-if="!isPlaying && !loading && !videoError && !playEnded" class="play-btn" @click.stop="togglePlay">
          <van-icon name="play-circle-o" size="60" color="#ffffff" />
        </div>
        
        <div v-show="showControlBar && !videoError && !playEnded" class="control-bar" @click.stop>
          <div class="control-top">
            <van-icon 
              :name="isPlaying ? 'pause-circle-o' : 'play-circle-o'" 
              size="28" 
              color="#ffffff"
              @click="togglePlay"
            />
            <span class="time-text">{{ formatTime(currentTime) }} / {{ formatTime(duration) }}</span>
            <div class="control-right">
              <van-icon 
                name="volume-o" 
                size="22" 
                color="#ffffff"
                @click="showVolumeSlider = !showVolumeSlider"
              />
              <span class="speed-btn" @click="showSpeedPicker = true">{{ currentSpeed }}x</span>
              <van-icon 
                :name="isFullscreen ? 'shrink' : 'expand'" 
                size="22" 
                color="#ffffff"
                @click="toggleFullscreen"
              />
            </div>
          </div>
          
          <div class="progress-wrapper">
            <van-slider 
              v-model="progressValue"
              active-color="#1989fa"
              inactive-color="#ffffff"
              :max="100"
              @change="onProgressChange"
              @start="onProgressStart"
              @end="onProgressEnd"
            />
          </div>
          
          <div v-if="showVolumeSlider" class="volume-slider">
            <van-icon name="volume-o" size="18" color="#ffffff" />
            <van-slider 
              v-model="volumeValue"
              active-color="#1989fa"
              inactive-color="#ffffff"
              :max="100"
              @change="onVolumeChange"
            />
            <van-icon name="volume" size="18" color="#ffffff" />
          </div>
        </div>
      </div>
    </div>
    
    <div v-if="!isFullscreen" class="video-info">
      <h3 class="video-title">{{ videoInfo.title }}</h3>
      <div class="video-stats">
        <span><van-icon name="play-circle-o" /> {{ videoInfo.playNum || 0 }}次播放</span>
        <span><van-icon name="like-o" /> {{ videoInfo.likeNum || 0 }}</span>
      </div>
      <p v-if="videoInfo.remark" class="video-desc">{{ videoInfo.remark }}</p>
      
      <div class="video-actions">
        <van-button 
          :type="videoInfo.isLike ? 'primary' : 'default'" 
          :icon="videoInfo.isLike ? 'like' : 'like-o'"
          size="small"
          @click="handleLike"
        >
          {{ videoInfo.isLike ? '已点赞' : '点赞' }}
        </van-button>
        <van-button 
          :type="videoInfo.isCollect ? 'primary' : 'default'" 
          :icon="videoInfo.isCollect ? 'star' : 'star-o'"
          size="small"
          @click="handleCollect"
        >
          {{ videoInfo.isCollect ? '已收藏' : '收藏' }}
        </van-button>
      </div>
    </div>
    
    <van-popup v-model="showSpeedPicker" position="bottom" round>
      <van-picker
        show-toolbar
        title="播放速度"
        :columns="speedOptions"
        :default-index="speedIndex"
        @confirm="onSpeedConfirm"
        @cancel="showSpeedPicker = false"
      />
    </van-popup>
  </div>
</template>

<script>
import { getVideoInfo, savePlay, saveLike, cancelLike, saveCollect, cancelCollect } from '@/api/video'
import { Toast, Loading } from 'vant'

export default {
  name: 'VideoPlay',
  data() {
    return {
      videoId: null,
      userId: null,
      videoInfo: {},
      videoPoster: '',
      loading: true,
      videoError: false,
      isPlaying: false,
      playEnded: false,
      showControls: true,
      showControlBar: true,
      isFullscreen: false,
      currentTime: 0,
      duration: 0,
      progressValue: 0,
      volumeValue: 80,
      currentSpeed: 1.0,
      showSpeedPicker: false,
      showVolumeSlider: false,
      speedOptions: [
        { text: '0.5x', value: 0.5 },
        { text: '0.75x', value: 0.75 },
        { text: '正常', value: 1.0 },
        { text: '1.25x', value: 1.25 },
        { text: '1.5x', value: 1.5 },
        { text: '2.0x', value: 2.0 }
      ],
      controlsTimer: null,
      isDragging: false
    }
  },
  computed: {
    speedIndex() {
      return this.speedOptions.findIndex(item => item.value === this.currentSpeed)
    }
  },
  created() {
    this.videoId = this.$route.query.id || this.$route.params.id
    this.userId = this.$route.query.userId || localStorage.getItem('userId')
    this.loadVideoInfo()
  },
  mounted() {
    this.initVideoEvents()
    document.addEventListener('fullscreenchange', this.onFullscreenChange)
    document.addEventListener('webkitfullscreenchange', this.onFullscreenChange)
    window.addEventListener('orientationchange', this.onOrientationChange)
  },
  beforeDestroy() {
    document.removeEventListener('fullscreenchange', this.onFullscreenChange)
    document.removeEventListener('webkitfullscreenchange', this.onFullscreenChange)
    window.removeEventListener('orientationchange', this.onOrientationChange)
    if (this.controlsTimer) {
      clearTimeout(this.controlsTimer)
    }
  },
  methods: {
    async loadVideoInfo() {
      if (!this.videoId) {
        Toast.fail('视频ID不存在')
        return
      }
      const loadingInstance = Loading.service({
        lock: true,
        text: '加载中...',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      try {
        const res = await getVideoInfo(this.videoId, this.userId)
        if (res.code === 200) {
          this.videoInfo = res.data || {}
          this.videoInfo.isLike = res.data.isLike || false
          this.videoInfo.isCollect = res.data.isCollect || false
          if (this.videoInfo.img) {
            this.videoPoster = this.videoInfo.img
          }
        } else {
          Toast.fail(res.msg || '获取视频信息失败')
        }
      } catch (error) {
        console.error('加载视频信息失败:', error)
        Toast.fail('获取视频信息失败')
      } finally {
        loadingInstance.close()
        this.loading = false
      }
    },
    
    initVideoEvents() {
      const video = this.$refs.videoPlayer
      if (!video) return
      video.volume = this.volumeValue / 100
    },
    
    onLoadedMetadata() {
      const video = this.$refs.videoPlayer
      this.duration = video.duration
      if (!this.videoInfo.img) {
        this.generatePoster()
      }
    },
    
    generatePoster() {
      const video = this.$refs.videoPlayer
      const canvas = document.createElement('canvas')
      canvas.width = video.videoWidth
      canvas.height = video.videoHeight
      const ctx = canvas.getContext('2d')
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height)
      this.videoPoster = canvas.toDataURL('image/jpeg')
    },
    
    onTimeUpdate() {
      if (this.isDragging) return
      const video = this.$refs.videoPlayer
      this.currentTime = video.currentTime
      this.progressValue = (this.currentTime / this.duration) * 100 || 0
    },
    
    onEnded() {
      this.isPlaying = false
      this.playEnded = true
      this.showControlBar = true
      this.showControls = true
    },
    
    onVideoError() {
      this.videoError = true
      this.loading = false
      Toast.fail('视频加载失败')
    },
    
    onWaiting() {
      this.loading = true
    },
    
    onCanPlay() {
      this.loading = false
    },
    
    onPlay() {
      this.isPlaying = true
      this.playEnded = false
      this.autoHideControls()
    },
    
    onPause() {
      this.isPlaying = false
      this.showControlBar = true
    },
    
    togglePlay() {
      const video = this.$refs.videoPlayer
      if (!video) return
      
      if (this.isPlaying) {
        video.pause()
      } else {
        this.playEnded = false
        video.play().catch(err => {
          console.error('播放失败:', err)
          Toast.fail('视频播放失败')
        })
        this.recordPlay()
      }
    },
    
    async recordPlay() {
      try {
        await savePlay(this.videoId)
        if (this.videoInfo.playNum !== undefined) {
          this.videoInfo.playNum++
        }
      } catch (error) {
        console.error('记录播放失败:', error)
      }
    },
    
    toggleControls() {
      this.showControls = !this.showControls
      if (this.showControls && this.isPlaying) {
        this.autoHideControls()
      }
    },
    
    autoHideControls() {
      if (this.controlsTimer) {
        clearTimeout(this.controlsTimer)
      }
      this.controlsTimer = setTimeout(() => {
        if (this.isPlaying && !this.isDragging) {
          this.showControlBar = false
        }
      }, 3000)
    },
    
    onProgressStart() {
      this.isDragging = true
      if (this.controlsTimer) {
        clearTimeout(this.controlsTimer)
      }
    },
    
    onProgressEnd() {
      this.isDragging = false
      if (this.isPlaying) {
        this.autoHideControls()
      }
    },
    
    onProgressChange(value) {
      const video = this.$refs.videoPlayer
      if (!video || !this.duration) return
      const time = (value / 100) * this.duration
      video.currentTime = time
      this.currentTime = time
    },
    
    onVolumeChange(value) {
      const video = this.$refs.videoPlayer
      if (video) {
        video.volume = value / 100
      }
    },
    
    onSpeedConfirm(value) {
      this.currentSpeed = value.value
      const video = this.$refs.videoPlayer
      if (video) {
        video.playbackRate = value.value
      }
      this.showSpeedPicker = false
      Toast.success('播放速度: ' + value.text)
    },
    
    toggleFullscreen() {
      if (this.isFullscreen) {
        this.exitFullscreen()
      } else {
        this.enterFullscreen()
      }
    },
    
    enterFullscreen() {
      const container = document.querySelector('.video-wrapper')
      if (!container) return
      
      if (container.requestFullscreen) {
        container.requestFullscreen()
      } else if (container.webkitRequestFullscreen) {
        container.webkitRequestFullscreen()
      } else if (container.webkitEnterFullscreen) {
        container.webkitEnterFullscreen()
      } else if (container.msRequestFullscreen) {
        container.msRequestFullscreen()
      }
      
      this.isFullscreen = true
      
      if (this.isIOS()) {
        const video = this.$refs.videoPlayer
        if (video && video.webkitEnterFullscreen) {
          video.webkitEnterFullscreen()
        }
      }
    },
    
    exitFullscreen() {
      if (document.exitFullscreen) {
        document.exitFullscreen()
      } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen()
      } else if (document.msExitFullscreen) {
        document.msExitFullscreen()
      }
      
      this.isFullscreen = false
    },
    
    onFullscreenChange() {
      this.isFullscreen = !!(document.fullscreenElement || document.webkitFullscreenElement)
    },
    
    onOrientationChange() {
      if (this.isFullscreen) {
        const video = this.$refs.videoPlayer
        if (video) {
          setTimeout(() => {
            video.style.width = window.innerWidth + 'px'
            video.style.height = window.innerHeight + 'px'
          }, 100)
        }
      }
    },
    
    isIOS() {
      return /iPad|iPhone|iPod/.test(navigator.userAgent)
    },
    
    replayVideo() {
      const video = this.$refs.videoPlayer
      if (video) {
        video.currentTime = 0
        this.playEnded = false
        video.play().catch(err => {
          console.error('重播失败:', err)
        })
      }
    },
    
    retryPlay() {
      this.videoError = false
      this.loading = true
      const video = this.$refs.videoPlayer
      if (video) {
        video.load()
      }
    },
    
    async handleLike() {
      try {
        if (this.videoInfo.isLike) {
          await cancelLike(this.videoId)
          this.videoInfo.isLike = false
          if (this.videoInfo.likeNum > 0) {
            this.videoInfo.likeNum--
          }
          Toast.success('取消点赞')
        } else {
          await saveLike(this.videoId)
          this.videoInfo.isLike = true
          this.videoInfo.likeNum++
          Toast.success('点赞成功')
        }
      } catch (error) {
        console.error('点赞操作失败:', error)
      }
    },
    
    async handleCollect() {
      try {
        if (this.videoInfo.isCollect) {
          await cancelCollect(this.videoId)
          this.videoInfo.isCollect = false
          Toast.success('取消收藏')
        } else {
          await saveCollect(this.videoId)
          this.videoInfo.isCollect = true
          Toast.success('收藏成功')
        }
      } catch (error) {
        console.error('收藏操作失败:', error)
      }
    },
    
    formatTime(seconds) {
      if (!seconds || isNaN(seconds)) return '00:00'
      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      const hours = Math.floor(mins / 60)
      const remainMins = mins % 60
      
      if (hours > 0) {
        return hours.toString().padStart(2, '0') + ':' + remainMins.toString().padStart(2, '0') + ':' + secs.toString().padStart(2, '0')
      }
      return mins.toString().padStart(2, '0') + ':' + secs.toString().padStart(2, '0')
    },
    
    onClickLeft() {
      if (this.isFullscreen) {
        this.exitFullscreen()
      } else {
        this.$router.back()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.video-play-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.video-wrapper {
  position: relative;
  width: 100%;
  background-color: #000;
  overflow: hidden;
}

.video-wrapper.is-fullscreen {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: 9999;
}

.video-player {
  width: 100%;
  display: block;
  background-color: #000;
}

.video-controls {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.loading-overlay,
.error-overlay,
.ended-overlay {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.loading-overlay p,
.error-overlay p,
.ended-overlay p {
  margin-top: 10px;
  font-size: 14px;
}

.play-btn {
  cursor: pointer;
  transition: transform 0.2s;
}

.control-bar {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  padding: 10px 15px;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
}

.control-top {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.time-text {
  color: #fff;
  font-size: 12px;
  margin-left: 10px;
  flex: 1;
}

.control-right {
  display: flex;
  align-items: center;
}

.control-right .van-icon {
  margin-left: 15px;
}

.speed-btn {
  color: #fff;
  font-size: 12px;
  padding: 2px 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 4px;
  margin-left: 15px;
}

.progress-wrapper {
  padding: 5px 0;
}

.volume-slider {
  position: absolute;
  bottom: 80px;
  left: 15px;
  right: 15px;
  display: flex;
  align-items: center;
  padding: 10px;
  background: rgba(0, 0, 0, 0.7);
  border-radius: 8px;
}

.volume-slider .van-slider {
  flex: 1;
  margin: 0 10px;
}

.video-info {
  padding: 15px;
  background-color: #fff;
}

.video-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 10px 0;
  line-height: 1.4;
}

.video-stats {
  display: flex;
  margin-bottom: 10px;
  font-size: 12px;
  color: #999;
}

.video-stats span {
  display: flex;
  align-items: center;
  margin-right: 20px;
}

.video-stats .van-icon {
  margin-right: 4px;
}

.video-desc {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin: 0 0 15px 0;
}

.video-actions {
  display: flex;
}

.video-actions .van-button {
  margin-right: 10px;
}
</style>
