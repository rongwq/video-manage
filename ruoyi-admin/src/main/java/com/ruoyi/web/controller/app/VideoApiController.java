package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.common.core.domain.entity.VideoCategory;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.enums.VideoType;
import com.ruoyi.system.domain.UserCollectRecord;
import com.ruoyi.system.domain.UserLikeRecord;
import com.ruoyi.system.domain.UserPlayRecord;
import com.ruoyi.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 视频Controller
 *
 * @author ruoyi
 * @date 2024-11-9
 */
@RestController
@RequestMapping("/api/video")
public class VideoApiController extends BaseController {
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IVideoAdService videoAdService;
    @Autowired
    private IVideoCategoryService videoCategoryService;
    @Autowired
    private IUserLikeRecordService userLikeRecordService;
    @Autowired
    private IUserCollectRecordService userCollectRecordService;
    @Autowired
    private IUserPlayRecordService userPlayRecordService;

    /**
     * 查询视频列表-根据分类等其它条件
     */
    @GetMapping("/list")
    @Anonymous
    public TableDataInfo list(Video video) {
        startPage();
        List<Video> list = videoService.selectVideoList(video);
        return getDataTable(list);
    }

    /**
     * 推荐视频
     */
    @GetMapping("/list/recommend")
    @Anonymous
    public TableDataInfo recommendList(Video video) {
        List<Video> list = videoService.selectRecommendList(video);
        return getDataTable(list);
    }

    /**
     * 获取视频详细信息
     */
    @GetMapping(value = "/{id}")
    @Anonymous
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        Video video = videoService.selectVideoById(id);
        VideoAd videoAd = new VideoAd();
        videoAd.setVideoId(id);
        video.setAdList(videoAdService.selectVideoAdList(videoAd));
        return success(video);
    }

    /**
     * 查询大类下面子类列表-含视频
     */
    @GetMapping("/childCategoryList")
    @Anonymous
    public TableDataInfo childCategoryList(VideoCategory videoCategory) {
        startPage();
        List<VideoCategory> list = videoCategoryService.selectVideoCategoryList(videoCategory);
        list.forEach(i -> {
            //查询分类下面最新的6条记录
            i.setVideoList(videoService.selectVideoListByCategoryId(i.getId()));
        });
        return getDataTable(list);
    }

    /**
     * 查询视频分类列表-所有大类
     */
    @GetMapping("/categoryList")
    @Anonymous
    public AjaxResult categoryList() {
        VideoCategory videoCategory = new VideoCategory();
        videoCategory.setStatus(UserStatus.OK.getCode());
        videoCategory.setParentId(0L);
        List<VideoCategory> list = videoCategoryService.selectVideoCategoryList(videoCategory);
        return success(list);
    }

    /**
     * 用户点赞
     */
    @PostMapping("/saveLike")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult saveLike(@RequestParam Long videoId) {
        //点赞量+1
        videoService.updateLikeNum(videoId);
        UserLikeRecord userLikeRecord = new UserLikeRecord();
        userLikeRecord.setUserId(getUserId());
        userLikeRecord.setVideoId(videoId);
        return toAjax(userLikeRecordService.insertUserLikeRecord(userLikeRecord));
    }

    /**
     * 用户取消点赞
     */
    @PostMapping("/cancelLike")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult cancelLike(@RequestParam Long videoId) {
        UserLikeRecord userLikeRecord = new UserLikeRecord();
        userLikeRecord.setUserId(getUserId());
        userLikeRecord.setVideoId(videoId);
        List<UserLikeRecord> userLikeRecords = userLikeRecordService.selectUserLikeRecordList(userLikeRecord);
        if(userLikeRecords.isEmpty()){
            return AjaxResult.warn("用户已取消点赞");
        }
        //点赞量-1
        videoService.cancelLikeNum(videoId);
        return toAjax(userLikeRecordService.deleteUserLikeRecordById(userLikeRecords.get(0).getId()));
    }

    /**
     * 用户收藏
     */
    @PostMapping("/saveCollect")
    public AjaxResult saveCollect(@RequestParam Long videoId) {
        UserCollectRecord userCollectRecord = new UserCollectRecord();
        userCollectRecord.setUserId(getUserId());
        userCollectRecord.setVideoId(videoId);
        return toAjax(userCollectRecordService.insertUserCollectRecord(userCollectRecord));
    }

    /**
     * 用户取消收藏
     */
    @PostMapping("/cancelCollect")
    public AjaxResult cancelCollect(@RequestParam Long videoId) {
        UserCollectRecord userCollectRecord = new UserCollectRecord();
        userCollectRecord.setUserId(getUserId());
        userCollectRecord.setVideoId(videoId);
        List<UserCollectRecord> list = userCollectRecordService.selectUserCollectRecordList(userCollectRecord);
        if(list.isEmpty()){
            return AjaxResult.warn("用户已取消收藏");
        }
        return toAjax(userCollectRecordService.deleteUserCollectRecordById(list.get(0).getId()));
    }

    /**
     * 用户播放
     */
    @PostMapping("/savePlay")
    @Transactional(rollbackFor = Exception.class)
    public R savePlay(@RequestParam Long videoId) {
        //检查视频类型
        Video video = videoService.selectVideoById(videoId);
        if(video==null){
            return R.fail("视频不存在");
        }
        //如果视频为购买类型，则需要校验是否购买，未购买则返回错误
        if(VideoType.BUY.getCode().equals(video.getType())) {
            return R.fail("视频未购买");
        }
        //播放量+1
        videoService.updatePlayNum(videoId);
        UserPlayRecord userPlayRecord = new UserPlayRecord();
        userPlayRecord.setUserId(getUserId());
        userPlayRecord.setVideoId(videoId);
        return R.ok(userPlayRecordService.insertUserPlayRecord(userPlayRecord));
    }

}
