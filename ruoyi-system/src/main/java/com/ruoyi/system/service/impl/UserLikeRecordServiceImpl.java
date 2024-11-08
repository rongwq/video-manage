package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UserLikeRecordMapper;
import com.ruoyi.system.domain.UserLikeRecord;
import com.ruoyi.system.service.IUserLikeRecordService;

/**
 * 用户点赞记录Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class UserLikeRecordServiceImpl implements IUserLikeRecordService 
{
    @Autowired
    private UserLikeRecordMapper userLikeRecordMapper;

    /**
     * 查询用户点赞记录
     * 
     * @param id 用户点赞记录主键
     * @return 用户点赞记录
     */
    @Override
    public UserLikeRecord selectUserLikeRecordById(Long id)
    {
        return userLikeRecordMapper.selectUserLikeRecordById(id);
    }

    /**
     * 查询用户点赞记录列表
     * 
     * @param userLikeRecord 用户点赞记录
     * @return 用户点赞记录
     */
    @Override
    public List<UserLikeRecord> selectUserLikeRecordList(UserLikeRecord userLikeRecord)
    {
        return userLikeRecordMapper.selectUserLikeRecordList(userLikeRecord);
    }

    /**
     * 新增用户点赞记录
     * 
     * @param userLikeRecord 用户点赞记录
     * @return 结果
     */
    @Override
    public int insertUserLikeRecord(UserLikeRecord userLikeRecord)
    {
        userLikeRecord.setCreateTime(DateUtils.getNowDate());
        return userLikeRecordMapper.insertUserLikeRecord(userLikeRecord);
    }

    /**
     * 修改用户点赞记录
     * 
     * @param userLikeRecord 用户点赞记录
     * @return 结果
     */
    @Override
    public int updateUserLikeRecord(UserLikeRecord userLikeRecord)
    {
        userLikeRecord.setUpdateTime(DateUtils.getNowDate());
        return userLikeRecordMapper.updateUserLikeRecord(userLikeRecord);
    }

    /**
     * 批量删除用户点赞记录
     * 
     * @param ids 需要删除的用户点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteUserLikeRecordByIds(Long[] ids)
    {
        return userLikeRecordMapper.deleteUserLikeRecordByIds(ids);
    }

    /**
     * 删除用户点赞记录信息
     * 
     * @param id 用户点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteUserLikeRecordById(Long id)
    {
        return userLikeRecordMapper.deleteUserLikeRecordById(id);
    }
}
