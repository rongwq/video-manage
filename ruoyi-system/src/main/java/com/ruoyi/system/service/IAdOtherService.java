package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.AdOther;

/**
 * 其它部位广告Service接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface IAdOtherService 
{
    /**
     * 查询其它部位广告
     * 
     * @param id 其它部位广告主键
     * @return 其它部位广告
     */
    public AdOther selectAdOtherById(Long id);

    /**
     * 查询其它部位广告列表
     * 
     * @param adOther 其它部位广告
     * @return 其它部位广告集合
     */
    public List<AdOther> selectAdOtherList(AdOther adOther);

    /**
     * 新增其它部位广告
     * 
     * @param adOther 其它部位广告
     * @return 结果
     */
    public int insertAdOther(AdOther adOther);

    /**
     * 修改其它部位广告
     * 
     * @param adOther 其它部位广告
     * @return 结果
     */
    public int updateAdOther(AdOther adOther);

    /**
     * 批量删除其它部位广告
     * 
     * @param ids 需要删除的其它部位广告主键集合
     * @return 结果
     */
    public int deleteAdOtherByIds(Long[] ids);

    /**
     * 删除其它部位广告信息
     * 
     * @param id 其它部位广告主键
     * @return 结果
     */
    public int deleteAdOtherById(Long id);
}
