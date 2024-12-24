package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AdRegRecordMapper;
import com.ruoyi.system.domain.AdRegRecord;
import com.ruoyi.system.service.IAdRegRecordService;

/**
 * 用户注册广告请求记录Service业务层处理
 * 
 * @author rongwq
 * @date 2024-12-24
 */
@Service
public class AdRegRecordServiceImpl implements IAdRegRecordService 
{
    @Autowired
    private AdRegRecordMapper adRegRecordMapper;

    /**
     * 查询用户注册广告请求记录
     * 
     * @param id 用户注册广告请求记录主键
     * @return 用户注册广告请求记录
     */
    @Override
    public AdRegRecord selectAdRegRecordById(Long id)
    {
        return adRegRecordMapper.selectAdRegRecordById(id);
    }

    /**
     * 查询用户注册广告请求记录列表
     * 
     * @param adRegRecord 用户注册广告请求记录
     * @return 用户注册广告请求记录
     */
    @Override
    public List<AdRegRecord> selectAdRegRecordList(AdRegRecord adRegRecord)
    {
        return adRegRecordMapper.selectAdRegRecordList(adRegRecord);
    }

    /**
     * 新增用户注册广告请求记录
     * 
     * @param adRegRecord 用户注册广告请求记录
     * @return 结果
     */
    @Override
    public int insertAdRegRecord(AdRegRecord adRegRecord)
    {
        adRegRecord.setCreateTime(DateUtils.getNowDate());
        return adRegRecordMapper.insertAdRegRecord(adRegRecord);
    }

    /**
     * 修改用户注册广告请求记录
     * 
     * @param adRegRecord 用户注册广告请求记录
     * @return 结果
     */
    @Override
    public int updateAdRegRecord(AdRegRecord adRegRecord)
    {
        adRegRecord.setUpdateTime(DateUtils.getNowDate());
        return adRegRecordMapper.updateAdRegRecord(adRegRecord);
    }

    /**
     * 批量删除用户注册广告请求记录
     * 
     * @param ids 需要删除的用户注册广告请求记录主键
     * @return 结果
     */
    @Override
    public int deleteAdRegRecordByIds(Long[] ids)
    {
        return adRegRecordMapper.deleteAdRegRecordByIds(ids);
    }

    /**
     * 删除用户注册广告请求记录信息
     * 
     * @param id 用户注册广告请求记录主键
     * @return 结果
     */
    @Override
    public int deleteAdRegRecordById(Long id)
    {
        return adRegRecordMapper.deleteAdRegRecordById(id);
    }
}
