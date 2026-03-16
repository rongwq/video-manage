package com.ruoyi.app.user.controller;

import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * APP用户列表查询控制器
 *
 * @author ruoyi
 */
@Api("APP用户列表接口")
@RestController
@RequestMapping("/app/api/user")
public class ListUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    /**
     * 获取APP用户列表
     */
    @ApiOperation("获取APP用户列表")
    @GetMapping("/list")
    @Anonymous
    public TableDataInfo list(AppUser appUser)
    {
        startPage();
        List<AppUser> list = appUserService.selectAppUserList(appUser);
        // 清除敏感信息
        if (list != null)
        {
            for (AppUser user : list)
            {
                user.setPassword(null);
            }
        }
        return getDataTable(list);
    }
}
