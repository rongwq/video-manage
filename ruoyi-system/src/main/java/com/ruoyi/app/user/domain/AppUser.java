package com.ruoyi.app.user.domain;

import java.util.Date;
import javax.validation.constraints.*;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.annotation.Excel.ColumnType;
import com.ruoyi.common.core.domain.BaseEntity;

@Data
public class AppUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long userId;

    @Excel(name = "用户账号")
    private String userName;

    @Excel(name = "用户昵称")
    private String nickName;

    @Excel(name = "用户邮箱")
    private String email;

    @Excel(name = "手机号码", cellType = ColumnType.TEXT)
    private String phonenumber;

    @Excel(name = "用户性别", readConverterExp = "0=男,1=女,2=未知")
    private String sex;

    private String avatar;

    private String password;

    @Excel(name = "帐号状态", readConverterExp = "0=正常,1=停用")
    private String status;

    private String delFlag;

    @Excel(name = "最后登录IP")
    private String loginIp;

    @Excel(name = "最后登录时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginDate;

    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    public String getUserName()
    {
        return userName;
    }

    @Size(min = 0, max = 30, message = "用户昵称长度不能超过30个字符")
    public String getNickName()
    {
        return nickName;
    }

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    public String getEmail()
    {
        return email;
    }

    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    public String getPhonenumber()
    {
        return phonenumber;
    }
}
