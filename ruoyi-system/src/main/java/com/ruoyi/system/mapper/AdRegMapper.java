package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.AdReg;

/**
 * 注册广告Mapper接口
 * 
 * @author rongwq
 * @date 2024-12-13
 */
public interface AdRegMapper 
{
    /**
     * 查询注册广告
     * 
     * @param id 注册广告主键
     * @return 注册广告
     */
    public AdReg selectAdRegById(Long id);

    /**
     * 查询注册广告列表
     * 
     * @param adReg 注册广告
     * @return 注册广告集合
     */
    public List<AdReg> selectAdRegList(AdReg adReg);

    AdReg selectAdRegByOrderNum(Long orderNum);

    /**
     * 新增注册广告
     * 
     * @param adReg 注册广告
     * @return 结果
     */
    public int insertAdReg(AdReg adReg);

    /**
     * 修改注册广告
     * 
     * @param adReg 注册广告
     * @return 结果
     */
    public int updateAdReg(AdReg adReg);

    /**
     * 删除注册广告
     * 
     * @param id 注册广告主键
     * @return 结果
     */
    public int deleteAdRegById(Long id);

    /**
     * 批量删除注册广告
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAdRegByIds(Long[] ids);
}
