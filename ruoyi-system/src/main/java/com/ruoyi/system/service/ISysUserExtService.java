package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysUserExt;

import java.util.List;

/**
 * 用户扩展Service接口
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
public interface ISysUserExtService 
{
    /**
     * 查询用户扩展
     * 
     * @param userId 用户扩展主键
     * @return 用户扩展
     */
    public SysUserExt selectSysUserExtByUserId(Long userId);

    SysUserExt selectByDomain(String domain);

    /**
     * 查询用户扩展列表
     * 
     * @param sysUserExt 用户扩展
     * @return 用户扩展集合
     */
    public List<SysUserExt> selectSysUserExtList(SysUserExt sysUserExt);

    /**
     * 新增用户扩展
     * 
     * @param sysUserExt 用户扩展
     * @return 结果
     */
    public int insertSysUserExt(SysUserExt sysUserExt);

    /**
     * 修改用户扩展
     * 
     * @param sysUserExt 用户扩展
     * @return 结果
     */
    public int updateSysUserExt(SysUserExt sysUserExt);

    /**
     * 批量删除用户扩展
     * 
     * @param userIds 需要删除的用户扩展主键集合
     * @return 结果
     */
    public int deleteSysUserExtByUserIds(Long[] userIds);

    /**
     * 删除用户扩展信息
     * 
     * @param userId 用户扩展主键
     * @return 结果
     */
    public int deleteSysUserExtByUserId(Long userId);
}
