package com.ruoyi.system.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 用户收藏记录对象 v_user_collect_record
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Data
public class UserCollectRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 视频ID */
    @Excel(name = "视频ID")
    private Long videoId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    //非数据库字段
    private String videoName;

    private String videoImg;
}
