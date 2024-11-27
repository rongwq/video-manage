package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AdUseRecordMapper;
import com.ruoyi.system.domain.AdUseRecord;
import com.ruoyi.system.service.IAdUseRecordService;

/**
 * 广告扫描记录Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-27
 */
@Service
public class AdUseRecordServiceImpl implements IAdUseRecordService 
{
    @Autowired
    private AdUseRecordMapper adUseRecordMapper;

    /**
     * 查询广告扫描记录
     * 
     * @param id 广告扫描记录主键
     * @return 广告扫描记录
     */
    @Override
    public AdUseRecord selectAdUseRecordById(Long id)
    {
        return adUseRecordMapper.selectAdUseRecordById(id);
    }

    /**
     * 查询广告扫描记录列表
     * 
     * @param adUseRecord 广告扫描记录
     * @return 广告扫描记录
     */
    @Override
    public List<AdUseRecord> selectAdUseRecordList(AdUseRecord adUseRecord)
    {
        return adUseRecordMapper.selectAdUseRecordList(adUseRecord);
    }
    @Override
    public List<AdUseRecord> selectAdUseRecordByIdAndUserId(AdUseRecord adUseRecord){
        return adUseRecordMapper.selectAdUseRecordByIdAndUserId(adUseRecord);
    }

    /**
     * 新增广告扫描记录
     * 
     * @param adUseRecord 广告扫描记录
     * @return 结果
     */
    @Override
    public int insertAdUseRecord(AdUseRecord adUseRecord)
    {
        adUseRecord.setCreateTime(DateUtils.getNowDate());
        return adUseRecordMapper.insertAdUseRecord(adUseRecord);
    }

    /**
     * 修改广告扫描记录
     * 
     * @param adUseRecord 广告扫描记录
     * @return 结果
     */
    @Override
    public int updateAdUseRecord(AdUseRecord adUseRecord)
    {
        adUseRecord.setUpdateTime(DateUtils.getNowDate());
        return adUseRecordMapper.updateAdUseRecord(adUseRecord);
    }

    /**
     * 批量删除广告扫描记录
     * 
     * @param ids 需要删除的广告扫描记录主键
     * @return 结果
     */
    @Override
    public int deleteAdUseRecordByIds(Long[] ids)
    {
        return adUseRecordMapper.deleteAdUseRecordByIds(ids);
    }

    /**
     * 删除广告扫描记录信息
     * 
     * @param id 广告扫描记录主键
     * @return 结果
     */
    @Override
    public int deleteAdUseRecordById(Long id)
    {
        return adUseRecordMapper.deleteAdUseRecordById(id);
    }
}
