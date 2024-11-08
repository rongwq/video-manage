package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.core.domain.entity.VideoCategory;

/**
 * 视频分类Service接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface IVideoCategoryService 
{
    /**
     * 查询视频分类
     * 
     * @param id 视频分类主键
     * @return 视频分类
     */
    public VideoCategory selectVideoCategoryById(Long id);

    /**
     * 查询视频分类列表
     * 
     * @param videoCategory 视频分类
     * @return 视频分类集合
     */
    public List<VideoCategory> selectVideoCategoryList(VideoCategory videoCategory);

    List<TreeSelect> buildTreeSelect(List<VideoCategory> treeList);

    List<VideoCategory> buildTree(List<VideoCategory> list);

    /**
     * 新增视频分类
     * 
     * @param videoCategory 视频分类
     * @return 结果
     */
    public int insertVideoCategory(VideoCategory videoCategory);

    /**
     * 修改视频分类
     * 
     * @param videoCategory 视频分类
     * @return 结果
     */
    public int updateVideoCategory(VideoCategory videoCategory);

    /**
     * 批量删除视频分类
     * 
     * @param ids 需要删除的视频分类主键集合
     * @return 结果
     */
    public int deleteVideoCategoryByIds(Long[] ids);

    /**
     * 删除视频分类信息
     * 
     * @param id 视频分类主键
     * @return 结果
     */
    public int deleteVideoCategoryById(Long id);
}
