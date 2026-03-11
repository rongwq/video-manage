package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 注册活动配置对象 v_reg_activity_config
 *
 * @author ruoyi
 * @date 2024-09-14
 */
public class RegActivityConfig extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String activityName;

    /** 活动开关 0关闭 1开启 */
    @Excel(name = "活动开关", readConverterExp = "0=关闭,1=开启")
    private String activityStatus;

    /** 赠送积分 */
    @Excel(name = "赠送积分")
    private Long giveIntegral;

    /** 活动开始时间 */
    @Excel(name = "活动开始时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /** 活动结束时间 */
    @Excel(name = "活动结束时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /** 备注 */
    @Excel(name = "备注")
    private String remark;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }

    public void setActivityName(String activityName)
    {
        this.activityName = activityName;
    }

    public String getActivityName()
    {
        return activityName;
    }

    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    public String getActivityStatus()
    {
        return activityStatus;
    }

    public void setGiveIntegral(Long giveIntegral)
    {
        this.giveIntegral = giveIntegral;
    }

    public Long getGiveIntegral()
    {
        return giveIntegral;
    }

    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }

    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("activityName", getActivityName())
                .append("activityStatus", getActivityStatus())
                .append("giveIntegral", getGiveIntegral())
                .append("startTime", getStartTime())
                .append("endTime", getEndTime())
                .append("remark", getRemark())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
