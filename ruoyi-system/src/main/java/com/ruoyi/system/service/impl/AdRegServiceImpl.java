package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.mapper.AdRegRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AdRegMapper;
import com.ruoyi.system.domain.AdReg;
import com.ruoyi.system.service.IAdRegService;

/**
 * 注册广告Service业务层处理
 * 
 * @author rongwq
 * @date 2024-12-13
 */
@Service
public class AdRegServiceImpl implements IAdRegService 
{
    @Autowired
    private AdRegMapper adRegMapper;
    @Autowired
    private AdRegRecordMapper adRegRecordMapper;

    /**
     * 查询注册广告
     * 
     * @param id 注册广告主键
     * @return 注册广告
     */
    @Override
    public AdReg selectAdRegById(Long id)
    {
        return adRegMapper.selectAdRegById(id);
    }

    /**
     * 查询注册广告列表
     * 
     * @param adReg 注册广告
     * @return 注册广告
     */
    @Override
    public List<AdReg> selectAdRegList(AdReg adReg)
    {
        List<AdReg> list = adRegMapper.selectAdRegList(adReg);
        for (AdReg reg : list) {
            AdReg result = adRegRecordMapper.selectAdRegRecordByAdId(reg.getId());
            if(result!=null){
                reg.setYesterdayNum(result.getYesterdayNum());
                reg.setTodayNum(result.getTodayNum());
                reg.setAllNum(result.getAllNum());
            }
        }
        return list;
    }

    @Override
    public AdReg selectAdRegByOrderNum(Long orderNum) {
        return adRegMapper.selectAdRegByOrderNum(orderNum);
    }

    /**
     * 新增注册广告
     * 
     * @param adReg 注册广告
     * @return 结果
     */
    @Override
    public int insertAdReg(AdReg adReg)
    {
        adReg.setCreateTime(DateUtils.getNowDate());
        return adRegMapper.insertAdReg(adReg);
    }

    /**
     * 修改注册广告
     * 
     * @param adReg 注册广告
     * @return 结果
     */
    @Override
    public int updateAdReg(AdReg adReg)
    {
        adReg.setUpdateTime(DateUtils.getNowDate());
        return adRegMapper.updateAdReg(adReg);
    }

    /**
     * 批量删除注册广告
     * 
     * @param ids 需要删除的注册广告主键
     * @return 结果
     */
    @Override
    public int deleteAdRegByIds(Long[] ids)
    {
        return adRegMapper.deleteAdRegByIds(ids);
    }

    /**
     * 删除注册广告信息
     * 
     * @param id 注册广告主键
     * @return 结果
     */
    @Override
    public int deleteAdRegById(Long id)
    {
        return adRegMapper.deleteAdRegById(id);
    }
}
