package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanValidators;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.VideoMapper;
import com.ruoyi.common.core.domain.entity.Video;
import com.ruoyi.system.service.IVideoService;

import javax.validation.Validator;

/**
 * 视频Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-10-13
 */
@Service
@Slf4j
public class VideoServiceImpl implements IVideoService
{
    @Autowired
    private VideoMapper VideoMapper;
    @Autowired
    protected Validator validator;

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
        validateTitleUnique(video.getTitle(), null);
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
        validateTitleUnique(video.getTitle(), video.getId());
        video.setUpdateTime(DateUtils.getNowDate());
        return VideoMapper.updateVideo(video);
    }

    /**
     * 校验标题唯一性
     * 
     * @param title 视频标题
     * @param excludeId 排除的视频ID（修改时排除自身）
     */
    private void validateTitleUnique(String title, Long excludeId)
    {
        if (StringUtils.isNotEmpty(title))
        {
            Video existingVideo = VideoMapper.selectVideoByTitle(title.trim());
            if (existingVideo != null && !existingVideo.getId().equals(excludeId))
            {
                throw new ServiceException("视频标题 '" + title + "' 已存在");
            }
        }
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

    @Override
    public String importData(List<Video> videoList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(videoList) || videoList.size() == 0)
        {
            throw new ServiceException("导入数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (Video video : videoList)
        {
            try
            {
                // 验证是否存在视频
                Video v = VideoMapper.selectVideoById(video.getId());
                if (StringUtils.isNull(v))
                {
                    BeanValidators.validateWithException(validator, video);
                    video.setCreateBy(operName);
                    VideoMapper.insertVideo(video);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、视频 " + video.getTitle() + " 导入成功");
                }
                else if (isUpdateSupport)
                {
                    BeanValidators.validateWithException(validator, video);
                    video.setUpdateBy(operName);
                    VideoMapper.updateVideo(video);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、视频 " + video.getTitle() + " 更新成功");
                }
                else
                {
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、视频 " + video.getTitle() + " 已存在");
                }
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、视频 " +  video.getTitle() + " 导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
        }
        return successMsg.toString();
    }

}
