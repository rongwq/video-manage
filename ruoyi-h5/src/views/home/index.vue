<template>
  <div class="home-page">
    <!-- 导航栏 -->
    <van-nav-bar title="视频列表" fixed placeholder :border="false" />

    <!-- 视频列表 -->
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
        class="video-list"
      >
        <div
          v-for="video in videoList"
          :key="video.id"
          class="video-card"
          @click="goToPlay(video.id)"
        >
          <div class="video-cover">
            <van-image
              :src="video.img || defaultCover"
              fit="cover"
              width="100%"
              height="100%"
              lazy-load
            />
            <div class="video-duration" v-if="video.duration">
              {{ formatVideoTime(video.duration) }}
            </div>
            <div class="play-icon">
              <van-icon name="play-circle-o" size="40" />
            </div>
          </div>
          <div class="video-info">
            <h3 class="video-title">{{ video.title }}</h3>
            <p class="video-desc" v-if="video.remark">{{ video.remark }}</p>
            <div class="video-stats">
              <span class="stats-item">
                <van-icon name="play-circle-o" />
                {{ video.playNum || 0 }}
              </span>
              <span class="stats-item" v-if="video.likeNum">
                <van-icon name="good-job-o" />
                {{ video.likeNum }}
              </span>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script>
import { listVideo } from '@/api/video'
import { formatVideoTime } from '@/utils/index'

export default {
  name: 'Home',
  data() {
    return {
      videoList: [],
      loading: false,
      finished: false,
      refreshing: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      defaultCover: 'https://img.yzcdn.cn/vant/cat.jpeg'
    }
  },
  methods: {
    formatVideoTime,

    /**
     * 加载视频列表
     */
    onLoad() {
      listVideo(this.queryParams).then(response => {
        const rows = response.rows || []
        const total = response.total || 0

        if (this.refreshing) {
          this.videoList = rows
          this.refreshing = false
        } else {
          this.videoList.push(...rows)
        }

        this.loading = false

        if (this.videoList.length >= total) {
          this.finished = true
        } else {
          this.queryParams.pageNum++
        }
      }).catch(() => {
        this.loading = false
        this.finished = true
      })
    },

    /**
     * 下拉刷新
     */
    onRefresh() {
      this.finished = false
      this.queryParams.pageNum = 1
      this.onLoad()
    },

    /**
     * 跳转到播放页
     */
    goToPlay(id) {
      this.$router.push(`/video/play/${id}`)
    }
  }
}
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.video-list {
  padding: 12px;
}

.video-card {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  &:active {
    opacity: 0.9;
  }
}

.video-cover {
  position: relative;
  width: 100%;
  padding-top: 56.25%; // 16:9
  background-color: #000;

  .van-image {
    position: absolute;
    top: 0;
    left: 0;
  }

  .video-duration {
    position: absolute;
    right: 8px;
    bottom: 8px;
    padding: 2px 6px;
    background-color: rgba(0, 0, 0, 0.7);
    color: #fff;
    font-size: 12px;
    border-radius: 4px;
  }

  .play-icon {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    color: rgba(255, 255, 255, 0.9);
  }
}

.video-info {
  padding: 12px;

  .video-title {
    margin: 0 0 8px 0;
    font-size: 16px;
    font-weight: 600;
    color: #323233;
    line-height: 1.4;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .video-desc {
    margin: 0 0 8px 0;
    font-size: 13px;
    color: #666;
    line-height: 1.5;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }

  .video-stats {
    display: flex;
    gap: 16px;

    .stats-item {
      display: flex;
      align-items: center;
      gap: 4px;
      font-size: 12px;
      color: #969799;

      .van-icon {
        font-size: 14px;
      }
    }
  }
}
</style>
