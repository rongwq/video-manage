package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.PromotionActivity;

/**
 * 活动管理Service接口
 * 
 * @author rongwq
 * @date 2024-11-13
 */
public interface IPromotionActivityService 
{
    /**
     * 查询活动
     * 
     * @param id 活动主键
     * @return 活动
     */
    public PromotionActivity selectPromotionActivityById(Long id);

    /**
     * 查询活动列表
     * 
     * @param promotionActivity 活动
     * @return 活动集合
     */
    public List<PromotionActivity> selectPromotionActivityList(PromotionActivity promotionActivity);

    /**
     * 新增活动
     * 
     * @param promotionActivity 活动
     * @return 结果
     */
    public int insertPromotionActivity(PromotionActivity promotionActivity);

    /**
     * 修改活动
     * 
     * @param promotionActivity 活动
     * @return 结果
     */
    public int updatePromotionActivity(PromotionActivity promotionActivity);

    /**
     * 批量删除活动
     * 
     * @param ids 需要删除的活动主键集合
     * @return 结果
     */
    public int deletePromotionActivityByIds(Long[] ids);

    /**
     * 删除活动信息
     * 
     * @param id 活动主键
     * @return 结果
     */
    public int deletePromotionActivityById(Long id);
}
