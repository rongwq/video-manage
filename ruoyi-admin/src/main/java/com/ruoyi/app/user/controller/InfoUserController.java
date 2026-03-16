package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * APP用户信息查询控制器
 *
 * @author ruoyi
 */
@Api("APP用户信息接口")
@RestController
@RequestMapping("/app/api/user")
public class InfoUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * 获取当前登录用户信息
     */
    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info")
    public R info()
    {
        String username = SecurityUtils.getUsername();
        if (StringUtils.isEmpty(username))
        {
            return R.fail("获取用户信息失败");
        }

        AppUser appUser = appUserService.selectAppUserByUserName(username);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }

        // 清除敏感信息
        appUser.setPassword(null);

        return R.ok(appUser);
    }

    /**
     * 根据用户ID获取用户信息
     */
    @ApiOperation("根据用户ID获取用户信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    @GetMapping("/{userId}")
    public R getUserById(@PathVariable Long userId)
    {
        AppUser appUser = appUserService.selectAppUserById(userId);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }

        // 清除敏感信息
        appUser.setPassword(null);

        return R.ok(appUser);
    }

    /**
     * 根据用户名获取用户信息
     */
    @ApiOperation("根据用户名获取用户信息")
    @ApiImplicitParam(name = "userName", value = "用户名", required = true, dataType = "String", paramType = "path")
    @GetMapping("/username/{userName}")
    public R getUserByName(@PathVariable String userName)
    {
        AppUser appUser = appUserService.selectAppUserByUserName(userName);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }

        // 清除敏感信息
        appUser.setPassword(null);

        return R.ok(appUser);
    }

    /**
     * 修改用户信息
     */
    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public R update(@RequestBody AppUser appUser)
    {
        String username = SecurityUtils.getUsername();
        if (StringUtils.isEmpty(username))
        {
            return R.fail("获取用户信息失败");
        }

        AppUser currentUser = appUserService.selectAppUserByUserName(username);
        if (currentUser == null)
        {
            return R.fail("用户不存在");
        }

        // 只能修改自己的信息
        if (!currentUser.getUserId().equals(appUser.getUserId()))
        {
            return R.fail("无权修改其他用户信息");
        }

        // 不允许修改用户名
        appUser.setUserName(null);
        // 不允许修改密码（有专门的修改密码接口）
        appUser.setPassword(null);

        appUser.setUserId(currentUser.getUserId());
        int result = appUserService.updateAppUser(appUser);
        if (result > 0)
        {
            return R.ok("修改成功");
        }
        return R.fail("修改失败");
    }

    /**
     * 修改密码
     */
    @ApiOperation("修改密码")
    @PutMapping("/updatePwd")
    public R updatePwd(@RequestParam String oldPassword, @RequestParam String newPassword)
    {
        String username = SecurityUtils.getUsername();
        if (StringUtils.isEmpty(username))
        {
            return R.fail("获取用户信息失败");
        }

        AppUser appUser = appUserService.selectAppUserByUserName(username);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }

        // 校验旧密码
        if (!SecurityUtils.matchesPassword(oldPassword, appUser.getPassword()))
        {
            return R.fail("旧密码错误");
        }

        // 校验新密码长度
        if (newPassword.length() < 6 || newPassword.length() > 20)
        {
            return R.fail("新密码长度必须在6-20个字符之间");
        }

        boolean result = appUserService.resetAppUserPwd(username, newPassword);
        if (result)
        {
            return R.ok("密码修改成功");
        }
        return R.fail("密码修改失败");
    }
}
