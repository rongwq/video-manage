package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.core.domain.TreeSelect;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.VideoCategoryMapper;
import com.ruoyi.common.core.domain.entity.VideoCategory;
import com.ruoyi.system.service.IVideoCategoryService;

/**
 * 视频分类Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class VideoCategoryServiceImpl implements IVideoCategoryService 
{
    @Autowired
    private VideoCategoryMapper videoCategoryMapper;

    /**
     * 查询视频分类
     * 
     * @param id 视频分类主键
     * @return 视频分类
     */
    @Override
    public VideoCategory selectVideoCategoryById(Long id)
    {
        return videoCategoryMapper.selectVideoCategoryById(id);
    }

    /**
     * 查询视频分类列表
     * 
     * @param videoCategory 视频分类
     * @return 视频分类
     */
    @Override
    public List<VideoCategory> selectVideoCategoryList(VideoCategory videoCategory)
    {
        return videoCategoryMapper.selectVideoCategoryList(videoCategory);
    }

    @Override
    public List<TreeSelect> buildTreeSelect(List<VideoCategory> videoCategoryList) {
        List<VideoCategory> trees = buildTree(videoCategoryList);
        return trees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param list 列表
     * @return 树结构列表
     */
    @Override
    public List<VideoCategory> buildTree(List<VideoCategory> list)
    {
        List<VideoCategory> returnList = new ArrayList<VideoCategory>();
        List<Long> tempList = list.stream().map(VideoCategory::getId).collect(Collectors.toList());
        for (Iterator<VideoCategory> iterator = list.iterator(); iterator.hasNext();)
        {
            VideoCategory menu = (VideoCategory) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(menu.getParentId()))
            {
                recursionFn(list, menu);
                returnList.add(menu);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = list;
        }
        return returnList;
    }

    /**
     * 新增视频分类
     * 
     * @param videoCategory 视频分类
     * @return 结果
     */
    @Override
    public int insertVideoCategory(VideoCategory videoCategory)
    {
        videoCategory.setCreateTime(DateUtils.getNowDate());
        return videoCategoryMapper.insertVideoCategory(videoCategory);
    }

    /**
     * 修改视频分类
     * 
     * @param videoCategory 视频分类
     * @return 结果
     */
    @Override
    public int updateVideoCategory(VideoCategory videoCategory)
    {
        videoCategory.setUpdateTime(DateUtils.getNowDate());
        return videoCategoryMapper.updateVideoCategory(videoCategory);
    }

    /**
     * 批量删除视频分类
     * 
     * @param ids 需要删除的视频分类主键
     * @return 结果
     */
    @Override
    public int deleteVideoCategoryByIds(Long[] ids)
    {
        return videoCategoryMapper.deleteVideoCategoryByIds(ids);
    }

    /**
     * 删除视频分类信息
     * 
     * @param id 视频分类主键
     * @return 结果
     */
    @Override
    public int deleteVideoCategoryById(Long id)
    {
        return videoCategoryMapper.deleteVideoCategoryById(id);
    }

    /**
     * 递归列表
     *
     * @param list 分类表
     * @param t 子节点
     */
    private void recursionFn(List<VideoCategory> list, VideoCategory t)
    {
        // 得到子节点列表
        List<VideoCategory> childList = getChildList(list, t);
        t.setChildren(childList);
        for (VideoCategory tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<VideoCategory> getChildList(List<VideoCategory> list, VideoCategory t)
    {
        List<VideoCategory> tlist = new ArrayList<VideoCategory>();
        Iterator<VideoCategory> it = list.iterator();
        while (it.hasNext())
        {
            VideoCategory n = (VideoCategory) it.next();
            if (n.getParentId().longValue() == t.getId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<VideoCategory> list, VideoCategory t)
    {
        return getChildList(list, t).size() > 0;
    }
}
