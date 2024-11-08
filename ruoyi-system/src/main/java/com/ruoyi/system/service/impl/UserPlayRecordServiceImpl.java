package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.UserPlayRecordMapper;
import com.ruoyi.system.domain.UserPlayRecord;
import com.ruoyi.system.service.IUserPlayRecordService;

/**
 * 用户视频播放记录Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class UserPlayRecordServiceImpl implements IUserPlayRecordService 
{
    @Autowired
    private UserPlayRecordMapper userPlayRecordMapper;

    /**
     * 查询用户视频播放记录
     * 
     * @param id 用户视频播放记录主键
     * @return 用户视频播放记录
     */
    @Override
    public UserPlayRecord selectUserPlayRecordById(Long id)
    {
        return userPlayRecordMapper.selectUserPlayRecordById(id);
    }

    /**
     * 查询用户视频播放记录列表
     * 
     * @param userPlayRecord 用户视频播放记录
     * @return 用户视频播放记录
     */
    @Override
    public List<UserPlayRecord> selectUserPlayRecordList(UserPlayRecord userPlayRecord)
    {
        return userPlayRecordMapper.selectUserPlayRecordList(userPlayRecord);
    }

    /**
     * 新增用户视频播放记录
     * 
     * @param userPlayRecord 用户视频播放记录
     * @return 结果
     */
    @Override
    public int insertUserPlayRecord(UserPlayRecord userPlayRecord)
    {
        userPlayRecord.setCreateTime(DateUtils.getNowDate());
        return userPlayRecordMapper.insertUserPlayRecord(userPlayRecord);
    }

    /**
     * 修改用户视频播放记录
     * 
     * @param userPlayRecord 用户视频播放记录
     * @return 结果
     */
    @Override
    public int updateUserPlayRecord(UserPlayRecord userPlayRecord)
    {
        userPlayRecord.setUpdateTime(DateUtils.getNowDate());
        return userPlayRecordMapper.updateUserPlayRecord(userPlayRecord);
    }

    /**
     * 批量删除用户视频播放记录
     * 
     * @param ids 需要删除的用户视频播放记录主键
     * @return 结果
     */
    @Override
    public int deleteUserPlayRecordByIds(Long[] ids)
    {
        return userPlayRecordMapper.deleteUserPlayRecordByIds(ids);
    }

    /**
     * 删除用户视频播放记录信息
     * 
     * @param id 用户视频播放记录主键
     * @return 结果
     */
    @Override
    public int deleteUserPlayRecordById(Long id)
    {
        return userPlayRecordMapper.deleteUserPlayRecordById(id);
    }
}
