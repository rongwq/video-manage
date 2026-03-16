package com.ruoyi.app.user.domain;

import lombok.Data;
import javax.validation.constraints.NotBlank;

/**
 * APP用户登录请求体
 */
@Data
public class AppLoginBody {
    
    @NotBlank(message = "用户账号不能为空")
    private String userName;

    @NotBlank(message = "用户密码不能为空")
    private String password;

    private String deviceId;
}
