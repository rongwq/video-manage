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
import com.ruoyi.system.domain.AdHome;
import com.ruoyi.system.service.IAdHomeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 首页广告Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/video/homeAd")
public class AdHomeController extends BaseController
{
    @Autowired
    private IAdHomeService adHomeService;

    /**
     * 查询首页广告列表
     */
    @PreAuthorize("@ss.hasPermi('system:home:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdHome adHome)
    {
        startPage();
        List<AdHome> list = adHomeService.selectAdHomeList(adHome);
        return getDataTable(list);
    }

    /**
     * 导出首页广告列表
     */
    @PreAuthorize("@ss.hasPermi('system:home:export')")
    @Log(title = "首页广告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdHome adHome)
    {
        List<AdHome> list = adHomeService.selectAdHomeList(adHome);
        ExcelUtil<AdHome> util = new ExcelUtil<AdHome>(AdHome.class);
        util.exportExcel(response, list, "首页广告数据");
    }

    /**
     * 获取首页广告详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:home:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(adHomeService.selectAdHomeById(id));
    }

    /**
     * 新增首页广告
     */
    @PreAuthorize("@ss.hasPermi('system:home:add')")
    @Log(title = "首页广告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdHome adHome)
    {
        return toAjax(adHomeService.insertAdHome(adHome));
    }

    /**
     * 修改首页广告
     */
    @PreAuthorize("@ss.hasPermi('system:home:edit')")
    @Log(title = "首页广告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdHome adHome)
    {
        return toAjax(adHomeService.updateAdHome(adHome));
    }

    /**
     * 删除首页广告
     */
    @PreAuthorize("@ss.hasPermi('system:home:remove')")
    @Log(title = "首页广告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(adHomeService.deleteAdHomeByIds(ids));
    }
}
