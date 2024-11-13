package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.common.enums.IntegralType;
import com.ruoyi.system.domain.RechargeActivity;
import com.ruoyi.system.domain.RechargeKey;

/**
 * 充值活动Service接口
 * 
 * @author rongwq
 * @date 2024-11-13
 */
public interface IRechargeActivityService 
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
     * 批量删除充值活动
     * 
     * @param ids 需要删除的充值活动主键集合
     * @return 结果
     */
    public int deleteRechargeActivityByIds(Long[] ids);

    /**
     * 删除充值活动信息
     * 
     * @param id 充值活动主键
     * @return 结果
     */
    public int deleteRechargeActivityById(Long id);

    /**
     * 充值卡充值
     * @param rechargeKey
     * @return
     */
    boolean recharge(RechargeKey rechargeKey);

    /**
     * 保存流水
     * @param userId
     * @param recordId
     * @param integral
     * @param type
     */
    void saveUserIntegralRecord(Long userId, Long recordId, Integer integral, IntegralType type,String remark);
}
