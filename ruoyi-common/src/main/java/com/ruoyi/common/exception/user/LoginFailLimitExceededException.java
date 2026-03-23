package com.ruoyi.common.exception.user;

/**
 * 登录失败次数超限异常类
 *
 * @author ruoyi
 */
public class LoginFailLimitExceededException extends UserException
{
    private static final long serialVersionUID = 1L;

    public LoginFailLimitExceededException()
    {
        super("user.login.fail.limit.exceeded", null);
    }
}
