package com.ruoyi.web.controller.video;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.RechargeKey;
import com.ruoyi.system.service.IRechargeKeyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 充值卡Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/video/rechargeKey")
public class RechargeKeyController extends BaseController
{
    @Autowired
    private IRechargeKeyService rechargeKeyService;

    /**
     * 查询充值卡列表
     */
    @PreAuthorize("@ss.hasPermi('system:key:list')")
    @GetMapping("/list")
    public TableDataInfo list(RechargeKey rechargeKey)
    {
        startPage();
        List<RechargeKey> list = rechargeKeyService.selectRechargeKeyList(rechargeKey);
        return getDataTable(list);
    }

    /**
     * 导出充值卡列表
     */
    @PreAuthorize("@ss.hasPermi('system:key:export')")
    @Log(title = "充值卡", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RechargeKey rechargeKey)
    {
        List<RechargeKey> list = rechargeKeyService.selectRechargeKeyList(rechargeKey);
        ExcelUtil<RechargeKey> util = new ExcelUtil<RechargeKey>(RechargeKey.class);
        util.exportExcel(response, list, "充值卡数据");
    }

    /**
     * 获取充值卡详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:key:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(rechargeKeyService.selectRechargeKeyById(id));
    }

    /**
     * 新增充值卡
     */
    @PreAuthorize("@ss.hasPermi('system:key:add')")
    @Log(title = "充值卡", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RechargeKey rechargeKey)
    {
        return toAjax(rechargeKeyService.insertRechargeKey(rechargeKey));
    }

    /**
     * 生成充值卡
     */
    @PreAuthorize("@ss.hasPermi('system:key:add')")
    @Log(title = "充值卡", businessType = BusinessType.INSERT)
    @PostMapping("/autoCreate")
    public AjaxResult autoCreate(@RequestParam Integer number, @RequestParam Long money)
    {
        return toAjax(rechargeKeyService.autoCreatekey(number,money));
    }


    /**
     * 修改充值卡
     */
    @PreAuthorize("@ss.hasPermi('system:key:edit')")
    @Log(title = "充值卡", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RechargeKey rechargeKey)
    {
        return toAjax(rechargeKeyService.updateRechargeKey(rechargeKey));
    }

    /**
     * 删除充值卡
     */
    @PreAuthorize("@ss.hasPermi('system:key:remove')")
    @Log(title = "充值卡", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rechargeKeyService.deleteRechargeKeyByIds(ids));
    }
}
