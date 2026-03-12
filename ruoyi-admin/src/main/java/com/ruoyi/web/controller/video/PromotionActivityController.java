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
import com.ruoyi.system.domain.PromotionActivity;
import com.ruoyi.system.service.IPromotionActivityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 活动管理Controller
 * 
 * @author rongwq
 * @date 2024-11-13
 */
@RestController
@RequestMapping("/system/promotion")
public class PromotionActivityController extends BaseController
{
    @Autowired
    private IPromotionActivityService promotionActivityService;

    /**
     * 查询活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:list')")
    @GetMapping("/list")
    public TableDataInfo list(PromotionActivity promotionActivity)
    {
        startPage();
        List<PromotionActivity> list = promotionActivityService.selectPromotionActivityList(promotionActivity);
        return getDataTable(list);
    }

    /**
     * 导出活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:export')")
    @Log(title = "活动管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PromotionActivity promotionActivity)
    {
        List<PromotionActivity> list = promotionActivityService.selectPromotionActivityList(promotionActivity);
        ExcelUtil<PromotionActivity> util = new ExcelUtil<PromotionActivity>(PromotionActivity.class);
        util.exportExcel(response, list, "活动管理数据");
    }

    /**
     * 获取活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(promotionActivityService.selectPromotionActivityById(id));
    }

    /**
     * 新增活动
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:add')")
    @Log(title = "活动管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PromotionActivity promotionActivity)
    {
        return toAjax(promotionActivityService.insertPromotionActivity(promotionActivity));
    }

    /**
     * 修改活动
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:edit')")
    @Log(title = "活动管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PromotionActivity promotionActivity)
    {
        return toAjax(promotionActivityService.updatePromotionActivity(promotionActivity));
    }

    /**
     * 删除活动
     */
    @PreAuthorize("@ss.hasPermi('system:promotion:remove')")
    @Log(title = "活动管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(promotionActivityService.deletePromotionActivityByIds(ids));
    }
}
