package com.ruoyi.web.controller.app;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;

@RestController
@RequestMapping("/app/user")
public class SysAppUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    @PreAuthorize("@ss.hasPermi('app:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppUser appUser)
    {
        startPage();
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        return getDataTable(list);
    }

    @Log(title = "APP用户管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('app:user:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppUser appUser)
    {
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        ExcelUtil<AppUser> util = new ExcelUtil<AppUser>(AppUser.class);
        util.exportExcel(response, list, "APP用户数据");
    }

    @PreAuthorize("@ss.hasPermi('app:user:query')")
    @GetMapping(value = "/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId)
    {
        return AjaxResult.success(appUserService.selectAppUserById(userId));
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
        appUser.setCreateBy(getUsername());
        if (StringUtils.isEmpty(appUser.getUserName()))
        {
            appUser.setUserName(appUser.getPhonenumber());
        }
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        return toAjax(appUserService.registerAppUser(appUser) ? 1 : 0);
    }

    @PreAuthorize("@ss.hasPermi('app:user:edit')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody AppUser appUser)
    {
        appUser.setUpdateBy(getUsername());
        appUser.setPhonenumber(null);
        appUser.setPassword(null);
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
        AppUser user = appUserService.selectAppUserById(appUser.getUserId());
        if (user == null)
        {
            return error("用户不存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUser(user));
    }

    @PreAuthorize("@ss.hasPermi('app:user:edit')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody AppUser appUser)
    {
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUser(appUser));
    }
}
