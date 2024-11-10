package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.enums.CdkeyStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Cdkey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.RechargeKeyMapper;
import com.ruoyi.system.domain.RechargeKey;
import com.ruoyi.system.service.IRechargeKeyService;

import static com.ruoyi.system.service.impl.CdkeyServiceImpl.generateActivationCode;

/**
 * 充值卡Service业务层处理
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@Service
public class RechargeKeyServiceImpl implements IRechargeKeyService 
{
    @Autowired
    private RechargeKeyMapper rechargeKeyMapper;

    /**
     * 查询充值卡
     * 
     * @param id 充值卡主键
     * @return 充值卡
     */
    @Override
    public RechargeKey selectRechargeKeyById(Long id)
    {
        return rechargeKeyMapper.selectRechargeKeyById(id);
    }

    /**
     * 查询充值卡列表
     * 
     * @param rechargeKey 充值卡
     * @return 充值卡
     */
    @Override
    public List<RechargeKey> selectRechargeKeyList(RechargeKey rechargeKey)
    {
        return rechargeKeyMapper.selectRechargeKeyList(rechargeKey);
    }

    /**
     * 新增充值卡
     * 
     * @param rechargeKey 充值卡
     * @return 结果
     */
    @Override
    public int insertRechargeKey(RechargeKey rechargeKey)
    {
        rechargeKey.setCreateTime(DateUtils.getNowDate());
        return rechargeKeyMapper.insertRechargeKey(rechargeKey);
    }

    @Override
    public int autoCreatekey(Integer number, Long money) {
        for (int i = 0; i < number; i++) {
            RechargeKey key = new RechargeKey();
            key.setCode(generateActivationCode());
            key.setStatus(CdkeyStatus.UN_ENABLED.getCode());
            key.setCreateTime(DateUtils.getNowDate());
            key.setMoney(money);
            rechargeKeyMapper.insertRechargeKey(key);
        }
        return 1;
    }

    /**
     * 修改充值卡
     * 
     * @param rechargeKey 充值卡
     * @return 结果
     */
    @Override
    public int updateRechargeKey(RechargeKey rechargeKey)
    {
        rechargeKey.setUpdateTime(DateUtils.getNowDate());
        return rechargeKeyMapper.updateRechargeKey(rechargeKey);
    }

    /**
     * 批量删除充值卡
     * 
     * @param ids 需要删除的充值卡主键
     * @return 结果
     */
    @Override
    public int deleteRechargeKeyByIds(Long[] ids)
    {
        return rechargeKeyMapper.deleteRechargeKeyByIds(ids);
    }

    /**
     * 删除充值卡信息
     * 
     * @param id 充值卡主键
     * @return 结果
     */
    @Override
    public int deleteRechargeKeyById(Long id)
    {
        return rechargeKeyMapper.deleteRechargeKeyById(id);
    }
}
