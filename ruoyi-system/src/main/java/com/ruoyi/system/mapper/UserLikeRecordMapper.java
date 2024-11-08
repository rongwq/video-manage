package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserLikeRecord;

/**
 * 用户点赞记录Mapper接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface UserLikeRecordMapper 
{
    /**
     * 查询用户点赞记录
     * 
     * @param id 用户点赞记录主键
     * @return 用户点赞记录
     */
    public UserLikeRecord selectUserLikeRecordById(Long id);

    /**
     * 查询用户点赞记录列表
     * 
     * @param userLikeRecord 用户点赞记录
     * @return 用户点赞记录集合
     */
    public List<UserLikeRecord> selectUserLikeRecordList(UserLikeRecord userLikeRecord);

    /**
     * 新增用户点赞记录
     * 
     * @param userLikeRecord 用户点赞记录
     * @return 结果
     */
    public int insertUserLikeRecord(UserLikeRecord userLikeRecord);

    /**
     * 修改用户点赞记录
     * 
     * @param userLikeRecord 用户点赞记录
     * @return 结果
     */
    public int updateUserLikeRecord(UserLikeRecord userLikeRecord);

    /**
     * 删除用户点赞记录
     * 
     * @param id 用户点赞记录主键
     * @return 结果
     */
    public int deleteUserLikeRecordById(Long id);

    /**
     * 批量删除用户点赞记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserLikeRecordByIds(Long[] ids);
}
