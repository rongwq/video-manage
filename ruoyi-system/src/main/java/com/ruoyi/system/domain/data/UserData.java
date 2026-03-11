package com.ruoyi.system.domain.data;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.xss.Xss;
import com.ruoyi.common.core.domain.entity.SysUserExt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @Description: UserData-传输对象
 * @ClassName: UserData
 * @Author: rongwq
 * @Date: 2024/9/13
 * @Version: 1.0
 */
@ApiModel(value = "UserData-传输对象")
public interface UserData {
    @Data
    class RegUserParam {
        @NotBlank(message = "激活码不能为空")
        @ApiModelProperty(value = "激活码")
        private String cdkey;

        @Xss(message = "用户账号不能包含脚本字符")
        @NotBlank(message = "用户账号不能为空")
        @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
        @ApiModelProperty(value = "用户名")
        private String userName;

        @NotBlank(message = "密码不能为空")
        @ApiModelProperty(value = "密码")
        private String password;

        @NotBlank(message = "手机号码不能为空")
        @ApiModelProperty(value = "手机号码")
        private String phonenumber;

        @ApiModelProperty(value = "身份证")
        private String idCard;

        @ApiModelProperty(value = "真实姓名")
        private String realName;

        @ApiModelProperty(value = "代理账号")
        private String puserId;

        @ApiModelProperty(value = "用户邮箱")
        private String email;
    }

    @Data
    class ActivityRegUserParam {
        @Xss(message = "用户账号不能包含脚本字符")
        @NotBlank(message = "用户账号不能为空")
        @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
        @ApiModelProperty(value = "用户名")
        private String userName;

        @NotBlank(message = "密码不能为空")
        @ApiModelProperty(value = "密码")
        private String password;

        @NotBlank(message = "手机号码不能为空")
        @ApiModelProperty(value = "手机号码")
        private String phonenumber;

        @ApiModelProperty(value = "用户邮箱")
        private String email;
    }

    @Data
    class UserQuery {
        @ApiModelProperty(value = "用户")
        private SysUser user;

        @ApiModelProperty(value = "用户扩展信息")
        private SysUserExt userExt;
    }
}
