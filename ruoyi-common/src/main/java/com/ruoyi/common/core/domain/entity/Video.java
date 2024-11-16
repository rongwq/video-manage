package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * 视频对象 v_video
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
@Data
public class Video extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 视频ID */
    private Long id;

    @Excel(name = "视频图片url")
    private String img;

    /** 视频url */
    @Excel(name = "视频url")
    private String url;

    /** 视频标题 */
    @Excel(name = "视频标题")
    private String title;

    /** 视频分类 */
    @Excel(name = "视频分类")
    private String category;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 视频类型1免费无广告 2收费 3免费有广告 */
    @Excel(name = "视频类型1免费无广告 2收费 3免费有广告")
    private String type;

    /** 广告列表 */
    private List<VideoAd> adList;

    /** 点赞量 */
    private Integer likeNum;

    /** 播放量 */
    private Integer playNum;

    /** 是否点赞 */
    private Boolean like;

    /** 是否收藏 */
    private Boolean collect;



}
