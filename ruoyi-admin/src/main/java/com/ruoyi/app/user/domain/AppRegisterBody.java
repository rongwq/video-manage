package com.ruoyi.app.user.domain;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * APP用户注册请求体
 */
@Data
public class AppRegisterBody {
    
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 2, max = 20, message = "用户账号长度必须介于2和20之间")
    private String userName;

    @NotBlank(message = "用户密码不能为空")
    @Size(min = 5, max = 20, message = "用户密码长度必须介于5和20之间")
    private String password;

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    @Size(min = 0, max = 50, message = "用户昵称长度不能超过50个字符")
    private String nickName;

    private String deviceId;

    private String inviteCode;
}
