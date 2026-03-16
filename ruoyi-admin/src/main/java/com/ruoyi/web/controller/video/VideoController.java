package com.ruoyi.web.controller.video;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.common.core.domain.entity.VideoCategory;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.VideoValidator;
import com.ruoyi.system.service.IVideoAdService;
import com.ruoyi.system.service.IVideoCategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.system.service.IVideoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 视频Controller
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
@RestController
@RequestMapping("/system/video")
public class VideoController extends BaseController
{
    @Autowired
    private IVideoService VideoService;
    @Autowired
    private IVideoAdService videoAdService;
    @Autowired
    private IVideoCategoryService videoCategoryService;

    /**
     * 查询视频列表
     */
    @GetMapping("/list")
    @Anonymous
    public TableDataInfo list(Video video)
    {
        startPage();
        List<Video> list = VideoService.selectVideoList(video);
        VideoAd videoAd = new VideoAd();
        list.forEach(i->{
            videoAd.setVideoId(i.getId());
            i.setAdList(videoAdService.selectVideoAdList(videoAd));
            //将视频的分类转化为文字
            if(StringUtils.isNotEmpty(i.getCategory())) {
                String[] categoryArr = i.getCategory().split(",");
                List<String> newCategory = new ArrayList<>();
                for(String s : categoryArr){
                    VideoCategory videoCategory = videoCategoryService.selectVideoCategoryById(Long.parseLong(s));
                    if(videoCategory!=null){
                        newCategory.add(videoCategory.getName());
                    }
                }
                i.setCategory(StringUtils.join(newCategory, ","));

            }
        });
        return getDataTable(list);
    }

    /**
     * 导出视频列表
     */
    @PreAuthorize("@ss.hasPermi('system:video:export')")
    @Log(title = "视频", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Video video)
    {
        List<Video> list = VideoService.selectVideoList(video);
        ExcelUtil<Video> util = new ExcelUtil<Video>(Video.class);
        util.exportExcel(response, list, "视频数据");
    }

    /**
     * 获取视频详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:video:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(VideoService.selectVideoById(id));
    }

    /**
     * 新增视频
     */
    @PreAuthorize("@ss.hasPermi('system:video:add')")
    @Log(title = "视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Video video)
    {
        try {
            VideoValidator.validateVideo(video, false);
            return toAjax(VideoService.insertVideo(video));
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 修改视频
     */
    @PreAuthorize("@ss.hasPermi('system:video:edit')")
    @Log(title = "视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Video video)
    {
        try {
            if (video.getId() == null) {
                return AjaxResult.error("视频ID不能为空");
            }
            VideoValidator.validateVideo(video, true);
            return toAjax(VideoService.updateVideo(video));
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 删除视频
     */
    @PreAuthorize("@ss.hasPermi('system:video:remove')")
    @Log(title = "视频", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(VideoService.deleteVideoByIds(ids));
    }

    @Log(title = "视频管理", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:video:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<Video> util = new ExcelUtil(Video.class);
        List<Video> videoList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = VideoService.importData(videoList, updateSupport, operName);
        return success(message);
    }

    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<Video> util = new ExcelUtil(Video.class);
        util.importTemplateExcel(response, "视频数据");
    }
}
