package com.ruoyi.common.enums;

/**
 * 激活码状态
 * 
 * @author ruoyi
 */
public enum CdkeyStatus
{
    ENABLED("1", "激活"), UN_ENABLED("0", "未激活");

    private final String code;
    private final String info;

    CdkeyStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
