package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.CdkeyStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Cdkey;
import com.ruoyi.system.mapper.CdkeyMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ICdkeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 激活码Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
@Service
public class CdkeyServiceImpl implements ICdkeyService
{
    @Autowired
    private CdkeyMapper cdkeyMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 查询激活码
     * 
     * @param cdkeyId 激活码主键
     * @return 激活码
     */
    @Override
    public Cdkey selectCdkeyByCdkeyId(Long cdkeyId)
    {
        return cdkeyMapper.selectCdkeyByCdkeyId(cdkeyId);
    }

    @Override
    public Cdkey selectCdkeyByCdkeyCode(String cdkeyCode)
    {
        return cdkeyMapper.selectCdkeyByCdkeyCode(cdkeyCode);
    }

    /**
     * 查询激活码列表
     * 
     * @param Cdkey 激活码
     * @return 激活码
     */
    @Override
    public List<Cdkey> selectCdkeyList(Cdkey Cdkey)
    {
        List<Cdkey> list = cdkeyMapper.selectCdkeyList(Cdkey);
        list.forEach(i->{
            SysUser user = sysUserMapper.selectUserById(i.getUserId());
            if(user!=null){
                i.setUserName(user.getUserName());
            }
        });
        return list;
    }

    /**
     * 新增激活码
     * 
     * @param Cdkey 激活码
     * @return 结果
     */
    @Override
    public int insertCdkey(Cdkey Cdkey)
    {
        Cdkey.setCreateTime(DateUtils.getNowDate());
        return cdkeyMapper.insertCdkey(Cdkey);
    }

    @Override
    public int autoCreateCdkey(Integer number)
    {
        for (int i = 0; i < number; i++) {
            Cdkey Cdkey = new Cdkey();
            Cdkey.setCdkeyCode(generateActivationCode());
            Cdkey.setStatus(CdkeyStatus.ENABLED.getCode());
            Cdkey.setCreateTime(DateUtils.getNowDate());
            cdkeyMapper.insertCdkey(Cdkey);
        }
        return 1;
    }

    public static String generateActivationCode() {
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString().replace("-", ""); // 去掉 UUID 中的短横线
        return uuidString.substring(0, 32);
    }

    /**
     * 修改激活码
     * 
     * @param Cdkey 激活码
     * @return 结果
     */
    @Override
    public int updateCdkey(Cdkey Cdkey)
    {
        Cdkey.setUpdateTime(DateUtils.getNowDate());
        return cdkeyMapper.updateCdkey(Cdkey);
    }

    /**
     * 批量删除激活码
     * 
     * @param cdkeyIds 需要删除的激活码主键
     * @return 结果
     */
    @Override
    public int deleteCdkeyByCdkeyIds(Long[] cdkeyIds)
    {
        return cdkeyMapper.deleteCdkeyByCdkeyIds(cdkeyIds);
    }

    /**
     * 删除激活码信息
     * 
     * @param cdkeyId 激活码主键
     * @return 结果
     */
    @Override
    public int deleteCdkeyByCdkeyId(Long cdkeyId)
    {
        return cdkeyMapper.deleteCdkeyByCdkeyId(cdkeyId);
    }
}
