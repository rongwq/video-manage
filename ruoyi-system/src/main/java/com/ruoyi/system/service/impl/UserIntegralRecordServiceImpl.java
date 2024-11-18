package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.SysUserExt;
import com.ruoyi.common.enums.IntegralType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.UserIntegralRecord;
import com.ruoyi.system.mapper.SysUserExtMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.UserIntegralRecordMapper;
import com.ruoyi.system.service.IUserIntegralRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 积分记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-14
 */
@Service
public class UserIntegralRecordServiceImpl implements IUserIntegralRecordService 
{
    @Autowired
    private UserIntegralRecordMapper userIntegralRecordMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysUserExtMapper sysUserExtMapper;

    /**
     * 查询积分记录
     * 
     * @param id 积分记录主键
     * @return 积分记录
     */
    @Override
    public UserIntegralRecord selectUserIntegralRecordById(Long id)
    {
        return userIntegralRecordMapper.selectUserIntegralRecordById(id);
    }

    /**
     * 查询积分记录列表
     * 
     * @param userIntegralRecord 积分记录
     * @return 积分记录
     */
    @Override
    public List<UserIntegralRecord> selectUserIntegralRecordList(UserIntegralRecord userIntegralRecord)
    {
        List<UserIntegralRecord> list = userIntegralRecordMapper.selectUserIntegralRecordList(userIntegralRecord);
        list.forEach(i->{
            SysUser user = sysUserMapper.selectUserById(i.getUserId());
            if(user!=null){
                i.setUserName(user.getUserName());
            }
        });
        return list;
    }

    /**
     * 新增积分记录
     * 
     * @param userIntegralRecord 积分记录
     * @return 结果
     */
    @Override
    public int insertUserIntegralRecord(UserIntegralRecord userIntegralRecord)
    {
        userIntegralRecord.setCreateTime(DateUtils.getNowDate());
        return userIntegralRecordMapper.insertUserIntegralRecord(userIntegralRecord);
    }

    /**
     * 修改积分记录
     * 
     * @param userIntegralRecord 积分记录
     * @return 结果
     */
    @Override
    public int updateUserIntegralRecord(UserIntegralRecord userIntegralRecord)
    {
        userIntegralRecord.setUpdateTime(DateUtils.getNowDate());
        return userIntegralRecordMapper.updateUserIntegralRecord(userIntegralRecord);
    }

    /**
     * 批量删除积分记录
     * 
     * @param ids 需要删除的积分记录主键
     * @return 结果
     */
    @Override
    public int deleteUserIntegralRecordByIds(Long[] ids)
    {
        return userIntegralRecordMapper.deleteUserIntegralRecordByIds(ids);
    }

    /**
     * 删除积分记录信息
     * 
     * @param id 积分记录主键
     * @return 结果
     */
    @Override
    public int deleteUserIntegralRecordById(Long id)
    {
        return userIntegralRecordMapper.deleteUserIntegralRecordById(id);
    }

    /**
     * 保存流水
     * @param userId
     * @param recordId
     * @param integral
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUserIntegralRecord(Long userId, Long recordId, Integer integral, IntegralType type, String remark) {
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
