package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.AdTextHomeMapper;
import com.ruoyi.system.domain.AdTextHome;
import com.ruoyi.system.service.IAdTextHomeService;

/**
 * 首页文字广告Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class AdTextHomeServiceImpl implements IAdTextHomeService 
{
    @Autowired
    private AdTextHomeMapper adTextHomeMapper;

    /**
     * 查询首页文字广告
     * 
     * @param id 首页文字广告主键
     * @return 首页文字广告
     */
    @Override
    public AdTextHome selectAdTextHomeById(Long id)
    {
        return adTextHomeMapper.selectAdTextHomeById(id);
    }

    /**
     * 查询首页文字广告列表
     * 
     * @param adTextHome 首页文字广告
     * @return 首页文字广告
     */
    @Override
    public List<AdTextHome> selectAdTextHomeList(AdTextHome adTextHome)
    {
        return adTextHomeMapper.selectAdTextHomeList(adTextHome);
    }

    /**
     * 新增首页文字广告
     * 
     * @param adTextHome 首页文字广告
     * @return 结果
     */
    @Override
    public int insertAdTextHome(AdTextHome adTextHome)
    {
        adTextHome.setCreateTime(DateUtils.getNowDate());
        return adTextHomeMapper.insertAdTextHome(adTextHome);
    }

    /**
     * 修改首页文字广告
     * 
     * @param adTextHome 首页文字广告
     * @return 结果
     */
    @Override
    public int updateAdTextHome(AdTextHome adTextHome)
    {
        adTextHome.setUpdateTime(DateUtils.getNowDate());
        return adTextHomeMapper.updateAdTextHome(adTextHome);
    }

    /**
     * 批量删除首页文字广告
     * 
     * @param ids 需要删除的首页文字广告主键
     * @return 结果
     */
    @Override
    public int deleteAdTextHomeByIds(Long[] ids)
    {
        return adTextHomeMapper.deleteAdTextHomeByIds(ids);
    }

    /**
     * 删除首页文字广告信息
     * 
     * @param id 首页文字广告主键
     * @return 结果
     */
    @Override
    public int deleteAdTextHomeById(Long id)
    {
        return adTextHomeMapper.deleteAdTextHomeById(id);
    }
}
