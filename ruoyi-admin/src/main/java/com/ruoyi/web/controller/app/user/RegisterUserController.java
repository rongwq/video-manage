package com.ruoyi.web.controller.app.user;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("APP用户注册接口")
@RestController
@RequestMapping("/app/api/user")
public class RegisterUserController
{
    @Autowired
    private IAppUserService appUserService;

    @Anonymous
    @PostMapping("/register")
    @ApiOperation("APP用户注册")
    public R register(@Valid @RequestBody AppUser appUser)
    {
        if (!appUserService.checkUserNameUnique(appUser))
        {
            return R.fail("注册失败，用户账号已存在");
        }
        if (StringUtils.isNotEmpty(appUser.getPhonenumber()) && !appUserService.checkPhoneUnique(appUser))
        {
            return R.fail("注册失败，手机号码已存在");
        }
        if (StringUtils.isNotEmpty(appUser.getEmail()) && !appUserService.checkEmailUnique(appUser))
        {
            return R.fail("注册失败，邮箱已存在");
        }
        appUser.setPassword(SecurityUtils.encryptPassword(appUser.getPassword()));
        appUser.setCreateBy(appUser.getUserName());
        return toR(appUserService.insertAppUser(appUser));
    }

    private R toR(int rows)
    {
        return rows > 0 ? R.ok() : R.fail();
    }
}
