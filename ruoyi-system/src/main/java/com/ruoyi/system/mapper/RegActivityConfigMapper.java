package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.RegActivityConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 注册活动配置Mapper接口
 *
 * @author ruoyi
 * @date 2024-09-14
 */
public interface RegActivityConfigMapper
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
     * 根据活动状态查询活动配置
     *
     * @param activityStatus 活动状态
     * @return 注册活动配置
     */
    public RegActivityConfig selectRegActivityConfigByStatus(@Param("activityStatus") String activityStatus);

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
     * 删除注册活动配置
     *
     * @param id 注册活动配置主键
     * @return 结果
     */
    public int deleteRegActivityConfigById(Long id);

    /**
     * 批量删除注册活动配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRegActivityConfigByIds(Long[] ids);
}
