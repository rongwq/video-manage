package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.RechargeActivity;

/**
 * 充值活动Mapper接口
 * 
 * @author rongwq
 * @date 2024-11-13
 */
public interface RechargeActivityMapper 
{
    /**
     * 查询充值活动
     * 
     * @param id 充值活动主键
     * @return 充值活动
     */
    public RechargeActivity selectRechargeActivityById(Long id);

    /**
     * 查询充值活动列表
     * 
     * @param rechargeActivity 充值活动
     * @return 充值活动集合
     */
    public List<RechargeActivity> selectRechargeActivityList(RechargeActivity rechargeActivity);

    /**
     * 新增充值活动
     * 
     * @param rechargeActivity 充值活动
     * @return 结果
     */
    public int insertRechargeActivity(RechargeActivity rechargeActivity);

    /**
     * 修改充值活动
     * 
     * @param rechargeActivity 充值活动
     * @return 结果
     */
    public int updateRechargeActivity(RechargeActivity rechargeActivity);

    /**
     * 删除充值活动
     * 
     * @param id 充值活动主键
     * @return 结果
     */
    public int deleteRechargeActivityById(Long id);

    /**
     * 批量删除充值活动
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRechargeActivityByIds(Long[] ids);
}
