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
import com.ruoyi.system.domain.UserPlayRecord;
import com.ruoyi.system.service.IUserPlayRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户视频播放记录Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/video/user/play/record")
public class UserPlayRecordController extends BaseController
{
    @Autowired
    private IUserPlayRecordService userPlayRecordService;

    /**
     * 查询用户视频播放记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserPlayRecord userPlayRecord)
    {
        startPage();
        List<UserPlayRecord> list = userPlayRecordService.selectUserPlayRecordList(userPlayRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户视频播放记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "用户视频播放记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserPlayRecord userPlayRecord)
    {
        List<UserPlayRecord> list = userPlayRecordService.selectUserPlayRecordList(userPlayRecord);
        ExcelUtil<UserPlayRecord> util = new ExcelUtil<UserPlayRecord>(UserPlayRecord.class);
        util.exportExcel(response, list, "用户视频播放记录数据");
    }

    /**
     * 获取用户视频播放记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userPlayRecordService.selectUserPlayRecordById(id));
    }

    /**
     * 新增用户视频播放记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "用户视频播放记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserPlayRecord userPlayRecord)
    {
        return toAjax(userPlayRecordService.insertUserPlayRecord(userPlayRecord));
    }

    /**
     * 修改用户视频播放记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "用户视频播放记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserPlayRecord userPlayRecord)
    {
        return toAjax(userPlayRecordService.updateUserPlayRecord(userPlayRecord));
    }

    /**
     * 删除用户视频播放记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "用户视频播放记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userPlayRecordService.deleteUserPlayRecordByIds(ids));
    }
}
