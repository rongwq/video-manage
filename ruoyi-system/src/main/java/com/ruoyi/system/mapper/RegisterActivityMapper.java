package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.RegisterActivity;

import java.util.List;

/**
 * 注册送积分活动配置Mapper接口
 * 
 * @author ruoyi
 * @date 2026-03-11
 */
public interface RegisterActivityMapper
{
    /**
     * 查询注册送积分活动配置
     * 
     * @param id 注册送积分活动配置主键
     * @return 注册送积分活动配置
     */
    public RegisterActivity selectRegisterActivityById(Long id);

    /**
     * 查询注册送积分活动配置列表
     * 
     * @param registerActivity 注册送积分活动配置
     * @return 注册送积分活动配置集合
     */
    public List<RegisterActivity> selectRegisterActivityList(RegisterActivity registerActivity);

    /**
     * 新增注册送积分活动配置
     * 
     * @param registerActivity 注册送积分活动配置
     * @return 结果
     */
    public int insertRegisterActivity(RegisterActivity registerActivity);

    /**
     * 修改注册送积分活动配置
     * 
     * @param registerActivity 注册送积分活动配置
     * @return 结果
     */
    public int updateRegisterActivity(RegisterActivity registerActivity);

    /**
     * 删除注册送积分活动配置
     * 
     * @param id 注册送积分活动配置主键
     * @return 结果
     */
    public int deleteRegisterActivityById(Long id);

    /**
     * 批量删除注册送积分活动配置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRegisterActivityByIds(Long[] ids);

    /**
     * 查询当前有效的活动配置
     * 
     * @return 活动配置
     */
    public RegisterActivity selectCurrentActivity();
}
