package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.enums.CdkeyStatus;
import com.ruoyi.common.enums.IntegralType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.RechargeKey;
import com.ruoyi.system.domain.SysUserExt;
import com.ruoyi.system.domain.UserIntegralRecord;
import com.ruoyi.system.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.domain.RechargeActivity;
import com.ruoyi.system.service.IRechargeActivityService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 充值活动Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-13
 */
@Service
public class RechargeActivityServiceImpl implements IRechargeActivityService 
{
    @Autowired
    private RechargeActivityMapper rechargeActivityMapper;
    @Autowired
    private RechargeKeyMapper rechargeKeyMapper;
    @Autowired
    private UserIntegralRecordMapper userIntegralRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserExtMapper sysUserExtMapper;

    /**
     * 查询充值活动
     * 
     * @param id 充值活动主键
     * @return 充值活动
     */
    @Override
    public RechargeActivity selectRechargeActivityById(Long id)
    {
        return rechargeActivityMapper.selectRechargeActivityById(id);
    }

    /**
     * 查询充值活动列表
     * 
     * @param rechargeActivity 充值活动
     * @return 充值活动
     */
    @Override
    public List<RechargeActivity> selectRechargeActivityList(RechargeActivity rechargeActivity)
    {
        return rechargeActivityMapper.selectRechargeActivityList(rechargeActivity);
    }

    /**
     * 新增充值活动
     * 
     * @param rechargeActivity 充值活动
     * @return 结果
     */
    @Override
    public int insertRechargeActivity(RechargeActivity rechargeActivity)
    {
        rechargeActivity.setCreateTime(DateUtils.getNowDate());
        return rechargeActivityMapper.insertRechargeActivity(rechargeActivity);
    }

    /**
     * 修改充值活动
     * 
     * @param rechargeActivity 充值活动
     * @return 结果
     */
    @Override
    public int updateRechargeActivity(RechargeActivity rechargeActivity)
    {
        rechargeActivity.setUpdateTime(DateUtils.getNowDate());
        return rechargeActivityMapper.updateRechargeActivity(rechargeActivity);
    }

    /**
     * 批量删除充值活动
     * 
     * @param ids 需要删除的充值活动主键
     * @return 结果
     */
    @Override
    public int deleteRechargeActivityByIds(Long[] ids)
    {
        return rechargeActivityMapper.deleteRechargeActivityByIds(ids);
    }

    /**
     * 删除充值活动信息
     * 
     * @param id 充值活动主键
     * @return 结果
     */
    @Override
    public int deleteRechargeActivityById(Long id)
    {
        return rechargeActivityMapper.deleteRechargeActivityById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recharge(RechargeKey rechargeKey) {
        //将充值卡片状态改为已激活
        RechargeKey queryResult = rechargeKeyMapper.selectRechargeKeyByCode(rechargeKey.getCode());
        if(queryResult==null){
            throw new ServiceException("充值卡无效");
        }
        if(CdkeyStatus.ENABLED.getCode().equals(queryResult.getStatus())){
            throw new ServiceException("充值卡已激活");
        }
        queryResult.setStatus(CdkeyStatus.ENABLED.getCode());
        queryResult.setUpdateTime(DateUtils.getNowDate());
        queryResult.setUserId(rechargeKey.getUserId());
        //更新充值卡信息
        rechargeKeyMapper.updateRechargeKey(queryResult);
        //保存流水
        saveUserIntegralRecord(rechargeKey.getUserId(),queryResult.getId(),queryResult.getMoney().intValue(),IntegralType.RECHARGE,"充值卡激活");
        return true;
    }

    /**
     * 保存流水
     * @param userId
     * @param recordId
     * @param integral
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserIntegralRecord(Long userId, Long recordId, Integer integral, IntegralType type,String remark) {
        //增加用户积分
        SysUserExt userExt = sysUserExtMapper.selectSysUserExtByUserId(userId);
        if(userExt==null){
            throw new ServiceException("用户信息异常");
        }
        userExt.setIntegral(userExt.getIntegral()+integral);
        sysUserExtMapper.updateSysUserExt(userExt);
        //保存流水
        UserIntegralRecord userIntegralRecord = new UserIntegralRecord();
        userIntegralRecord.setType(type.getCode());
        userIntegralRecord.setIntegral(BigDecimal.valueOf(integral));
        userIntegralRecord.setUserId(userId);
        userIntegralRecord.setRecordId(recordId);
        userIntegralRecord.setIntegralBefore(BigDecimal.valueOf(userExt.getIntegral()-integral));
        userIntegralRecord.setIntegralAfter(BigDecimal.valueOf(userExt.getIntegral()));
        userIntegralRecord.setRemark(remark);
        userIntegralRecord.setCreateTime(new Date());
        userIntegralRecordMapper.insertUserIntegralRecord(userIntegralRecord);
    }
}
