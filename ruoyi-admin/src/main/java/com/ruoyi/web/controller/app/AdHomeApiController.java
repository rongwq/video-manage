package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.system.domain.AdHome;
import com.ruoyi.system.service.IAdHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 首页广告Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/api/homeAd")
public class AdHomeApiController extends BaseController
{
    @Autowired
    private IAdHomeService adHomeService;

    /**
     * 查询首页广告列表
     */
    @GetMapping("/list")
    @Anonymous
    public AjaxResult list()
    {
        AdHome adHome = new AdHome();
        adHome.setStatus(UserStatus.OK.getCode());
        List<AdHome> list = adHomeService.selectAdHomeList(adHome);
        return success(list);
    }
}
