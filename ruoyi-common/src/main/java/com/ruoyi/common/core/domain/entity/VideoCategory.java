package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 视频分类对象 v_video_category
 *
 * @author rongwq
 * @date 2024-11-08
 */
@Data
public class VideoCategory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 视频分类id
     */
    private Long id;

    /**
     * 分类名称
     */
    @Excel(name = "分类名称")
    private String name;

    /**
     * 分类状态（0正常 1停用）
     */
    @Excel(name = "分类状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    /**
     * 父菜单ID
     */
    private Long parentId;

    /**
     * 显示顺序
     */
    private Integer orderNum;

    private List<VideoCategory> children = new ArrayList<VideoCategory>();

    private List<Video> videoList = new ArrayList<Video>();
}
