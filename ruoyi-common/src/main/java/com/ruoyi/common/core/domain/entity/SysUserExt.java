package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 用户扩展对象 sys_user_ext
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
@Data
public class SysUserExt implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 身份证 */
    @Excel(name = "身份证")
    private String idCard;

    /** 真实姓名 */
    @Excel(name = "真实姓名")
    private String realName;

    /** 银行卡 */
    @Excel(name = "银行卡")
    private String bankCard;

    /** 二维码 */
    @Excel(name = "二维码")
    private String qrCode;

    /** 积分 */
    @Excel(name = "积分")
    private Long integral;

    /** 代理id */
    @Excel(name = "代理id")
    private Long puserId;

    /** 获取文章间隔时间 */
    @Excel(name = "获取文章间隔时间")
    private Integer getArticleTime;

    /** 充值码店铺地址 */
    private String shopUrl;

    /** 注册码店铺地址 */
    private String cdKeyShopUrl;

}
