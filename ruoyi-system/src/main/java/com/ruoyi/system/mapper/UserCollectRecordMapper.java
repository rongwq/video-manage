package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.UserCollectRecord;

/**
 * 用户收藏记录Mapper接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface UserCollectRecordMapper 
{
    /**
     * 查询用户收藏记录
     * 
     * @param id 用户收藏记录主键
     * @return 用户收藏记录
     */
    public UserCollectRecord selectUserCollectRecordById(Long id);

    /**
     * 查询用户收藏记录列表
     * 
     * @param userCollectRecord 用户收藏记录
     * @return 用户收藏记录集合
     */
    public List<UserCollectRecord> selectUserCollectRecordList(UserCollectRecord userCollectRecord);

    /**
     * 新增用户收藏记录
     * 
     * @param userCollectRecord 用户收藏记录
     * @return 结果
     */
    public int insertUserCollectRecord(UserCollectRecord userCollectRecord);

    /**
     * 修改用户收藏记录
     * 
     * @param userCollectRecord 用户收藏记录
     * @return 结果
     */
    public int updateUserCollectRecord(UserCollectRecord userCollectRecord);

    /**
     * 删除用户收藏记录
     * 
     * @param id 用户收藏记录主键
     * @return 结果
     */
    public int deleteUserCollectRecordById(Long id);

    /**
     * 批量删除用户收藏记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserCollectRecordByIds(Long[] ids);
}
