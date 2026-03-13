package com.ruoyi.common.core.domain.entity;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import javax.validation.constraints.*;
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
    @Excel(name = "视频ID")
    private Long id;

    @Excel(name = "视频图片url")
    private String img;

    /** 视频url */
    @Excel(name = "视频url")
    @NotBlank(message = "视频URL不能为空")
    @Size(max = 500, message = "视频URL长度不能超过500字符")
    @Pattern(regexp = "^(http|https)://.*\\.(mp4|avi|mov|rmvb|flv|wmv)$", message = "视频URL格式错误，仅支持mp4/avi/mov等格式")
    private String url;

    /** 视频标题 */
    @Excel(name = "视频标题")
    @NotBlank(message = "视频标题不能为空")
    @Size(min = 1, max = 100, message = "标题长度需在1-100字符之间")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9_-]+$", message = "标题包含非法特殊字符，仅允许中文、英文、数字、-_")
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
    @Excel(name = "点赞量")
    private Integer likeNum;

    /** 播放量 */
    @Excel(name = "播放量")
    private Integer playNum;

    /** 是否点赞 */
    private Boolean like;

    /** 是否收藏 */
    private Boolean collect;

    /** 收费视频金额 */
    @Excel(name = "收费视频金额")
    @Max(value = 9999, message = "金额不能超过9999")
    private Integer money;

    /** 是否购买 */
    private Boolean buy;

}
