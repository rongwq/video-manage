<template>
  <div class="video-list-container">
    <van-nav-bar title="视频列表" fixed placeholder />
    
    <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
      <van-list
        v-model="loading"
        :finished="finished"
        finished-text="没有更多了"
        @load="onLoad"
      >
        <div class="video-grid">
          <div 
            v-for="item in videoList" 
            :key="item.id" 
            class="video-item"
            @click="goToPlay(item)"
          >
            <div class="video-cover">
              <img :src="item.img || defaultCover" :alt="item.title" />
              <div class="video-duration">{{ formatDuration(item.duration) }}</div>
              <div class="play-count">
                <van-icon name="play-circle-o" size="12" />
                {{ item.playNum || 0 }}
              </div>
            </div>
            <div class="video-info">
              <h4 class="video-title van-ellipsis">{{ item.title }}</h4>
              <p class="video-desc van-ellipsis">{{ item.remark || '暂无描述' }}</p>
            </div>
          </div>
        </div>
      </van-list>
    </van-pull-refresh>
  </div>
</template>

<script>
import { getVideoList } from '@/api/video'

export default {
  name: 'VideoList',
  data() {
    return {
      loading: false,
      finished: false,
      refreshing: false,
      videoList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 20
      },
      total: 0,
      defaultCover: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMzAwIiBoZWlnaHQ9IjIwMCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cmVjdCB3aWR0aD0iMTAwJSIgaGVpZ2h0PSIxMDAlIiBmaWxsPSIjZWNlY2VjIi8+PHRleHQgeD0iNTAlIiB5PSI1MCUiIGZvbnQtZmFtaWx5PSJBcmlhbCIgZm9udC1zaXplPSIxNiIgZmlsbD0iIzk5OSIgdGV4dC1hbmNob3I9Im1pZGRsZSIgZHk9Ii4zZW0iPuWbvuS5puS4reaWh+ahozwvdGV4dD48L3N2Zz4='
    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      try {
        const res = await getVideoList(this.queryParams)
        if (res.code === 200) {
          if (this.refreshing) {
            this.videoList = []
            this.refreshing = false
          }
          this.videoList = this.videoList.concat(res.rows || [])
          this.total = res.total || 0
          this.loading = false
          if (this.videoList.length >= this.total) {
            this.finished = true
          }
        }
      } catch (error) {
        console.error('获取视频列表失败:', error)
        this.loading = false
        this.finished = true
      }
    },
    
    onLoad() {
      this.queryParams.pageNum++
      this.getList()
    },
    
    onRefresh() {
      this.queryParams.pageNum = 1
      this.finished = false
      this.loading = true
      this.getList()
    },
    
    goToPlay(item) {
      this.$router.push({
        path: '/video/play',
        query: { id: item.id }
      })
    },
    
    formatDuration(seconds) {
      if (!seconds) return '00:00'
      const mins = Math.floor(seconds / 60)
      const secs = Math.floor(seconds % 60)
      return mins.toString().padStart(2, '0') + ':' + secs.toString().padStart(2, '0')
    }
  }
}
</script>

<style lang="scss" scoped>
.video-list-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 20px;
}

.video-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 10px;
  padding: 10px;
}

.video-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.video-cover {
  position: relative;
  width: 100%;
  padding-top: 56.25%;
}

.video-cover img {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.video-duration {
  position: absolute;
  bottom: 8px;
  right: 8px;
  background: rgba(0, 0, 0, 0.7);
  color: #fff;
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
}

.play-count {
  position: absolute;
  bottom: 8px;
  left: 8px;
  display: flex;
  align-items: center;
  color: #fff;
  font-size: 12px;
  background: rgba(0, 0, 0, 0.5);
  padding: 2px 6px;
  border-radius: 4px;
}

.video-info {
  padding: 8px 10px;
}

.video-title {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 4px 0;
  line-height: 1.4;
}

.video-desc {
  font-size: 12px;
  color: #999;
  margin: 0;
  line-height: 1.4;
}
</style>
