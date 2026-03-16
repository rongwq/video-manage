package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * APP用户注册控制器
 *
 * @author ruoyi
 */
@Api("APP用户注册接口")
@RestController
@RequestMapping("/app/api/user")
public class RegisterUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * APP用户注册
     */
    @ApiOperation("APP用户注册")
    @PostMapping("/register")
    @Anonymous
    public R register(@RequestBody AppUser appUser)
    {
        if (StringUtils.isEmpty(appUser.getUserName()))
        {
            return R.fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(appUser.getPassword()))
        {
            return R.fail("密码不能为空");
        }
        if (appUser.getUserName().length() < 2 || appUser.getUserName().length() > 30)
        {
            return R.fail("用户名长度必须在2-30个字符之间");
        }
        if (appUser.getPassword().length() < 6 || appUser.getPassword().length() > 20)
        {
            return R.fail("密码长度必须在6-20个字符之间");
        }

        // 校验用户名是否唯一
        if (!appUserService.checkUserNameUnique(appUser))
        {
            return R.fail("注册用户'" + appUser.getUserName() + "'失败，登录账号已存在");
        }

        // 校验手机号是否唯一
        if (StringUtils.isNotEmpty(appUser.getPhonenumber()) && !appUserService.checkPhoneUnique(appUser))
        {
            return R.fail("注册用户'" + appUser.getUserName() + "'失败，手机号码已存在");
        }

        // 校验邮箱是否唯一
        if (StringUtils.isNotEmpty(appUser.getEmail()) && !appUserService.checkEmailUnique(appUser))
        {
            return R.fail("注册用户'" + appUser.getUserName() + "'失败，邮箱账号已存在");
        }

        // 设置默认昵称
        if (StringUtils.isEmpty(appUser.getNickName()))
        {
            appUser.setNickName(appUser.getUserName());
        }

        // 设置默认状态
        appUser.setStatus(UserConstants.NORMAL);

        boolean result = appUserService.registerAppUser(appUser);
        if (result)
        {
            return R.ok("注册成功");
        }
        return R.fail("注册失败，请联系管理员");
    }
}
