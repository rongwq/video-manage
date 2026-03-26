package com.ruoyi.web.controller.video;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.RegActivityConfig;
import com.ruoyi.system.service.IRegActivityConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 注册活动配置Controller
 *
 * @author ruoyi
 * @date 2024-09-14
 */
@RestController
@RequestMapping("/video/regActivityConfig")
public class RegActivityConfigController extends BaseController
{
    @Autowired
    private IRegActivityConfigService regActivityConfigService;

    /**
     * 查询注册活动配置列表
     */
    @PreAuthorize("@ss.hasPermi('video:regActivityConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(RegActivityConfig regActivityConfig)
    {
        startPage();
        List<RegActivityConfig> list = regActivityConfigService.selectRegActivityConfigList(regActivityConfig);
        return getDataTable(list);
    }

    /**
     * 导出注册活动配置列表
     */
    @PreAuthorize("@ss.hasPermi('video:regActivityConfig:export')")
    @Log(title = "注册活动配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RegActivityConfig regActivityConfig)
    {
        List<RegActivityConfig> list = regActivityConfigService.selectRegActivityConfigList(regActivityConfig);
        ExcelUtil<RegActivityConfig> util = new ExcelUtil<RegActivityConfig>(RegActivityConfig.class);
        util.exportExcel(response, list, "注册活动配置数据");
    }

    /**
     * 获取注册活动配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('video:regActivityConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(regActivityConfigService.selectRegActivityConfigById(id));
    }

    /**
     * 新增注册活动配置
     */
    @PreAuthorize("@ss.hasPermi('video:regActivityConfig:add')")
    @Log(title = "注册活动配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RegActivityConfig regActivityConfig)
    {
        return toAjax(regActivityConfigService.insertRegActivityConfig(regActivityConfig));
    }

    /**
     * 修改注册活动配置
     */
    @PreAuthorize("@ss.hasPermi('video:regActivityConfig:edit')")
    @Log(title = "注册活动配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RegActivityConfig regActivityConfig)
    {
        return toAjax(regActivityConfigService.updateRegActivityConfig(regActivityConfig));
    }

    /**
     * 删除注册活动配置
     */
    @PreAuthorize("@ss.hasPermi('video:regActivityConfig:remove')")
    @Log(title = "注册活动配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(regActivityConfigService.deleteRegActivityConfigByIds(ids));
    }
}
