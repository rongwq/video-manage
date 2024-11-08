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
import com.ruoyi.system.domain.UserCollectRecord;
import com.ruoyi.system.service.IUserCollectRecordService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户收藏记录Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/video/user/collect/record")
public class UserCollectRecordController extends BaseController
{
    @Autowired
    private IUserCollectRecordService userCollectRecordService;

    /**
     * 查询用户收藏记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserCollectRecord userCollectRecord)
    {
        startPage();
        List<UserCollectRecord> list = userCollectRecordService.selectUserCollectRecordList(userCollectRecord);
        return getDataTable(list);
    }

    /**
     * 导出用户收藏记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "用户收藏记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserCollectRecord userCollectRecord)
    {
        List<UserCollectRecord> list = userCollectRecordService.selectUserCollectRecordList(userCollectRecord);
        ExcelUtil<UserCollectRecord> util = new ExcelUtil<UserCollectRecord>(UserCollectRecord.class);
        util.exportExcel(response, list, "用户收藏记录数据");
    }

    /**
     * 获取用户收藏记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userCollectRecordService.selectUserCollectRecordById(id));
    }

    /**
     * 新增用户收藏记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "用户收藏记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserCollectRecord userCollectRecord)
    {
        return toAjax(userCollectRecordService.insertUserCollectRecord(userCollectRecord));
    }

    /**
     * 修改用户收藏记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "用户收藏记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserCollectRecord userCollectRecord)
    {
        return toAjax(userCollectRecordService.updateUserCollectRecord(userCollectRecord));
    }

    /**
     * 删除用户收藏记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "用户收藏记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userCollectRecordService.deleteUserCollectRecordByIds(ids));
    }
}
