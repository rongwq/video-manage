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
import com.ruoyi.system.domain.AdRegRecord;
import com.ruoyi.system.service.IAdRegRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户注册广告请求记录Controller
 * 
 * @author rongwq
 * @date 2024-12-24
 */
@RestController
@RequestMapping("/video/adRegRecord")
public class AdRegRecordController extends BaseController
{
    @Autowired
    private IAdRegRecordService adRegRecordService;

    /**
     * 查询用户注册广告请求记录列表
     */
    @PreAuthorize("@ss.hasPermi('video:adRegRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(AdRegRecord adRegRecord)
    {
        startPage();
        List<AdRegRecord> list = adRegRecordService.selectAdRegRecordList(adRegRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户注册广告请求记录列表
     */
    @PreAuthorize("@ss.hasPermi('video:adRegRecord:export')")
    @Log(title = "用户注册广告请求记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AdRegRecord adRegRecord)
    {
        List<AdRegRecord> list = adRegRecordService.selectAdRegRecordList(adRegRecord);
        ExcelUtil<AdRegRecord> util = new ExcelUtil<AdRegRecord>(AdRegRecord.class);
        util.exportExcel(response, list, "用户注册广告请求记录数据");
    }

    /**
     * 获取用户注册广告请求记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('video:adRegRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(adRegRecordService.selectAdRegRecordById(id));
    }

    /**
     * 新增用户注册广告请求记录
     */
    @PreAuthorize("@ss.hasPermi('video:adRegRecord:add')")
    @Log(title = "用户注册广告请求记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AdRegRecord adRegRecord)
    {
        return toAjax(adRegRecordService.insertAdRegRecord(adRegRecord));
    }

    /**
     * 修改用户注册广告请求记录
     */
    @PreAuthorize("@ss.hasPermi('video:adRegRecord:edit')")
    @Log(title = "用户注册广告请求记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AdRegRecord adRegRecord)
    {
        return toAjax(adRegRecordService.updateAdRegRecord(adRegRecord));
    }

    /**
     * 删除用户注册广告请求记录
     */
    @PreAuthorize("@ss.hasPermi('video:adRegRecord:remove')")
    @Log(title = "用户注册广告请求记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(adRegRecordService.deleteAdRegRecordByIds(ids));
    }
}
