package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserPlayRecord;

/**
 * 用户视频播放记录Mapper接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface UserPlayRecordMapper 
{
    /**
     * 查询用户视频播放记录
     * 
     * @param id 用户视频播放记录主键
     * @return 用户视频播放记录
     */
    public UserPlayRecord selectUserPlayRecordById(Long id);

    /**
     * 查询用户视频播放记录列表
     * 
     * @param userPlayRecord 用户视频播放记录
     * @return 用户视频播放记录集合
     */
    public List<UserPlayRecord> selectUserPlayRecordList(UserPlayRecord userPlayRecord);

    /**
     * 新增用户视频播放记录
     * 
     * @param userPlayRecord 用户视频播放记录
     * @return 结果
     */
    public int insertUserPlayRecord(UserPlayRecord userPlayRecord);

    /**
     * 修改用户视频播放记录
     * 
     * @param userPlayRecord 用户视频播放记录
     * @return 结果
     */
    public int updateUserPlayRecord(UserPlayRecord userPlayRecord);

    /**
     * 删除用户视频播放记录
     * 
     * @param id 用户视频播放记录主键
     * @return 结果
     */
    public int deleteUserPlayRecordById(Long id);

    /**
     * 批量删除用户视频播放记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserPlayRecordByIds(Long[] ids);
}
