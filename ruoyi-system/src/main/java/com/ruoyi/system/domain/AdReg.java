package com.ruoyi.system.domain;

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

    /** 广告类型1斗音 2快手 3拼多多 */
    @Excel(name = "广告类型1斗音 2快手 3拼多多")
    private String type;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("url", getUrl())
            .append("title", getTitle())
            .append("orderNum", getOrderNum())
            .append("remark", getRemark())
            .append("type", getType())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
