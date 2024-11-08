package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 充值卡对象 v_recharge_key
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public class RechargeKey extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 充值卡ID */
    private Long id;

    /** 充值卡编码 */
    @Excel(name = "充值卡编码")
    private String code;

    /** 充值卡额度 */
    @Excel(name = "充值卡额度")
    private Long money;

    /** 绑定用户 */
    @Excel(name = "绑定用户")
    private Long userId;

    /** 状态（0未激活 1激活） */
    @Excel(name = "状态", readConverterExp = "0=未激活,1=激活")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setMoney(Long money) 
    {
        this.money = money;
    }

    public Long getMoney() 
    {
        return money;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("money", getMoney())
            .append("userId", getUserId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
