package com.ruoyi.web.controller.app;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 其它接口
 *
 * @author rongwq
 */
@Api("其它接口")
@RestController
@RequestMapping("/api/config")
public class ConfigApiController extends BaseController {
    @Autowired
    private ISysConfigService sysConfigService;

    @ApiOperation("获取配置信息")
    @PostMapping("/getConfig")
    @Anonymous
    public R getConfig() {
        String homeUrl = sysConfigService.selectConfigByKey("home_url");
        String contactImg = sysConfigService.selectConfigByKey("contact.img");
        String contactTxt = sysConfigService.selectConfigByKey("contact.txt");
        JSONObject js = new JSONObject();
        js.put("homeUrl", homeUrl);
        js.put("contactImg", contactImg);
        js.put("contactTxt", contactTxt);
        return R.ok(js);
    }

}
