package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AdOtherMapper;
import com.ruoyi.system.domain.AdOther;
import com.ruoyi.system.service.IAdOtherService;

/**
 * 其它部位广告Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class AdOtherServiceImpl implements IAdOtherService 
{
    @Autowired
    private AdOtherMapper adOtherMapper;

    /**
     * 查询其它部位广告
     * 
     * @param id 其它部位广告主键
     * @return 其它部位广告
     */
    @Override
    public AdOther selectAdOtherById(Long id)
    {
        return adOtherMapper.selectAdOtherById(id);
    }

    /**
     * 查询其它部位广告列表
     * 
     * @param adOther 其它部位广告
     * @return 其它部位广告
     */
    @Override
    public List<AdOther> selectAdOtherList(AdOther adOther)
    {
        return adOtherMapper.selectAdOtherList(adOther);
    }

    /**
     * 新增其它部位广告
     * 
     * @param adOther 其它部位广告
     * @return 结果
     */
    @Override
    public int insertAdOther(AdOther adOther)
    {
        adOther.setCreateTime(DateUtils.getNowDate());
        return adOtherMapper.insertAdOther(adOther);
    }

    /**
     * 修改其它部位广告
     * 
     * @param adOther 其它部位广告
     * @return 结果
     */
    @Override
    public int updateAdOther(AdOther adOther)
    {
        adOther.setUpdateTime(DateUtils.getNowDate());
        return adOtherMapper.updateAdOther(adOther);
    }

    /**
     * 批量删除其它部位广告
     * 
     * @param ids 需要删除的其它部位广告主键
     * @return 结果
     */
    @Override
    public int deleteAdOtherByIds(Long[] ids)
    {
        return adOtherMapper.deleteAdOtherByIds(ids);
    }

    /**
     * 删除其它部位广告信息
     * 
     * @param id 其它部位广告主键
     * @return 结果
     */
    @Override
    public int deleteAdOtherById(Long id)
    {
        return adOtherMapper.deleteAdOtherById(id);
    }
}
