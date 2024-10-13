package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 视频广告关联对象 v_video_ad
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
public class VideoAd extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 视频id */
    @Excel(name = "视频id")
    private Long videoId;

    /** 广告id */
    @Excel(name = "广告id")
    private Long adId;

    private String adUrl;

    private String adTitle;

    private String adType;

    /** 广告在视频播放多久时间后弹出/s */
    @Excel(name = "广告在视频播放多久时间后弹出/s")
    private Long adShowTime;

    /** 广告持续显示多久时间后可关闭/s */
    @Excel(name = "广告持续显示多久时间后可关闭/s")
    private Long adContinueTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setVideoId(Long videoId) 
    {
        this.videoId = videoId;
    }

    public Long getVideoId() 
    {
        return videoId;
    }
    public void setAdId(Long adId) 
    {
        this.adId = adId;
    }

    public Long getAdId() 
    {
        return adId;
    }
    public void setAdShowTime(Long adShowTime) 
    {
        this.adShowTime = adShowTime;
    }

    public Long getAdShowTime() 
    {
        return adShowTime;
    }
    public void setAdContinueTime(Long adContinueTime) 
    {
        this.adContinueTime = adContinueTime;
    }

    public Long getAdContinueTime() 
    {
        return adContinueTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("videoId", getVideoId())
            .append("adId", getAdId())
            .append("adShowTime", getAdShowTime())
            .append("adContinueTime", getAdContinueTime())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }

    public String getAdUrl() {
        return adUrl;
    }

    public void setAdUrl(String adUrl) {
        this.adUrl = adUrl;
    }

    public String getAdTitle() {
        return adTitle;
    }

    public void setAdTitle(String adTitle) {
        this.adTitle = adTitle;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }
}
