package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.ip.Address;
import com.ruoyi.common.utils.ip.AddressUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.domain.AdOther;
import com.ruoyi.system.domain.AdReg;
import com.ruoyi.system.domain.AdRegRecord;
import com.ruoyi.system.service.IAdOtherService;
import com.ruoyi.system.service.IAdRegRecordService;
import com.ruoyi.system.service.IAdRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 其它部位广告Controller
 *
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/api/otherAd")
public class AdOtherApiController extends BaseController {
    @Autowired
    private IAdOtherService adOtherService;
    @Autowired
    private IAdRegService adRegService;
    @Autowired
    private IAdRegRecordService adRegRecordService;

    /**
     * 查询其它部位广告列表
     */
    @GetMapping("/list")
    @Anonymous
    public AjaxResult list() {
        AdOther adOther = new AdOther();
        adOther.setStatus(UserStatus.OK.getCode());
        List<AdOther> list = adOtherService.selectAdOtherList(adOther);
        return success(list.get(0));
    }

    /**
     * 查询注册广告列表
     */
    @GetMapping("/regAd")
    @Anonymous
    public AjaxResult regAd(Long orderNum) {
        AdReg adReg = adRegService.selectAdRegByOrderNum(orderNum);
        if (adReg == null) {
            adReg = adRegService.selectAdRegByOrderNum(0L);
        }
        String title = adReg.getTitle();
        Long id = adReg.getId();
        HttpServletRequest request = ServletUtils.getRequest();
        //保存请求记录-异步
        Thread thread = new Thread(() -> {
            saveAdRegRecord(id, title, request);
        });
        thread.start();
        return success(adReg);
    }

    String saveAdRegRecord(Long id, String title, HttpServletRequest request) {
        AdRegRecord adRegRecord = new AdRegRecord();
        adRegRecord.setAdId(id);
        String ip = IpUtils.getIpAddr(request);
        adRegRecord.setIp(ip);
        try {
            adRegRecord.setAddress(AddressUtils.getRealAddressByIP(ip));
        }catch (Exception e){
            logger.error("请求IP地址异常：" + e.getMessage(),e);
        }
        adRegRecord.setRemark("请求广告:" + title);
        adRegRecord.setCreateTime(new Date());
        adRegRecordService.insertAdRegRecord(adRegRecord);
        return "1";
    }
}
