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
import com.ruoyi.system.domain.AdReg;
import com.ruoyi.system.service.IAdRegService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 注册广告Controller
 * 
 * @author rongwq
 * @date 2024-12-13
 */
@RestController
@RequestMapping("/system/reg")
public class AdRegController extends BaseController
{
    @Autowired
    private IAdRegService adRegService;

    /**
     * 查询注册广告列表
     */
    @PreAuthorize("@ss.hasPermi('system:reg:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdReg adReg)
    {
        startPage();
        List<AdReg> list = adRegService.selectAdRegList(adReg);
        return getDataTable(list);
    }

    /**
     * 导出注册广告列表
     */
    @PreAuthorize("@ss.hasPermi('system:reg:export')")
    @Log(title = "注册广告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdReg adReg)
    {
        List<AdReg> list = adRegService.selectAdRegList(adReg);
        ExcelUtil<AdReg> util = new ExcelUtil<AdReg>(AdReg.class);
        util.exportExcel(response, list, "注册广告数据");
    }

    /**
     * 获取注册广告详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:reg:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(adRegService.selectAdRegById(id));
    }

    /**
     * 新增注册广告
     */
    @PreAuthorize("@ss.hasPermi('system:reg:add')")
    @Log(title = "注册广告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdReg adReg)
    {
        return toAjax(adRegService.insertAdReg(adReg));
    }

    /**
     * 修改注册广告
     */
    @PreAuthorize("@ss.hasPermi('system:reg:edit')")
    @Log(title = "注册广告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdReg adReg)
    {
        return toAjax(adRegService.updateAdReg(adReg));
    }

    /**
     * 删除注册广告
     */
    @PreAuthorize("@ss.hasPermi('system:reg:remove')")
    @Log(title = "注册广告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(adRegService.deleteAdRegByIds(ids));
    }
}
