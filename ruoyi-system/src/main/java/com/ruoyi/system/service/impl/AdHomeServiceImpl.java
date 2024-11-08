package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AdHomeMapper;
import com.ruoyi.system.domain.AdHome;
import com.ruoyi.system.service.IAdHomeService;

/**
 * 首页广告Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class AdHomeServiceImpl implements IAdHomeService 
{
    @Autowired
    private AdHomeMapper adHomeMapper;

    /**
     * 查询首页广告
     * 
     * @param id 首页广告主键
     * @return 首页广告
     */
    @Override
    public AdHome selectAdHomeById(Long id)
    {
        return adHomeMapper.selectAdHomeById(id);
    }

    /**
     * 查询首页广告列表
     * 
     * @param adHome 首页广告
     * @return 首页广告
     */
    @Override
    public List<AdHome> selectAdHomeList(AdHome adHome)
    {
        return adHomeMapper.selectAdHomeList(adHome);
    }

    /**
     * 新增首页广告
     * 
     * @param adHome 首页广告
     * @return 结果
     */
    @Override
    public int insertAdHome(AdHome adHome)
    {
        adHome.setCreateTime(DateUtils.getNowDate());
        return adHomeMapper.insertAdHome(adHome);
    }

    /**
     * 修改首页广告
     * 
     * @param adHome 首页广告
     * @return 结果
     */
    @Override
    public int updateAdHome(AdHome adHome)
    {
        adHome.setUpdateTime(DateUtils.getNowDate());
        return adHomeMapper.updateAdHome(adHome);
    }

    /**
     * 批量删除首页广告
     * 
     * @param ids 需要删除的首页广告主键
     * @return 结果
     */
    @Override
    public int deleteAdHomeByIds(Long[] ids)
    {
        return adHomeMapper.deleteAdHomeByIds(ids);
    }

    /**
     * 删除首页广告信息
     * 
     * @param id 首页广告主键
     * @return 结果
     */
    @Override
    public int deleteAdHomeById(Long id)
    {
        return adHomeMapper.deleteAdHomeById(id);
    }
}
