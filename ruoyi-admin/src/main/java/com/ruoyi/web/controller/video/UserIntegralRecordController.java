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
import com.ruoyi.system.domain.UserIntegralRecord;
import com.ruoyi.system.service.IUserIntegralRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 积分记录Controller
 * 
 * @author rongwq
 * @date 2024-12-25
 */
@RestController
@RequestMapping("/record/integralRecord")
public class UserIntegralRecordController extends BaseController
{
    @Autowired
    private IUserIntegralRecordService userIntegralRecordService;

    /**
     * 查询积分记录列表
     */
    @PreAuthorize("@ss.hasPermi('record:integralRecord:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserIntegralRecord userIntegralRecord)
    {
        startPage();
        List<UserIntegralRecord> list = userIntegralRecordService.selectUserIntegralRecordList(userIntegralRecord);
        return getDataTable(list);
    }

    /**
     * 导出积分记录列表
     */
    @PreAuthorize("@ss.hasPermi('record:integralRecord:export')")
    @Log(title = "积分记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserIntegralRecord userIntegralRecord)
    {
        List<UserIntegralRecord> list = userIntegralRecordService.selectUserIntegralRecordList(userIntegralRecord);
        ExcelUtil<UserIntegralRecord> util = new ExcelUtil<UserIntegralRecord>(UserIntegralRecord.class);
        util.exportExcel(response, list, "积分记录数据");
    }

    /**
     * 获取积分记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('record:integralRecord:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userIntegralRecordService.selectUserIntegralRecordById(id));
    }

    /**
     * 新增积分记录
     */
    @PreAuthorize("@ss.hasPermi('record:integralRecord:add')")
    @Log(title = "积分记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserIntegralRecord userIntegralRecord)
    {
        return toAjax(userIntegralRecordService.insertUserIntegralRecord(userIntegralRecord));
    }

    /**
     * 修改积分记录
     */
    @PreAuthorize("@ss.hasPermi('record:integralRecord:edit')")
    @Log(title = "积分记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserIntegralRecord userIntegralRecord)
    {
        return toAjax(userIntegralRecordService.updateUserIntegralRecord(userIntegralRecord));
    }

    /**
     * 删除积分记录
     */
    @PreAuthorize("@ss.hasPermi('record:integralRecord:remove')")
    @Log(title = "积分记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userIntegralRecordService.deleteUserIntegralRecordByIds(ids));
    }
}
