package com.ruoyi.system.domain;

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

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }
    public void setRealName(String realName) 
    {
        this.realName = realName;
    }

    public String getRealName() 
    {
        return realName;
    }
    public void setBankCard(String bankCard) 
    {
        this.bankCard = bankCard;
    }

    public String getBankCard() 
    {
        return bankCard;
    }
    public void setQrCode(String qrCode) 
    {
        this.qrCode = qrCode;
    }

    public String getQrCode() 
    {
        return qrCode;
    }
    public void setIntegral(Long integral) 
    {
        this.integral = integral;
    }

    public Long getIntegral() 
    {
        return integral;
    }
    public void setPuserId(Long puserId) 
    {
        this.puserId = puserId;
    }

    public Long getPuserId() 
    {
        return puserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("userId", getUserId())
            .append("idCard", getIdCard())
            .append("realName", getRealName())
            .append("bankCard", getBankCard())
            .append("qrCode", getQrCode())
            .append("integral", getIntegral())
            .append("puserId", getPuserId())
            .toString();
    }
}
