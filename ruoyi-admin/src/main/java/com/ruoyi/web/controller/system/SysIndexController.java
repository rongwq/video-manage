package com.ruoyi.web.controller.system;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.Ad;
import com.ruoyi.system.domain.AdUseRecord;
import com.ruoyi.system.service.IAdService;
import com.ruoyi.system.service.IAdUseRecordService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 首页
 *
 * @author ruoyi
 */
@RestController
@Slf4j
public class SysIndexController {
    @Autowired
    private IAdUseRecordService adUseRecordService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private IAdService adService;

    /**
     * 访问首页，提示语
     */
    @RequestMapping("/")
    @Transactional(rollbackFor = Exception.class)
    public String index(Long id) {
        Ad ad = adService.selectAdById(id);
        if(ad==null){
            return "广告不存在";
        }
        String ip = IpUtils.getIpAddr();
        //存储记录
        AdUseRecord adUseRecord = new AdUseRecord();
        adUseRecord.setAdId(id);
        adUseRecord.setIp(ip);
        adUseRecord.setCreateTime(new Date());
        //根据ip查询用户信息
        SysUser user = sysUserService.selectUserByIp(ip);
        if(user!=null){
            adUseRecord.setUserId(user.getUserId());
        }
        adUseRecordService.insertAdUseRecord(adUseRecord);
        //广告扫描数+1
        if(ad.getUseNum()==null){
            ad.setUseNum(1);
        }else {
            ad.setUseNum(ad.getUseNum() + 1);
        }
        adService.updateAd(ad);
        return ip;
    }
}
