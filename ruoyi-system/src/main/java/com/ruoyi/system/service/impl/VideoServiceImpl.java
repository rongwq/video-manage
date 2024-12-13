package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.VideoMapper;
import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.system.service.IVideoService;

/**
 * 视频Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
@Service
public class VideoServiceImpl implements IVideoService
{
    @Autowired
    private VideoMapper VideoMapper;

    /**
     * 查询视频
     * 
     * @param id 视频主键
     * @return 视频
     */
    @Override
    public Video selectVideoById(Long id)
    {
        return VideoMapper.selectVideoById(id);
    }

    /**
     * 查询视频列表
     * 
     * @param video 视频
     * @return 视频
     */
    @Override
    public List<Video> selectVideoList(Video video)
    {
        return VideoMapper.selectVideoList(video);
    }

    @Override
    public List<Video> selectRecommendList(Video video)
    {
        return VideoMapper.selectRecommendList(video);
    }

    @Override
    public List<Video> selectHomeRecommendList()
    {
        return VideoMapper.selectHomeRecommendList();
    }

    @Override
    public List<Video> selectVideoListByCategoryId(Long categoryId){
        return VideoMapper.selectVideoListByCategoryId(categoryId);
    }

    /**
     * 新增视频
     * 
     * @param video 视频
     * @return 结果
     */
    @Override
    public int insertVideo(Video video)
    {
        video.setCreateTime(DateUtils.getNowDate());
        return VideoMapper.insertVideo(video);
    }

    /**
     * 修改视频
     * 
     * @param video 视频
     * @return 结果
     */
    @Override
    public int updateVideo(Video video)
    {
        video.setUpdateTime(DateUtils.getNowDate());
        return VideoMapper.updateVideo(video);
    }

    /**
     * 批量删除视频
     * 
     * @param ids 需要删除的视频主键
     * @return 结果
     */
    @Override
    public int deleteVideoByIds(Long[] ids)
    {
        return VideoMapper.deleteVideoByIds(ids);
    }

    /**
     * 删除视频信息
     * 
     * @param id 视频主键
     * @return 结果
     */
    @Override
    public int deleteVideoById(Long id)
    {
        return VideoMapper.deleteVideoById(id);
    }

    @Override
    public int updateLikeNum(Long id) {
        return VideoMapper.updateLikeNum(id);
    }

    @Override
    public int cancelLikeNum(Long id) {
        return VideoMapper.cancelLikeNum(id);
    }

    @Override
    public int updatePlayNum(Long id) {
        return VideoMapper.updatePlayNum(id);
    }


}
