package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

/**
 * 用户注册广告请求记录对象 v_ad_reg_record
 *
 * @author rongwq
 * @date 2024-12-24
 */
@Data
public class AdRegRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 广告ID
     */
    @Excel(name = "广告ID")
    private Long adId;

    /**
     * IP
     */
    @Excel(name = "IP")
    private String ip;

    @Excel(name = "地址")
    private String address;
}
