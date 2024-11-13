package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.UserIntegralRecord;

import java.util.List;

/**
 * 积分记录Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-14
 */
public interface UserIntegralRecordMapper 
{
    /**
     * 查询积分记录
     * 
     * @param id 积分记录主键
     * @return 积分记录
     */
    public UserIntegralRecord selectUserIntegralRecordById(Long id);

    /**
     * 查询积分记录列表
     * 
     * @param userIntegralRecord 积分记录
     * @return 积分记录集合
     */
    public List<UserIntegralRecord> selectUserIntegralRecordList(UserIntegralRecord userIntegralRecord);

    /**
     * 新增积分记录
     * 
     * @param userIntegralRecord 积分记录
     * @return 结果
     */
    public int insertUserIntegralRecord(UserIntegralRecord userIntegralRecord);

    /**
     * 修改积分记录
     * 
     * @param userIntegralRecord 积分记录
     * @return 结果
     */
    public int updateUserIntegralRecord(UserIntegralRecord userIntegralRecord);

    /**
     * 删除积分记录
     * 
     * @param id 积分记录主键
     * @return 结果
     */
    public int deleteUserIntegralRecordById(Long id);

    /**
     * 批量删除积分记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserIntegralRecordByIds(Long[] ids);
}
