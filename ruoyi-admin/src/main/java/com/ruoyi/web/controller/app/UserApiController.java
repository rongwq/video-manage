package com.ruoyi.web.controller.app;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.SysUserExt;
import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.CdkeyStatus;
import com.ruoyi.common.enums.IntegralType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.web.service.SysLoginService;
import com.ruoyi.system.domain.AdUseRecord;
import com.ruoyi.system.domain.Cdkey;
import com.ruoyi.system.domain.data.UserData;
import com.ruoyi.system.service.IAdUseRecordService;
import com.ruoyi.system.service.ICdkeyService;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysUserExtService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.IUserIntegralRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private IAdUseRecordService adUseRecordService;
    @Autowired
    private ISysConfigService configService;
    @Autowired
    private IUserIntegralRecordService userIntegralRecordService;

    private static final String ACTIVITY_SWITCH_KEY = "sys.activity.register.switch";
    private static final String ACTIVITY_EXPIRE_TIME_KEY = "sys.activity.register.expireTime";
    private static final String ACTIVITY_INTEGRAL_KEY = "sys.activity.register.integral";

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

    @ApiOperation("活动注册用户")
    @PostMapping("/activityReg")
    @Transactional(rollbackFor = Exception.class)
    @Anonymous
    public R<String> activityReg(@RequestBody UserData.ActivityRegUserParam regUser) {
        String switchValue = configService.selectConfigByKey(ACTIVITY_SWITCH_KEY);
        if (!"true".equals(switchValue)) {
            return R.fail("活动未开启");
        }
        String expireTimeStr = configService.selectConfigByKey(ACTIVITY_EXPIRE_TIME_KEY);
        if (StringUtils.isNotEmpty(expireTimeStr)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date expireTime = sdf.parse(expireTimeStr);
                if (new Date().after(expireTime)) {
                    return R.fail("活动已过期");
                }
            } catch (ParseException e) {
                log.error("活动过期时间格式错误", e);
            }
        }
        SysUser user = new SysUser();
        user.setUserName(regUser.getUserName());
        user.setPassword(regUser.getPassword());
        user.setPhonenumber(regUser.getPhonenumber());
        user.setEmail(regUser.getEmail());
        user.setNickName(regUser.getUserName());
        user.setRoleId(100L);
        user.setDeptId(100L);
        if (!userService.checkUserNameUnique(user)) {
            return R.fail("注册用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user)) {
            return R.fail("注册用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        int result = userService.insertUser(user);
        if (result > 0) {
            String integralStr = configService.selectConfigByKey(ACTIVITY_INTEGRAL_KEY);
            int giftIntegral = 100;
            if (StringUtils.isNotEmpty(integralStr)) {
                try {
                    giftIntegral = Integer.parseInt(integralStr);
                } catch (NumberFormatException e) {
                    log.error("活动赠送积分配置错误", e);
                }
            }
            SysUserExt ext = userExtService.selectSysUserExtByUserId(user.getUserId());
            if (ext != null) {
                userIntegralRecordService.saveUserIntegralRecord(
                    user.getUserId(),
                    null,
                    giftIntegral,
                    IntegralType.REGISTER_GIFT,
                    "新用户注册赠送积分"
                );
            }
        }
        return R.ok("注册成功，已赠送" + getGiftIntegral() + "积分");
    }

    private int getGiftIntegral() {
        String integralStr = configService.selectConfigByKey(ACTIVITY_INTEGRAL_KEY);
        if (StringUtils.isNotEmpty(integralStr)) {
            try {
                return Integer.parseInt(integralStr);
            } catch (NumberFormatException e) {
                log.error("活动赠送积分配置错误", e);
            }
        }
        return 100;
    }

    @ApiOperation("查询活动状态")
    @GetMapping("/activityStatus")
    @Anonymous
    public R<Map<String, Object>> getActivityStatus() {
        Map<String, Object> result = new HashMap<>();
        String switchValue = configService.selectConfigByKey(ACTIVITY_SWITCH_KEY);
        boolean isOpen = "true".equals(switchValue);
        result.put("isOpen", isOpen);
        String expireTimeStr = configService.selectConfigByKey(ACTIVITY_EXPIRE_TIME_KEY);
        result.put("expireTime", expireTimeStr);
        String integralStr = configService.selectConfigByKey(ACTIVITY_INTEGRAL_KEY);
        result.put("giftIntegral", StringUtils.isNotEmpty(integralStr) ? Integer.parseInt(integralStr) : 100);
        if (isOpen && StringUtils.isNotEmpty(expireTimeStr)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date expireTime = sdf.parse(expireTimeStr);
                result.put("isExpired", new Date().after(expireTime));
            } catch (ParseException e) {
                result.put("isExpired", false);
            }
        } else {
            result.put("isExpired", false);
        }
        return R.ok(result);
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
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        Map map = new HashMap<>();
        map.put(Constants.TOKEN, token);
        return R.ok(map);
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
