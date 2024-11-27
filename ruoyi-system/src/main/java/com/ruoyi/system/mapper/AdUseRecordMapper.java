package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AdUseRecord;

/**
 * 广告扫描记录Mapper接口
 * 
 * @author rongwq
 * @date 2024-11-27
 */
public interface AdUseRecordMapper 
{
    /**
     * 查询广告扫描记录
     * 
     * @param id 广告扫描记录主键
     * @return 广告扫描记录
     */
    public AdUseRecord selectAdUseRecordById(Long id);

    /**
     * 查询广告扫描记录列表
     * 
     * @param adUseRecord 广告扫描记录
     * @return 广告扫描记录集合
     */
    public List<AdUseRecord> selectAdUseRecordList(AdUseRecord adUseRecord);

    /**
     * 用户广告记录 当天
     * @param adUseRecord
     * @return
     */
    List<AdUseRecord> selectAdUseRecordByIdAndUserId(AdUseRecord adUseRecord);

    /**
     * 新增广告扫描记录
     * 
     * @param adUseRecord 广告扫描记录
     * @return 结果
     */
    public int insertAdUseRecord(AdUseRecord adUseRecord);

    /**
     * 修改广告扫描记录
     * 
     * @param adUseRecord 广告扫描记录
     * @return 结果
     */
    public int updateAdUseRecord(AdUseRecord adUseRecord);

    /**
     * 删除广告扫描记录
     * 
     * @param id 广告扫描记录主键
     * @return 结果
     */
    public int deleteAdUseRecordById(Long id);

    /**
     * 批量删除广告扫描记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAdUseRecordByIds(Long[] ids);
}
