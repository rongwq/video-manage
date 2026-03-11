package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.RegisterActivity;
import com.ruoyi.system.mapper.RegisterActivityMapper;
import com.ruoyi.system.service.IRegisterActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 注册送积分活动配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2026-03-11
 */
@Service
public class RegisterActivityServiceImpl implements IRegisterActivityService
{
    @Autowired
    private RegisterActivityMapper registerActivityMapper;

    /**
     * 查询注册送积分活动配置
     * 
     * @param id 注册送积分活动配置主键
     * @return 注册送积分活动配置
     */
    @Override
    public RegisterActivity selectRegisterActivityById(Long id)
    {
        return registerActivityMapper.selectRegisterActivityById(id);
    }

    /**
     * 查询注册送积分活动配置列表
     * 
     * @param registerActivity 注册送积分活动配置
     * @return 注册送积分活动配置集合
     */
    @Override
    public List<RegisterActivity> selectRegisterActivityList(RegisterActivity registerActivity)
    {
        return registerActivityMapper.selectRegisterActivityList(registerActivity);
    }

    /**
     * 新增注册送积分活动配置
     * 
     * @param registerActivity 注册送积分活动配置
     * @return 结果
     */
    @Override
    public int insertRegisterActivity(RegisterActivity registerActivity)
    {
        registerActivity.setCreateTime(DateUtils.getNowDate());
        registerActivity.setUpdateTime(DateUtils.getNowDate());
        return registerActivityMapper.insertRegisterActivity(registerActivity);
    }

    /**
     * 修改注册送积分活动配置
     * 
     * @param registerActivity 注册送积分活动配置
     * @return 结果
     */
    @Override
    public int updateRegisterActivity(RegisterActivity registerActivity)
    {
        registerActivity.setUpdateTime(DateUtils.getNowDate());
        return registerActivityMapper.updateRegisterActivity(registerActivity);
    }

    /**
     * 删除注册送积分活动配置
     * 
     * @param id 注册送积分活动配置主键
     * @return 结果
     */
    @Override
    public int deleteRegisterActivityById(Long id)
    {
        return registerActivityMapper.deleteRegisterActivityById(id);
    }

    /**
     * 批量删除注册送积分活动配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int deleteRegisterActivityByIds(Long[] ids)
    {
        return registerActivityMapper.deleteRegisterActivityByIds(ids);
    }

    /**
     * 查询当前有效的活动配置
     * 
     * @return 活动配置
     */
    @Override
    public RegisterActivity selectCurrentActivity()
    {
        return registerActivityMapper.selectCurrentActivity();
    }
}
