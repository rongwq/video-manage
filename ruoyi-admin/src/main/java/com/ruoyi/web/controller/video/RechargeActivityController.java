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
import com.ruoyi.system.domain.RechargeActivity;
import com.ruoyi.system.service.IRechargeActivityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值活动Controller
 * 
 * @author rongwq
 * @date 2024-11-13
 */
@RestController
@RequestMapping("/system/activity")
public class RechargeActivityController extends BaseController
{
    @Autowired
    private IRechargeActivityService rechargeActivityService;

    /**
     * 查询充值活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:activity:list')")
    @GetMapping("/list")
    public TableDataInfo list(RechargeActivity rechargeActivity)
    {
        startPage();
        List<RechargeActivity> list = rechargeActivityService.selectRechargeActivityList(rechargeActivity);
        return getDataTable(list);
    }

    /**
     * 导出充值活动列表
     */
    @PreAuthorize("@ss.hasPermi('system:activity:export')")
    @Log(title = "充值活动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RechargeActivity rechargeActivity)
    {
        List<RechargeActivity> list = rechargeActivityService.selectRechargeActivityList(rechargeActivity);
        ExcelUtil<RechargeActivity> util = new ExcelUtil<RechargeActivity>(RechargeActivity.class);
        util.exportExcel(response, list, "充值活动数据");
    }

    /**
     * 获取充值活动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:activity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(rechargeActivityService.selectRechargeActivityById(id));
    }

    /**
     * 新增充值活动
     */
    @PreAuthorize("@ss.hasPermi('system:activity:add')")
    @Log(title = "充值活动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RechargeActivity rechargeActivity)
    {
        return toAjax(rechargeActivityService.insertRechargeActivity(rechargeActivity));
    }

    /**
     * 修改充值活动
     */
    @PreAuthorize("@ss.hasPermi('system:activity:edit')")
    @Log(title = "充值活动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RechargeActivity rechargeActivity)
    {
        return toAjax(rechargeActivityService.updateRechargeActivity(rechargeActivity));
    }

    /**
     * 删除充值活动
     */
    @PreAuthorize("@ss.hasPermi('system:activity:remove')")
    @Log(title = "充值活动", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rechargeActivityService.deleteRechargeActivityByIds(ids));
    }
}
