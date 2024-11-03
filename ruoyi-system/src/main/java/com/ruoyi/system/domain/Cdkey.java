package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 岗位信息对象 sys_cdkey
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
@Data
public class Cdkey extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 激活码ID */
    private Long cdkeyId;

    /** 激活码编码 */
    @Excel(name = "激活码编码")
    private String cdkeyCode;

    /** 绑定用户 */
    @Excel(name = "绑定用户")
    private Long userId;

    private String userName;

    /** 状态（0未激活 1激活） */
    @Excel(name = "状态", readConverterExp = "0=未激活,1=激活")
    private String status;

    public void setCdkeyId(Long cdkeyId) 
    {
        this.cdkeyId = cdkeyId;
    }

    public Long getCdkeyId() 
    {
        return cdkeyId;
    }
    public void setCdkeyCode(String cdkeyCode) 
    {
        this.cdkeyCode = cdkeyCode;
    }

    public String getCdkeyCode() 
    {
        return cdkeyCode;
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
            .append("cdkeyId", getCdkeyId())
            .append("cdkeyCode", getCdkeyCode())
            .append("userId", getUserId())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
