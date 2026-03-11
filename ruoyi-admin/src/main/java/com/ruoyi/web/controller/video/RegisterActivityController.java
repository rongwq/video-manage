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
import com.ruoyi.system.domain.RegisterActivity;
import com.ruoyi.system.service.IRegisterActivityService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 注册送积分活动配置Controller
 * 
 * @author ruoyi
 * @date 2026-03-11
 */
@RestController
@RequestMapping("/video/registerActivity")
public class RegisterActivityController extends BaseController
{
    @Autowired
    private IRegisterActivityService registerActivityService;

    /**
     * 查询注册送积分活动配置列表
     */
    @PreAuthorize("@ss.hasPermi('video:registerActivity:list')")
    @GetMapping("/list")
    public TableDataInfo list(RegisterActivity registerActivity)
    {
        startPage();
        List<RegisterActivity> list = registerActivityService.selectRegisterActivityList(registerActivity);
        return getDataTable(list);
    }

    /**
     * 导出注册送积分活动配置列表
     */
    @PreAuthorize("@ss.hasPermi('video:registerActivity:export')")
    @Log(title = "注册送积分活动配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RegisterActivity registerActivity)
    {
        List<RegisterActivity> list = registerActivityService.selectRegisterActivityList(registerActivity);
        ExcelUtil<RegisterActivity> util = new ExcelUtil<RegisterActivity>(RegisterActivity.class);
        util.exportExcel(response, list, "注册送积分活动配置数据");
    }

    /**
     * 获取注册送积分活动配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('video:registerActivity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(registerActivityService.selectRegisterActivityById(id));
    }

    /**
     * 新增注册送积分活动配置
     */
    @PreAuthorize("@ss.hasPermi('video:registerActivity:add')")
    @Log(title = "注册送积分活动配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RegisterActivity registerActivity)
    {
        return toAjax(registerActivityService.insertRegisterActivity(registerActivity));
    }

    /**
     * 修改注册送积分活动配置
     */
    @PreAuthorize("@ss.hasPermi('video:registerActivity:edit')")
    @Log(title = "注册送积分活动配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RegisterActivity registerActivity)
    {
        return toAjax(registerActivityService.updateRegisterActivity(registerActivity));
    }

    /**
     * 删除注册送积分活动配置
     */
    @PreAuthorize("@ss.hasPermi('video:registerActivity:remove')")
    @Log(title = "注册送积分活动配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(registerActivityService.deleteRegisterActivityByIds(ids));
    }

    /**
     * 查询当前有效的活动配置
     */
    @GetMapping("/current")
    public AjaxResult getCurrentActivity()
    {
        return success(registerActivityService.selectCurrentActivity());
    }
}
