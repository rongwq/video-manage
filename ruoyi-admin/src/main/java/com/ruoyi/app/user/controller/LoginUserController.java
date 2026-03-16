package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppLoginBody;
import com.ruoyi.app.user.service.AppLoginService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * APP用户登录控制器
 */
@Api("APP用户登录接口")
@RestController
@RequestMapping("/app/api/user")
public class LoginUserController {

    @Autowired
    private AppLoginService appLoginService;

    @ApiOperation("APP用户登录")
    @Log(title = "APP用户登录", businessType = BusinessType.INSERT)
    @PostMapping("/login")
    @Anonymous
    public R<Map<String, Object>> login(@RequestBody AppLoginBody loginBody) {
        String token = appLoginService.login(loginBody.getUserName(), loginBody.getPassword());
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.TOKEN, token);
        return R.ok(result);
    }
}
