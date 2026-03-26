package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.RegActivityConfig;
import com.ruoyi.system.mapper.RegActivityConfigMapper;
import com.ruoyi.system.service.IRegActivityConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 注册活动配置Service业务层处理
 *
 * @author ruoyi
 * @date 2024-09-14
 */
@Service
public class RegActivityConfigServiceImpl implements IRegActivityConfigService
{
    @Autowired
    private RegActivityConfigMapper regActivityConfigMapper;

    /**
     * 查询注册活动配置
     *
     * @param id 注册活动配置主键
     * @return 注册活动配置
     */
    @Override
    public RegActivityConfig selectRegActivityConfigById(Long id)
    {
        return regActivityConfigMapper.selectRegActivityConfigById(id);
    }

    /**
     * 查询注册活动配置列表
     *
     * @param regActivityConfig 注册活动配置
     * @return 注册活动配置
     */
    @Override
    public List<RegActivityConfig> selectRegActivityConfigList(RegActivityConfig regActivityConfig)
    {
        return regActivityConfigMapper.selectRegActivityConfigList(regActivityConfig);
    }

    /**
     * 获取当前有效的注册活动配置
     * 1. 活动开关为开启状态
     * 2. 当前时间在活动开始时间和结束时间之间
     *
     * @return 注册活动配置，如果没有有效活动则返回null
     */
    @Override
    public RegActivityConfig getValidRegActivityConfig()
    {
        RegActivityConfig config = regActivityConfigMapper.selectRegActivityConfigByStatus("1");
        if (config == null)
        {
            return null;
        }
        // 检查活动是否在有效期内
        Date now = new Date();
        if (config.getStartTime() != null && now.before(config.getStartTime()))
        {
            return null;
        }
        if (config.getEndTime() != null && now.after(config.getEndTime()))
        {
            return null;
        }
        return config;
    }

    /**
     * 新增注册活动配置
     *
     * @param regActivityConfig 注册活动配置
     * @return 结果
     */
    @Override
    public int insertRegActivityConfig(RegActivityConfig regActivityConfig)
    {
        regActivityConfig.setCreateTime(DateUtils.getNowDate());
        return regActivityConfigMapper.insertRegActivityConfig(regActivityConfig);
    }

    /**
     * 修改注册活动配置
     *
     * @param regActivityConfig 注册活动配置
     * @return 结果
     */
    @Override
    public int updateRegActivityConfig(RegActivityConfig regActivityConfig)
    {
        regActivityConfig.setUpdateTime(DateUtils.getNowDate());
        return regActivityConfigMapper.updateRegActivityConfig(regActivityConfig);
    }

    /**
     * 批量删除注册活动配置
     *
     * @param ids 需要删除的注册活动配置主键
     * @return 结果
     */
    @Override
    public int deleteRegActivityConfigByIds(Long[] ids)
    {
        return regActivityConfigMapper.deleteRegActivityConfigByIds(ids);
    }

    /**
     * 删除注册活动配置信息
     *
     * @param id 注册活动配置主键
     * @return 结果
     */
    @Override
    public int deleteRegActivityConfigById(Long id)
    {
        return regActivityConfigMapper.deleteRegActivityConfigById(id);
    }
}
