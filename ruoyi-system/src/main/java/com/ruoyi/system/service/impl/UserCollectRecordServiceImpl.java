package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UserCollectRecordMapper;
import com.ruoyi.system.domain.UserCollectRecord;
import com.ruoyi.system.service.IUserCollectRecordService;

/**
 * 用户收藏记录Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class UserCollectRecordServiceImpl implements IUserCollectRecordService 
{
    @Autowired
    private UserCollectRecordMapper userCollectRecordMapper;

    /**
     * 查询用户收藏记录
     * 
     * @param id 用户收藏记录主键
     * @return 用户收藏记录
     */
    @Override
    public UserCollectRecord selectUserCollectRecordById(Long id)
    {
        return userCollectRecordMapper.selectUserCollectRecordById(id);
    }

    /**
     * 查询用户收藏记录列表
     * 
     * @param userCollectRecord 用户收藏记录
     * @return 用户收藏记录
     */
    @Override
    public List<UserCollectRecord> selectUserCollectRecordList(UserCollectRecord userCollectRecord)
    {
        return userCollectRecordMapper.selectUserCollectRecordList(userCollectRecord);
    }

    /**
     * 新增用户收藏记录
     * 
     * @param userCollectRecord 用户收藏记录
     * @return 结果
     */
    @Override
    public int insertUserCollectRecord(UserCollectRecord userCollectRecord)
    {
        userCollectRecord.setCreateTime(DateUtils.getNowDate());
        return userCollectRecordMapper.insertUserCollectRecord(userCollectRecord);
    }

    /**
     * 修改用户收藏记录
     * 
     * @param userCollectRecord 用户收藏记录
     * @return 结果
     */
    @Override
    public int updateUserCollectRecord(UserCollectRecord userCollectRecord)
    {
        userCollectRecord.setUpdateTime(DateUtils.getNowDate());
        return userCollectRecordMapper.updateUserCollectRecord(userCollectRecord);
    }

    /**
     * 批量删除用户收藏记录
     * 
     * @param ids 需要删除的用户收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteUserCollectRecordByIds(Long[] ids)
    {
        return userCollectRecordMapper.deleteUserCollectRecordByIds(ids);
    }

    /**
     * 删除用户收藏记录信息
     * 
     * @param id 用户收藏记录主键
     * @return 结果
     */
    @Override
    public int deleteUserCollectRecordById(Long id)
    {
        return userCollectRecordMapper.deleteUserCollectRecordById(id);
    }
}
