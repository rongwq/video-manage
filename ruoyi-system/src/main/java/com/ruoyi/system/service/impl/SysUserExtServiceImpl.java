package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUserExt;
import com.ruoyi.system.mapper.SysUserExtMapper;
import com.ruoyi.system.service.ISysUserExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户扩展Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
@Service
public class SysUserExtServiceImpl implements ISysUserExtService 
{
    @Autowired
    private SysUserExtMapper sysUserExtMapper;

    /**
     * 查询用户扩展
     * 
     * @param userId 用户扩展主键
     * @return 用户扩展
     */
    @Override
    public SysUserExt selectSysUserExtByUserId(Long userId)
    {
        return sysUserExtMapper.selectSysUserExtByUserId(userId);
    }

    @Override
    public SysUserExt selectByDomain(String domain)
    {
        return sysUserExtMapper.selectByDomain(domain);
    }

    /**
     * 查询用户扩展列表
     * 
     * @param sysUserExt 用户扩展
     * @return 用户扩展
     */
    @Override
    public List<SysUserExt> selectSysUserExtList(SysUserExt sysUserExt)
    {
        return sysUserExtMapper.selectSysUserExtList(sysUserExt);
    }

    /**
     * 新增用户扩展
     * 
     * @param sysUserExt 用户扩展
     * @return 结果
     */
    @Override
    public int insertSysUserExt(SysUserExt sysUserExt)
    {
        return sysUserExtMapper.insertSysUserExt(sysUserExt);
    }

    /**
     * 修改用户扩展
     * 
     * @param sysUserExt 用户扩展
     * @return 结果
     */
    @Override
    public int updateSysUserExt(SysUserExt sysUserExt)
    {
        return sysUserExtMapper.updateSysUserExt(sysUserExt);
    }

    /**
     * 批量删除用户扩展
     * 
     * @param userIds 需要删除的用户扩展主键
     * @return 结果
     */
    @Override
    public int deleteSysUserExtByUserIds(Long[] userIds)
    {
        return sysUserExtMapper.deleteSysUserExtByUserIds(userIds);
    }

    /**
     * 删除用户扩展信息
     * 
     * @param userId 用户扩展主键
     * @return 结果
     */
    @Override
    public int deleteSysUserExtByUserId(Long userId)
    {
        return sysUserExtMapper.deleteSysUserExtByUserId(userId);
    }
}
