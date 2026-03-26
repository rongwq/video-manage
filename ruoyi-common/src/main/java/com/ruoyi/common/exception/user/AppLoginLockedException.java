package com.ruoyi.common.exception.user;

public class AppLoginLockedException extends UserException
{
    private static final long serialVersionUID = 1L;

    public AppLoginLockedException()
    {
        super("app.login.locked", null);
    }
}
