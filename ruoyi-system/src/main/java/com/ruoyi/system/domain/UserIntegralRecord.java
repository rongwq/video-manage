package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 积分记录对象 user_integral_record
 * 
 * @author ruoyi
 * @date 2024-09-14
 */
@Data
public class UserIntegralRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    private String userName;

    @Excel(name = "充值卡ID,购买记录ID")
    private Long recordId;

    /** 充值卡号 */
    private String rechargeCode;

    /** 积分 */
    @Excel(name = "积分")
    private BigDecimal integral;

    /** 变动前积分 */
    @Excel(name = "变动前积分")
    private BigDecimal integralBefore;

    /** 变动后积分 */
    @Excel(name = "变动后积分")
    private BigDecimal integralAfter;

    /** 积分类型 1阅读文章 2提现 */
    @Excel(name = "积分类型 1阅读文章 2提现")
    private String type;
}
