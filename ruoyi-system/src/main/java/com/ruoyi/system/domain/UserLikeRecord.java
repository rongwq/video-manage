package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 用户点赞记录对象 v_user_like_record
 *
 * @author rongwq
 * @date 2024-11-08
 */
@Data
public class UserLikeRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 视频ID
     */
    @Excel(name = "视频ID")
    private Long videoId;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long userId;

    //非数据库字段
    private String videoTitle;

    private String videoImg;

    private String videoType;
}
