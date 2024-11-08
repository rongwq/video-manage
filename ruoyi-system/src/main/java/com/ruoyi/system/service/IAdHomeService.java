package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AdHome;

/**
 * 首页广告Service接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface IAdHomeService 
{
    /**
     * 查询首页广告
     * 
     * @param id 首页广告主键
     * @return 首页广告
     */
    public AdHome selectAdHomeById(Long id);

    /**
     * 查询首页广告列表
     * 
     * @param adHome 首页广告
     * @return 首页广告集合
     */
    public List<AdHome> selectAdHomeList(AdHome adHome);

    /**
     * 新增首页广告
     * 
     * @param adHome 首页广告
     * @return 结果
     */
    public int insertAdHome(AdHome adHome);

    /**
     * 修改首页广告
     * 
     * @param adHome 首页广告
     * @return 结果
     */
    public int updateAdHome(AdHome adHome);

    /**
     * 批量删除首页广告
     * 
     * @param ids 需要删除的首页广告主键集合
     * @return 结果
     */
    public int deleteAdHomeByIds(Long[] ids);

    /**
     * 删除首页广告信息
     * 
     * @param id 首页广告主键
     * @return 结果
     */
    public int deleteAdHomeById(Long id);
}
