package com.ruoyi.app.user.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.util.Date;

/**
 * APP用户对象 app_user
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AppUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long userId;

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 50, message = "用户账号长度不能超过50个字符")
    private String userName;

    @Size(min = 0, max = 50, message = "用户昵称长度不能超过50个字符")
    private String nickName;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 100, message = "邮箱长度不能超过100个字符")
    private String email;

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    private String phonenumber;

    private String sex;

    private String avatar;

    private String password;

    private String status;

    private String delFlag;

    private String loginIp;

    private Date loginDate;

    private String deviceId;

    private Integer vipLevel;

    private Date vipExpireTime;

    private Long integral;
}
