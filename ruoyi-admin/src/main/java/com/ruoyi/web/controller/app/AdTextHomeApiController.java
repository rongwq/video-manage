package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.AdTextHome;
import com.ruoyi.system.service.IAdTextHomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 首页文字广告Controller
 * 
 * @author rongwq
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/api/adTextHome")
public class AdTextHomeApiController extends BaseController
{
    @Autowired
    private IAdTextHomeService adTextHomeService;

    /**
     * 查询首页文字广告列表
     */
    @Anonymous
    @GetMapping("/list")
    public AjaxResult list()
    {
        AdTextHome adTextHome = new AdTextHome();
        adTextHome.setStatus(UserStatus.OK.getCode());
        List<AdTextHome> list = adTextHomeService.selectAdTextHomeList(adTextHome);
        return success(list);
    }
}
