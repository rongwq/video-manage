package com.ruoyi.common.enums;

/**
 * 视频类型
 * 
 * @author ruoyi
 */
public enum VideoType
{
    FREE("1", "免费"),
    FREE_HAVE_AD("3", "免费含广告"),
    VIP("2", "收费"),
    BUY("4", "金币购买"),
    ;

    private final String code;
    private final String info;

    VideoType(String code, String info)
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
