package com.ruoyi.system.service;

import com.ruoyi.system.domain.Cdkey;

import java.util.List;

/**
 * 激活码Service接口
 * 
 * @author ruoyi
 * @date 2024-09-13
 */
public interface ICdkeyService
{
    /**
     * 查询激活码
     * 
     * @param cdkeyId 激活码主键
     * @return 激活码
     */
    public Cdkey selectCdkeyByCdkeyId(Long cdkeyId);

    Cdkey selectCdkeyByCdkeyCode(String cdkeyCode);

    /**
     * 查询激活码列表
     * 
     * @param Cdkey 激活码
     * @return 激活码集合
     */
    public List<Cdkey> selectCdkeyList(Cdkey Cdkey);

    /**
     * 新增激活码
     * 
     * @param Cdkey 激活码
     * @return 结果
     */
    public int insertCdkey(Cdkey Cdkey);

    /**
     * 批量生成激活码
     *
     * @param number 激活码数量
     * @return 结果
     */
    public int autoCreateCdkey(Integer number);

    /**
     * 修改激活码
     * 
     * @param Cdkey 激活码
     * @return 结果
     */
    public int updateCdkey(Cdkey Cdkey);

    /**
     * 批量删除激活码
     * 
     * @param cdkeyIds 需要删除的激活码主键集合
     * @return 结果
     */
    public int deleteCdkeyByCdkeyIds(Long[] cdkeyIds);

    /**
     * 删除激活码信息
     * 
     * @param cdkeyId 激活码主键
     * @return 结果
     */
    public int deleteCdkeyByCdkeyId(Long cdkeyId);
}
