package com.ruoyi.web.controller.app;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP用户管理控制器
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/app/manage/user")
public class AppUserManageController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * 获取APP用户列表
     */
    @PreAuthorize("@ss.hasPermi('app:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(AppUser appUser)
    {
        startPage();
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        return getDataTable(list);
    }

    /**
     * 导出APP用户列表
     */
    @PreAuthorize("@ss.hasPermi('app:user:export')")
    @Log(title = "APP用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AppUser appUser)
    {
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        ExcelUtil<AppUser> util = new ExcelUtil<AppUser>(AppUser.class);
        util.exportExcel(response, list, "APP用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('app:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(userId))
        {
            AppUser appUser = appUserService.selectAppUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, appUser);
        }
        return ajax;
    }

    /**
     * 新增APP用户
     */
    @PreAuthorize("@ss.hasPermi('app:user:add')")
    @Log(title = "APP用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AppUser appUser)
    {
        // 校验手机号
        if (StringUtils.isEmpty(appUser.getPhonenumber()))
        {
            return error("手机号不能为空");
        }
        // 校验手机号格式
        if (!appUser.getPhonenumber().matches("^1[3|4|5|6|7|8|9][0-9]\\d{8}$"))
        {
            return error("请输入正确的手机号码");
        }
        // 校验手机号唯一性
        if (!appUserService.checkPhoneUnique(appUser))
        {
            return error("新增用户失败，手机号码已存在");
        }
        // 校验用户名唯一性
        if (StringUtils.isNotEmpty(appUser.getUserName()) && !appUserService.checkUserNameUnique(appUser))
        {
            return error("新增用户失败，登录账号已存在");
        }
        // 设置默认用户名（如果没有提供）
        if (StringUtils.isEmpty(appUser.getUserName()))
        {
            appUser.setUserName(appUser.getPhonenumber());
        }
        // 设置默认昵称（如果没有提供）
        if (StringUtils.isEmpty(appUser.getNickName()))
        {
            appUser.setNickName(appUser.getUserName());
        }
        // 设置默认状态
        if (StringUtils.isEmpty(appUser.getStatus()))
        {
            appUser.setStatus(UserConstants.NORMAL);
        }
        // 设置默认密码（如果没有提供）
        if (StringUtils.isEmpty(appUser.getPassword()))
        {
            appUser.setPassword("123456");
        }
        appUser.setCreateBy(getUsername());
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        return toAjax(appUserService.insertAppUser(appUser));
    }

    /**
     * 修改APP用户
     */
    @PreAuthorize("@ss.hasPermi('app:user:edit')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AppUser appUser)
    {
        // 禁止修改手机号
        appUser.setPhonenumber(null);
        // 禁止修改用户名
        appUser.setUserName(null);
        // 禁止修改密码（通过单独接口）
        appUser.setPassword(null);
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUser(appUser));
    }

    /**
     * 删除APP用户
     */
    @PreAuthorize("@ss.hasPermi('app:user:remove')")
    @Log(title = "APP用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable Long[] userIds)
    {
        return toAjax(appUserService.deleteAppUserByIds(userIds));
    }

    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('app:user:resetPwd')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody AppUser appUser)
    {
        if (StringUtils.isEmpty(appUser.getPassword()))
        {
            return error("新密码不能为空");
        }
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUser(appUser));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('app:user:edit')")
    @Log(title = "APP用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody AppUser appUser)
    {
        appUser.setUpdateBy(getUsername());
        return toAjax(appUserService.updateAppUser(appUser));
    }

    /**
     * 校验手机号是否唯一
     */
    @PreAuthorize("@ss.hasPermi('app:user:add')")
    @GetMapping("/checkPhoneUnique")
    public AjaxResult checkPhoneUnique(AppUser appUser)
    {
        return AjaxResult.success(appUserService.checkPhoneUnique(appUser));
    }

    /**
     * 校验用户名是否唯一
     */
    @PreAuthorize("@ss.hasPermi('app:user:add')")
    @GetMapping("/checkUserNameUnique")
    public AjaxResult checkUserNameUnique(AppUser appUser)
    {
        return AjaxResult.success(appUserService.checkUserNameUnique(appUser));
    }
}
