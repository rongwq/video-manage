package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.CdkeyStatus;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.web.service.AppLoginFailLimitService;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.system.domain.AdUseRecord;
import com.ruoyi.system.domain.Cdkey;
import com.ruoyi.common.core.domain.entity.SysUserExt;
import com.ruoyi.system.domain.data.UserData;
import com.ruoyi.system.service.IAdUseRecordService;
import com.ruoyi.system.service.ICdkeyService;
import com.ruoyi.system.service.ISysUserExtService;
import com.ruoyi.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户对外接口
 *
 * @author rongwq
 */
@Api("用户对外接口")
@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserApiController extends BaseController {
    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserExtService userExtService;
    @Autowired
    private ICdkeyService CdkeyService;
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private AppLoginFailLimitService appLoginFailLimitService;
    @Autowired
    private IAdUseRecordService adUseRecordService;

    @ApiOperation("获取用户详细")
    @ApiImplicitParam(name = "userName", value = "用户账号", required = true, dataType = "int", paramType = "path", dataTypeClass = Integer.class)
    @GetMapping("/{userName}")
    public R<UserData.UserQuery> getUser(@PathVariable String userName) {
        SysUser user = userService.selectUserByUserName(userName);
        if (user == null) {
            return R.fail("用户不存在");
        }
        UserData.UserQuery userQuery = new UserData.UserQuery();
        userQuery.setUser(user);
        userQuery.setUserExt(userExtService.selectSysUserExtByUserId(user.getUserId()));
        return R.ok(userQuery);
    }

    @ApiOperation("注册用户")
    @PostMapping("/reg")
    @Transactional(rollbackFor = Exception.class)
    @Anonymous
    public R<String> reg(@RequestBody UserData.RegUserParam regUser) {
        SysUser user = new SysUser();
        user.setUserName(regUser.getUserName());
        user.setPassword(regUser.getPassword());
        user.setPhonenumber(regUser.getPhonenumber());
        user.setRoleId(100L);//代理角色id
        user.setDeptId(100L);//若依科技
        user.setEmail(regUser.getEmail());
        user.setNickName(regUser.getUserName());
        SysUser puser = null;
        if(StringUtils.isNotEmpty(regUser.getPuserId())) {
            puser = userService.selectUserByUserName(regUser.getPuserId());
            if (puser == null) {
                return R.fail("代理用户'" + regUser.getPuserId() + "'不存在");
            }
        }
        Cdkey Cdkey = CdkeyService.selectCdkeyByCdkeyCode(regUser.getCdkey());
        if (Cdkey == null) {
            return R.fail("注册用户'" + user.getUserName() + "'失败，注册码不存在");
        }
        if (Cdkey != null && CdkeyStatus.ENABLED.getCode().equals(Cdkey.getStatus())) {
            return R.fail("注册用户'" + user.getUserName() + "'失败，注册码已激活");
        }
        if (Cdkey != null && CdkeyStatus.UN_ENABLED.getCode().equals(Cdkey.getStatus()) && Cdkey.getUserId() != null) {
            SysUser keyUser = userService.selectUserById(Cdkey.getUserId());
            if (keyUser != null && !keyUser.getUserName().equals(regUser.getUserName())) {
                return R.fail("注册用户'" + user.getUserName() + "'失败，注册码已绑定用户");
            }
        }
        if (!userService.checkUserNameUnique(user)) {
            return R.fail("注册用户'" + user.getUserName() + "'失败，登录账号已存在");
        } else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return R.fail("注册用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        //保存用户
        int result = userService.insertUser(user);
        if (result > 0) {
            //更新激活码状态
            Cdkey.setUserId(user.getUserId());
            Cdkey.setStatus(CdkeyStatus.ENABLED.getCode());
            CdkeyService.updateCdkey(Cdkey);
            //保存用户扩展信息-更新，上面的userService.insertUser已经保存
            SysUserExt ext = userExtService.selectSysUserExtByUserId(user.getUserId());
            if(ext!=null) {
                BeanUtils.copyBeanProp(ext, regUser);
                if(puser!=null) {
                    ext.setPuserId(puser.getUserId());
                }
                userExtService.updateSysUserExt(ext);
            }
        }
        return R.ok();
    }

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    @Log(title = "用户登录", businessType = BusinessType.INSERT)
    @Anonymous
    public R login(@RequestBody LoginBody loginBody) {
        String username = loginBody.getUsername();

        // 检查登录失败次数限制
        appLoginFailLimitService.validate(username);

        try {
            // 生成令牌
            String token = loginService.login(username, loginBody.getPassword(), loginBody.getCode(),
                    loginBody.getUuid());

            // 登录成功，清空失败次数
            appLoginFailLimitService.clearFailRecord(username);

            Map map = new HashMap<>();
            map.put(Constants.TOKEN, token);
            return R.ok(map);
        } catch (Exception e) {
            // 登录失败，记录失败次数
            appLoginFailLimitService.recordFail(username);
            throw e;
        }
    }

    /**
     * 查询用户当天是否有点击过广告
     *
     * @param adId 广告id
     * @return 结果
     */
    @GetMapping("/getAdUseResult")
    @ApiOperation("查询用户当天是否有点击过广告")
    @Anonymous
    public R getAdUseResult(Long adId,Long userId) {
        AdUseRecord adUseRecord = new AdUseRecord();
        adUseRecord.setAdId(adId);
        adUseRecord.setUserId(userId);
        adUseRecord.setIp(IpUtils.getIpAddr());
        log.info("查询用户当天是否有点击过广告，ip："+adUseRecord.getIp());
        List<AdUseRecord> list = adUseRecordService.selectAdUseRecordByIdAndUserId(adUseRecord);
        if(CollectionUtils.isEmpty(list)){
            return R.ok(false);
        }
        return R.ok(true);
    }
}
