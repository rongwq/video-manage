package com.ruoyi.web.controller.video;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Cdkey;
import com.ruoyi.system.service.ICdkeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 激活码Controller
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
@RestController
@RequestMapping("/system/cdkey")
public class CdkeyController extends BaseController
{
    @Autowired
    private ICdkeyService CdkeyService;

    /**
     * 查询激活码列表
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:list')")
    @GetMapping("/list")
    public TableDataInfo list(Cdkey Cdkey)
    {
        startPage();
        //非系统管理员admin用户
        if(!getLoginUser().getUser().isAdmin()) {
            Cdkey.setUserId(getUserId());
        }
        List<Cdkey> list = CdkeyService.selectCdkeyList(Cdkey);
        return getDataTable(list);
    }

    /**
     * 导出激活码列表
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:export')")
    @Log(title = "激活码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Cdkey Cdkey)
    {
        List<Cdkey> list = CdkeyService.selectCdkeyList(Cdkey);
        ExcelUtil<Cdkey> util = new ExcelUtil<Cdkey>(Cdkey.class);
        util.exportExcel(response, list, "激活码数据");
    }

    /**
     * 获取激活码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:query')")
    @GetMapping(value = "/{cdkeyId}")
    public AjaxResult getInfo(@PathVariable("cdkeyId") Long cdkeyId)
    {
        return success(CdkeyService.selectCdkeyByCdkeyId(cdkeyId));
    }

    /**
     * 新增激活码
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:add')")
    @Log(title = "激活码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Cdkey Cdkey)
    {
        return toAjax(CdkeyService.insertCdkey(Cdkey));
    }

    /**
     * 生成激活码
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:add')")
    @Log(title = "激活码", businessType = BusinessType.INSERT)
    @PostMapping("/autoCreate")
    public AjaxResult autoCreate(@RequestParam Integer number)
    {
        return toAjax(CdkeyService.autoCreateCdkey(number));
    }

    /**
     * 修改激活码
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:edit')")
    @Log(title = "激活码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Cdkey Cdkey)
    {
        return toAjax(CdkeyService.updateCdkey(Cdkey));
    }

    /**
     * 删除激活码
     */
    @PreAuthorize("@ss.hasPermi('system:cdkey:remove')")
    @Log(title = "激活码", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cdkeyIds}")
    public AjaxResult remove(@PathVariable Long[] cdkeyIds)
    {
        return toAjax(CdkeyService.deleteCdkeyByCdkeyIds(cdkeyIds));
    }
}
