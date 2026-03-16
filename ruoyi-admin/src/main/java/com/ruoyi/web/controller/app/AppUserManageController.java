package com.ruoyi.web.controller.app;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/user")
public class AppUserManageController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    @PreAuthorize("@ss.hasPermi('app:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppUser appUser)
    {
        startPage();
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        for (AppUser user : list)
        {
            user.setPassword(null);
        }
        return getDataTable(list);
    }

    @Log(title = "APP用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('app:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppUser appUser)
    {
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        for (AppUser user : list)
        {
            user.setPassword(null);
        }
        ExcelUtil<AppUser> util = new ExcelUtil<AppUser>(AppUser.class);
        util.exportExcel(response, list, "APP用户数据");
    }

    @PreAuthorize("@ss.hasPermi('app:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable("userId") Long userId)
    {
        AppUser user = appUserService.selectAppUserById(userId);
        user.setPassword(null);
        return success(user);
    }

    @PreAuthorize("@ss.hasPermi('app:user:add')")
    @Log(title = "APP用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AppUser appUser)
    {
        if (StringUtils.isNotEmpty(appUser.getPhonenumber()) && !appUserService.checkPhoneUnique(appUser))
        {
            return error("新增用户'" + appUser.getNickName() + "'失败，手机号码已存在");
        }
        appUser.setUserName(appUser.getPhonenumber());
        appUser.setCreateBy(getUsername());
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        return toAjax(appUserService.insertAppUser(appUser));
    }

    @PreAuthorize("@ss.hasPermi('app:user:edit')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AppUser appUser)
    {
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUser(appUser));
    }

    @PreAuthorize("@ss.hasPermi('app:user:remove')")
    @Log(title = "APP用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(appUserService.deleteAppUserByIds(userIds));
    }

    @PreAuthorize("@ss.hasPermi('app:user:resetPwd')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody AppUser appUser)
    {
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.resetAppUserPwdById(appUser));
    }

    @PreAuthorize("@ss.hasPermi('app:user:edit')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody AppUser appUser)
    {
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUserStatus(appUser));
    }
}
