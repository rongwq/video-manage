package com.ruoyi.system.domain;

import lombok.Data;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;

/**
 * 注册送积分活动配置对象 register_activity
 * 
 * @author ruoyi
 * @date 2026-03-11
 */
@Data
public class RegisterActivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    private Long id;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String activityName;

    /** 活动开关 */
    @Excel(name = "活动开关", readConverterExp = "0=关闭,1=开启")
    private Integer status;

    /** 过期时间 */
    @Excel(name = "过期时间")
    private Date expireTime;

    /** 赠送积分 */
    @Excel(name = "赠送积分")
    private Integer integral;

    /** 活动描述 */
    @Excel(name = "活动描述")
    private String description;
}
