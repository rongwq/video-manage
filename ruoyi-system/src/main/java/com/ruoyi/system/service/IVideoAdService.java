package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.common.core.domain.entity.VideoAd;

/**
 * 视频广告关联Service接口
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
public interface IVideoAdService 
{
    /**
     * 查询视频广告关联
     * 
     * @param id 视频广告关联主键
     * @return 视频广告关联
     */
    public VideoAd selectVideoAdById(Long id);

    /**
     * 查询视频广告关联列表
     * 
     * @param videoAd 视频广告关联
     * @return 视频广告关联集合
     */
    public List<VideoAd> selectVideoAdList(VideoAd videoAd);

    /**
     * 新增视频广告关联
     * 
     * @param videoAd 视频广告关联
     * @return 结果
     */
    public int insertVideoAd(VideoAd videoAd);

    /**
     * 修改视频广告关联
     * 
     * @param videoAd 视频广告关联
     * @return 结果
     */
    public int updateVideoAd(VideoAd videoAd);

    /**
     * 批量删除视频广告关联
     * 
     * @param ids 需要删除的视频广告关联主键集合
     * @return 结果
     */
    public int deleteVideoAdByIds(Long[] ids);

    /**
     * 删除视频广告关联信息
     * 
     * @param id 视频广告关联主键
     * @return 结果
     */
    public int deleteVideoAdById(Long id);
}
