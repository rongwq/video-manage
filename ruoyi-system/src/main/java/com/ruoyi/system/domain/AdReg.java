package com.ruoyi.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 注册广告对象 v_ad_reg
 * 
 * @author rongwq
 * @date 2024-12-13
 */
@Data
public class AdReg extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 广告ID */
    private Long id;

    /** 广告url */
    @Excel(name = "广告url")
    private String url;

    /** 广告标题 */
    @Excel(name = "广告标题")
    private String title;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    //昨天统计
    private Long yesterdayNum;

    //今天统计
    private Long todayNum;

    //所有
    private Long allNum;

    /** 广告类型1斗音 2快手 3拼多多 */
    @Excel(name = "广告类型1斗音 2快手 3拼多多")
    private String type;
}
