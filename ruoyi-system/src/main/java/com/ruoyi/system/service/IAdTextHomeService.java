package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AdTextHome;

/**
 * 首页文字广告Service接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface IAdTextHomeService 
{
    /**
     * 查询首页文字广告
     * 
     * @param id 首页文字广告主键
     * @return 首页文字广告
     */
    public AdTextHome selectAdTextHomeById(Long id);

    /**
     * 查询首页文字广告列表
     * 
     * @param adTextHome 首页文字广告
     * @return 首页文字广告集合
     */
    public List<AdTextHome> selectAdTextHomeList(AdTextHome adTextHome);

    /**
     * 新增首页文字广告
     * 
     * @param adTextHome 首页文字广告
     * @return 结果
     */
    public int insertAdTextHome(AdTextHome adTextHome);

    /**
     * 修改首页文字广告
     * 
     * @param adTextHome 首页文字广告
     * @return 结果
     */
    public int updateAdTextHome(AdTextHome adTextHome);

    /**
     * 批量删除首页文字广告
     * 
     * @param ids 需要删除的首页文字广告主键集合
     * @return 结果
     */
    public int deleteAdTextHomeByIds(Long[] ids);

    /**
     * 删除首页文字广告信息
     * 
     * @param id 首页文字广告主键
     * @return 结果
     */
    public int deleteAdTextHomeById(Long id);
}
