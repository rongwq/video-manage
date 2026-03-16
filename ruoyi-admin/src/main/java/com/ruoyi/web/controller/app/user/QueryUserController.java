package com.ruoyi.web.controller.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.app.user.domain.AppUser;
import com.ruoyi.app.user.service.IAppUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiImplicitParam;

@Api("APP用户查询接口")
@RestController
@RequestMapping("/app/api/user")
public class QueryUserController extends BaseController
{
    @Autowired
    private IAppUserService appUserService;

    @GetMapping("/list")
    @ApiOperation("查询APP用户列表")
    public TableDataInfo list(AppUser appUser)
    {
        startPage();
        return getDataTable(appUserService.selectAppUserList(appUser));
    }

    @GetMapping("/{userId}")
    @ApiOperation("根据用户ID查询详细信息")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long", paramType = "path")
    public R getInfo(@PathVariable Long userId)
    {
        AppUser appUser = appUserService.selectAppUserById(userId);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }
        appUser.setPassword(null);
        return R.ok(appUser);
    }

    @GetMapping("/info/{userName}")
    @ApiOperation("根据用户名查询详细信息")
    @ApiImplicitParam(name = "userName", value = "用户账号", required = true, dataType = "String", paramType = "path")
    public R getInfoByUserName(@PathVariable String userName)
    {
        AppUser appUser = appUserService.selectAppUserByUserName(userName);
        if (appUser == null)
        {
            return R.fail("用户不存在");
        }
        appUser.setPassword(null);
        return R.ok(appUser);
    }
}
