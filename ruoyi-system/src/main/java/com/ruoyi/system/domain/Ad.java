package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 广告对象 v_ad
 *
 * @author ruoyi
 * @date 2024-10-13
 */
@Data
public class Ad extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 广告ID
     */
    private Long id;

    /**
     * 广告url
     */
    @Excel(name = "广告url")
    private String url;

    /**
     * 广告标题
     */
    @Excel(name = "广告标题")
    private String title;

    /**
     * 状态（0正常 1停用）
     */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /**
     * 广告类型1图片 2视频 3文章
     */
    @Excel(name = "广告类型1图片 2视频 3文章")
    private String type;

    /**
     * 使用次数
     */
    private Integer useNum;
}
