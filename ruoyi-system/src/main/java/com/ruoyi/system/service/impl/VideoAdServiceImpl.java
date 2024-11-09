package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.VideoAdMapper;
import com.ruoyi.common.core.domain.entity.VideoAd;
import com.ruoyi.system.service.IVideoAdService;

/**
 * 视频广告关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
@Service
public class VideoAdServiceImpl implements IVideoAdService 
{
    @Autowired
    private VideoAdMapper videoAdMapper;

    /**
     * 查询视频广告关联
     * 
     * @param id 视频广告关联主键
     * @return 视频广告关联
     */
    @Override
    public VideoAd selectVideoAdById(Long id)
    {
        return videoAdMapper.selectVideoAdById(id);
    }

    /**
     * 查询视频广告关联列表
     * 
     * @param videoAd 视频广告关联
     * @return 视频广告关联
     */
    @Override
    public List<VideoAd> selectVideoAdList(VideoAd videoAd)
    {
        return videoAdMapper.selectVideoAdList(videoAd);
    }

    /**
     * 新增视频广告关联
     * 
     * @param videoAd 视频广告关联
     * @return 结果
     */
    @Override
    public int insertVideoAd(VideoAd videoAd)
    {
        videoAd.setCreateTime(DateUtils.getNowDate());
        return videoAdMapper.insertVideoAd(videoAd);
    }

    /**
     * 修改视频广告关联
     * 
     * @param videoAd 视频广告关联
     * @return 结果
     */
    @Override
    public int updateVideoAd(VideoAd videoAd)
    {
        videoAd.setUpdateTime(DateUtils.getNowDate());
        return videoAdMapper.updateVideoAd(videoAd);
    }

    /**
     * 批量删除视频广告关联
     * 
     * @param ids 需要删除的视频广告关联主键
     * @return 结果
     */
    @Override
    public int deleteVideoAdByIds(Long[] ids)
    {
        return videoAdMapper.deleteVideoAdByIds(ids);
    }

    /**
     * 删除视频广告关联信息
     * 
     * @param id 视频广告关联主键
     * @return 结果
     */
    @Override
    public int deleteVideoAdById(Long id)
    {
        return videoAdMapper.deleteVideoAdById(id);
    }
}
