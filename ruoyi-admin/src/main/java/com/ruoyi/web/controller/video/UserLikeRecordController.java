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
import com.ruoyi.system.domain.UserLikeRecord;
import com.ruoyi.system.service.IUserLikeRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户点赞记录Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/video/user/like/record")
public class UserLikeRecordController extends BaseController
{
    @Autowired
    private IUserLikeRecordService userLikeRecordService;

    /**
     * 查询用户点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserLikeRecord userLikeRecord)
    {
        startPage();
        List<UserLikeRecord> list = userLikeRecordService.selectUserLikeRecordList(userLikeRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "用户点赞记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserLikeRecord userLikeRecord)
    {
        List<UserLikeRecord> list = userLikeRecordService.selectUserLikeRecordList(userLikeRecord);
        ExcelUtil<UserLikeRecord> util = new ExcelUtil<UserLikeRecord>(UserLikeRecord.class);
        util.exportExcel(response, list, "用户点赞记录数据");
    }

    /**
     * 获取用户点赞记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userLikeRecordService.selectUserLikeRecordById(id));
    }

    /**
     * 新增用户点赞记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "用户点赞记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserLikeRecord userLikeRecord)
    {
        return toAjax(userLikeRecordService.insertUserLikeRecord(userLikeRecord));
    }

    /**
     * 修改用户点赞记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "用户点赞记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserLikeRecord userLikeRecord)
    {
        return toAjax(userLikeRecordService.updateUserLikeRecord(userLikeRecord));
    }

    /**
     * 删除用户点赞记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "用户点赞记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userLikeRecordService.deleteUserLikeRecordByIds(ids));
    }
}
