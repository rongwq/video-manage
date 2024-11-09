package com.ruoyi.web.controller.video;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.system.service.IVideoAdService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 视频广告关联Controller
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
@RestController
@RequestMapping("/system/video/ad")
public class VideoAdController extends BaseController
{
    @Autowired
    private IVideoAdService videoAdService;

    /**
     * 查询视频广告关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(VideoAd videoAd)
    {
        startPage();
        List<VideoAd> list = videoAdService.selectVideoAdList(videoAd);
        return getDataTable(list);
    }

    /**
     * 导出视频广告关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:video:export')")
    @Log(title = "视频广告关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VideoAd videoAd)
    {
        List<VideoAd> list = videoAdService.selectVideoAdList(videoAd);
        ExcelUtil<VideoAd> util = new ExcelUtil<VideoAd>(VideoAd.class);
        util.exportExcel(response, list, "视频广告关联数据");
    }

    /**
     * 获取视频广告关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:video:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(videoAdService.selectVideoAdById(id));
    }

    /**
     * 新增视频广告关联
     */
    @PreAuthorize("@ss.hasPermi('system:video:add')")
    @Log(title = "视频广告关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VideoAd videoAd)
    {
        return toAjax(videoAdService.insertVideoAd(videoAd));
    }

    /**
     * 修改视频广告关联
     */
    @PreAuthorize("@ss.hasPermi('system:video:edit')")
    @Log(title = "视频广告关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VideoAd videoAd)
    {
        return toAjax(videoAdService.updateVideoAd(videoAd));
    }

    /**
     * 删除视频广告关联
     */
    @PreAuthorize("@ss.hasPermi('system:video:remove')")
    @Log(title = "视频广告关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(videoAdService.deleteVideoAdByIds(ids));
    }
}
