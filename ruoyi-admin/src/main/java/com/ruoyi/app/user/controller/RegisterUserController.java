package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppRegisterBody;
import com.ruoyi.app.user.service.AppLoginService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

/**
 * APP用户注册控制器
 */
@Api("APP用户注册接口")
@RestController
@RequestMapping("/app/api/user")
public class RegisterUserController {

    @Autowired
    private AppLoginService appLoginService;

    @ApiOperation("APP用户注册")
    @PostMapping("/register")
    @Anonymous
    public R<Map<String, Object>> register(@RequestBody AppRegisterBody registerBody) {
        String token = appLoginService.register(registerBody);
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.TOKEN, token);
        return R.ok(result);
    }
}
