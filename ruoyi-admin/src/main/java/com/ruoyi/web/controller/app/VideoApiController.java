package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.VideoCategory;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.system.service.IVideoAdService;
import com.ruoyi.system.service.IVideoCategoryService;
import com.ruoyi.system.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class VideoApiController extends BaseController
{
    @Autowired
    private IVideoService videoService;
    @Autowired
    private IVideoAdService videoAdService;
    @Autowired
    private IVideoCategoryService videoCategoryService;

    /**
     * 查询视频列表-根据分类等其它条件
     */
    @GetMapping("/list")
    @Anonymous
    public TableDataInfo list(Video video)
    {
        startPage();
        List<Video> list = videoService.selectVideoList(video);
        VideoAd videoAd = new VideoAd();
        list.forEach(i->{
            videoAd.setVideoId(i.getId());
            i.setAdList(videoAdService.selectVideoAdList(videoAd));
        });
        return getDataTable(list);
    }

    /**
     * 获取视频详细信息
     */
    @GetMapping(value = "/{id}")
    @Anonymous
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(videoService.selectVideoById(id));
    }

    /**
     * 查询大类下面子类列表-含视频
     */
    @GetMapping("/childCategoryList")
    @Anonymous
    public TableDataInfo childCategoryList(VideoCategory videoCategory)
    {
        startPage();
        List<VideoCategory> list = videoCategoryService.selectVideoCategoryList(videoCategory);
        list.forEach(i->{
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
    public AjaxResult categoryList()
    {
        VideoCategory videoCategory = new VideoCategory();
        videoCategory.setStatus(UserStatus.OK.getCode());
        videoCategory.setParentId(0L);
        List<VideoCategory> list = videoCategoryService.selectVideoCategoryList(videoCategory);
        return success(list);
    }

}
