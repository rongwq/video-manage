<template>
  <div class="video-play-container">
    <div
      class="video-wrapper"
      :class="{ 'fullscreen-mode': isFullscreen }"
      ref="videoWrapper"
    >
      <el-loading
        v-if="loading"
        class="video-loading"
        text="视频加载中..."
        background="rgba(0, 0, 0, 0.7)"
      />

      <div v-else-if="videoError" class="video-error">
        <i class="el-icon-warning-outline"></i>
        <p>视频播放失败，请重试</p>
        <el-button type="primary" size="mini" @click="reloadVideo">重新加载</el-button>
      </div>

      <template v-else>
        <div class="video-container" @click="toggleControls">
          <video
            ref="videoElement"
            class="video-element"
            :src="videoUrl"
            :poster="posterUrl"
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
            <i class="el-icon-refresh"></i>
            <p>点击重新播放</p>
          </div>

          <div v-if="!isPlaying && !showEndedOverlay && showPlayBtn" class="play-btn-overlay" @click.stop="togglePlay">
            <i class="el-icon-video-play"></i>
          </div>
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
                <i
                  :class="isPlaying ? 'el-icon-video-pause' : 'el-icon-video-play'"
                  class="control-btn play-btn"
                  @click.stop="togglePlay"
                ></i>
              </div>

              <div class="controls-center">
                <div class="volume-control" v-if="!isIOS">
                  <i class="el-icon-volume-off control-btn" @click.stop="toggleMute"></i>
                  <div class="volume-slider">
                    <div class="volume-track">
                      <div class="volume-fill" :style="{ height: volume + '%' }"></div>
                      <div class="volume-dot" :style="{ bottom: volume + '%' }"></div>
                    </div>
                  </div>
                </div>

                <div class="speed-control">
                  <span class="speed-text" @click.stop="showSpeedMenu = !showSpeedMenu">{{ playbackRate }}x</span>
                  <div v-show="showSpeedMenu" class="speed-menu">
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
                </div>
              </div>

              <div class="controls-right">
                <i
                  :class="isFullscreen ? 'el-icon-copy-document' : 'el-icon-full-screen'"
                  class="control-btn fullscreen-btn"
                  @click.stop="toggleFullscreen"
                ></i>
              </div>
            </div>
          </div>
        </transition>
      </template>
    </div>
  </div>
</template>

<script>
import { getVideo } from "@/api/video/video";
import screenfull from "screenfull";

export default {
  name: "VideoPlay",
  data() {
    return {
      loading: true,
      videoError: false,
      videoUrl: "",
      posterUrl: "",
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
    };
  },
  created() {
    this.initVideo();
  },
  mounted() {
    this.bindEvents();
    this.hideControlsAfterDelay();
  },
  beforeDestroy() {
    this.unbindEvents();
    if (this.controlsTimer) {
      clearTimeout(this.controlsTimer);
    }
    if (this.isFullscreen) {
      this.exitFullscreen();
    }
  },
  methods: {
    initVideo() {
      const videoId = this.$route.params.id;
      if (!videoId) {
        this.$modal.msgError("视频ID不存在");
        this.$router.back();
        return;
      }
      this.loadVideoData(videoId);
    },
    loadVideoData(videoId) {
      this.loading = true;
      this.videoError = false;
      getVideo(videoId)
        .then((response) => {
          const videoData = response.data;
          this.videoUrl = videoData.url;
          this.posterUrl = videoData.img || "";
          this.$nextTick(() => {
            this.captureFirstFrameAsPoster();
          });
        })
        .catch((error) => {
          console.error("加载视频数据失败:", error);
          this.videoError = true;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    captureFirstFrameAsPoster() {
      if (this.posterUrl || !this.$refs.videoElement) return;
      const video = this.$refs.videoElement;
      const tempCanvas = document.createElement("canvas");
      const ctx = tempCanvas.getContext("2d");
      const handleCanPlay = () => {
        tempCanvas.width = video.videoWidth;
        tempCanvas.height = video.videoHeight;
        ctx.drawImage(video, 0, 0, tempCanvas.width, tempCanvas.height);
        this.posterUrl = tempCanvas.toDataURL();
        video.removeEventListener("canplay", handleCanPlay);
      };
      video.addEventListener("canplay", handleCanPlay);
    },
    bindEvents() {
      document.addEventListener("fullscreenchange", this.handleFullscreenChange);
      document.addEventListener("webkitfullscreenchange", this.handleFullscreenChange);
      document.addEventListener("mozfullscreenchange", this.handleFullscreenChange);
      document.addEventListener("MSFullscreenChange", this.handleFullscreenChange);
      window.addEventListener("orientationchange", this.handleOrientationChange);
    },
    unbindEvents() {
      document.removeEventListener("fullscreenchange", this.handleFullscreenChange);
      document.removeEventListener("webkitfullscreenchange", this.handleFullscreenChange);
      document.removeEventListener("mozfullscreenchange", this.handleFullscreenChange);
      document.removeEventListener("MSFullscreenChange", this.handleFullscreenChange);
      window.removeEventListener("orientationchange", this.handleOrientationChange);
    },
    togglePlay() {
      const video = this.$refs.videoElement;
      if (!video) return;
      if (this.isPlaying) {
        video.pause();
      } else {
        video.play().catch((err) => {
          console.error("播放失败:", err);
          this.$modal.msgError("视频播放失败");
        });
      }
    },
    handlePlay() {
      this.isPlaying = true;
      this.showPlayBtn = false;
      this.showEndedOverlay = false;
      this.hideControlsAfterDelay();
    },
    handlePause() {
      this.isPlaying = false;
      this.showPlayBtn = true;
      this.showControls = true;
    },
    handleVideoEnded() {
      this.isPlaying = false;
      this.showEndedOverlay = true;
      this.showControls = true;
    },
    handleVideoError() {
      this.videoError = true;
      this.loading = false;
    },
    handleLoadedMetadata() {
      const video = this.$refs.videoElement;
      this.duration = video.duration;
      if (!this.isIOS) {
        video.volume = this.volume / 100;
      }
    },
    handleTimeUpdate() {
      const video = this.$refs.videoElement;
      this.currentTime = video.currentTime;
      this.progressPercent = (this.currentTime / this.duration) * 100;
      this.updateBuffered();
    },
    handleWaiting() {
      this.showControls = true;
    },
    handleCanPlay() {
      this.loading = false;
    },
    updateBuffered() {
      const video = this.$refs.videoElement;
      if (video.buffered.length > 0) {
        const bufferedEnd = video.buffered.end(video.buffered.length - 1);
        this.bufferedPercent = (bufferedEnd / this.duration) * 100;
      }
    },
    toggleMute() {
      const video = this.$refs.videoElement;
      if (this.isIOS) return;
      this.isMuted = !this.isMuted;
      video.muted = this.isMuted;
    },
    setPlaybackRate(rate) {
      const video = this.$refs.videoElement;
      this.playbackRate = rate;
      video.playbackRate = rate;
      this.showSpeedMenu = false;
    },
    toggleFullscreen() {
      const wrapper = this.$refs.videoWrapper;
      if (!wrapper) return;
      if (screenfull.isEnabled) {
        if (!this.isFullscreen) {
          screenfull.request(wrapper);
        } else {
          screenfull.exit();
        }
      } else {
        this.fallbackFullscreen();
      }
    },
    fallbackFullscreen() {
      this.isFullscreen = !this.isFullscreen;
      const wrapper = this.$refs.videoWrapper;
      if (this.isFullscreen) {
        wrapper.style.position = "fixed";
        wrapper.style.top = "0";
        wrapper.style.left = "0";
        wrapper.style.width = "100%";
        wrapper.style.height = "100%";
        wrapper.style.zIndex = "9999";
        document.body.style.overflow = "hidden";
      } else {
        wrapper.style = "";
        document.body.style.overflow = "";
      }
    },
    handleFullscreenChange() {
      this.isFullscreen = document.fullscreenElement || 
        document.webkitFullscreenElement || 
        document.mozFullScreenElement || 
        document.msFullscreenElement;
    },
    handleOrientationChange() {
      if (window.orientation === 90 || window.orientation === -90) {
        if (!this.isFullscreen) {
          this.toggleFullscreen();
        }
      } else {
        if (this.isFullscreen) {
          this.toggleFullscreen();
        }
      }
    },
    exitFullscreen() {
      if (screenfull.isEnabled && screenfull.isFullscreen) {
        screenfull.exit();
      } else if (this.isFullscreen) {
        this.fallbackFullscreen();
      }
    },
    startSeek(e) {
      this.isSeeking = true;
      this.handleSeek(e);
    },
    handleSeekMove(e) {
      if (this.isSeeking) {
        this.handleSeek(e);
      }
    },
    endSeek() {
      this.isSeeking = false;
    },
    handleSeek(e) {
      const progressBar = this.$refs.progressBar;
      if (!progressBar) return;
      const rect = progressBar.getBoundingClientRect();
      const touch = e.touches[0] || e.changedTouches[0];
      const pos = (touch.clientX - rect.left) / rect.width;
      const seekTime = Math.max(0, Math.min(pos, 1)) * this.duration;
      this.$refs.videoElement.currentTime = seekTime;
      this.progressPercent = pos * 100;
    },
    toggleControls() {
      this.showControls = !this.showControls;
      if (this.showControls) {
        this.hideControlsAfterDelay();
      }
    },
    hideControlsAfterDelay() {
      if (this.controlsTimer) {
        clearTimeout(this.controlsTimer);
      }
      if (this.isPlaying) {
        this.controlsTimer = setTimeout(() => {
          this.showControls = false;
          this.showSpeedMenu = false;
        }, 3000);
      }
    },
    formatTime(seconds) {
      if (isNaN(seconds)) return "00:00";
      const mins = Math.floor(seconds / 60);
      const secs = Math.floor(seconds % 60);
      return `${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
    },
    reloadVideo() {
      const videoId = this.$route.params.id;
      if (videoId) {
        this.loadVideoData(videoId);
      }
    },
    replay() {
      const video = this.$refs.videoElement;
      video.currentTime = 0;
      this.showEndedOverlay = false;
      video.play().catch((err) => {
        console.error("重新播放失败:", err);
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.video-play-container {
  width: 100%;
  min-height: 100vh;
  background: #000;
  position: relative;
}

.video-wrapper {
  position: relative;
  width: 100%;
  height: 100vh;
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
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
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

  i {
    font-size: 50px;
    margin-bottom: 15px;
    color: #ff4d4f;
  }

  p {
    margin: 0 0 20px 0;
    font-size: 16px;
  }
}

.video-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
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
  width: 60px;
  height: 60px;
  border-radius: 50%;
  cursor: pointer;

  i {
    font-size: 30px;
  }

  p {
    margin: 5px 0 0 0;
    font-size: 12px;
  }
}

.ended-overlay {
  width: 80px;
  height: 80px;
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
  background: #409eff;
  border-radius: 2px;
}

.progress-dot {
  position: absolute;
  right: -6px;
  top: 50%;
  transform: translateY(-50%);
  width: 12px;
  height: 12px;
  background: #409eff;
  border-radius: 50%;
  box-shadow: 0 0 4px rgba(0, 0, 0, 0.5);
}

.controls-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.control-btn {
  font-size: 24px;
  cursor: pointer;
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
  gap: 20px;
}

.volume-control {
  position: relative;
  display: flex;
  align-items: center;
}

.volume-slider {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 80px;
  background: rgba(0, 0, 0, 0.8);
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.volume-track {
  width: 4px;
  height: 60px;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 2px;
  position: relative;
}

.volume-fill {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background: #409eff;
  border-radius: 2px;
}

.volume-dot {
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  width: 10px;
  height: 10px;
  background: #409eff;
  border-radius: 50%;
}

.speed-control {
  position: relative;
}

.speed-text {
  cursor: pointer;
  padding: 5px 10px;
  background: rgba(0, 0, 0, 0.5);
  border-radius: 4px;
  font-size: 14px;
}

.speed-menu {
  position: absolute;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.8);
  border-radius: 4px;
  overflow: hidden;
}

.speed-item {
  padding: 8px 15px;
  cursor: pointer;
  font-size: 14px;

  &:hover,
  &.active {
    background: #409eff;
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

@media screen and (max-width: 768px) {
  .control-btn {
    font-size: 20px;
  }

  .speed-text {
    font-size: 12px;
    padding: 3px 8px;
  }

  .video-controls {
    padding: 10px;
  }
}
</style>
