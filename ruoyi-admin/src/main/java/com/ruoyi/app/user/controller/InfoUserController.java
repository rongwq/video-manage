package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppLoginUser;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.AppTokenService;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * APP用户信息控制器
 */
@Api("APP用户信息接口")
@RestController
@RequestMapping("/app/api/user")
public class InfoUserController {

    @Autowired
    private AppTokenService appTokenService;

    @Autowired
    private IAppUserService appUserService;

    @ApiOperation("获取APP用户信息")
    @GetMapping("/info")
    public R<Map<String, Object>> getUserInfo(HttpServletRequest request) {
        AppLoginUser loginUser = appTokenService.getLoginUser(request);
        if (loginUser == null) {
            return R.fail("用户未登录");
        }
        AppUser user = appUserService.selectAppUserById(loginUser.getUserId());
        user.setPassword(null);
        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        return R.ok(result);
    }

    @ApiOperation("更新APP用户信息")
    @PutMapping("/info")
    public R<String> updateUserInfo(@RequestBody AppUser appUser, HttpServletRequest request) {
        AppLoginUser loginUser = appTokenService.getLoginUser(request);
        if (loginUser == null) {
            return R.fail("用户未登录");
        }
        appUser.setUserId(loginUser.getUserId());
        appUser.setPassword(null);
        appUser.setUserName(null);
        appUser.setStatus(null);
        appUser.setDelFlag(null);
        return R.ok(appUserService.updateAppUser(appUser) > 0 ? "修改成功" : "修改失败");
    }

    @ApiOperation("APP用户退出登录")
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        AppLoginUser loginUser = appTokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            appTokenService.delLoginUser(loginUser.getToken());
        }
        return R.ok("退出成功");
    }
}
