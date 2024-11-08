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
import com.ruoyi.system.domain.AdOther;
import com.ruoyi.system.service.IAdOtherService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 其它部位广告Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/video/otherAd")
public class AdOtherController extends BaseController
{
    @Autowired
    private IAdOtherService adOtherService;

    /**
     * 查询其它部位广告列表
     */
    @PreAuthorize("@ss.hasPermi('system:other:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdOther adOther)
    {
        startPage();
        List<AdOther> list = adOtherService.selectAdOtherList(adOther);
        return getDataTable(list);
    }

    /**
     * 导出其它部位广告列表
     */
    @PreAuthorize("@ss.hasPermi('system:other:export')")
    @Log(title = "其它部位广告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdOther adOther)
    {
        List<AdOther> list = adOtherService.selectAdOtherList(adOther);
        ExcelUtil<AdOther> util = new ExcelUtil<AdOther>(AdOther.class);
        util.exportExcel(response, list, "其它部位广告数据");
    }

    /**
     * 获取其它部位广告详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:other:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(adOtherService.selectAdOtherById(id));
    }

    /**
     * 新增其它部位广告
     */
    @PreAuthorize("@ss.hasPermi('system:other:add')")
    @Log(title = "其它部位广告", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdOther adOther)
    {
        return toAjax(adOtherService.insertAdOther(adOther));
    }

    /**
     * 修改其它部位广告
     */
    @PreAuthorize("@ss.hasPermi('system:other:edit')")
    @Log(title = "其它部位广告", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdOther adOther)
    {
        return toAjax(adOtherService.updateAdOther(adOther));
    }

    /**
     * 删除其它部位广告
     */
    @PreAuthorize("@ss.hasPermi('system:other:remove')")
    @Log(title = "其它部位广告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(adOtherService.deleteAdOtherByIds(ids));
    }
}
