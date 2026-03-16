package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * APP用户登录控制器
 *
 * @author ruoyi
 */
@Api("APP用户登录接口")
@RestController
@RequestMapping("/app/api/user")
public class LoginUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    @Autowired
    private TokenService tokenService;

    /**
     * APP用户登录
     */
    @ApiOperation("APP用户登录")
    @PostMapping("/login")
    @Anonymous
    public R login(@RequestBody LoginBody loginBody)
    {
        String username = loginBody.getUsername();
        String password = loginBody.getPassword();

        if (StringUtils.isEmpty(username))
        {
            return R.fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(password))
        {
            return R.fail("密码不能为空");
        }

        // 查询用户
        AppUser appUser = appUserService.selectAppUserByUserName(username);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }

        // 校验密码
        if (!SecurityUtils.matchesPassword(password, appUser.getPassword()))
        {
            return R.fail("密码错误");
        }

        // 校验用户状态
        if ("1".equals(appUser.getStatus()))
        {
            return R.fail("账号已被停用，请联系管理员");
        }

        // 记录登录信息
        appUser.setLoginIp(IpUtils.getIpAddr());
        appUser.setLoginDate(new java.util.Date());
        appUserService.recordLoginInfo(appUser);

        // 生成token
        String token = tokenService.createToken(createLoginUser(appUser));

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.TOKEN, token);
        map.put("userId", appUser.getUserId());
        map.put("userName", appUser.getUserName());
        map.put("nickName", appUser.getNickName());
        map.put("avatar", appUser.getAvatar());

        return R.ok(map);
    }

    /**
     * 创建登录用户对象
     */
    private com.ruoyi.common.core.domain.model.LoginUser createLoginUser(AppUser appUser)
    {
        // 创建SysUser对象来适配LoginUser
        com.ruoyi.common.core.domain.entity.SysUser sysUser = new com.ruoyi.common.core.domain.entity.SysUser();
        sysUser.setUserId(appUser.getUserId());
        sysUser.setUserName(appUser.getUserName());
        sysUser.setNickName(appUser.getNickName());
        sysUser.setPassword(appUser.getPassword());
        sysUser.setStatus(appUser.getStatus());
        
        com.ruoyi.common.core.domain.model.LoginUser loginUser = new com.ruoyi.common.core.domain.model.LoginUser();
        loginUser.setUserId(appUser.getUserId());
        loginUser.setUser(sysUser);
        return loginUser;
    }
}
