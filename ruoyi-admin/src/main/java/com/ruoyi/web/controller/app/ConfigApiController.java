package com.ruoyi.web.controller.app;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUserExt;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserExtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private ISysUserExtService sysUserExtService;

    @ApiOperation("获取配置信息")
    @RequestMapping("/getConfig")
    @Anonymous
    public R getConfig() {
        //测试打印ip
        JSONObject js = new JSONObject();
        js.put("homeUrl", sysConfigService.selectConfigByKey("home_url"));
        js.put("contactImg", sysConfigService.selectConfigByKey("contact.img"));
        js.put("contactTxt", sysConfigService.selectConfigByKey("contact.txt"));
        js.put("regTxt", sysConfigService.selectConfigByKey("reg.txt"));
        return R.ok(js);
    }

    @ApiOperation("获取相关地址")
    @RequestMapping("/getKey")
    @Anonymous
    public R getKey(@RequestParam(required = false) Long userId) {
        JSONObject js = new JSONObject();
        if (userId != null) {
            SysUserExt sysUserExt = sysUserExtService.selectSysUserExtByUserId(userId);
            if (sysUserExt != null) {
                String shopUrl = sysUserExt.getShopUrl();
                if (StringUtils.isNotEmpty(shopUrl)) {
                    js.put("rechargeUrl", shopUrl);
                } else {
                    js.put("rechargeUrl", sysConfigService.selectConfigByKey("recharge_key_url"));
                }
                String cdKeyShopUrl = sysUserExt.getCdKeyShopUrl();
                if (StringUtils.isNotEmpty(cdKeyShopUrl)) {
                    js.put("cdKeyUrl", cdKeyShopUrl);
                } else {
                    js.put("cdKeyUrl", sysConfigService.selectConfigByKey("cd_key_url"));
                }
                String h5Url = sysConfigService.selectConfigByKey("h5_url");
                js.put("h5Url", h5Url+"?id="+userId);
            }else {
                setUrl(js);
            }
        } else {
            setUrl(js);
        }
        return R.ok(js);
    }

    private void setUrl(JSONObject js) {
        js.put("rechargeUrl", sysConfigService.selectConfigByKey("recharge_key_url"));
        js.put("cdKeyUrl", sysConfigService.selectConfigByKey("cd_key_url"));
        js.put("h5Url", sysConfigService.selectConfigByKey("h5_url"));
    }

}
