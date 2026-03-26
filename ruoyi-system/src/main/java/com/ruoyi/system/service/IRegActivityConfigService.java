package com.ruoyi.system.service;

import com.ruoyi.system.domain.RegActivityConfig;

import java.util.List;

/**
 * 注册活动配置Service接口
 *
 * @author ruoyi
 * @date 2024-09-14
 */
public interface IRegActivityConfigService
{
    /**
     * 查询注册活动配置
     *
     * @param id 注册活动配置主键
     * @return 注册活动配置
     */
    public RegActivityConfig selectRegActivityConfigById(Long id);

    /**
     * 查询注册活动配置列表
     *
     * @param regActivityConfig 注册活动配置
     * @return 注册活动配置集合
     */
    public List<RegActivityConfig> selectRegActivityConfigList(RegActivityConfig regActivityConfig);

    /**
     * 获取当前有效的注册活动配置
     *
     * @return 注册活动配置
     */
    public RegActivityConfig getValidRegActivityConfig();

    /**
     * 新增注册活动配置
     *
     * @param regActivityConfig 注册活动配置
     * @return 结果
     */
    public int insertRegActivityConfig(RegActivityConfig regActivityConfig);

    /**
     * 修改注册活动配置
     *
     * @param regActivityConfig 注册活动配置
     * @return 结果
     */
    public int updateRegActivityConfig(RegActivityConfig regActivityConfig);

    /**
     * 批量删除注册活动配置
     *
     * @param ids 需要删除的注册活动配置主键集合
     * @return 结果
     */
    public int deleteRegActivityConfigByIds(Long[] ids);

    /**
     * 删除注册活动配置信息
     *
     * @param id 注册活动配置主键
     * @return 结果
     */
    public int deleteRegActivityConfigById(Long id);
}
