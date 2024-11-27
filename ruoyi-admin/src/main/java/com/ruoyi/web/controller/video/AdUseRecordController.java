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
import com.ruoyi.system.domain.AdUseRecord;
import com.ruoyi.system.service.IAdUseRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 广告扫描记录Controller
 * 
 * @author rongwq
 * @date 2024-11-27
 */
@RestController
@RequestMapping("/video/adUseRecord")
public class AdUseRecordController extends BaseController
{
    @Autowired
    private IAdUseRecordService adUseRecordService;

    /**
     * 查询广告扫描记录列表
     */
    @PreAuthorize("@ss.hasPermi('video:adUseRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdUseRecord adUseRecord)
    {
        startPage();
        List<AdUseRecord> list = adUseRecordService.selectAdUseRecordList(adUseRecord);
        return getDataTable(list);
    }

    /**
     * 导出广告扫描记录列表
     */
    @PreAuthorize("@ss.hasPermi('video:adUseRecord:export')")
    @Log(title = "广告扫描记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdUseRecord adUseRecord)
    {
        List<AdUseRecord> list = adUseRecordService.selectAdUseRecordList(adUseRecord);
        ExcelUtil<AdUseRecord> util = new ExcelUtil<AdUseRecord>(AdUseRecord.class);
        util.exportExcel(response, list, "广告扫描记录数据");
    }

    /**
     * 获取广告扫描记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('video:adUseRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(adUseRecordService.selectAdUseRecordById(id));
    }

    /**
     * 新增广告扫描记录
     */
    @PreAuthorize("@ss.hasPermi('video:adUseRecord:add')")
    @Log(title = "广告扫描记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdUseRecord adUseRecord)
    {
        return toAjax(adUseRecordService.insertAdUseRecord(adUseRecord));
    }

    /**
     * 修改广告扫描记录
     */
    @PreAuthorize("@ss.hasPermi('video:adUseRecord:edit')")
    @Log(title = "广告扫描记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdUseRecord adUseRecord)
    {
        return toAjax(adUseRecordService.updateAdUseRecord(adUseRecord));
    }

    /**
     * 删除广告扫描记录
     */
    @PreAuthorize("@ss.hasPermi('video:adUseRecord:remove')")
    @Log(title = "广告扫描记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(adUseRecordService.deleteAdUseRecordByIds(ids));
    }
}
