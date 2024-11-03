package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysUserExt;

import java.util.List;

/**
 * 用户扩展Mapper接口
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
public interface SysUserExtMapper 
{
    /**
     * 查询用户扩展
     * 
     * @param userId 用户扩展主键
     * @return 用户扩展
     */
    public SysUserExt selectSysUserExtByUserId(Long userId);

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
     * 删除用户扩展
     * 
     * @param userId 用户扩展主键
     * @return 结果
     */
    public int deleteSysUserExtByUserId(Long userId);

    /**
     * 批量删除用户扩展
     * 
     * @param userIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysUserExtByUserIds(Long[] userIds);
}
