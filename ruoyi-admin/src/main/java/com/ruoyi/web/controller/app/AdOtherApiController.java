package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.system.domain.AdOther;
import com.ruoyi.system.domain.AdReg;
import com.ruoyi.system.service.IAdOtherService;
import com.ruoyi.system.service.IAdRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return success(adReg);
    }
}
