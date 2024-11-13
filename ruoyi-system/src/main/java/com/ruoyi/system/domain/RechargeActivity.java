package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 充值活动对象 v_recharge_activity
 * 
 * @author rongwq
 * @date 2024-11-13
 */
public class RechargeActivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 标题 */
    @Excel(name = "标题")
    private String title;

    /** 充值金额 */
    @Excel(name = "充值金额")
    private Long money;

    /** 赠送金额 */
    @Excel(name = "赠送金额")
    private Long moneyGive;

    /** 购买所需金额 */
    @Excel(name = "购买所需金额")
    private Long moneyBuy;

    /** 顺序 */
    @Excel(name = "顺序")
    private Long orderNum;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setMoney(Long money) 
    {
        this.money = money;
    }

    public Long getMoney() 
    {
        return money;
    }
    public void setMoneyGive(Long moneyGive) 
    {
        this.moneyGive = moneyGive;
    }

    public Long getMoneyGive() 
    {
        return moneyGive;
    }
    public void setMoneyBuy(Long moneyBuy) 
    {
        this.moneyBuy = moneyBuy;
    }

    public Long getMoneyBuy() 
    {
        return moneyBuy;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("money", getMoney())
            .append("moneyGive", getMoneyGive())
            .append("moneyBuy", getMoneyBuy())
            .append("orderNum", getOrderNum())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
