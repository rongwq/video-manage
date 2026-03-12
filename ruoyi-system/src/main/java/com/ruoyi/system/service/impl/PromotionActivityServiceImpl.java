package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.PromotionActivityMapper;
import com.ruoyi.system.domain.PromotionActivity;
import com.ruoyi.system.service.IPromotionActivityService;

/**
 * 活动管理Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-13
 */
@Service
public class PromotionActivityServiceImpl implements IPromotionActivityService 
{
    @Autowired
    private PromotionActivityMapper promotionActivityMapper;

    /**
     * 查询活动
     * 
     * @param id 活动主键
     * @return 活动
     */
    @Override
    public PromotionActivity selectPromotionActivityById(Long id)
    {
        return promotionActivityMapper.selectPromotionActivityById(id);
    }

    /**
     * 查询活动列表
     * 
     * @param promotionActivity 活动
     * @return 活动
     */
    @Override
    public List<PromotionActivity> selectPromotionActivityList(PromotionActivity promotionActivity)
    {
        return promotionActivityMapper.selectPromotionActivityList(promotionActivity);
    }

    /**
     * 新增活动
     * 
     * @param promotionActivity 活动
     * @return 结果
     */
    @Override
    public int insertPromotionActivity(PromotionActivity promotionActivity)
    {
        promotionActivity.setCreateTime(DateUtils.getNowDate());
        return promotionActivityMapper.insertPromotionActivity(promotionActivity);
    }

    /**
     * 修改活动
     * 
     * @param promotionActivity 活动
     * @return 结果
     */
    @Override
    public int updatePromotionActivity(PromotionActivity promotionActivity)
    {
        promotionActivity.setUpdateTime(DateUtils.getNowDate());
        return promotionActivityMapper.updatePromotionActivity(promotionActivity);
    }

    /**
     * 批量删除活动
     * 
     * @param ids 需要删除的活动主键
     * @return 结果
     */
    @Override
    public int deletePromotionActivityByIds(Long[] ids)
    {
        return promotionActivityMapper.deletePromotionActivityByIds(ids);
    }

    /**
     * 删除活动信息
     * 
     * @param id 活动主键
     * @return 结果
     */
    @Override
    public int deletePromotionActivityById(Long id)
    {
        return promotionActivityMapper.deletePromotionActivityById(id);
    }
}
