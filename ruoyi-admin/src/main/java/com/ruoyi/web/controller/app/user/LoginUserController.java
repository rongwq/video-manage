package com.ruoyi.web.controller.app.user;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("APP用户登录接口")
@RestController
@RequestMapping("/app/api/user")
public class LoginUserController
{
    @Autowired
    private IAppUserService appUserService;

    @Anonymous
    @PostMapping("/login")
    @ApiOperation("APP用户登录")
    public R login(@RequestBody LoginBody loginBody)
    {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();
        
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            return R.fail("用户名或密码不能为空");
        }
        
        AppUser appUser = appUserService.selectAppUserByUserName(username);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }
        if ("1".equals(appUser.getStatus()))
        {
            return R.fail("用户已被停用");
        }
        if (!SecurityUtils.matchesPassword(password, appUser.getPassword()))
        {
            return R.fail("密码错误");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", appUser.getUserId());
        result.put("userName", appUser.getUserName());
        result.put("nickName", appUser.getNickName());
        result.put("avatar", appUser.getAvatar());
        return R.ok(result);
    }
}
