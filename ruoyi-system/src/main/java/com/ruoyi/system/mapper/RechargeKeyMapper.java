package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.RechargeKey;

/**
 * 充值卡Mapper接口
 * 
 * @author rongwq
 * @date 2024-11-08
 */
public interface RechargeKeyMapper 
{
    /**
     * 查询充值卡
     * 
     * @param id 充值卡主键
     * @return 充值卡
     */
    public RechargeKey selectRechargeKeyById(Long id);

    public RechargeKey selectRechargeKeyByCode(String code);

    /**
     * 查询充值卡列表
     * 
     * @param rechargeKey 充值卡
     * @return 充值卡集合
     */
    public List<RechargeKey> selectRechargeKeyList(RechargeKey rechargeKey);

    /**
     * 新增充值卡
     * 
     * @param rechargeKey 充值卡
     * @return 结果
     */
    public int insertRechargeKey(RechargeKey rechargeKey);

    /**
     * 修改充值卡
     * 
     * @param rechargeKey 充值卡
     * @return 结果
     */
    public int updateRechargeKey(RechargeKey rechargeKey);

    /**
     * 删除充值卡
     * 
     * @param id 充值卡主键
     * @return 结果
     */
    public int deleteRechargeKeyById(Long id);

    /**
     * 批量删除充值卡
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteRechargeKeyByIds(Long[] ids);
}
