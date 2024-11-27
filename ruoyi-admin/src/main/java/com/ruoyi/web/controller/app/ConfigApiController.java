package com.ruoyi.web.controller.app;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    @RequestMapping("/getConfig")
    @Anonymous
    public R getConfig() {
        //测试打印ip
        String ip = IpUtils.getIpAddr();
        logger.info("ip=======================" + ip);
        JSONObject js = new JSONObject();
        js.put("homeUrl", sysConfigService.selectConfigByKey("home_url"));
        js.put("contactImg", sysConfigService.selectConfigByKey("contact.img"));
        js.put("contactTxt", sysConfigService.selectConfigByKey("contact.txt"));
        js.put("regTxt", sysConfigService.selectConfigByKey("reg.txt"));
        return R.ok(js);
    }

}
