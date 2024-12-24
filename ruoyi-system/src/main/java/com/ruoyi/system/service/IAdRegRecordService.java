package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AdRegRecord;

/**
 * 用户注册广告请求记录Service接口
 * 
 * @author rongwq
 * @date 2024-12-24
 */
public interface IAdRegRecordService 
{
    /**
     * 查询用户注册广告请求记录
     * 
     * @param id 用户注册广告请求记录主键
     * @return 用户注册广告请求记录
     */
    public AdRegRecord selectAdRegRecordById(Long id);

    /**
     * 查询用户注册广告请求记录列表
     * 
     * @param adRegRecord 用户注册广告请求记录
     * @return 用户注册广告请求记录集合
     */
    public List<AdRegRecord> selectAdRegRecordList(AdRegRecord adRegRecord);

    /**
     * 新增用户注册广告请求记录
     * 
     * @param adRegRecord 用户注册广告请求记录
     * @return 结果
     */
    public int insertAdRegRecord(AdRegRecord adRegRecord);

    /**
     * 修改用户注册广告请求记录
     * 
     * @param adRegRecord 用户注册广告请求记录
     * @return 结果
     */
    public int updateAdRegRecord(AdRegRecord adRegRecord);

    /**
     * 批量删除用户注册广告请求记录
     * 
     * @param ids 需要删除的用户注册广告请求记录主键集合
     * @return 结果
     */
    public int deleteAdRegRecordByIds(Long[] ids);

    /**
     * 删除用户注册广告请求记录信息
     * 
     * @param id 用户注册广告请求记录主键
     * @return 结果
     */
    public int deleteAdRegRecordById(Long id);
}
