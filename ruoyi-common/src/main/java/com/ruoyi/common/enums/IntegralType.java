package com.ruoyi.common.enums;

/**
 * 积分类型
 * 
 * @author rongwq
 */
public enum IntegralType
{
    RECHARGE("1", "积分充值"), BUY_VIDEO("2", "购买视频"), REGISTER("3", "注册赠送");

    private final String code;
    private final String info;

    IntegralType(String code, String info)
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
